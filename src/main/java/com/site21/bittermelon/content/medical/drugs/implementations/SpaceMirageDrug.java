package com.site21.bittermelon.content.medical.drugs.implementations;

import com.site21.bittermelon.content.medical.drugs.Drug;
import com.site21.bittermelon.content.medical.medicalstats.AnimalMedicalStats;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.world.effect.MobEffectInstance;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterMobEffects.HALLUCINATION;

public class SpaceMirageDrug extends Drug {
    public SpaceMirageDrug(float eliminationRate, float absorptionRate) {
        super(eliminationRate, absorptionRate);
    }

    public void tickDrug(@NotNull MedicalStats medicalStats, float amount) {
        if (medicalStats instanceof AnimalMedicalStats stats) {
            stats.getEntity().addEffect(new MobEffectInstance(HALLUCINATION, 2, 0, true, false, false));
        }
    }
}
