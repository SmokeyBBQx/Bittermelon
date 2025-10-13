package com.site21.bittermelon.content.items.wires.wire.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.devices.ElectronicDevice;
import com.site21.bittermelon.content.blocks.devices.wiring.InputPort;
import com.site21.bittermelon.content.blocks.devices.wiring.OutputPort;
import com.site21.bittermelon.content.items.wires.wire.networking.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.CORD_CONNECTION;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.PORT_ID;

@OnlyIn(Dist.CLIENT)
public class WiringScreen extends Screen {
    private static final ResourceLocation WIRE_TERMINAL_SPRITE = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "wiring/wire_terminal");
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "generic_background");

    private final ElectronicDevice electronic;
    private final InteractionHand hand;
    private List<PortButton> inputPorts;
    private List<PortButton> outputPorts;
    private int leftX = 0;
    private int backgroundWidth = 0;
    private int backgroundHeight = 0;

    public WiringScreen(@NotNull ElectronicDevice electronic, InteractionHand hand) {
        super(Component.literal("Wiring"));
        this.electronic = electronic;
        this.hand = hand;
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
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        for (PortButton port : inputPorts) {
            String id = port.getPort().id;
            int x = port.getX() + port.getWidth() + 10;
            int y = port.getY() + port.getHeight() / 2;
            guiGraphics.drawString(minecraft.font, id, x, y, 0xFFFFFF);
        }

        for (PortButton port : outputPorts) {
            String id = port.getPort().id;
            int textWidth = minecraft.font.width(id);
            int x = port.getX() - textWidth - 4;
            int y = port.getY() + port.getHeight() / 2;
            guiGraphics.drawString(minecraft.font, id, x, y, 0xFFFFFF);
        }

        int spriteWidth = 26;
        int spriteHeight = 89;
        guiGraphics.blitSprite(WIRE_TERMINAL_SPRITE,
                mouseX - spriteWidth / 2,
                mouseY - spriteHeight / 7,
                spriteWidth,
                spriteHeight);
    }


    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        int margin = 4;

        RenderSystem.enableBlend();
        guiGraphics.blitSprite(BACKGROUND, leftX - margin, height / 6 - margin * 4, backgroundWidth + margin * 2, backgroundHeight);
        RenderSystem.disableBlend();
    }

    private void handleInputPortClick(@NotNull InputPort port) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack wireItem = player.getItemInHand(hand);
        BlockPos wirePort = wireItem.get(CORD_CONNECTION);
        String wireId = wireItem.get(PORT_ID);

        if (wirePort == null) {
            PacketDistributor.sendToServer(new WiringDataUpdate(port.pos, port.id, player.getUUID(), hand));
            wireItem.set(CORD_CONNECTION, port.pos);
            wireItem.set(PORT_ID, port.id);
        } else {
            if (port.connectedPos != null) {
                PacketDistributor.sendToServer(new SpliceOutputWire(wirePort, port.connectedPos, wireId, port.connectedPortId));
            } else {
                PacketDistributor.sendToServer(new MakeWireConnection(port.pos, wirePort, port.id, wireId));
            }

            PacketDistributor.sendToServer(new RemoveWiringData(player.getUUID(), hand));
            wireItem.remove(CORD_CONNECTION);
            wireItem.remove(PORT_ID);
        }

        onClose();
    }

    private void handleOutputPortClick(@NotNull OutputPort port) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack wireItem = player.getItemInHand(hand);
        BlockPos wirePort = wireItem.get(CORD_CONNECTION);
        String wireId = wireItem.get(PORT_ID);

        if (wirePort == null) {
            PacketDistributor.sendToServer(new WiringDataUpdate(port.pos, port.id, player.getUUID(), hand));
            wireItem.set(CORD_CONNECTION, port.pos);
            wireItem.set(PORT_ID, port.id);
        } else {
            if (port.connectedPos != null) {
                PacketDistributor.sendToServer(new SpliceInputWire(wirePort, port.connectedPos, wireId, port.connectedPortId));
            } else {
                PacketDistributor.sendToServer(new MakeWireConnection(wirePort, port.pos, wireId, port.id));
            }

            PacketDistributor.sendToServer(new RemoveWiringData(player.getUUID(), hand));
            wireItem.remove(CORD_CONNECTION);
            wireItem.remove(PORT_ID);
        }

        onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
