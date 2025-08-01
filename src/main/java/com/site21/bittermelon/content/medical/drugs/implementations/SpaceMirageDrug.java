package com.site21.bittermelon.content.medical.drugs.implementations;

import com.site21.bittermelon.content.medical.drugs.Drug;
import com.site21.bittermelon.content.medical.medicalstats.AnimalMedicalStats;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import org.jetbrains.annotations.NotNull;

public class SpaceMirageDrug extends Drug {
    public SpaceMirageDrug(float eliminationRate, float absorptionRate) {
        super(eliminationRate, absorptionRate);
    }

    public void tickDrug(@NotNull MedicalStats medicalStats, float amount) {
        if (medicalStats instanceof AnimalMedicalStats stats) {

        }
    }
}
