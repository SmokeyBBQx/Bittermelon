package com.site21.bittermelon.common.systems.stress;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.STRESS_RELIEF;
import static com.site21.bittermelon.init.neoforge.BitterMobEffects.EUPHORIA;

public class StressHandler {
    public static void tickStress(@NotNull Player player) {
        if (player.getData(STRESS_RELIEF) == 0 || player.hasEffect(EUPHORIA)) return;

        if (player.level().getGameTime() % 40 == 0) {
            StressUtil.updateStressRelief(player, -0.01f);
        }
    }

    public static void triggerStressEvent(@NotNull Player player, int level) {
        player.playNotifySound(SoundEvents.ITEM_BREAK.value(), SoundSource.UI, 1.0f, 0.5f - (level - 1) / 10f);
    }
}
