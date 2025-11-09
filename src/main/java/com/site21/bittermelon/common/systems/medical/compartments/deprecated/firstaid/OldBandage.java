package com.site21.bittermelon.common.systems.medical.compartments.deprecated.firstaid;

import com.site21.bittermelon.common.systems.medical.compartments.deprecated.CompartmentOld;
import com.site21.bittermelon.common.systems.medical.compartments.CompartmentTag;
import com.site21.bittermelon.common.systems.medical.medicalstats.deprecated.MedicalStatsOld;

import java.util.EnumSet;

public class OldBandage extends FirstAid {
    public OldBandage(String name, CompartmentOld owner, float maxHealth) {
        super(EnumSet.of(CompartmentTag.OLD_BANDAGE), name, owner, maxHealth, 0);
    }

    @Override
    public void update(MedicalStatsOld medicalStats) {
        super.update(medicalStats);
    }

    @Override
    public void onDeath(MedicalStatsOld medicalStats) {
        // TODO: Infection
    }
}
