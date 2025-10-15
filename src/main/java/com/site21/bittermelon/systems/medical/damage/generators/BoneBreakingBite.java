package com.site21.bittermelon.systems.medical.damage.generators;

import com.site21.bittermelon.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.systems.medical.compartment.CompartmentTag;
import com.site21.bittermelon.systems.medical.damage.InjuryResult;
import com.site21.bittermelon.systems.medical.medicalstats.MedicalStats;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class BoneBreakingBite extends Bite {
    public BoneBreakingBite(EnumSet<CompartmentTag> allowedCompartments) {
        super(allowedCompartments);
    }

    @Override
    protected InjuryResult createInjury(float damage, @NotNull CompartmentInstance target, MedicalStats medicalStats) {
//        for (CompartmentTag type : target.getTags()) {
//            switch (type) {
//                case CompartmentTag.SOFT_TISSUE -> {
//                    CompartmentInstance bite = new CompartmentInstance(INJURY.get(), damage, "Bite Wound", false);
//                    bite.initializeWithParent(target);
//                    target.setHidden(false);
//                    String message = "tearing the " + target.getName().toLowerCase();
//                    Bleed.generateBleed(bite, medicalStats, bite.getMaxHealth());
//                    return new InjuryResult(bite, message);
//                }
//                case CompartmentTag.JOINT -> {
//                    CompartmentInstance dislocation = new CompartmentInstance(INJURY.get(), damage, "Dislocation", false);
//                    dislocation.initializeWithParent(target);
//                    target.setHidden(false);
//                    String message = "dislocating the " + target.getName().toLowerCase();
//                    return new InjuryResult(dislocation, message);
//                }
//                case CompartmentTag.HARD_TISSUE -> {
//                    CompartmentInstance fracture = new CompartmentInstance(INJURY.get(), damage, "Fracture", false);
//                    fracture.initializeWithParent(target);
//                    target.setHidden(false);
//                    String message = "fracturing the " + target.getName().toLowerCase();
//                    return new InjuryResult(fracture, message);
//                }
//            }
//        }

        return null;
    }
}
