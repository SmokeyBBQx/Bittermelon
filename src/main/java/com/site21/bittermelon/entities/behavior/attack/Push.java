package com.site21.bittermelon.entities.behavior.attack;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.character.CharacterManager;
import com.site21.bittermelon.miscellaneous.stumble.StumbleHandler;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.BitterSounds.STAB;
import static com.site21.bittermelon.init.BitterSounds.WRESTLE;

public class Push<E extends Mob> extends AnimatableMeleeAttack<E> {
    public Push(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        super.checkExtraStartConditions(level, entity);

        assert target != null;
        return !StumbleHandler.containsUUID(target.getUUID()) && !StumbleHandler.containsUUID(entity.getUUID());
    }

    @Override
    protected void doDelayedAction(@NotNull E entity) {
        BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, this.attackIntervalSupplier.apply(entity));

        if (this.target == null)
            return;

        if (!entity.getSensing().hasLineOfSight(this.target) || !entity.isWithinMeleeAttackRange(this.target))
            return;

        entity.level().playSound(null, entity.getOnPos(), WRESTLE.get(), SoundSource.AMBIENT);

        CharacterManager characterManager = CharacterManager.getInstance();
        Character entityCharacter = characterManager.getActiveCharacter(entity.getUUID());
        Character targetCharacter = characterManager.getActiveCharacter(target.getUUID());

        if (entityCharacter != null && targetCharacter != null) {
            int textColor = entityCharacter.getEmoteColor();

            LocalMessageHelper.sendLocalMessage(entity, 10, Component.literal(
                    entityCharacter.getName() + " pushes " + targetCharacter.getName() + ".").withColor(textColor));
        }

        StumbleHandler.stumble(target, entity.getLookAngle());
    }
}
