package com.site21.bittermelon.common.systems.stress;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class StressEventHandler {
    public static void triggerStressEvent(@NotNull Player player, int level) {
        player.playNotifySound(SoundEvents.ITEM_BREAK.value(), SoundSource.UI, 1.0f, 0.5f - (level - 1) / 10f);
    }
}
