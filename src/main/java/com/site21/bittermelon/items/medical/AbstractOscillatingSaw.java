package com.site21.bittermelon.items.medical;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.medical.client.gui.minigame.CauteryMinigame;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.compartments.conditions.Cut;
import com.site21.bittermelon.medical.medicalstats.MedicalStats;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;

public interface AbstractOscillatingSaw extends MedicalItem {
    @Override
    default String getActionDescription() {
        return "Cauterize";
    }

    @Override
    default EnumSet<CompartmentType> getAllowedCompartments() {
        return EnumSet.of(
                CompartmentType.HARD_TISSUE
        );
    }

    @Override
    default void use(Compartment compartment, MedicalStats medicalStats, Character character, ItemStack item) {
        Minecraft.getInstance().setScreen(new CauteryMinigame(item, compartment, medicalStats, character));
    }

    @Override
    default void finishAction(Compartment compartment, MedicalStats medicalStats, Character character, float quality, ItemStack item) {
        Cut sawCut = new Cut("Saw Cut", compartment, (int) quality, character, null);
        sawCut.reveal();
        medicalStats.addCompartment(sawCut);
    }
}
