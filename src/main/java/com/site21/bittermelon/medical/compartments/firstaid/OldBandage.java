package com.site21.bittermelon.medical.compartments.firstaid;

import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.medicalstats.MedicalStats;

import java.util.EnumSet;
import java.util.Set;

public class OldBandage extends FirstAid {
    public OldBandage(String name, Compartment owner, float maxHealth) {
        super(EnumSet.of(CompartmentType.OLD_BANDAGE), name, owner, maxHealth, 0);
    }

    @Override
    public void update(MedicalStats medicalStats) {
        super.update(medicalStats);
        modifyHealth(-0.01f);
    }

    @Override
    public void onDeath(MedicalStats medicalStats) {
        // TODO: Infection
    }
}
