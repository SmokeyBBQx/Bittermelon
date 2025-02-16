package com.site21.bittermelon.content.inventory;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AbstractContainerScreen<T extends AbstractContainerMenu> extends Screen {
    protected int leftPos;
    protected int topPos;
    private final T menu;

    public AbstractContainerScreen(Component title, T menu) {
        super(title);
        this.menu = menu;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        for (SlotComponent slotComponent : menu.slotComponents) {
            for (int i = 0; i < slotComponent.slots.size(); i++) {
                Slot slot = slotComponent.slots.get(i);
                renderSlot(guiGraphics, slot);

                if (this.isHovering(slot, mouseX, mouseY)) {
                    renderSlotHighlight(guiGraphics, slot);
                }
            }
        }

        if (menu.carried != null) {
            renderFloatingItem(guiGraphics, menu.carried, mouseX, mouseY);
        }
    }

    @Contract(pure = true)
    private void renderSlot(GuiGraphics guiGraphics, @NotNull Slot slot) {
        ItemStack stack = slot.slotComponent.items.get(slot.itemIndex);

        if (slot.hasItem()) {

        }
    }

    private void renderSlotHighlight(GuiGraphics guiGraphics, Slot slot) {

    }

    private void renderFloatingItem(GuiGraphics guiGraphics, ItemStack stack, double x, double y) {

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Slot slot = findSlot(mouseX, mouseY);

        if (button == 1) {
            if (slot == null) return false;

            if (slot.hasItem()) {

            } else if (menu.carried != null) {
                if (slot.slotComponent.mayPlace(slot.index)) {
                    menu.place(slot.index, slot.slotComponent);
                }
            }
        }

        return true;
    }

    private @Nullable Slot findSlot(double mouseX, double mouseY) {
        for (SlotComponent slotComponent : menu.slotComponents) {
            for (int i = 0; i < slotComponent.slots.size(); i++) {
                Slot slot = slotComponent.slots.get(i);
                if (isHovering(slot, mouseX, mouseY)) {
                    return slot;
                }
            }
        }

        return null;
    }

    private boolean isHovering(@NotNull Slot slot, double mouseX, double mouseY) {
        return this.isHovering(slot.x, slot.y, 16, 16, mouseX, mouseY);
    }

    protected boolean isHovering(int x, int y, int width, int height, double mouseX, double mouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        mouseX -= (double)i;
        mouseY -= (double)j;
        return mouseX >= (double)(x - 1) && mouseX < (double)(x + width + 1) && mouseY >= (double)(y - 1) && mouseY < (double)(y + height + 1);
    }


}
