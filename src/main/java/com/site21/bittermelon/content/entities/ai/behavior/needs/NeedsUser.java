package com.site21.bittermelon.content.entities.ai.behavior.needs;

import com.site21.bittermelon.content.entities.base.NeedsStat;
import com.site21.bittermelon.content.entities.base.StatConfig;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface NeedsUser<E extends LivingEntity & NeedsUser<E>> {
    List<Need<E>> getNeeds();
    float getMood();

    float getStat(@NotNull NeedsStat stat);
    void setStat(@NotNull NeedsStat stat, float value);
    default void modifyStat(NeedsStat stat, float amount) {
        setStat(stat, getStat(stat) + amount);
    }

    default void updateStress() {
        if (getMood() < 40) {
            modifyStat(NeedsStat.STRESS, -0.01f);
        }
    }
}
