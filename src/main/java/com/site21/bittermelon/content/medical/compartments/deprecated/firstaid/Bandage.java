package com.site21.bittermelon.content.medical.compartments.deprecated.firstaid;

import com.site21.bittermelon.content.medical.compartments.deprecated.CompartmentOld;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.FunctionType;
import com.site21.bittermelon.content.medical.compartments.deprecated.conditionsold.Bleed;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStatsOld;

import java.util.EnumSet;

public class Bandage extends FirstAid {
    public Bandage(String name, CompartmentOld owner, float maxHealth, float quality) {
        super(EnumSet.of(CompartmentTag.BANDAGE), name, owner, maxHealth, quality);
    }

    @Override
    public void update(MedicalStatsOld medicalStats) {
        super.update(medicalStats);
        modifyHealth(-0.0001f);

        if (getHealth() <= 0) {
            setAttribute(FunctionType.BLEED, 0f);
            return;
        }

        float healthPercentage = getHealth() / maxHealth;
        float totalBleed = 0;
        for (CompartmentOld compartment : owner.getChildren()) {
            if (compartment instanceof Bleed bleed) {
                totalBleed += bleed.getAttribute(FunctionType.BLEED);
            }
        }
        setAttribute(FunctionType.BLEED, -totalBleed * healthPercentage * 0.9f);
    }

    @Override
    public void onDeath(MedicalStatsOld mammalMedicalStats) {
        super.onDeath(mammalMedicalStats);
        mammalMedicalStats.addCompartment(new OldBandage("Old Bandage", owner, maxHealth));
    }
}
