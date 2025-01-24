package com.site21.bittermelon.entities.ai.behavior.target;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.character.CharacterManager;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class InvalidateAttackTarget<E extends LivingEntity> extends net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget<E> {
    @Override
    protected boolean isTargetInvalid(@NotNull E entity, @NotNull LivingEntity target) {
        if (entity.level() != target.level())
            return true;

        Character character = CharacterManager.getInstance().getActiveCharacter(target.getUUID());
        if (character != null) {
            return character.getMedicalStats().getConsciousness() <= 0;
        }

        return target.isDeadOrDying() || target.isRemoved();
    }
}
