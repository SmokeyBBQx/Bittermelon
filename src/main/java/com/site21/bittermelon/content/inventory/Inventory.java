package com.site21.bittermelon.content.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Inventory implements Container {
    public final NonNullList<ItemStack> mainHand;
    public final NonNullList<ItemStack> offHand;
    public final NonNullList<ItemStack> wearables;
    public final Player player;

    public Inventory(Player player) {
        this.mainHand = NonNullList.withSize(1, ItemStack.EMPTY);
        this.offHand = NonNullList.withSize(1, ItemStack.EMPTY);
        this.wearables = NonNullList.withSize(12, ItemStack.EMPTY);
        this.player = player;
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public @NotNull ItemStack getItem(int i) {
        return null;
    }

    @Override
    public @NotNull ItemStack removeItem(int i, int i1) {
        return null;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int i) {
        return null;
    }

    @Override
    public void setItem(int i, @NotNull ItemStack itemStack) {

    }

    @Override
    public void setChanged() {

    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return false;
    }

    @Override
    public void clearContent() {

    }
}
