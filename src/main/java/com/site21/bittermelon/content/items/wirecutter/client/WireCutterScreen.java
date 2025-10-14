package com.site21.bittermelon.content.items.wirecutter.client;

import com.site21.bittermelon.content.blocks.devices.ElectronicDevice;
import com.site21.bittermelon.content.blocks.devices.wiring.InputPort;
import com.site21.bittermelon.content.blocks.devices.wiring.OutputPort;
import com.site21.bittermelon.content.items.wirecutter.networking.CutWire;
import com.site21.bittermelon.content.items.wires.wire.client.WiringScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterItems.WIRE_CUTTERS;

public class WireCutterScreen extends WiringScreen {
    public WireCutterScreen(@NotNull ElectronicDevice electronic) {
        super(electronic);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(mouseX - 16, mouseY - 16, 0);
        guiGraphics.pose().scale(2.0f, 2.0f, 2.0f);
        guiGraphics.renderFakeItem(WIRE_CUTTERS.toStack(), 0, 0);
        guiGraphics.pose().popPose();
    }

    @Override
    protected void handleInputPortClick(@NotNull InputPort port) {
        if (!port.isConnected()) return;

        PacketDistributor.sendToServer(new CutWire(port.pos, port.id, minecraft.player.getUUID(), true));
        onClose();
    }

    @Override
    protected void handleOutputPortClick(@NotNull OutputPort port) {
        if (!port.isConnected()) return;

        PacketDistributor.sendToServer(new CutWire(port.pos, port.id, minecraft.player.getUUID(), false));
        onClose();
    }
}
