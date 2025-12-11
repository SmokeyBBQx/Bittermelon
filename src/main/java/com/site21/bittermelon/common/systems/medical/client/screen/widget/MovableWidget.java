package com.site21.bittermelon.common.systems.medical.client.screen.widget;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public abstract class MovableWidget extends AbstractWidget {
    protected boolean isDragging = false;
    protected int dragOffsetX, dragOffsetY;
    protected boolean isOpen = true;
    protected int originalHeight;

    public MovableWidget(int x, int y, int width, int height, Component message) {
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

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isDragging) {
            setX((int) (mouseX - dragOffsetX));
            setY((int) (mouseY - dragOffsetY));
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean result = isDragging;
        isDragging = false;

        return result || super.mouseReleased(mouseX, mouseY, button);
    }
}