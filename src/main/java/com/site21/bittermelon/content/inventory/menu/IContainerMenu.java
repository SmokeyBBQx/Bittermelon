package com.site21.bittermelon.content.inventory.menu;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IContainerMenu {
    ItemStack getCarried();
    void setCarried(ItemStack stack);

    boolean addItem(@NotNull ItemStack stack, int startX, int startY);

    boolean canItemFit(@NotNull ItemStack stack, int startX, int startY);

    ItemStack removeItemAt(int startX, int startY);



    ItemStack getItemAt(int x, int y);

    int getSlotIndexAt(int x, int y);

    int[] getDimensions();

}
