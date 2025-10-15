package com.site21.bittermelon.content.drugs;

import com.site21.bittermelon.systems.medical.drug.Drug;
import com.site21.bittermelon.systems.medical.medicalstats.AnimalMedicalStats;
import com.site21.bittermelon.systems.medical.medicalstats.MedicalStats;
import org.jetbrains.annotations.NotNull;

public class CyanideDrug extends Drug {
    public CyanideDrug() {
        super(0.0002f, 0.05f);
    }

    public void tickDrug(@NotNull MedicalStats medicalStats, float amount) {
        if (medicalStats instanceof AnimalMedicalStats stats) {
            stats.modifyOxygenSaturation(-0.1f * amount);
        }
    }
}
