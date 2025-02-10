package com.site21.bittermelon.items.medical;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.medical.client.gui.minigame.RetractMinigame;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.compartments.conditions.Cut;
import com.site21.bittermelon.medical.medicalstats.MedicalStats;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.EnumSet;

public interface AbstractRetractor extends MedicalItem {
    @Override
    default EnumSet<CompartmentType> getAllowedCompartments() {
        return EnumSet.of(
                CompartmentType.SOFT_TISSUE,
                CompartmentType.HARD_TISSUE
        );
    }

    @Override
    default boolean canInteract(Compartment compartment) {
        if (compartment.getChildren().stream().anyMatch(compartment1 -> compartment1 instanceof Cut)) {
            return MedicalItem.super.canInteract(compartment);
        }
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    default void use(Compartment compartment, MedicalStats medicalStats, Character character, ItemStack item) {
        Minecraft.getInstance().setScreen(new RetractMinigame(item, compartment, medicalStats, character));
    }

    @Override
    default void finishAction(Compartment compartment, MedicalStats medicalStats, Character character, float quality, ItemStack item) {
//            medicalStats.addCompartment(new com.site21.bittermelon.medical.compartments.firstaid.Retractor("Retractor" + " (" + compartment.getName() + ")", compartment, 20, 5));
            medicalStats.addCompartment(new com.site21.bittermelon.medical.compartments.firstaid.Retractor("Retractor", compartment, 20, 5, item));
    }

    @Override
    default boolean shouldConsumeItem() {
        return true;
    }

    @Override
    default String getActionDescription() {
        return "Retract Tissue";
    }
}
