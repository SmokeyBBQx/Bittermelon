package com.site21.bittermelon.common.systems.stress;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.common.systems.stress.StressHandler.triggerStressEvent;
import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.STRESS;
import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.STRESS_RELIEF;

public class StressUtil {
    public static final int MAX_STRESS = 400;

    public static void updateStress(@NotNull Player player, int delta) {
        int currentStress = player.getData(STRESS);
        int newStress = Mth.clamp(currentStress + delta, 0, MAX_STRESS);
        float stressRelief = 1 - player.getData(STRESS_RELIEF);
        int levelIncrease = (int) (((float) newStress / 100 - (float) currentStress / 100) * stressRelief);

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

    public static void updateStressRelief(@NotNull Entity entity, float delta) {
        float currentStressRelief = entity.getData(STRESS_RELIEF);
        float newStressRelief = Mth.clamp(currentStressRelief + delta, 0, 1);
        entity.setData(STRESS_RELIEF, newStressRelief);
    }
}
