package com.site21.bittermelon.substance.medical;

import com.site21.bittermelon.medical.medicalstats.MedicalStats;
import net.minecraft.world.entity.LivingEntity;

public abstract class SubstanceEffect {
    public abstract void tick(MedicalStats medicalStats, LivingEntity entity);
}
