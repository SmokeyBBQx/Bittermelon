package com.site21.bittermelon.content.inventory;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ContainerMenu implements IContainerMenu {
    private final List<SlotComponent> components = new ArrayList<>();
    private final List<int[]> componentPositions = new ArrayList<>();

    public int addSlotComponent(SlotComponent component, int offsetX, int offsetY) {
        components.add(component);
        componentPositions.add(new int[] { offsetX, offsetY });
        return components.size() - 1;
    }

    public SlotComponent getSlotComponent(int index) {
        if (index < 0 || index >= components.size()) {
            return null;
        }
        return components.get(index);
    }

    private int @Nullable [] findComponentAt(int x, int y) {
        for (int i = 0; i < components.size(); i++) {
            SlotComponent component = components.get(i);
            int[] position = componentPositions.get(i);

            int localX = x - position[0];
            int localY = y - position[1];

            if (component.isInBounds(localX, localY)) {
                return new int[] { i, localX, localY };
            }
        }
        return null;
    }

    @Override
    public boolean addItem(@NotNull ItemStack stack, int startX, int startY) {
        int[] componentInfo = findComponentAt(startX, startY);
        if (componentInfo == null) {
            return false;
        }

        SlotComponent component = components.get(componentInfo[0]);
        return component.addItem(stack, componentInfo[1], componentInfo[2]);
    }

    @Override
    public boolean canItemFit(@NotNull ItemStack stack, int startX, int startY) {
        int[] componentInfo = findComponentAt(startX, startY);
        if (componentInfo == null) {
            return false;
        }

        SlotComponent component = components.get(componentInfo[0]);
        return component.canItemFit(stack, componentInfo[1], componentInfo[2]);
    }

    @Override
    public ItemStack removeItemAt(int startX, int startY) {
        int[] componentInfo = findComponentAt(startX, startY);
        if (componentInfo == null) {
            return ItemStack.EMPTY;
        }

        SlotComponent component = components.get(componentInfo[0]);
        return component.removeItemAt(componentInfo[1], componentInfo[2]);
    }

    @Override
    public ItemStack getItemAt(int x, int y) {
        int[] componentInfo = findComponentAt(x, y);
        if (componentInfo == null) {
            return ItemStack.EMPTY;
        }

        SlotComponent component = components.get(componentInfo[0]);
        return component.getItemAt(componentInfo[1], componentInfo[2]);
    }

    @Override
    public int getSlotIndexAt(int x, int y) {
        int[] componentInfo = findComponentAt(x, y);
        if (componentInfo == null) {
            return -1;
        }

        SlotComponent component = components.get(componentInfo[0]);
        return component.getSlotIndexAt(componentInfo[1], componentInfo[2]);
    }

    public int[] getDimensions() {
        int maxWidth = 0;
        int maxHeight = 0;

        for (int i = 0; i < components.size(); i++) {
            SlotComponent component = components.get(i);
            int[] position = componentPositions.get(i);

            int componentRight = position[0] + component.columns;
            int componentBottom = position[1] + component.rows;

            maxWidth = Math.max(maxWidth, componentRight);
            maxHeight = Math.max(maxHeight, componentBottom);
        }

        return new int[] { maxWidth, maxHeight };
    }

    public int[] findEmptySpace(@NotNull ItemStack stack) {
        return null;
    }
}
