package com.site21.bittermelon.systems.ai.base;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface NeedsUser {
    Map<Need, NeedInstance> getNeeds();
    float getMood();

    float getNeed(@NotNull Need stat);
    void setNeed(@NotNull Need stat, float value);
    default void modifyNeed(Need stat, float amount) {
        setNeed(stat, getNeed(stat) + amount);
    }

    default void updateStress() {
        if (getMood() < 40) {
            modifyNeed(Need.STRESS, -0.01f);
        }
    }
}
