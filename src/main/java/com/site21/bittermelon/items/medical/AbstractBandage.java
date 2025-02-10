package com.site21.bittermelon.items.medical;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.compartments.firstaid.Bandage;
import com.site21.bittermelon.medical.medicalstats.MedicalStats;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public interface AbstractBandage extends MedicalItem {
    @Override
    default String getActionDescription() {
        return "Apply Bandage";
    }

    @Override
    default EnumSet<CompartmentType> getAllowedCompartments() {
        return EnumSet.of(
                CompartmentType.CUT,
                CompartmentType.BITE,
                CompartmentType.STAB
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    default void use(@NotNull Compartment compartment, @NotNull MedicalStats medicalStats, com.site21.bittermelon.character.Character character, ItemStack item) {
        medicalStats.addCompartment(new Bandage("Bandage", compartment, 10, 1));
    }

    @Override
    default boolean shouldConsumeItem() {
        return true;
    }

    @Override
    default void finishAction(Compartment compartment, MedicalStats medicalStats, Character character, float quality, ItemStack item) {
    }
}
