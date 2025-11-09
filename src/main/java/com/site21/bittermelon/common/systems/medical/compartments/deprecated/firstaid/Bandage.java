package com.site21.bittermelon.common.systems.medical.compartments.deprecated.firstaid;

import com.site21.bittermelon.common.systems.medical.compartments.MedicalAttribute;
import com.site21.bittermelon.common.systems.medical.compartments.deprecated.CompartmentOld;
import com.site21.bittermelon.common.systems.medical.compartments.CompartmentTag;
import com.site21.bittermelon.common.systems.medical.compartments.deprecated.conditionsold.Bleed;
import com.site21.bittermelon.common.systems.medical.medicalstats.deprecated.MedicalStatsOld;

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
            setAttribute(MedicalAttribute.BLEED, 0f);
            return;
        }

        float healthPercentage = getHealth() / maxHealth;
        float totalBleed = 0;
        for (CompartmentOld compartment : owner.getChildren()) {
            if (compartment instanceof Bleed bleed) {
                totalBleed += bleed.getAttribute(MedicalAttribute.BLEED);
            }
        }
        setAttribute(MedicalAttribute.BLEED, -totalBleed * healthPercentage * 0.9f);
    }

    @Override
    public void onDeath(MedicalStatsOld mammalMedicalStats) {
        super.onDeath(mammalMedicalStats);
        mammalMedicalStats.addCompartment(new OldBandage("Old Bandage", owner, maxHealth));
    }
}
