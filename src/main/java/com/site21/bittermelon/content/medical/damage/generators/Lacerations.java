package com.site21.bittermelon.content.medical.damage.generators;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.conditions.Bleed;
import com.site21.bittermelon.content.medical.damage.DamageGenerator;
import com.site21.bittermelon.content.medical.damage.InjuryResult;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

import static com.site21.bittermelon.init.custom.Compartments.INJURY;

public class Lacerations extends DamageGenerator {
    public Lacerations(EnumSet<CompartmentTag> allowedCompartments) {
        super(allowedCompartments);
        shouldDismember = true;
    }

    @Override
    protected InjuryResult createInjury(float damage, @NotNull CompartmentInstance target, MedicalStats medicalStats) {
        for (CompartmentTag type : target.getTags()) {
            switch (type) {
                case CompartmentTag.SOFT_TISSUE -> {
                    CompartmentInstance laceration = new CompartmentInstance(INJURY.get(), damage, "Laceration", false);
                    laceration.initializeWithParent(target);
                    target.setHidden(false);
                    laceration.setIcon((ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/sprites/medical/slash.png")));
                    String message = "lacerating the " + target.getName().toLowerCase();
                    Bleed.generateBleed(laceration, medicalStats, laceration.getMaxHealth());
                    return new InjuryResult(laceration, message);
                }
                case CompartmentTag.HARD_TISSUE -> {
                    if (damage > target.getHealth(medicalStats)) {
                        CompartmentInstance fracture = new CompartmentInstance(INJURY.get(), damage, "Fracture", false);
                        fracture.initializeWithParent(target);
                        target.setHidden(false);
                        String message = "fracturing the " + target.getName().toLowerCase();
                        return new InjuryResult(fracture, message);
                    } else {
                        CompartmentInstance scratch = new CompartmentInstance(INJURY.get(), damage / 2, "Scratch", false);
                        scratch.initializeWithParent(target);
                        target.setHidden(false);
                        String message = "scratching the " + target.getName().toLowerCase();
                        return new InjuryResult(scratch, message);
                    }
                }
            }
        }

        return null;
    }
}
