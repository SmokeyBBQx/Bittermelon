package com.site21.bittermelon.items;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.items.medical.MedicalItem;
import com.site21.bittermelon.medical.client.gui.minigame.IncisionMinigame;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.compartments.conditions.Cut;
import com.site21.bittermelon.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.util.ServerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;

public interface SharpObject extends MedicalItem {
    @Override
    default String getActionDescription() {
        return "Make Incision";
    }

    @Override
    default EnumSet<CompartmentType> getAllowedCompartments() {
        return EnumSet.of(
                CompartmentType.SOFT_TISSUE,
                CompartmentType.HARD_TISSUE
        );
    }

    @Override
    default void use(Compartment compartment, MedicalStats medicalStats, Character character, ItemStack item) {
        Minecraft.getInstance().setScreen(new IncisionMinigame(item, compartment, medicalStats, character));
    }

    @Override
    default void finishAction(Compartment compartment, MedicalStats medicalStats, Character character, float quality, ItemStack item) {
        LivingEntity entity = ServerUtil.getLivingEntity(character.getEntityUUID());
        Cut cut = new Cut("Scalpel Cut", compartment, (int) (1 + 100 - quality * 100), character, entity);
        cut.reveal();
        cut.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/sprites/medical/cut.png"));
        medicalStats.addCompartment(cut);

        // TODO: Announce action
    }
}
