package com.site21.bittermelon.content.entities.ai.behavior.attack;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.stumble.StumbleHandler;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterSounds.DRAG;

public class Pull<E extends Mob> extends AnimatableMeleeAttack<E> {

    public Pull(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        super.checkExtraStartConditions(level, entity);

        assert target != null;
        return StumbleHandler.isStumbled(target) && !StumbleHandler.isStumbled(entity);
    }

    @Override
    protected void doDelayedAction(@NotNull E entity) {
        BrainUtil.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, this.attackIntervalSupplier.applyAsInt(entity));

        if (this.target == null)
            return;

        if (!entity.getSensing().hasLineOfSight(this.target) || !entity.isWithinMeleeAttackRange(this.target))
            return;

        Vec3 pullDirection = entity.getLookAngle().multiply(-2, 1, -2);
        target.setDeltaMovement(pullDirection);
        target.hurtMarked = true;

        entity.level().playSound(null, entity.getOnPos(), DRAG.get(), SoundSource.AMBIENT);

        CharacterManager characterManager = CharacterManager.get(entity.level());
        Character entityCharacter = characterManager.getActiveCharacter(entity);
        Character targetCharacter = characterManager.getActiveCharacter(target);

        if (entityCharacter != null && targetCharacter != null) {
            int textColor = entityCharacter.getEmoteColor();

            LocalMessageHelper.sendLocalMessage(entity, 10, Component.literal(
                    entityCharacter.getName() + " pulls " + targetCharacter.getName() + ".").withColor(textColor));
        }
    }
}
