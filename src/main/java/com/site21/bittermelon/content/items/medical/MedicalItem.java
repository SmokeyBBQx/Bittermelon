package com.site21.bittermelon.content.items.medical;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.Compartment;
import com.site21.bittermelon.content.medical.compartments.CompartmentType;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.EnumSet;

public interface MedicalItem {
    EnumSet<CompartmentType> getAllowedCompartments();
    @OnlyIn(Dist.CLIENT)
    void use(Compartment compartment, MedicalStats medicalStats, Character character, ItemStack item);
    void finishAction(Compartment compartment, MedicalStats medicalStats, Character character, float quality, ItemStack item);
    default boolean shouldConsumeItem() {
        return false;
    }
    default void consumeItem(ItemStack item, Player player) {
        if (shouldConsumeItem()) {
            player.getInventory().removeItem(item);
        }
    }
    default boolean canInteract(Compartment compartment) {
        for (CompartmentType type : getAllowedCompartments()) {
            if (compartment.hasType(type)) {
                return true;
            }
        }
        return false;
    }
    String getActionDescription();
}
