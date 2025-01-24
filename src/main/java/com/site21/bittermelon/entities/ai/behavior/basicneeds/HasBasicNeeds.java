package com.site21.bittermelon.entities.ai.behavior.basicneeds;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.blocks.blockentities.FluidBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface HasBasicNeeds {
    default boolean wantsToEat(ItemStack stack) {
        return false;
    }

    default List<ItemEntity> sortEdibleItems(List<ItemEntity> unsortedItems) {
        return unsortedItems;
    }

    void modifyHunger(float hunger);

    default boolean wantsToDrink(@NotNull FluidBlockEntity fluid) {
        return fluid.getSubstances().stream().anyMatch(substanceStack ->
                substanceStack.getSubstanceHolder().is(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "liquid_water")));
    }

    default List<FluidBlockEntity> sortFluids(List<FluidBlockEntity> unsortedFluids) {
        return unsortedFluids;
    }

    void modifyThirst(float thirst);

    void modifyDefecation(float defecation);

    void modifyHygiene(float hygiene);
}
