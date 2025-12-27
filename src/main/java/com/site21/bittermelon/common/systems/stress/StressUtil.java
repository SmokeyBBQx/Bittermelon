package com.site21.bittermelon.common.systems.stress;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.common.systems.stress.StressEventHandler.triggerStressEvent;
import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.STRESS;

public class StressUtil {
    public static void updateStress(@NotNull Player player, int delta) {
        int currentStress = player.getData(STRESS);
        int newStress = currentStress + delta;
        int levelIncrease = newStress / 100 - currentStress / 100;

        if (levelIncrease > 0) {
            for (int i = 1; i <= levelIncrease; i++) {
                triggerStressEvent(player, i);
            }
        }

        setStress(player, currentStress + delta);
    }

    public static void setStress(@NotNull Player player, int stress) {
        player.setData(STRESS, stress);
    }

    public static void setStressLevel(Player player, int level) {
        setStress(player, level * 100);
        triggerStressEvent(player, level);
    }


}
