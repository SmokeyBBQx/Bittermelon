package com.site21.bittermelon.content.substance.medical;

import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.world.entity.LivingEntity;

public abstract class SubstanceEffect {
    public abstract void tick(MedicalStats medicalStats, LivingEntity entity);
}
