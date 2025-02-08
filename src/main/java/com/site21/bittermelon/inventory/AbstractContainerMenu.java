package com.site21.bittermelon.inventory;

import com.site21.bittermelon.items.base.BaseItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AbstractContainerMenu {
    public ItemStack carried = ItemStack.EMPTY;
    public final List<SlotComponent> slotComponents = new ArrayList<>();

    protected void clicked(int slotID, int button, @NotNull ClickType clickType, @NotNull Player player) {
        if (button == 1) {
            place(slotID, null);
        }

    }

    public void attemptToPlace(int slotID, @NotNull SlotComponent slotComponent) {
        if (slotComponent.mayPlace(slotID)) {
            place(slotID, slotComponent);
        }
    }

    protected void place(int slotID, SlotComponent slotComponent) {
        if (carried == ItemStack.EMPTY || !(carried.getItem() instanceof BaseItem item)) return;
        int itemHeight = item.getItemHeight();
        int itemWidth = item.getItemWidth();

        if (!slotComponent.mayPlace(slotID)) return;

        ItemStack stack = carried.copy();
        slotComponent.items.add(stack);
        int itemIndex = slotComponent.items.indexOf(stack);

        for (int i = 0; i < itemHeight; i++) {
            for (int j = 0; j < itemWidth; j++) {
                int slotIndex = slotID + i * slotComponent.columns + j;
                slotComponent.slots.get(slotIndex).itemIndex = itemIndex;
            }
        }

        carried = ItemStack.EMPTY;
    }
}
