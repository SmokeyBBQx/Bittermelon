package com.site21.bittermelon.content.items.medical;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.client.screen.minigame.CauteryMinigame;
import com.site21.bittermelon.content.medical.compartments.Compartment;
import com.site21.bittermelon.content.medical.compartments.CompartmentType;
import com.site21.bittermelon.content.medical.compartments.firstaid.Clamp;
import com.site21.bittermelon.content.medical.compartments.conditions.Bleed;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.EnumSet;

public interface AbstractClamp extends MedicalItem {
    @Override
    default EnumSet<CompartmentType> getAllowedCompartments() {
        return EnumSet.of(
                CompartmentType.ARTERIAL_BLEED,
                CompartmentType.VENOUS_BLEED
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    default void use(Compartment compartment, MedicalStats medicalStats, Character character, ItemStack item) {
        Minecraft.getInstance().setScreen(new CauteryMinigame(item, compartment, medicalStats, character));
    }

    @Override
    default void finishAction(Compartment compartment, MedicalStats medicalStats, Character character, float quality, ItemStack item) {
        if (compartment instanceof Bleed bleed) {
            Clamp clamp = new Clamp("Clamp", bleed, 20, 5, item);
            medicalStats.addCompartment(clamp);
        }
    }

    @Override
    default boolean shouldConsumeItem() {
        return true;
    }

    @Override
    default String getActionDescription() {
        return "Clamp";
    }
}
