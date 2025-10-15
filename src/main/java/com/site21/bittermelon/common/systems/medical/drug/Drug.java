package com.site21.bittermelon.common.systems.medical.drug;

import com.site21.bittermelon.common.systems.medical.compartment.MedicalAttribute;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public class Drug {
    protected final float eliminationRate;
    protected final float absorptionRate;
    protected final EnumMap<MedicalAttribute, Float> attributes;

    public Drug(float eliminationRate, float absorptionRate) {
        this.eliminationRate = eliminationRate;
        this.absorptionRate = absorptionRate;
        attributes = new EnumMap<>(MedicalAttribute.class);
    }

    public boolean shouldApplyTick(int duration) {
        return true;
    }

    public void tickDrug(@NotNull MedicalStats medicalStats, float amount) {}

    public void onAdded(MedicalStats medicalStats) {}

    public void onRemoval(MedicalStats medicalStats) {}

    public float getEliminationRate() {
        return eliminationRate;
    }

    public float getAbsorptionRate() {
        return absorptionRate;
    }

    public EnumMap<MedicalAttribute, Float> getAttributes() {
        return attributes;
    }

    public Drug setAttribute(MedicalAttribute attribute, float value) {
        attributes.put(attribute, value);
        return this;
    }
}
