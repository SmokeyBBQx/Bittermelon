package com.site21.bittermelon.common.systems.ai.behavior.attack;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.stumble.StumbleHandler;
import com.site21.bittermelon.util.LocalMessageUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterSounds.WRESTLE;

public class Push<E extends Mob> extends AnimatableMeleeAttack<E> {
    public Push(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        super.checkExtraStartConditions(level, entity);

        assert target != null;
        return !StumbleHandler.isStumbled(target) && !StumbleHandler.isStumbled(entity);
    }

    @Override
    protected void doDelayedAction(@NotNull E entity) {
        BrainUtil.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, attackInterval.applyAsInt(entity, target));

        if (target == null)
            return;

        if (!entity.getSensing().hasLineOfSight(target) || !entity.isWithinMeleeAttackRange(target))
            return;

        entity.level().playSound(null, entity.getOnPos(), WRESTLE.value(), SoundSource.AMBIENT);

        CharacterManager characterManager = CharacterManager.get(entity.level());
        Character entityCharacter = characterManager.getActiveCharacter(entity);
        Character targetCharacter = characterManager.getActiveCharacter(target);

        if (entityCharacter != null && targetCharacter != null) {
            int textColor = entityCharacter.getEmoteColor();

            LocalMessageUtil.sendLocalMessage(entity, 10, Component.literal(
                    entityCharacter.getName() + " pushes " + targetCharacter.getName() + ".").withColor(textColor));
        }

        StumbleHandler.stumble(target, entity.getLookAngle());
    }
}
