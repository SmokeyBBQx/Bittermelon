package com.site21.bittermelon.common.systems.medical.component;

import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import net.minecraft.world.level.Level;

public interface MedicalTicker {
    void tick(MedicalStats stats, Level level);
}
