package com.site21.bittermelon.entities.scps.SCP939.behavior;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.character.CharacterManager;
import com.site21.bittermelon.miscellaneous.StumbleHandler;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Mob;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import org.jetbrains.annotations.NotNull;

public class Push<E extends Mob> extends AnimatableMeleeAttack<E> {
    public Push(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected void doDelayedAction(@NotNull E entity) {
        StumbleHandler.stumble(target, entity.getLookAngle());

        if (target != null) {
            CharacterManager characterManager = CharacterManager.getInstance();
            Character entityCharacter = characterManager.getActiveCharacter(entity.getUUID());
            Character targetCharacter = characterManager.getActiveCharacter(target.getUUID());
            if (entityCharacter != null && targetCharacter != null) {
                LocalMessageHelper.sendLocalMessage(entity, 10, Component.literal(
                        entityCharacter.getName() + "pushes " + targetCharacter.getName() + "."));
//                        .setStyle(Style.EMPTY.withColor(TextColor.parseColor(
//                                "#" + entityCharacter.getEmoteColor()).getOrThrow())));
            }
        }
    }
}
