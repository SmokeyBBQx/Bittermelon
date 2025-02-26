package com.site21.bittermelon.content.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class Inventory implements Container {
    public final NonNullList<ItemStack> items;
    public final NonNullList<ItemStack> offhand;
    public int selected;
    public final Player player;

    public Inventory(Player player) {
        this.items = NonNullList.withSize(36, ItemStack.EMPTY);
        this.offhand = NonNullList.withSize(1, ItemStack.EMPTY);
        this.player = player;
    }

    public ItemStack getSelected() {
        return isHotbarSlot(this.selected) ? (ItemStack)this.items.get(this.selected) : ItemStack.EMPTY;
    }

    public static boolean isHotbarSlot(int index) {
        return index == 0 || index == 1;
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
    public ItemStack getItem(int i) {
        return null;
    }

    @Override
    public ItemStack removeItem(int i, int i1) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return null;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {

    }

    @Override
    public void setChanged() {

    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }

    @Override
    public void clearContent() {

    }
}
