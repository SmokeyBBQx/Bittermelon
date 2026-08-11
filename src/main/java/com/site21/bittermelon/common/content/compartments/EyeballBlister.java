package com.site21.bittermelon.common.content.compartments;

import com.site21.bittermelon.common.systems.medical.legacy.compartment.Compartment;
import com.site21.bittermelon.common.systems.medical.legacy.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.legacy.medicalstats.MedicalStats;
import org.jetbrains.annotations.NotNull;

public class EyeballBlister extends Compartment {
    public EyeballBlister(String id, @NotNull Properties properties) {
        super(id, properties);
    }

    @Override
    protected boolean shouldTick(MedicalStats medicalStats, CompartmentInstance instance, long gameTime) {
        return gameTime % 20 == 0;
    }

    @Override
    public void tick(MedicalStats medicalStats, @NotNull CompartmentInstance instance) {
    }
}
