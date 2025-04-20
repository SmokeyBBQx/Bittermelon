package com.site21.bittermelon.content.items.medical;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.client.screen.minigame.RetractMinigame;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.UUID;

import static com.site21.bittermelon.init.custom.Compartments.RETRACTOR;
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
    default boolean canInteract(@NotNull CompartmentInstance compartment, MedicalStats medicalStats) {
//        if (compartment.getChildren().stream().anyMatch(compartment1 -> medicalStats.getCompartment(compartment1).hasTag(CompartmentTag.CUT))) {
//            return MedicalItem.super.canInteract(compartment, medicalStats);
//        }
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    default void use(CompartmentInstance compartment, MedicalStats medicalStats, Character character, ItemStack item) {
        Minecraft.getInstance().setScreen(new RetractMinigame(item, compartment, medicalStats, character));
    }

    @Override
    default void finishAction(CompartmentInstance compartment, @NotNull MedicalStats medicalStats, float quality, ItemStack item) {
//        CompartmentInstance retractor = new CompartmentInstance(RETRACTOR.get(), 20, "Retractor", false);
//        retractor.setItem(item);
//        retractor.initializeWithParent(compartment);
//        for (UUID childID : compartment.getChildren()) {
//            CompartmentInstance child = medicalStats.getCompartment(childID);
//            if (child != null) {
//                child.setHidden(false);
//            }
//        }
//        medicalStats.addCompartment(retractor);
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
