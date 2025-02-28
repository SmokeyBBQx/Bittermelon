package com.site21.bittermelon.content.inventory;

import com.site21.bittermelon.content.items.base.BaseItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlotComponent implements IContainerMenu {
    public final int[][] slots;
    public final Map<Integer, ItemStack> items = new HashMap<>();
    public final int rows;
    public final int columns;

    private int nextSlotIndex = 0;

    protected SlotComponent(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.slots = new int[rows][columns];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                slots[y][x] = -1;
            }
        }
    }

    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < columns && y >= 0 && y < rows;
    }

    public ItemStack getItemAt(int x, int y) {
        if (!isInBounds(x, y)) {
            return ItemStack.EMPTY;
        }

        int slotIndex = slots[y][x];
        if (slotIndex == -1 || !items.containsKey(slotIndex)) {
            return ItemStack.EMPTY;
        }

        return items.get(slotIndex);
    }

    public int getSlotIndexAt(int x, int y) {
        if (!isInBounds(x, y)) {
            return -1;
        }
        return slots[y][x];
    }

    public boolean canItemFit(@NotNull ItemStack stack, int startX, int startY) {
        if (stack.isEmpty()) {
            return true;
        }

        int width, height;

        if (stack.getItem() instanceof BaseItem item) {
            int[] dimensions = item.getShape(stack);
            width = dimensions[0];
            height = dimensions[1];
        } else {
            // Vanilla items are 1x1
            width = height = 1;
        }

        if (!isInBounds(startX + width, startY + height)) {
            return false;
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (slots[startY + y][startX + x] != -1) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean addItem(@NotNull ItemStack stack, int startX, int startY) {
        if (stack.isEmpty()) {
            return true;
        }

        if (!canItemFit(stack, startX, startY)) {
            return false;
        }

        int width, height;
        int slotIndex = nextSlotIndex++;
        items.put(slotIndex, stack);

        if (stack.getItem() instanceof BaseItem item) {
            int[] dimensions = item.getShape(stack);
            width = dimensions[0];
            height = dimensions[1];
        } else {
            width = height = 1;
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                slots[startY + y][startX + x] = slotIndex;
            }
        }

        return true;
    }

    public ItemStack removeItemAt(int startX, int startY) {
        ItemStack stack = getItemAt(startX, startY);

        int width, height;

        if (stack.getItem() instanceof BaseItem item) {
            int[] dimensions = item.getShape(stack);
            width = dimensions[0];
            height = dimensions[1];
        } else {
            width = height = 1;
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                slots[startY + y][startX + x] = -1;
            }
        }

        items.remove(slots[startY][startX]);

        return stack;
    }

    public int[] findEmptySpace(@NotNull ItemStack stack) {
        if (stack.isEmpty()) {
            return new int[] { 0, 0 };
        }

        if (stack.getItem() instanceof BaseItem item) {

        }

        return null;
    }
}
