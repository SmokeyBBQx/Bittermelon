package com.site21.bittermelon.content.items.medical;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.compartments.CompartmentOld;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.firstaid.Bandage;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStatsOld;
import com.site21.bittermelon.init.custom.Compartments;
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
    default EnumSet<CompartmentTag> getAllowedCompartments() {
        return EnumSet.of(
                CompartmentTag.CUT,
                CompartmentTag.BITE,
                CompartmentTag.STAB
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    default void use(@NotNull CompartmentInstance compartment, @NotNull MedicalStats medicalStats, Character character, ItemStack item) {
        medicalStats.addCompartment(new CompartmentInstance(Compartments.BANDAGE.get(), 10, "Bandage", false));
    }

    @Override
    default boolean shouldConsumeItem() {
        return true;
    }

    @Override
    default void finishAction(CompartmentInstance compartment, MedicalStats medicalStats, Character character, float quality, ItemStack item) {
    }
}
