package com.site21.bittermelon.common.content.items.medical;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentTag;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.EnumSet;

public interface MedicalItem {
    EnumSet<CompartmentTag> getAllowedCompartments();
    @OnlyIn(Dist.CLIENT)
    void use(CompartmentInstance compartment, MedicalStats medicalStats, Character character, ItemStack item);
    void finishAction(CompartmentInstance compartment, MedicalStats medicalStats, float quality, ItemStack item);
    default boolean shouldConsumeItem() {
        return false;
    }
    default void consumeItem(ItemStack item, Player player) {
        if (shouldConsumeItem()) {
            player.getInventory().removeItem(item);
            player.getInventory().removeItem(0, 1);
        }
    }
    default boolean canInteract(CompartmentInstance compartment, MedicalStats medicalStats) {
        for (CompartmentTag type : getAllowedCompartments()) {
            if (compartment.hasTag(type)) {
                return true;
            }
        }
        return false;
    }
    String getActionDescription();
}
