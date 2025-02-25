package com.site21.bittermelon.content.inventory;

import net.minecraft.world.item.ItemStack;

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
    // When a slot is clicked on, it checks the slot's data and calls the handler on the item
    // When an item is placed, it will first condition if the slots do not contain a value
    // Otherwise, it will fill the numbers in the array
    // Quick place, it will search the whole list of slots for an area containing the number of empty slots


    public boolean mayPlace(int index) {
        return false;
    }

    protected void pick(int slotID) {}

    protected void quickTransfer() {}


}
