package com.site21.bittermelon.entities.ai.personality;

import java.util.EnumMap;

public class Personality {
    private final EnumMap<PersonalityStat, Float> stats = new EnumMap<>(PersonalityStat.class);

    public Personality() {}

    public float getStat(PersonalityStat stat) {
        return stats.getOrDefault(stat, 0.0f);
    }

    public void modifyStat(PersonalityStat stat, float value) {
        stats.computeIfPresent(stat, (k, currentValue) -> currentValue + value);
    }
}
