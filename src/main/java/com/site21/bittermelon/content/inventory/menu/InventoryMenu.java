package com.site21.bittermelon.content.inventory.menu;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InventoryMenu implements IContainerMenu {
    private final List<ContainerMenu> menus = new ArrayList<>();
    private ItemStack carried = ItemStack.EMPTY;

    @Override
    public boolean addItem(@NotNull ItemStack stack, int startX, int startY) {
        return false;
    }

    @Override
    public boolean canItemFit(@NotNull ItemStack stack, int startX, int startY) {
        return false;
    }

    @Override
    public ItemStack removeItemAt(int startX, int startY) {
        return null;
    }

    @Override
    public ItemStack getItemAt(int x, int y) {
        return null;
    }

    @Override
    public int getSlotIndexAt(int x, int y) {
        return 0;
    }

    @Override
    public int[] getDimensions() {
        return new int[0];
    }

    @Override
    public ItemStack getCarried() {
        return carried;
    }

    @Override
    public void setCarried(@NotNull ItemStack stack) {
        carried = stack.copy();
    }
}


