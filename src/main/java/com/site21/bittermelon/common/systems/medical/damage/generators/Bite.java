package com.site21.bittermelon.common.systems.medical.damage.generators;

import com.site21.bittermelon.common.systems.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartments.CompartmentTag;
import com.site21.bittermelon.common.systems.medical.damage.DamageGenerator;
import com.site21.bittermelon.common.systems.medical.damage.InjuryResult;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class Bite extends DamageGenerator {
    public Bite(EnumSet<CompartmentTag> allowedCompartments) {
        super(allowedCompartments);
        shouldDismember = true;
    }

    public Bite() {
        super();
    }

    @Override
    protected InjuryResult createInjury(float damage, @NotNull CompartmentInstance target, MedicalStats medicalStats) {
        for (CompartmentTag type : target.getTags()) {
//            switch (type) {
//                case CompartmentTag.SOFT_TISSUE -> {
//                    CompartmentInstance bite = new CompartmentInstance(INJURY.get(), damage, "Bite Wound", false);
//                    bite.initializeWithParent(target);
//                    target.setHidden(false);
//                    String message = "tearing the " + target.getName().toLowerCase();
//                    Bleed.generateBleed(bite, medicalStats, bite.getMaxHealth());
//                    return new InjuryResult(bite, message);
//                }
//                case CompartmentTag.HARD_TISSUE -> {
//                    if (damage > target.getHealth(medicalStats)) {
//                        CompartmentInstance fracture = new CompartmentInstance(INJURY.get(), damage, "Fracture", false);
//                        fracture.initializeWithParent(target);
//                        target.setHidden(false);
//                        String message = "fracturing the " + target.getName().toLowerCase();
//                        return new InjuryResult(fracture, message);
//                    } else {
//                        CompartmentInstance scratch = new CompartmentInstance(INJURY.get(), damage, "Scratch", false);
//                        scratch.initializeWithParent(target);
//                        target.setHidden(false);
//                        String message = "scratching the " + target.getName().toLowerCase();
//                        return new InjuryResult(scratch, message);
//                    }
//                }
//            }
        }

        return null;
    }
}
