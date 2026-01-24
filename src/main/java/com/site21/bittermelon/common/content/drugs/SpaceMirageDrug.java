package com.site21.bittermelon.common.content.drugs;

import com.site21.bittermelon.common.systems.medical.drug.Drug;
import com.site21.bittermelon.common.systems.medical.medicalstats.AnimalMedicalStats;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import org.jetbrains.annotations.NotNull;

public class SpaceMirageDrug extends Drug {
    public SpaceMirageDrug(float eliminationRate, float absorptionRate) {
        super(eliminationRate, absorptionRate);
    }

    public void tickDrug(@NotNull MedicalStats medicalStats, float amount) {
        if (medicalStats instanceof AnimalMedicalStats stats) {
//            stats.getEntity().addEffect(new MobEffectInstance(HALLUCINATION, 2, 0, true, false, false));
        }
    }
}
