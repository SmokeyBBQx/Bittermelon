package com.site21.bittermelon.common.systems.throwing;

import com.site21.bittermelon.common.content.entities.ThrownItemProjectile;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.util.LocalMessageHelper.sendLocalMessage;

public class ThrowItem {
    public static void throwItem(@NotNull Player player) {
        Level level = player.level();
        ItemStack heldItem = player.getMainHandItem();
        if (heldItem.isEmpty()) return;

        playThrowSound(level, player);
        spawnProjectile(level, player, heldItem);
        sendEmoteMessage(level, player, heldItem);
        heldItem.shrink(1);
    }

    private static void playThrowSound(@NotNull Level level, @NotNull Player player) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW,
                SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    private static void spawnProjectile(Level level, Player player, @NotNull ItemStack heldItem) {
        ThrownItemProjectile projectile = new ThrownItemProjectile(player, level, heldItem.copy());
        projectile.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
        projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1f, 1.0f);
        level.addFreshEntity(projectile);
    }

    private static void sendEmoteMessage(Level level, Player player, ItemStack heldItem) {
        Character character = CharacterManager.get(level).getActiveCharacter(player);
        if (character != null) {
            Component component = Component.literal(character.getName() + " throws " + heldItem.getHoverName().getString().toLowerCase() + ".")
                    .setStyle(Style.EMPTY.withColor(character.getEmoteColor()));
            sendLocalMessage(player, 10, component);
        }
    }
}
