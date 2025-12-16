package com.site21.bittermelon.common.systems.medical.client.widget;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public abstract class MovableResizableWidget extends AbstractWidget {
    protected boolean isDragging = false;
    protected boolean isResizing = false;
    protected int dragOffsetX, dragOffsetY;
    protected int resizeStartX, resizeStartY;
    protected int resizeStartWidth;
    protected boolean isOpen = true;
    protected int originalHeight;
    private final float aspectRatio;

    public MovableResizableWidget(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
        this.originalHeight = height;
        this.aspectRatio = (float) width / height;
    }

    public void toggleOpen() {
        if (isOpen) {
            originalHeight = height;
            isOpen = false;
            setHeight(getHeaderHeight());
        } else {
            setHeight(originalHeight);
            isOpen = true;
        }
    }

    public boolean isOpen() {
        return isOpen;
    }

    protected int getHeaderHeight() {
        return 15;
    }

    protected int getMinWidth() {
        return 50;
    }

    protected int getMinHeight() {
        return getHeaderHeight() + 10;
    }

    protected int getMaxWidth() {
        return 200;
    }

    protected int getMaxHeight() {
        return 200;
    }

    protected boolean isInResizeArea(double mouseX, double mouseY) {
        if (!isOpen) return false;

        int handleSize = 12;

        return mouseX >= getX() + width - handleSize && mouseX < getX() + width &&
                mouseY >= getY() + height - handleSize && mouseY < getY() + height;
    }

    protected boolean isInDragArea(double mouseX, double mouseY) {
        return mouseX >= getX() && mouseX < getX() + width &&
                mouseY >= getY() && mouseY < getY() + getHeaderHeight();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isInResizeArea(mouseX, mouseY)) {
            isResizing = true;
            resizeStartX = (int) mouseX;
            resizeStartY = (int) mouseY;
            resizeStartWidth = width;
            return true;
        }

        if (isInDragArea(mouseX, mouseY)) {
            isDragging = true;
            dragOffsetX = (int) (mouseX - getX());
            dragOffsetY = (int) (mouseY - getY());
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isDragging) {
            setX((int) (mouseX - dragOffsetX));
            setY((int) (mouseY - dragOffsetY));
            return true;
        }

        if (isResizing) {
            int deltaX = (int) mouseX - resizeStartX;
            int deltaY = (int) mouseY - resizeStartY;

            int delta = Math.max(Math.abs(deltaX), Math.abs(deltaY));
            if (deltaX < 0 || deltaY < 0) {
                delta = -delta;
            }

            int newWidth = Math.clamp(resizeStartWidth + delta, getMinWidth(), getMaxWidth());
            int newHeight = (int) Math.clamp(newWidth / aspectRatio, getMinHeight(), getMaxHeight());

            setWidth(newWidth);
            setHeight(newHeight);
            originalHeight = newHeight;

            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean result = isDragging || isResizing;
        isDragging = false;
        isResizing = false;

        return result || super.mouseReleased(mouseX, mouseY, button);
    }

    protected void renderResizeHandle(net.minecraft.client.gui.GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!isOpen) return;

        int handleSize = 12;
        int handleX = getX() + width - handleSize;
        int handleY = getY() + height - handleSize;

        boolean isHovering = mouseX >= handleX && mouseX < getX() + width &&
                mouseY >= handleY && mouseY < getY() + height;

        int borderColor = isHovering || isResizing ? 0xFFFFFFFF : 0xFF888888;
        guiGraphics.fill(handleX, getY() + height - 1, getX() + width, getY() + height, borderColor);
        guiGraphics.fill(getX() + width - 1, handleY, getX() + width, getY() + height, borderColor);

        for (int i = 0; i < 3; i++) {
            int offset = i * 4;
            guiGraphics.fill(getX() + width - handleSize + offset, getY() + height - 2,
                    getX() + width - handleSize + offset + 2, getY() + height, borderColor);
            guiGraphics.fill(getX() + width - 2, getY() + height - handleSize + offset,
                    getX() + width, getY() + height - handleSize + offset + 2, borderColor);
        }
    }

    protected void renderDragHandle(net.minecraft.client.gui.@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int headerHeight = getHeaderHeight();
        boolean isHovering = isInDragArea(mouseX, mouseY);
        int borderColor = isHovering || isDragging ? 0xFFFFFFFF : 0xFF888888;

        guiGraphics.fill(getX(), getY(), getX() + width, getY() + getHeaderHeight(), 0xDD000000);
        guiGraphics.fill(getX(), getY(), getX() + width, getY() + 1, borderColor); // Top
        guiGraphics.fill(getX(), getY(), getX() + 1, getY() + headerHeight, borderColor); // Left
        guiGraphics.fill(getX() + width - 1, getY(), getX() + width, getY() + headerHeight, borderColor); // Right
        guiGraphics.fill(getX(), getY() + headerHeight - 1, getX() + width, getY() + headerHeight, borderColor);

        int dotSize = 2;
        int spacing = 4;
        int centerX = getX() + width / 2;
        int centerY = getY() + headerHeight / 2;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int dotX = centerX + i * spacing - dotSize / 2;
                int dotY = centerY + j * spacing - dotSize / 2;
                guiGraphics.fill(dotX, dotY, dotX + dotSize, dotY + dotSize, borderColor);
            }
        }
    }
}