package com.site21.bittermelon.common.systems.stress;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.common.systems.stress.StressEventHandler.triggerStressEvent;
import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.STRESS;

public class StressUtil {
    public static final int MAX_STRESS = 400;

    public static void updateStress(@NotNull Player player, int delta) {
        int currentStress = player.getData(STRESS);
        int newStress = Mth.clamp(currentStress + delta, 0, MAX_STRESS);
        int levelIncrease = newStress / 100 - currentStress / 100;

        if (levelIncrease > 0) {
            for (int i = 1; i <= levelIncrease; i++) {
                triggerStressEvent(player, i);
            }
        }

        setStress(player, currentStress + delta);
    }

    public static void setStress(@NotNull Player player, int stress) {
        player.setData(STRESS, Mth.clamp(stress, 0, MAX_STRESS));
    }

    public static void setStressLevel(Player player, int level) {
        setStress(player, Mth.clamp(level * 100, 0, MAX_STRESS));
        triggerStressEvent(player, level);
    }
}
