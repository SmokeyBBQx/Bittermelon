package com.site21.bittermelon.content.items.medical;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.compartments.CompartmentOld;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStatsOld;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.EnumSet;

public interface MedicalItem {
    EnumSet<CompartmentTag> getAllowedCompartments();
    @OnlyIn(Dist.CLIENT)
    void use(CompartmentInstance compartment, MedicalStats medicalStats, Character character, ItemStack item);
    void finishAction(CompartmentInstance compartment, MedicalStats medicalStats, Character character, float quality, ItemStack item);
    default boolean shouldConsumeItem() {
        return false;
    }
    default void consumeItem(ItemStack item, Player player) {
        if (shouldConsumeItem()) {
            player.getInventory().removeItem(item);
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
