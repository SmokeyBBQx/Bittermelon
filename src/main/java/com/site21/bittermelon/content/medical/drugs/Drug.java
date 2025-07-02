package com.site21.bittermelon.content.medical.drugs;

import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import org.jetbrains.annotations.NotNull;

public class Drug {
    private final float eliminationRate;
    private final float absorptionRate;

    public Drug(float eliminationRate, float absorptionRate) {
        this.eliminationRate = eliminationRate;
        this.absorptionRate = absorptionRate;
    }

    public boolean shouldApplyTick(int duration) {
        return true;
    }

    public void tickDrug(@NotNull MedicalStats medicalStats, float amount) {


    }

    public void onRemoval(MedicalStats medicalStats) {

    }

    public float getEliminationRate() {
        return eliminationRate;
    }

    public float getAbsorptionRate() {
        return absorptionRate;
    }
}
