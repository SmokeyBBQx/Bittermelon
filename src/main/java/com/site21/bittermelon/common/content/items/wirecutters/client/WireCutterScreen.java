package com.site21.bittermelon.common.content.items.wirecutters.client;

import com.site21.bittermelon.common.content.items.wire.client.WiringScreen;
import com.site21.bittermelon.common.content.items.wirecutters.networking.CutWire;
import com.site21.bittermelon.common.systems.electronics.ElectronicDevice;
import com.site21.bittermelon.common.systems.electronics.wiring.InputPort;
import com.site21.bittermelon.common.systems.electronics.wiring.OutputPort;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterItems.WIRE_CUTTERS;

public class WireCutterScreen extends WiringScreen {
    public WireCutterScreen(@NotNull ElectronicDevice electronic) {
        super(electronic);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(mouseX - 16, mouseY - 16);
        guiGraphics.pose().scale(2.0f, 2.0f);
        guiGraphics.renderFakeItem(WIRE_CUTTERS.toStack(), 0, 0);
        guiGraphics.pose().popMatrix();
    }

    @Override
    protected void handleInputPortClick(@NotNull InputPort port) {
        if (!port.isConnected()) return;

        ClientPacketDistributor.sendToServer(new CutWire(port.pos, port.id, minecraft.player.getUUID(), true));
        onClose();
    }

    @Override
    protected void handleOutputPortClick(@NotNull OutputPort port) {
        if (!port.isConnected()) return;

        ClientPacketDistributor.sendToServer(new CutWire(port.pos, port.id, minecraft.player.getUUID(), false));
        onClose();
    }
}
