package com.site21.bittermelon.common.content.blocks;

import com.site21.bittermelon.init.neoforge.BitterSounds;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DoorHelper {

    /**
     * Handles knocking on a door when the player is sneaking and not on cooldown.
     * @param level the level the player is in
     * @param player the player attempting to knock
     * @return true if knocking was handled, false otherwise
     */
    public static boolean handleKnocking(Level level, @NotNull Player player) {
        if (!player.isShiftKeyDown()) return false;

        ItemCooldowns cooldowns = player.getCooldowns();
        if (cooldowns.isOnCooldown(player.getMainHandItem().getItem()) ||
                cooldowns.isOnCooldown(player.getOffhandItem().getItem())) return false;

        if (level.isClientSide) return true;

        level.playSound(null, player.getOnPos(), BitterSounds.KNOCK.value(), SoundSource.PLAYERS, 1.0f, 1.0f);
        LocalMessageHelper.sendEmoteMessage(level, player, 10, "knocks on the door.");

        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            cooldowns.addCooldown(stack.getItem(), 10);
        }

        return true;
    }
}
