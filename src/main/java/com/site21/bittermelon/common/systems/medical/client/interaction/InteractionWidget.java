package com.site21.bittermelon.common.systems.medical.client.interaction;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public abstract class InteractionWidget extends AbstractWidget {

    public InteractionWidget(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {

    }
}
