package com.site21.bittermelon.common.systems.medical.client.screen;

import com.site21.bittermelon.common.systems.medical.client.screen.widget.MovableWidget;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class CompartmentWidget extends MovableWidget {
    public CompartmentWidget(int x, int y, int width, int height, @NotNull CompartmentInstance compartment) {
        super(x, y, width, height, Component.literal(compartment.getName()));
    }

    private void initializeButtons() {

    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
