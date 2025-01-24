package com.site21.bittermelon.entities.ai.behavior.attack;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.character.CharacterManager;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import org.jetbrains.annotations.NotNull;

public class YankItem<E extends Mob> extends AnimatableMeleeAttack<E> {

    public YankItem(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        super.checkExtraStartConditions(level, entity);

        assert target != null;
        return target.hasItemInSlot(EquipmentSlot.MAINHAND) || target.hasItemInSlot(EquipmentSlot.OFFHAND);
    }

    @Override
    protected void doDelayedAction(E entity) {
        if (target == null) return;
//        if (entity.getRandom().nextFloat() <= 0.25f) return;

        if (target.hasItemInSlot(EquipmentSlot.MAINHAND)) {
            attemptToGrab(EquipmentSlot.MAINHAND, target, entity);
        } else if (target.hasItemInSlot(EquipmentSlot.OFFHAND)) {
            attemptToGrab(EquipmentSlot.OFFHAND, target, entity);
        }
    }

    private void attemptToGrab(EquipmentSlot slot, @NotNull LivingEntity target, E attacker) {
        ItemStack item = target.getItemBySlot(slot);
        if (target instanceof Player player) {
            player.drop(item, true);
        } else {
            ItemEntity itemEntity = new ItemEntity(target.level(), target.getX(), target.getY(), target.getZ(), item);
            target.level().addFreshEntity(itemEntity);
        }
        target.setItemSlot(slot, Items.AIR.getDefaultInstance());

        CharacterManager characterManager = CharacterManager.getInstance();
        Character entityCharacter = characterManager.getActiveCharacter(attacker.getUUID());
        Character targetCharacter = characterManager.getActiveCharacter(target.getUUID());

        if (entityCharacter != null && targetCharacter != null) {
            int textColor = entityCharacter.getEmoteColor();

            LocalMessageHelper.sendLocalMessage(attacker, 10, Component.literal(
                    entityCharacter.getName() + " yanks " + targetCharacter.getName() + "'s " +
                            item.getHoverName().getString().toLowerCase() + ".").withColor(textColor));
        }
    }
}
