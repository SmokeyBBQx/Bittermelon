package com.site21.bittermelon.common.content.items.medical;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartments.CompartmentTag;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public interface IBandage extends MedicalItem {
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
//        CompartmentInstance bandage = new CompartmentInstance(Compartments.BANDAGE.get(), 10, "Bandage", false);
//        bandage.setItem(item);
//        bandage.initializeWithParent(compartment);
//        medicalStats.addCompartment(bandage);
    }

    @Override
    default boolean shouldConsumeItem() {
        return true;
    }

    @Override
    default void finishAction(CompartmentInstance compartment, MedicalStats medicalStats, float quality, ItemStack item) {
    }
}
