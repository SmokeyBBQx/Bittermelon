package com.site21.bittermelon.medical.compartments.firstaid;

import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.compartments.FunctionType;
import com.site21.bittermelon.medical.compartments.conditions.Bleed;
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
        modifyHealth(-0.0001f);

        if (getHealth() <= 0) {
            setAttribute(FunctionType.BLEED, 0f);
            return;
        }

        float healthPercentage = getHealth() / maxHealth;
        float totalBleed = 0;
        for (Compartment compartment : owner.getChildren()) {
            if (compartment instanceof Bleed bleed) {
                totalBleed += bleed.getAttribute(FunctionType.BLEED);
            }
        }
        setAttribute(FunctionType.BLEED, -totalBleed * healthPercentage * 0.9f);
    }

    @Override
    public void onDeath(MedicalStats mammalMedicalStats) {
        super.onDeath(mammalMedicalStats);
        mammalMedicalStats.addCompartment(new OldBandage("Old Bandage", owner, maxHealth));
    }
}
