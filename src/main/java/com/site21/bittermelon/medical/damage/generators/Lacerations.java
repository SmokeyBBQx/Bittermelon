package com.site21.bittermelon.medical.damage.generators;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.compartments.Injury;
import com.site21.bittermelon.medical.compartments.conditions.Bleed;
import com.site21.bittermelon.medical.damage.DamageGenerator;
import com.site21.bittermelon.medical.damage.InjuryResult;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class Lacerations extends DamageGenerator {
    public Lacerations(EnumSet<CompartmentType> allowedCompartments) {
        super(allowedCompartments);
        shouldDismember = true;
    }

    @Override
    protected InjuryResult createInjury(float damage, @NotNull Compartment target, Character character, LivingEntity entity) {
        for (CompartmentType type : target.getTypes()) {
            switch (type) {
                case CompartmentType.SOFT_TISSUE -> {
                    Injury laceration = new Injury(EnumSet.of(CompartmentType.LACERATION), "Laceration", target, damage, character, entity);
                    target.reveal();
                    laceration.reveal();
                    laceration.setIcon((ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/sprites/medical/slash.png")));
                    String message = "lacerating the " + target.getName().toLowerCase();
                    Bleed.generateBleed(laceration, character, entity, laceration.getMaxHealth());
                    return new InjuryResult(laceration, message);
                }
                case CompartmentType.HARD_TISSUE -> {
                    if (damage > target.getHealth()) {
                        Injury fracture = new Injury(EnumSet.of(CompartmentType.FRACTURE), "Fracture", target, damage, character, entity);
                        target.reveal();
                        fracture.reveal();
                        String message = "fracturing the " + target.getName().toLowerCase();
                        return new InjuryResult(fracture, message);
                    } else {
                        Injury scratch = new Injury(EnumSet.of(CompartmentType.SCRATCH), "Scratch", target, damage / 2, character, entity);
                        target.reveal();
                        scratch.reveal();
                        String message = "scratching the " + target.getName().toLowerCase();
                        return new InjuryResult(scratch, message);
                    }
                }
            }
        }

        return null;
    }
}
