package com.site21.bittermelon.content.items.medical;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.client.screen.minigame.CauteryMinigame;
import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.compartments.CompartmentOld;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.conditionsold.Cut;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStatsOld;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.EnumSet;

import static com.site21.bittermelon.init.custom.Compartments.INJURY;

public interface AbstractOscillatingSaw extends MedicalItem {
    @Override
    default String getActionDescription() {
        return "Cauterize";
    }

    @Override
    default EnumSet<CompartmentTag> getAllowedCompartments() {
        return EnumSet.of(
                CompartmentTag.HARD_TISSUE
        );
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    default void use(CompartmentInstance compartment, MedicalStats medicalStats, Character character, ItemStack item) {
        Minecraft.getInstance().setScreen(new CauteryMinigame(item, compartment, medicalStats, character));
    }

    @Override
    default void finishAction(CompartmentInstance compartment, MedicalStats medicalStats, Character character, float quality, ItemStack item) {
        CompartmentInstance sawCut = new CompartmentInstance(INJURY.get(), quality, "Saw Cut", false);
        medicalStats.addCompartment(sawCut);
    }
}
