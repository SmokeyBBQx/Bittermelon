package com.site21.bittermelon.common.content.entities.scp1507;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.common.content.entities.scp1507.behavior.StopMovingWhenLookedAt;
import com.site21.bittermelon.common.content.entities.scp1507.behavior.TryToBecomeActive;
import com.site21.bittermelon.common.systems.ai.base.BitterMob;
import com.site21.bittermelon.common.systems.ai.base.Need;
import com.site21.bittermelon.common.systems.ai.base.NeedInstance;
import com.site21.bittermelon.common.systems.ai.behavior.attack.CollectivePush;
import com.site21.bittermelon.common.systems.ai.behavior.blockinteraction.FindBlockingBlock;
import com.site21.bittermelon.common.systems.ai.behavior.blockinteraction.FindRandomBreakTarget;
import com.site21.bittermelon.common.systems.ai.behavior.blockinteraction.InvalidateBreakTarget;
import com.site21.bittermelon.common.systems.ai.behavior.blockinteraction.LeapAndHurtBlock;
import com.site21.bittermelon.common.systems.ai.behavior.herd.VerifyOrFindLeader;
import com.site21.bittermelon.common.systems.ai.behavior.target.InvalidateAttackTarget;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.stumble.StumbleHandler;
import com.site21.bittermelon.init.neoforge.BitterActivity;
import com.site21.bittermelon.init.neoforge.BitterItems;
import com.site21.bittermelon.init.neoforge.BitterMemoryTypes;
import com.site21.bittermelon.init.neoforge.BitterParticles;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.LeapAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.ReactToUnreachableTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowEntity;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.navigation.SmoothGroundNavigation;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyBlocksSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

import static net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH;

public class SCP1507 extends BitterMob<SCP1507> implements SmartBrainOwner<SCP1507> {
    private static final EntityDataAccessor<Integer> ATTACK_TIME = SynchedEntityData.defineId(SCP1507.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> LEFT_LEG_ATTACHED = SynchedEntityData.defineId(SCP1507.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> RIGHT_LEG_ATTACHED = SynchedEntityData.defineId(SCP1507.class, EntityDataSerializers.BOOLEAN);

    public SCP1507(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected Character initializeCharacter() {
        return new Character(uuid, "SCP-1507-" + getRandom().nextInt(1, 24));
    }

    @Override
    protected Map<Need, NeedInstance> initializeNeeds() {
        return Map.of(
                Need.SOCIALIZATION, new NeedInstance(0.001f, value -> Math.pow(value, 1.2), BitterActivity.SOCIALIZE.get()),
                Need.ANGER, new NeedInstance(0f, value -> Math.pow(value, 2.0), Activity.FIGHT)
        );
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(MAX_HEALTH, 5)
                .add(Attributes.MOVEMENT_SPEED, 0.23f)
                .add(Attributes.ATTACK_DAMAGE, 4.0f);
    }

    @Override
    public List<? extends ExtendedSensor<?>> getSensors(SCP1507 owner) {
        return List.of(
                new NearbyLivingEntitySensor<>(),
                new HurtBySensor<>(),
                new NearbyBlocksSensor<SCP1507>().detectionRadius(10, 2)
        );
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new SmoothGroundNavigation(this, level);
    }

    @Override
    public List<? extends BehaviorControl<?>> getAlwaysRunningBehaviours(SCP1507 owner) {
        return List.of(
                new LookAtTarget<>(),
                new MoveToWalkTarget<>(),
                new InvalidateBreakTarget<>(),
                new ReactToUnreachableTarget<>()
                        .reaction((_, _) -> new FindBlockingBlock<>()),
                new LeapAndHurtBlock<SCP1507>(0)
                        .whenStarting(SCP1507::resetAttackTime)
                        .startCondition((entity) ->
                                BrainUtil.getMemory(entity, BitterMemoryTypes.BREAK_TARGET.get()).distSqr(entity.getOnPos()) <= 4),
                new TargetOrRetaliate<>()
                        .attackablePredicate(target -> !(target instanceof SCP1507))
                        .alertAlliesWhen((_, _) -> true)
        );
    }

    @Override
    public List<? extends BehaviorControl<?>> getIdleBehaviours(SCP1507 owner) {
        return List.of(
                new VerifyOrFindLeader<>(),
                new TryToBecomeActive().cooldownForBetween(600, 1200),
                new StopMovingWhenLookedAt(),
                new FindRandomBreakTarget<>()
                        .cooldownForBetween(60, 120),
                new OneRandomBehaviour<>(
                        new FirstApplicableBehaviour<>(
                                new FollowEntity<>()
                                        .following((entity) -> BrainUtil.getMemory(entity, BitterMemoryTypes.LEADER.get()))
                                        .stopFollowingWithin((entity, _) -> getFollowDistance(entity)),
                                new SetRandomWalkTarget<>().startCondition((SCP1507::isActive))
                        ),
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))
                )
        );
    }

    @Override
    public List<? extends BehaviorControl<?>> getFightingBehaviours(SCP1507 owner) {
        return List.of(
                new InvalidateAttackTarget<>(),
                new SetWalkTargetToAttackTarget<>(),
                new OneRandomBehaviour<>(
                        Pair.of(new LeapAtTarget<SCP1507>(0)
                                        .whenStarting(SCP1507::resetAttackTime)
                                        .startCondition((entity) ->
                                                BrainUtil.getTargetOfEntity(entity).distanceTo(entity) < 4)
                                        .whenStopping(SCP1507::attemptEmbedLeg),
                                10),
                        Pair.of(new CollectivePush<>(10, 5.0, 0), 1)
                ).whenStopping(SCP1507::setActive)
        );
    }

    @Override
    public void aiStep() {
        super.aiStep();
        int currentAttackTime = getAttackTime();
        if (currentAttackTime > 0) {
            entityData.set(ATTACK_TIME, currentAttackTime - 1);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACK_TIME, 0);
        builder.define(LEFT_LEG_ATTACHED, true);
        builder.define(RIGHT_LEG_ATTACHED, true);
    }

    public int getAttackTime() {
        return entityData.get(ATTACK_TIME);
    }

    public static void resetAttackTime(@NotNull SCP1507 entity) {
        entity.entityData.set(ATTACK_TIME, 20);
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if (deathTime <= 1) {
            makeShatterParticles(100);
        }
    }

    @Override
    public void onDamageTaken(@NotNull DamageContainer damageContainer) {
        super.onDamageTaken(damageContainer);
        makeShatterParticles(Math.min(100, (int) (damageContainer.getNewDamage() * 5)));
    }

    private void makeShatterParticles(int count) {
        if (!(level() instanceof ServerLevel level)) return;
        level.sendParticles(
                ColorParticleOption.create(BitterParticles.PLASTIC.get(), 0xFFF8748D),
                getX(),
                getY() + 0.4,
                getZ(),
                count,
                0.2,
                0.1,
                0.2,
                0.2
        );
    }

    private static float getFollowDistance(LivingEntity entity) {
        return isActive(entity) ? 4.0f : (float) entity.getAttributeValue(Attributes.FOLLOW_RANGE) / 1.5f;
    }

    public static boolean isActive(LivingEntity entity) {
        return Boolean.TRUE.equals(BrainUtil.getMemory(entity, BitterMemoryTypes.ACTIVE.get()));
    }

    public static void setActive(LivingEntity entity) {
        BrainUtil.setForgettableMemory(entity, BitterMemoryTypes.ACTIVE.get(), true, 1200);
    }

    public boolean isLeftLegAttached() {
        return entityData.get(LEFT_LEG_ATTACHED);
    }

    public boolean isRightLegAttached() {
        return entityData.get(RIGHT_LEG_ATTACHED);
    }

    public void attemptEmbedLeg() {
        if (random.nextFloat() < 0.1f) {
            StumbleHandler.stumble(this, 1000, getLookAngle());
            if (random.nextBoolean()) {
                entityData.set(LEFT_LEG_ATTACHED, false);
            } else {
                entityData.set(RIGHT_LEG_ATTACHED, false);
            }
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(BitterItems.PINK_PLASTIC_SCRAP)) {
            return attemptRepair(stack, player);
        } else if (stack.is(BitterItems.METAL_ROD)) {
            if (!entityData.get(LEFT_LEG_ATTACHED)) {
                repairLeg(stack, player, true);
                return InteractionResult.SUCCESS;
            } else if (!entityData.get(RIGHT_LEG_ATTACHED)) {
                repairLeg(stack, player, false);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }

        return super.mobInteract(player, hand);
    }

    private InteractionResult attemptRepair(ItemStack stack, Player player) {
        float healthBefore = getHealth();
        heal(getMaxHealth() / 3);

        if (getHealth() != healthBefore) {
            float pitch = 1.0f + (random.nextFloat() - random.nextFloat()) * 0.2f;
            playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0f, pitch);
            stack.consume(1, player);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    private void repairLeg(ItemStack legItem, Player player, boolean left) {
        float pitch = 1.0f + (random.nextFloat() - random.nextFloat()) * 0.2f;
        playSound(SoundEvents.ITEM_PICKUP, 1.0f, pitch);
        legItem.consume(1, player);

        if (left) {
            entityData.set(LEFT_LEG_ATTACHED, true);
        } else {
            entityData.set(RIGHT_LEG_ATTACHED, true);
        }
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.ZOMBIE_ATTACK_WOODEN_DOOR;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR;
    }

    @Override
    public float getVoicePitch() {
        return random.nextFloat() * 0.2f + 1.5f;
    }

    @Override
    protected float getSoundVolume() {
        return 0.2f;
    }
}
