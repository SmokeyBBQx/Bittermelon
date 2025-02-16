package com.site21.bittermelon.content.medical.compartments.firstaid;

import com.site21.bittermelon.content.medical.compartments.Compartment;
import com.site21.bittermelon.content.medical.compartments.CompartmentType;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;

import java.util.EnumSet;

public class OldBandage extends FirstAid {
    public OldBandage(String name, Compartment owner, float maxHealth) {
        super(EnumSet.of(CompartmentType.OLD_BANDAGE), name, owner, maxHealth, 0);
    }

    @Override
    public void update(MedicalStats medicalStats) {
        super.update(medicalStats);
    }

    @Override
    public void onDeath(MedicalStats medicalStats) {
        // TODO: Infection
    }
}
