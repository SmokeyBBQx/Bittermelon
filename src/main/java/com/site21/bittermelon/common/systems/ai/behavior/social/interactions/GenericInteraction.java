package com.site21.bittermelon.common.systems.ai.behavior.social.interactions;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.ai.base.NeedsUser;
import com.site21.bittermelon.common.systems.ai.behavior.social.Relationship;
import com.site21.bittermelon.common.systems.ai.behavior.social.Socializable;
import com.site21.bittermelon.common.systems.ai.base.Need;
import com.site21.bittermelon.init.neoforge.BitterMemoryTypes;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.ToIntBiFunction;

public class GenericInteraction<E extends LivingEntity & Socializable & NeedsUser> extends ExtendedBehaviour<E> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder(4).hasMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).noMemory(BitterMemoryTypes.SOCIALIZE_TARGET.get()).usesMemories(MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET);

    protected BiFunction<E, LivingEntity, Float> speedMod = (entity, partner) -> 1f;
    protected ToIntBiFunction<E, LivingEntity> closeEnoughDist = (entity, partner) -> 2;
    protected BiFunction<E, LivingEntity, Integer> socializeTime = (entity, partner) -> entity.getRandom().nextInt(60, 110);
    protected BiPredicate<E, LivingEntity> partnerPredicate = (entity, partner) -> entity.getType() == partner.getType();
    protected LivingEntity partner = null;
    protected List<String> messages;
    protected int socializeTick = -1;

    public GenericInteraction() {
        noTimeout();
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    public GenericInteraction<E> speedMod(final BiFunction<E, LivingEntity, Float> speedModifier) {
        this.speedMod = speedModifier;

        return this;
    }

    public GenericInteraction<E> messages(List<String> messages) {
        this.messages = messages;

        return this;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        this.partner = findPartner(entity);

        return this.partner != null;
    }

    public GenericInteraction<E> closeEnoughDist(final ToIntBiFunction<E, LivingEntity> closeEnoughDist) {
        this.closeEnoughDist = closeEnoughDist;

        return this;
    }


    @Override
    protected boolean shouldKeepRunning(E entity) {
        return this.partner != null && this.partner.isAlive() && entity.tickCount <= this.socializeTick && BehaviorUtils.entityIsVisible(entity.getBrain(), this.partner) && this.partnerPredicate.test(entity, this.partner);
    }

    @Override
    protected void start(@NotNull E entity) {
        this.socializeTick = entity.tickCount + this.socializeTime.apply(entity, this.partner);

        BrainUtil.setMemory(entity, BitterMemoryTypes.SOCIALIZE_TARGET.get(), this.partner);
        BrainUtil.setMemory(this.partner, BitterMemoryTypes.SOCIALIZE_TARGET.get(), entity);
        BehaviorUtils.lockGazeAndWalkToEachOther(entity, this.partner, this.speedMod.apply(entity, this.partner), this.closeEnoughDist.applyAsInt(entity, this.partner));
        sendRandomMessage(entity);
    }

    @Override
    protected void tick(E entity) {
        BehaviorUtils.lockGazeAndWalkToEachOther(entity, this.partner, this.speedMod.apply(entity, this.partner), this.closeEnoughDist.applyAsInt(entity, this.partner));

        if (entity.closerThan(this.partner, closeEnoughDist.applyAsInt(entity, partner)) && entity.tickCount == this.socializeTick) {
            entity.modifyNeed(Need.SOCIALIZATION, -5);
            CharacterManager characterManager = CharacterManager.get(entity.level());
            Character partnerCharacter = characterManager.getActiveCharacter(partner);

            if (partnerCharacter != null) {
                Relationship entityPartnerRelationship = entity.getRelationship(partnerCharacter);
                if (entityPartnerRelationship != null) {
                    entityPartnerRelationship.modifyOpinion(2);
                }
            }

            if (partner instanceof NeedsUser needsUser) {
                needsUser.modifyNeed(Need.SOCIALIZATION, -5);
                Character entityCharacter = characterManager.getActiveCharacter(entity);
                if (entityCharacter != null && partner instanceof Socializable socializable) {
                    Relationship partnerEntityRelationship = socializable.getRelationship(entityCharacter);
                    if (partnerEntityRelationship != null) {
                        partnerEntityRelationship.modifyOpinion(2);
                    }
                }
            }

            BrainUtil.clearMemory(entity, BitterMemoryTypes.SOCIALIZE_TARGET.get());
            BrainUtil.clearMemory(this.partner, BitterMemoryTypes.SOCIALIZE_TARGET.get());
        }
    }


    @Override
    protected void stop(E entity) {
        BrainUtil.clearMemories(entity, BitterMemoryTypes.SOCIALIZE_TARGET.get(), MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET);

        if (this.partner != null)
            BrainUtil.clearMemories(this.partner, BitterMemoryTypes.SOCIALIZE_TARGET.get(), MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET);

        this.socializeTick = -1;
        this.partner = null;
    }

    @Nullable
    protected LivingEntity findPartner(E entity) {
        return BrainUtil.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).findClosest(entity2 -> entity2 instanceof LivingEntity partner && this.partnerPredicate.test(entity, partner)).map(LivingEntity.class::cast).orElse(null);
    }

    protected void sendRandomMessage(E entity) {
        if (messages != null && partner != null && !messages.isEmpty()) {
            CharacterManager characterManager = CharacterManager.get(entity.level());
            Character entityCharacter = characterManager.getActiveCharacter(entity);
            Character partnerCharacter = characterManager.getActiveCharacter(partner);

            if (entityCharacter != null && partnerCharacter != null) {
                Random random = new Random();
                String message = messages.get(random.nextInt(messages.size()));
                LocalMessageHelper.sendLocalMessage(entity, 10,
                        Component.literal(entityCharacter.getName() + message + partnerCharacter.getName() + ".")
                                .withColor(entityCharacter.getEmoteColor())
                );
            }
        }
    }
}
