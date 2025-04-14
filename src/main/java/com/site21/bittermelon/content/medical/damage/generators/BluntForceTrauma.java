package com.site21.bittermelon.content.medical.damage.generators;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.compartments.CompartmentOld;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.Injury;
import com.site21.bittermelon.content.medical.damage.DamageGenerator;
import com.site21.bittermelon.content.medical.damage.InjuryResult;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

import static com.site21.bittermelon.init.custom.Compartments.INJURY;

public class BluntForceTrauma extends DamageGenerator {
    public BluntForceTrauma(EnumSet<CompartmentTag> allowedCompartments) {
        super(allowedCompartments);
        shouldDismember = true;
    }

    @Override
    protected InjuryResult createInjury(float damage, @NotNull CompartmentInstance target, MedicalStats medicalStats) {
        for (CompartmentTag type : target.getTags()) {
            switch (type) {
                case CompartmentTag.SOFT_TISSUE -> {
                    CompartmentInstance bruise = new CompartmentInstance(INJURY.get(), damage, "Bruise", true);
                    if (!target.isHidden()) {
                        bruise.setHidden(false);
                    }
                    bruise.setIcon((ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/sprites/medical/bruise.png")));
                    String message = "bruising the " + target.getName().toLowerCase();
                    return new InjuryResult(bruise, message);
                }
                case CompartmentTag.JOINT -> {
                    CompartmentInstance dislocation = new CompartmentInstance(INJURY.get(), damage, "Dislocation", false);
                    target.setHidden(false);
                    String message = "dislocating the " + target.getName().toLowerCase();
                    return new InjuryResult(dislocation, message);
                }
                case CompartmentTag.HARD_TISSUE -> {
                    CompartmentInstance fracture = new CompartmentInstance(INJURY.get(), damage, "Fracture", true);
                    if (!target.isHidden()) {
                        fracture.setHidden(false);
                    }
                    fracture.setIcon((ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/sprites/medical/fracture.png")));
                    String message = "fracturing the " + target.getName().toLowerCase();
                    return new InjuryResult(fracture, message);
                }
            }
        }

        return null;
    }
}
