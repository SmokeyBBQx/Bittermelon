package com.site21.bittermelon.content.items.medical;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.compartments.CompartmentOld;
import com.site21.bittermelon.content.medical.compartments.firstaid.Retractor;
import com.site21.bittermelon.content.medical.client.screen.minigame.RetractMinigame;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.conditionsold.Cut;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStatsOld;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.EnumSet;

import static com.site21.bittermelon.init.custom.Compartments.TOOL;

public interface AbstractRetractor extends MedicalItem {
    @Override
    default EnumSet<CompartmentTag> getAllowedCompartments() {
        return EnumSet.of(
                CompartmentTag.SOFT_TISSUE,
                CompartmentTag.HARD_TISSUE
        );
    }

    @Override
    default boolean canInteract(CompartmentInstance compartment, MedicalStats medicalStats) {
        if (compartment.getChildren().stream().anyMatch(compartment1 -> medicalStats.getCompartment(compartment1).hasTag(CompartmentTag.CUT))) {
            return MedicalItem.super.canInteract(compartment, medicalStats);
        }
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    default void use(CompartmentInstance compartment, MedicalStats medicalStats, Character character, ItemStack item) {
        Minecraft.getInstance().setScreen(new RetractMinigame(item, compartment, medicalStats, character));
    }

    @Override
    default void finishAction(CompartmentInstance compartment, MedicalStats medicalStats, Character character, float quality, ItemStack item) {
//            medicalStats.addCompartment(new com.site21.bittermelon.medical.compartments.firstaid.Retractor("Retractor" + " (" + compartment.getName() + ")", compartment, 20, 5));
            medicalStats.addCompartment(new CompartmentInstance(TOOL.get(), 20, "Retractor", false));
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
