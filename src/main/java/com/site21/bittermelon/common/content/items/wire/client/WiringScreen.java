package com.site21.bittermelon.common.content.items.wire.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.electronics.ElectronicDevice;
import com.site21.bittermelon.common.systems.electronics.wiring.InputPort;
import com.site21.bittermelon.common.systems.electronics.wiring.OutputPort;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class WiringScreen extends Screen {
    private static final Identifier BACKGROUND = Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "generic_background");

    private final ElectronicDevice electronic;
    private List<PortButton> inputPorts;
    private List<PortButton> outputPorts;
    private int leftX = 0;
    private int backgroundWidth = 0;
    private int backgroundHeight = 0;

    protected WiringScreen(@NotNull ElectronicDevice electronic) {
        super(Component.literal("Wiring"));
        this.electronic = electronic;
    }

    @Override
    protected void init() {
        inputPorts = new ArrayList<>();
        outputPorts = new ArrayList<>();

        int marginX = (int) (width / 3.5);
        int startY = height / 6;

        int portSize = Math.max(20, height / 25);
        int portSpacing = portSize + 8;

        int inputX = marginX - portSize;
        int outputX = width - marginX;

        int iteration = 0;
        for (InputPort port : electronic.getInputPorts().values()) {
            int y = startY + iteration * portSpacing;
            iteration++;

            PortButton portButton = new PortButton(
                    inputX, y, portSize, portSize, port,
                    button -> handleInputPortClick(port)
            );

            OutputPort connectedPort = port.getConnectedPort(minecraft.level);
            if (connectedPort != null) {
                portButton.setWired(true);
                portButton.setTooltip(Tooltip.create(Component.literal(connectedPort.id)));
            }

            inputPorts.add(portButton);
            addRenderableWidget(portButton);
        }

        iteration = 0;
        for (OutputPort port : electronic.getOutputPorts().values()) {
            int y = startY + iteration * portSpacing;
            iteration++;

            PortButton portButton = new PortButton(
                    outputX, y, portSize, portSize, port,
                    button -> handleOutputPortClick(port)
            );

            InputPort connectedPort = port.getConnectedPort(minecraft.level);
            if (connectedPort != null) {
                portButton.setWired(true);
                portButton.setTooltip(Tooltip.create(Component.literal(connectedPort.id)));
            }

            outputPorts.add(portButton);
            addRenderableWidget(portButton);
        }

        leftX = inputX - 64;
        backgroundWidth = outputX - leftX + 89;
        backgroundHeight = 136 * Math.round((float) backgroundWidth / 256f);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);

        for (PortButton port : inputPorts) {
            String id = port.getPort().id;
            int x = port.getX() + port.getWidth() + 10;
            int y = port.getY() + port.getHeight() / 2;
            graphics.text(minecraft.font, id, x, y, 0xFFFFFFFF);
        }

        for (PortButton port : outputPorts) {
            String id = port.getPort().id;
            int textWidth = minecraft.font.width(id);
            int x = port.getX() - textWidth - 4;
            int y = port.getY() + port.getHeight() / 2;
            graphics.text(minecraft.font, id, x, y, 0xFFFFFFFF);
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int margin = 4;
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND, leftX - margin, height / 6 - margin * 4, backgroundWidth + margin * 2, backgroundHeight);
    }

    protected abstract void handleInputPortClick(@NotNull InputPort port);

    protected abstract void handleOutputPortClick(@NotNull OutputPort port);

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
