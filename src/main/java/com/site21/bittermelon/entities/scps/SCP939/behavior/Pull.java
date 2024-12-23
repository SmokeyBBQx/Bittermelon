package com.site21.bittermelon.entities.scps.SCP939.behavior;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.character.CharacterManager;
import com.site21.bittermelon.miscellaneous.stumble.StumbleHandler;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class Pull<E extends Mob> extends AnimatableMeleeAttack<E> {

    public Pull(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        super.checkExtraStartConditions(level, entity);

        return StumbleHandler.containsUUID(target.getUUID()) && !StumbleHandler.containsUUID(entity.getUUID());
    }

    @Override
    protected void doDelayedAction(@NotNull E entity) {
        BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, this.attackIntervalSupplier.apply(entity));

        if (this.target == null)
            return;

        if (!entity.getSensing().hasLineOfSight(this.target) || !entity.isWithinMeleeAttackRange(this.target))
            return;

        Vec3 pullDirection = entity.getLookAngle().multiply(-2, 1, -2);
        target.setDeltaMovement(pullDirection);
        target.hurtMarked = true;

        CharacterManager characterManager = CharacterManager.getInstance();
        Character entityCharacter = characterManager.getActiveCharacter(entity.getUUID());
        Character targetCharacter = characterManager.getActiveCharacter(target.getUUID());

        if (entityCharacter != null && targetCharacter != null) {
            LocalMessageHelper.sendLocalMessage(entity, 10, Component.literal(
                    entityCharacter.getName() + " pulls " + targetCharacter.getName() + "."));
        }
    }
}
