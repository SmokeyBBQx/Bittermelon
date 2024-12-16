package com.site21.bittermelon.medical.damage.generators;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.compartments.Injury;
import com.site21.bittermelon.medical.damage.InjuryResult;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class BoneBreakingBite extends Bite {
    public BoneBreakingBite(EnumSet<CompartmentType> allowedCompartments) {
        super(allowedCompartments);
    }

    @Override
    protected InjuryResult createInjury(float damage, @NotNull Compartment target, Character character, LivingEntity entity) {
        for (CompartmentType type : target.getTypes()) {
            switch (type) {
                case CompartmentType.SOFT_TISSUE -> {
                    Injury bite = new Injury(EnumSet.of(CompartmentType.BRUISE), "Bite Wound", target, damage, character, entity);
                    target.reveal();
                    bite.reveal();
                    String message = "tearing the " + target.getName().toLowerCase();
                    return new InjuryResult(bite, message);
                }
                case CompartmentType.JOINT -> {
                    Injury dislocation = new Injury(EnumSet.of(CompartmentType.DISLOCATION), "Dislocation", target, damage, character, entity);
                    target.reveal();
                    dislocation.reveal();
                    String message = "dislocating the " + target.getName().toLowerCase();
                    return new InjuryResult(dislocation, message);
                }
                case CompartmentType.HARD_TISSUE -> {
                    Injury fracture = new Injury(EnumSet.of(CompartmentType.FRACTURE), "Bite Fracture", target, damage, character, entity);
                    target.reveal();
                    fracture.reveal();
                    String message = "fracturing the " + target.getName().toLowerCase();
                    return new InjuryResult(fracture, message);
                }
            }
        }

        return null;
    }
}
