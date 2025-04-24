package com.site21.bittermelon.content.items.medical;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.client.screen.minigame.CauteryMinigame;
import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.EnumSet;

public interface IClamp extends MedicalItem {
    @Override
    default EnumSet<CompartmentTag> getAllowedCompartments() {
        return EnumSet.of(
                CompartmentTag.ARTERIAL_BLEED,
                CompartmentTag.VENOUS_BLEED
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    default void use(CompartmentInstance compartment, MedicalStats medicalStats, Character character, ItemStack item) {
        Minecraft.getInstance().setScreen(new CauteryMinigame(item, compartment, medicalStats, character));
    }

    @Override
    default void finishAction(CompartmentInstance compartment, MedicalStats medicalStats, float quality, ItemStack item) {
//        if (compartment.hasTag(CompartmentTag.BLEED)) {
//            CompartmentInstance clamp = new CompartmentInstance(TOOL.get(), 20, "Clamp", false);
//            clamp.setItem(item);
//            clamp.initializeWithParent(compartment);
//            medicalStats.addCompartment(clamp);
//        }
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
