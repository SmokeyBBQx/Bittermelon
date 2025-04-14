package com.site21.bittermelon.content.medical.damage.generators;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.compartments.CompartmentOld;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.Injury;
import com.site21.bittermelon.content.medical.compartments.conditions.Bleed;
import com.site21.bittermelon.content.medical.damage.DamageGenerator;
import com.site21.bittermelon.content.medical.damage.InjuryResult;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStatsOld;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

import static com.site21.bittermelon.init.custom.Compartments.INJURY;

public class Stab extends DamageGenerator {
    @Override
    protected InjuryResult createInjury(float damage, @NotNull CompartmentInstance target, MedicalStats medicalStats) {
        for (CompartmentTag type : target.getTags()) {
            switch (type) {
                case CompartmentTag.SOFT_TISSUE -> {
                    CompartmentInstance stab = new CompartmentInstance(INJURY.get(), damage, "Stab Wound", false);
                    target.setHidden(false);
                    String message = "piercing the " + target.getName().toLowerCase();
                    Bleed.generateBleed(stab, medicalStats, stab.getMaxHealth());
                    return new InjuryResult(stab, message);
                }
                case CompartmentTag.HARD_TISSUE -> {
                    if (damage > target.getHealth()) {
                        CompartmentInstance fracture = new CompartmentInstance(INJURY.get(), damage, "Fracture", false);
                        target.setHidden(false);
                        String message = "fracturing the " + target.getName().toLowerCase();
                        return new InjuryResult(fracture, message);
                    } else {
                        CompartmentInstance scratch = new CompartmentInstance(INJURY.get(), damage / 2, "Scratch", false);
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
