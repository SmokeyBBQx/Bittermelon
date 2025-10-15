package com.site21.bittermelon.common.content.items.medical;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.medical.client.screen.minigame.CauteryMinigame;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentTag;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.EnumSet;

public interface IOscillatingSaw extends MedicalItem {
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
    default void finishAction(CompartmentInstance compartment, MedicalStats medicalStats, float quality, ItemStack item) {
//        CompartmentInstance sawCut = new CompartmentInstance(INJURY.get(), quality, "Saw Cut", false);
//        sawCut.initializeWithParent(compartment);
//        medicalStats.addCompartment(sawCut);
    }
}
