package com.site21.bittermelon.common.content.items.wire.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.electronics.wiring.InputPort;
import com.site21.bittermelon.common.systems.electronics.wiring.Port;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class PortButton extends AbstractWidget {
    private static final Identifier PORT_SPRITE = Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "wiring/port");
    private static final Identifier PORT_HIGHLIGHTED_SPRITE = Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "wiring/port_highlighted");
    private static final Identifier WIRED_INPUT_PORT_SPRITE = Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "wiring/connected_wire_input");
    private static final Identifier WIRED_OUTPUT_PORT_SPRITE = Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "wiring/connected_wire_output");

    private final Port<?> port;
    private boolean wired;
    private final OnPress onPress;

    public PortButton(int x, int y, int width, int height, Port<?> port, OnPress onPress) {
        super(x, y, width, height, Component.literal("Port"));
        this.port = port;
        this.onPress = onPress;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        if (wired) {
            boolean isInputPort = port instanceof InputPort;
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, isInputPort ? WIRED_INPUT_PORT_SPRITE : WIRED_OUTPUT_PORT_SPRITE, isInputPort ? x - 64 : x, y, 89, 26);
        } else {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, isMouseOver(mouseX, mouseY) ? PORT_HIGHLIGHTED_SPRITE : PORT_SPRITE, x, y, 26, 26);
        }
    }

    public boolean isWired() {
        return wired;
    }

    public void setWired(boolean wired) {
        this.wired = wired;
    }

    public Port<?> getPort() {
        return port;
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        onPress.onPress(this);
    }

    public interface OnPress {
        void onPress(PortButton button);
    }
}
