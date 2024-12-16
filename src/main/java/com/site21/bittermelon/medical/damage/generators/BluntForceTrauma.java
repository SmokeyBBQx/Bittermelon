package com.site21.bittermelon.medical.damage.generators;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.compartments.Injury;
import com.site21.bittermelon.medical.damage.DamageGenerator;
import com.site21.bittermelon.medical.damage.InjuryResult;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class BluntForceTrauma extends DamageGenerator {
    public BluntForceTrauma(EnumSet<CompartmentType> allowedCompartments) {
        super(allowedCompartments);
        shouldDismember = true;
    }

    @Override
    protected InjuryResult createInjury(float damage, @NotNull Compartment target, Character character, LivingEntity entity) {
        for (CompartmentType type : target.getTypes()) {
            switch (type) {
                case CompartmentType.SOFT_TISSUE -> {
                    Injury bruise = new Injury(EnumSet.of(CompartmentType.BRUISE), "Bruise", target, damage, character, entity);
                    if (!target.isHidden()) {
                        bruise.reveal();
                    }
                    bruise.setIcon((ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/sprites/medical/bruise.png")));
                    String message = "bruising the " + target.getName().toLowerCase();
                    return new InjuryResult(bruise, message);
                }
                case CompartmentType.JOINT -> {
                    Injury dislocation = new Injury(EnumSet.of(CompartmentType.DISLOCATION), "Dislocation", target, damage, character, entity);
                    dislocation.reveal();
                    String message = "dislocating the " + target.getName().toLowerCase();
                    return new InjuryResult(dislocation, message);
                }
                case CompartmentType.HARD_TISSUE -> {
                    Injury fracture = new Injury(EnumSet.of(CompartmentType.FRACTURE), "Fracture", target, damage, character, entity);
                    if (!target.isHidden()) {
                        fracture.reveal();
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
