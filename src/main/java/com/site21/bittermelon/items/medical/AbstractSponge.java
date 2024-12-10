package com.site21.bittermelon.items.medical;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.medical.client.gui.minigame.CauteryMinigame;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.medicalstats.MedicalStats;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;

public interface AbstractSponge extends MedicalItem {
    @Override
    default EnumSet<CompartmentType> getAllowedCompartments() {
        return null;
    }

    @Override
    default boolean canInteract(Compartment compartment) {
        return compartment.isObscured();
    }

    @Override
    default void use(Compartment compartment, MedicalStats medicalStats, Character character, ItemStack item) {
        Minecraft.getInstance().setScreen(new CauteryMinigame(item, compartment, medicalStats, character));
    }

    @Override
    default void finishAction(Compartment compartment, MedicalStats medicalStats, Character character, float quality, ItemStack item) {
        compartment.setObscured(false);
    }

    @Override
    default boolean shouldConsumeItem() {
        return true;
    }

    @Override
    default String getActionDescription() {
        return "Absorb Blood";
    }
}
