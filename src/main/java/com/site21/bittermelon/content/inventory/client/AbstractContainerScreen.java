package com.site21.bittermelon.content.inventory.client;

import com.site21.bittermelon.content.inventory.menu.IContainerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class AbstractContainerScreen<T extends IContainerMenu> extends Screen {
    protected int imageWidth = 176;
    protected int imageHeight = 166;
    protected int leftPos;
    protected int topPos;
    protected final T menu;
//    protected ItemStack draggingItem;
protected int hoveredSlotIndex = -2;

    protected AbstractContainerScreen(Component title, T menu) {
        super(title);
        this.menu = menu;
    }

    @Override
    protected void init() {

    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        int[] dimensions = menu.getDimensions();

        for (int y = 0; y < dimensions[1]; y++) {
            for (int x = 0; x < dimensions[0]; x++) {
                renderSlot(guiGraphics, x, y, menu.getItemAt(x, y));
                if (menu.getItemAt(x, y) == getItem(mouseX, mouseY)) {
                    hoveredSlotIndex = getSlotIndex(x, y);
                }
                if (hoveredSlotIndex == getSlotIndex(x, y)) {
                    renderHighlight(guiGraphics, x, y);
                }
            }
        }
    }

    private void renderSlot(@NotNull GuiGraphics guiGraphics, int x, int y, @NotNull ItemStack itemStack) {
        if (itemStack.isEmpty()) return;

        guiGraphics.fill(x, y, x + 16, y + 16, 1);
        guiGraphics.renderItem(itemStack, x, y, x + y * imageWidth);
    }

    private void renderHighlight(@NotNull GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.fill(RenderType.guiOverlay(), x, y, x + 16, y + 16, 1);
    }

    private ItemStack getItem(double mouseX, double mouseY) {
        return menu.getItemAt((int) (mouseX - leftPos), (int) (mouseY - topPos));
    }

    private int getSlotIndex(double mouseX, double mouseY) {
        return menu.getSlotIndexAt((int) (mouseX - leftPos), (int) (mouseY - topPos));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) return true;

        ItemStack itemInSlot = getItem(mouseX, mouseY);
        ItemStack carriedItem = menu.getCarried();

        if (itemInSlot == ItemStack.EMPTY && carriedItem != ItemStack.EMPTY) {
            if (menu.addItem(carriedItem, (int) (mouseX - leftPos), (int) (mouseY - topPos))) {
                menu.setCarried(ItemStack.EMPTY);
                return true;
            } else {
                return false;
            }
        }

        if (itemInSlot == ItemStack.EMPTY) return false;

        if (carriedItem != null) {
            if (menu.addItem(carriedItem.copy(), (int) (mouseX - leftPos), (int) (mouseY - topPos))) {
                menu.setCarried(itemInSlot.copy());
                return true;
            } else {
                return false;
            }
        }

        menu.setCarried(itemInSlot.copy());
        menu.removeItemAt((int) (mouseX - leftPos), (int) (mouseY - topPos));
        return true;
    }
}
