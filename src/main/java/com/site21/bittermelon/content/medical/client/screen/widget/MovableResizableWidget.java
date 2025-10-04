package com.site21.bittermelon.content.medical.client.screen.widget;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public abstract class MovableResizableWidget extends AbstractWidget {
    protected boolean isDragging = false;
    protected boolean isResizing = false;
    protected int dragOffsetX, dragOffsetY;
    protected int resizeEdge = 0; // 0=none, 1=right, 2=bottom, 3=corner
    protected boolean isOpen = true;
    protected int originalHeight;

    public MovableResizableWidget(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
        this.originalHeight = height;
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
        boolean onRightEdge = mouseX >= getX() + width - handleSize && mouseX < getX() + width &&
                mouseY >= getY() && mouseY < getY() + height;
        boolean onBottomEdge = mouseX >= getX() && mouseX < getX() + width &&
                mouseY >= getY() + height - handleSize && mouseY < getY() + height;
        boolean onCorner = mouseX >= getX() + width - handleSize && mouseX < getX() + width &&
                mouseY >= getY() + height - handleSize && mouseY < getY() + height;

        if (onCorner) {
            resizeEdge = 3;
            return true;
        } else if (onRightEdge) {
            resizeEdge = 1;
            return true;
        } else if (onBottomEdge) {
            resizeEdge = 2;
            return true;
        }

        resizeEdge = 0;
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isInResizeArea(mouseX, mouseY)) {
            isResizing = true;
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
        } else if (isResizing) {
            if (resizeEdge == 1 || resizeEdge == 3) {
                setWidth((int) Mth.clamp(mouseX - getX(), getMinWidth(), getMaxWidth()));
            }
            if (resizeEdge == 2 || resizeEdge == 3) {
                setHeight((int) Mth.clamp(mouseY - getY(), getMinHeight(), getMaxHeight()));
            }
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
}