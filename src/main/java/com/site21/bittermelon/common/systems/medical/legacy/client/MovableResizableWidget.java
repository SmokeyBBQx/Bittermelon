package com.site21.bittermelon.common.systems.medical.legacy.client;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.input.MouseButtonEvent;
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
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (isInResizeArea(event.x(), event.y())) {
            isResizing = true;
            resizeStartX = (int) event.x();
            resizeStartY = (int) event.y();
            resizeStartWidth = width;
            return true;
        }

        if (isInDragArea(event.x(), event.y())) {
            isDragging = true;
            dragOffsetX = (int) (event.x() - getX());
            dragOffsetY = (int) (event.y() - getY());
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        if (isDragging) {
            setX((int) (event.x() - dragOffsetX));
            setY((int) (event.y() - dragOffsetY));
            return true;
        }

        if (isResizing) {
            int deltaX = (int) event.x() - resizeStartX;
            int deltaY = (int) event.y() - resizeStartY;

            int delta = Math.max(Math.abs(deltaX), Math.abs(deltaY));
            if (deltaX < 0 || deltaY < 0) {
                delta = -delta;
            }

            int newWidth = Math.max(resizeStartWidth + delta, getMinWidth());
            int newHeight = (int) Math.max(newWidth / aspectRatio, getMinHeight());

            setWidth(newWidth);
            setHeight(newHeight);
            originalHeight = newHeight;

            return true;
        }

        return super.mouseDragged(event, dx, dy);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        boolean result = isDragging || isResizing;
        isDragging = false;
        isResizing = false;

        return result || super.mouseReleased(event);
    }

    protected void extractResizeHandle(net.minecraft.client.gui.GuiGraphicsExtractor GuiGraphicsExtractor, int mouseX, int mouseY, float partialTick) {
        if (!isOpen) return;

        int handleSize = 12;
        int handleX = getX() + width - handleSize;
        int handleY = getY() + height - handleSize;

        boolean isHovering = mouseX >= handleX && mouseX < getX() + width &&
                mouseY >= handleY && mouseY < getY() + height;

        int borderColor = isHovering || isResizing ? 0xFFFFFFFF : 0xFF888888;
        GuiGraphicsExtractor.fill(handleX, getY() + height - 1, getX() + width, getY() + height, borderColor);
        GuiGraphicsExtractor.fill(getX() + width - 1, handleY, getX() + width, getY() + height, borderColor);

        for (int i = 0; i < 3; i++) {
            int offset = i * 4;
            GuiGraphicsExtractor.fill(getX() + width - handleSize + offset, getY() + height - 2,
                    getX() + width - handleSize + offset + 2, getY() + height, borderColor);
            GuiGraphicsExtractor.fill(getX() + width - 2, getY() + height - handleSize + offset,
                    getX() + width, getY() + height - handleSize + offset + 2, borderColor);
        }
    }

    protected void extractDragHandle(net.minecraft.client.gui.@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, int mouseX, int mouseY, float partialTick) {
        int headerHeight = getHeaderHeight();
        boolean isHovering = isInDragArea(mouseX, mouseY);
        int borderColor = isHovering || isDragging ? 0xFFFFFFFF : 0xFF888888;

        GuiGraphicsExtractor.fill(getX(), getY(), getX() + width, getY() + getHeaderHeight(), 0xDD000000);
        GuiGraphicsExtractor.fill(getX(), getY(), getX() + width, getY() + 1, borderColor); // Top
        GuiGraphicsExtractor.fill(getX(), getY(), getX() + 1, getY() + headerHeight, borderColor); // Left
        GuiGraphicsExtractor.fill(getX() + width - 1, getY(), getX() + width, getY() + headerHeight, borderColor); // Right
        GuiGraphicsExtractor.fill(getX(), getY() + headerHeight - 1, getX() + width, getY() + headerHeight, borderColor);

//        int dotSize = 2;
//        int spacing = 4;
//        int centerX = getX() + width / 2;
//        int centerY = getY() + headerHeight / 2;

//        for (int i = -1; i <= 1; i++) {
//            for (int j = -1; j <= 1; j++) {
//                int dotX = centerX + i * spacing - dotSize / 2;
//                int dotY = centerY + j * spacing - dotSize / 2;
//                GuiGraphicsExtractor.fill(dotX, dotY, dotX + dotSize, dotY + dotSize, borderColor);
//            }
//        }
    }
}