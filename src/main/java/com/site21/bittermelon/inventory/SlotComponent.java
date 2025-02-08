package com.site21.bittermelon.inventory;

import com.site21.bittermelon.items.base.BaseItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class SlotComponent {
    public final List<Slot> slots = new ArrayList<>();
    public final List<ItemStack> items = new ArrayList<>();
    public int rows;
    public int columns;

    protected SlotComponent() {
    }

    // Two arrays: items and slots
    // Draw slots
    // Slot data contains a target item
    // When a slot is clicked on, it checks the slot's data and calls the action on the item
    // When an item is placed, it will first check if the slots do not contain a value
    // Otherwise, it will fill the numbers in the array
    // Quick place, it will search the whole list of slots for an area containing the number of empty slots


    public boolean mayPlace(int index) {
        return false;
    }

    protected void pick(int slotID) {}

    protected void quickTransfer() {}


}
