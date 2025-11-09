package com.site21.bittermelon.common.systems.medical.compartments.deprecated.firstaid;

import com.site21.bittermelon.common.systems.medical.compartments.deprecated.CompartmentOld;
import com.site21.bittermelon.common.systems.medical.compartments.CompartmentTag;
import com.site21.bittermelon.common.systems.medical.medicalstats.deprecated.MedicalStatsOld;

import java.util.EnumSet;

public class FirstAid extends CompartmentOld {
    public FirstAid(EnumSet<CompartmentTag> types, String name, CompartmentOld owner, float maxHealth, float quality) {
        super(types, name, owner, maxHealth);
        hidden = false;
    }

    @Override
    public void onDeath(MedicalStatsOld medicalStats) {
        medicalStats.removeCompartment(this);
    }

    @Override
    public boolean canExtract() {
        return true;
    }
}
