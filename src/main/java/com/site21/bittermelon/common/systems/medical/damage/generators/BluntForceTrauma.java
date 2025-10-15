package com.site21.bittermelon.common.systems.medical.damage.generators;

import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentTag;
import com.site21.bittermelon.common.systems.medical.damage.DamageGenerator;
import com.site21.bittermelon.common.systems.medical.damage.InjuryResult;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class BluntForceTrauma extends DamageGenerator {
    public BluntForceTrauma(EnumSet<CompartmentTag> allowedCompartments) {
        super(allowedCompartments);
        shouldDismember = true;
    }

    @Override
    protected InjuryResult createInjury(float damage, @NotNull CompartmentInstance target, MedicalStats medicalStats) {
//        for (CompartmentTag type : target.getTags()) {
//            switch (type) {
//                case CompartmentTag.SOFT_TISSUE -> {
//                    CompartmentInstance bruise = new CompartmentInstance(INJURY.get(), damage, "Bruise", true);
//                    bruise.initializeWithParent(target);
//                    if (!target.isHidden()) {
//                        bruise.setHidden(false);
//                    }
//                    bruise.setIcon((ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/sprites/medical/bruise.png")));
//                    String message = "bruising the " + target.getName().toLowerCase();
//                    return new InjuryResult(bruise, message);
//                }
//                case CompartmentTag.JOINT -> {
//                    CompartmentInstance dislocation = new CompartmentInstance(INJURY.get(), damage, "Dislocation", false);
//                    dislocation.initializeWithParent(target);
//                    target.setHidden(false);
//                    String message = "dislocating the " + target.getName().toLowerCase();
//                    return new InjuryResult(dislocation, message);
//                }
//                case CompartmentTag.HARD_TISSUE -> {
//                    CompartmentInstance fracture = new CompartmentInstance(INJURY.get(), damage, "Fracture", true);
//                    fracture.initializeWithParent(target);
//                    if (!target.isHidden()) {
//                        fracture.setHidden(false);
//                    }
//                    fracture.setIcon((ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/sprites/medical/fracture.png")));
//                    String message = "fracturing the " + target.getName().toLowerCase();
//                    return new InjuryResult(fracture, message);
//                }
//            }
//        }

        return null;
    }
}
