package com.site21.bittermelon.content.items.wires.wire.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.devices.ElectronicDevice;
import com.site21.bittermelon.content.blocks.devices.wiring.InputPort;
import com.site21.bittermelon.content.blocks.devices.wiring.OutputPort;
import com.site21.bittermelon.content.items.wires.wire.networking.MakeWireConnection;
import com.site21.bittermelon.content.items.wires.wire.networking.RemoveWiringData;
import com.site21.bittermelon.content.items.wires.wire.networking.WiringDataUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
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

    private final ElectronicDevice electronic;
    private final InteractionHand hand;
    private List<PortButton> inputPorts;
    private List<PortButton> outputPorts;

    public WiringScreen(@NotNull ElectronicDevice electronic, InteractionHand hand) {
        super(Component.literal("Wiring"));
        this.electronic = electronic;
        this.hand = hand;
    }

    @Override
    protected void init() {
        inputPorts = new ArrayList<>();
        outputPorts = new ArrayList<>();

        int y = height / 6;
        int margin = 200;
        int inputX = margin - 30;
        int outputX = width - margin;
        int portSpacing = 30;
        int portSize = 60;

        int iteration = 0;

        for (InputPort port : electronic.getInputPorts().values()) {
            iteration++;
            PortButton portButton = new PortButton(inputX, y + iteration * portSpacing, 30, 30, port,
                    button -> handleInputPortClick(port));

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
            iteration++;
            PortButton portButton = new PortButton(outputX, y + iteration * portSpacing, 30, 30, port,
                    button -> handleOutputPortClick(port));

            InputPort connectedPort = port.getConnectedPort(minecraft.level);
            if (connectedPort != null) {
                portButton.setWired(true);
                portButton.setTooltip(Tooltip.create(Component.literal(connectedPort.id)));
            }

            outputPorts.add(portButton);
            addRenderableWidget(portButton);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        for (PortButton port : inputPorts) {
            String id = port.getPort().id;
            int x = port.getX() + port.getWidth();
            int y = port.getY() + port.getHeight() / 3;

            guiGraphics.drawString(minecraft.font, id, x, y, 0xFFFFFF);
        }

        for (PortButton port : outputPorts) {
            String id = port.getPort().id;
            int textWidth = minecraft.font.width(id);
            int x = port.getX() - textWidth - 5;
            int y = port.getY() + port.getHeight() / 3;

            guiGraphics.drawString(minecraft.font, id, x, y, 0xFFFFFF);
        }

        guiGraphics.blitSprite(WIRE_TERMINAL_SPRITE, mouseX - 26 / 2, mouseY - 10, 26, 89);
    }

    private void handleInputPortClick(@NotNull InputPort port) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack wireItem = player.getItemInHand(hand);

        if (wireItem.get(CORD_CONNECTION) == null) {
            PacketDistributor.sendToServer(new WiringDataUpdate(port.pos, port.id, player.getUUID(), hand));
            wireItem.set(CORD_CONNECTION, port.pos);
            wireItem.set(PORT_ID, port.id);
        } else {
            PacketDistributor.sendToServer(new MakeWireConnection(port.pos, wireItem.get(CORD_CONNECTION), port.id, wireItem.get(PORT_ID)));
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

        if (wireItem.get(CORD_CONNECTION) == null) {
            PacketDistributor.sendToServer(new WiringDataUpdate(port.pos, port.id, player.getUUID(), hand));
            wireItem.set(CORD_CONNECTION, port.pos);
            wireItem.set(PORT_ID, port.id);
        } else {
            PacketDistributor.sendToServer(new MakeWireConnection(wireItem.get(CORD_CONNECTION), port.pos, wireItem.get(PORT_ID), port.id));
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
