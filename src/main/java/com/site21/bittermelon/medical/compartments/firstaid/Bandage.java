package com.site21.bittermelon.medical.compartments.firstaid;

import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.medicalstats.MedicalStats;

import java.util.EnumSet;
import java.util.Set;

public class Bandage extends FirstAid {
    public Bandage(String name, Compartment owner, float maxHealth, float quality) {
        super(EnumSet.of(CompartmentType.BANDAGE), name, owner, maxHealth, quality);
    }

    @Override
    public void update(MedicalStats medicalStats) {
        super.update(medicalStats);
        owner.modifyHealth(-0.01f);
        modifyHealth(-0.01f);
    }

    @Override
    public void onDeath(MedicalStats mammalMedicalStats) {
        super.onDeath(mammalMedicalStats);
        mammalMedicalStats.addCompartment(new OldBandage("Old Bandage", owner, maxHealth));
    }
}
