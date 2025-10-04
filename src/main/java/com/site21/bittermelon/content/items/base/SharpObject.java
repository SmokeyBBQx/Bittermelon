package com.site21.bittermelon.content.items.base;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.items.medical.MedicalItem;
import com.site21.bittermelon.content.medical.client.screen.minigame.IncisionMinigame;
import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public interface SharpObject extends MedicalItem {
    @Override
    default String getActionDescription() {
        return "Make Incision";
    }

    @Override
    default EnumSet<CompartmentTag> getAllowedCompartments() {
        return EnumSet.of(
                CompartmentTag.SOFT_TISSUE,
                CompartmentTag.HARD_TISSUE
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    default void use(CompartmentInstance compartment, MedicalStats medicalStats, Character character, ItemStack item) {
        Minecraft.getInstance().setScreen(new IncisionMinigame(item, compartment, medicalStats, character));
    }

    @Override
    default void finishAction(CompartmentInstance compartment, @NotNull MedicalStats medicalStats, float quality, ItemStack item) {
//        CompartmentInstance cut = new CompartmentInstance(Compartments.INJURY.get(), (1 + 100 - quality * 100), "Scalpel Cut", false);
//        cut.addTag(CompartmentTag.CUT);
//        cut.initializeWithParent(compartment);
//        Bleed.generateBleed(cut, medicalStats, (1 + 100 - quality * 100));
//        cut.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/sprites/medical/cut.png"));
//        medicalStats.addCompartment(cut);

        // TODO: Announce handler
    }
}
