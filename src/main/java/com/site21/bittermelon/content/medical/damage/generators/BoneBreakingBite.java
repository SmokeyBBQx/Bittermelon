package com.site21.bittermelon.content.medical.damage.generators;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.compartments.CompartmentOld;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.Injury;
import com.site21.bittermelon.content.medical.compartments.conditions.Bleed;
import com.site21.bittermelon.content.medical.damage.InjuryResult;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

import static com.site21.bittermelon.init.custom.Compartments.INJURY;

public class BoneBreakingBite extends Bite {
    public BoneBreakingBite(EnumSet<CompartmentTag> allowedCompartments) {
        super(allowedCompartments);
    }

    @Override
    protected InjuryResult createInjury(float damage, @NotNull CompartmentInstance target, MedicalStats medicalStats) {
        for (CompartmentTag type : target.getTags()) {
            switch (type) {
                case CompartmentTag.SOFT_TISSUE -> {
                    CompartmentInstance bite = new CompartmentInstance(INJURY.get(), damage, "Bite Wound", false);
                    target.setHidden(false);
                    String message = "tearing the " + target.getName().toLowerCase();
                    Bleed.generateBleed(bite, medicalStats, bite.getMaxHealth());
                    return new InjuryResult(bite, message);
                }
                case CompartmentTag.JOINT -> {
                    CompartmentInstance dislocation = new CompartmentInstance(INJURY.get(), damage, "Dislocation", false);
                    target.setHidden(false);
                    String message = "dislocating the " + target.getName().toLowerCase();
                    return new InjuryResult(dislocation, message);
                }
                case CompartmentTag.HARD_TISSUE -> {
                    CompartmentInstance fracture = new CompartmentInstance(INJURY.get(), damage, "Fracture", false);
                    target.setHidden(false);
                    String message = "fracturing the " + target.getName().toLowerCase();
                    return new InjuryResult(fracture, message);
                }
            }
        }

        return null;
    }
}
