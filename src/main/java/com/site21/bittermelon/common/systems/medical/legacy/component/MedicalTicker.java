package com.site21.bittermelon.common.systems.medical.legacy.component;

import com.site21.bittermelon.common.systems.medical.legacy.medicalstats.MedicalStats;
import net.minecraft.world.level.Level;

public interface MedicalTicker {
    void tick(MedicalStats stats, Level level);
}
