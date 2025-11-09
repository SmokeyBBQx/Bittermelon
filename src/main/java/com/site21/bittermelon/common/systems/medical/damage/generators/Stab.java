package com.site21.bittermelon.common.systems.medical.damage.generators;

import com.site21.bittermelon.common.systems.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.damage.DamageGenerator;
import com.site21.bittermelon.common.systems.medical.damage.InjuryResult;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import org.jetbrains.annotations.NotNull;


public class Stab extends DamageGenerator {
    @Override
    protected InjuryResult createInjury(float damage, @NotNull CompartmentInstance target, MedicalStats medicalStats) {
//        for (CompartmentTag type : target.getTags()) {
//            switch (type) {
//                case CompartmentTag.SOFT_TISSUE -> {
//                    CompartmentInstance stab = new CompartmentInstance(INJURY.get(), damage, "Stab Wound", false);
//                    stab.initializeWithParent(target);
//                    target.setHidden(false);
//                    String message = "piercing the " + target.getName().toLowerCase();
//                    Bleed.generateBleed(stab, medicalStats, stab.getMaxHealth());
//                    return new InjuryResult(stab, message);
//                }
//                case CompartmentTag.HARD_TISSUE -> {
//                    if (damage > target.getHealth(medicalStats)) {
//                        CompartmentInstance fracture = new CompartmentInstance(INJURY.get(), damage, "Fracture", false);
//                        fracture.initializeWithParent(target);
//                        target.setHidden(false);
//                        String message = "fracturing the " + target.getName().toLowerCase();
//                        return new InjuryResult(fracture, message);
//                    } else {
//                        CompartmentInstance scratch = new CompartmentInstance(INJURY.get(), damage / 2, "Scratch", false);
//                        scratch.initializeWithParent(target);
//                        target.setHidden(false);
//                        String message = "scratching the " + target.getName().toLowerCase();
//                        return new InjuryResult(scratch, message);
//                    }
//                }
//            }
//        }

        return null;
    }
}
