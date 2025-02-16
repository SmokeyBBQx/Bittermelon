package com.site21.bittermelon.content.medical.compartments.firstaid;

import com.site21.bittermelon.content.medical.compartments.Compartment;
import com.site21.bittermelon.content.medical.compartments.CompartmentType;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;

import java.util.EnumSet;

public class FirstAid extends Compartment {
    public FirstAid(EnumSet<CompartmentType> types, String name, Compartment owner, float maxHealth, float quality) {
        super(types, name, owner, maxHealth);
        hidden = false;
    }

    @Override
    public void onDeath(MedicalStats medicalStats) {
        medicalStats.removeCompartment(this);
    }

    @Override
    public boolean canExtract() {
        return true;
    }
}
