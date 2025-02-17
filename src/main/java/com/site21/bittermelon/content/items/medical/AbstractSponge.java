package com.site21.bittermelon.content.items.medical;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.client.screen.minigame.CauteryMinigame;
import com.site21.bittermelon.content.medical.compartments.Compartment;
import com.site21.bittermelon.content.medical.compartments.CompartmentType;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public interface AbstractSponge extends MedicalItem {
    @Override
    default EnumSet<CompartmentType> getAllowedCompartments() {
        return null;
    }

    @Override
    default boolean canInteract(@NotNull Compartment compartment) {
        return compartment.isObscured();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    default void use(Compartment compartment, MedicalStats medicalStats, Character character, ItemStack item) {
        Minecraft.getInstance().setScreen(new CauteryMinigame(item, compartment, medicalStats, character));
    }

    @Override
    default void finishAction(@NotNull Compartment compartment, MedicalStats medicalStats, Character character, float quality, ItemStack item) {
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
