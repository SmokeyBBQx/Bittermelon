package com.site21.bittermelon.content.medical.drugs.implementations;

import com.site21.bittermelon.content.medical.drugs.Drug;
import com.site21.bittermelon.content.medical.medicalstats.AnimalMedicalStats;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import org.jetbrains.annotations.NotNull;

public class CyanideDrug extends Drug {
    public CyanideDrug() {
        super(2, 5);
    }

    public void tickDrug(@NotNull MedicalStats medicalStats, float amount) {
        if (medicalStats instanceof AnimalMedicalStats stats) {
            stats.modifyOxygenSaturation(-0.01f * amount);
        }
    }
}
