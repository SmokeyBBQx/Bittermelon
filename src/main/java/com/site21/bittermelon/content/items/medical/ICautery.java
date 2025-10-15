package com.site21.bittermelon.content.items.medical;

import com.site21.bittermelon.systems.character.Character;
import com.site21.bittermelon.systems.medical.client.screen.minigame.CauteryMinigame;
import com.site21.bittermelon.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.systems.medical.compartment.CompartmentTag;
import com.site21.bittermelon.systems.medical.medicalstats.MedicalStats;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public interface ICautery extends MedicalItem {
    @Override
    default String getActionDescription() {
        return "Cauterize";
    }

    @Override
    default EnumSet<CompartmentTag> getAllowedCompartments() {
        return EnumSet.of(
                CompartmentTag.CAPILLARY_BLEED
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    default void use(CompartmentInstance compartment, MedicalStats medicalStats, Character character, ItemStack item) {
        Minecraft.getInstance().setScreen(new CauteryMinigame(item, compartment, medicalStats, character));
    }

    @Override
    default void finishAction(CompartmentInstance compartment, @NotNull MedicalStats medicalStats, float quality, ItemStack item) {
        medicalStats.removeCompartment(compartment);
    }
}
