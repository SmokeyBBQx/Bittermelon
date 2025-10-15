package com.site21.bittermelon.common.content.items.wires.wire.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.items.wires.wire.networking.*;
import com.site21.bittermelon.common.systems.electronics.ElectronicDevice;
import com.site21.bittermelon.common.systems.electronics.wiring.InputPort;
import com.site21.bittermelon.common.systems.electronics.wiring.OutputPort;
import com.site21.bittermelon.content.items.wires.wire.networking.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.CORD_CONNECTION;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.PORT_ID;

@OnlyIn(Dist.CLIENT)
public class WireConnectionScreen extends WiringScreen {
    private static final ResourceLocation WIRE_TERMINAL_SPRITE = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "wiring/wire_terminal");
    private final InteractionHand hand;

    public WireConnectionScreen(@NotNull ElectronicDevice electronic, InteractionHand hand) {
        super(electronic);
        this.hand = hand;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int spriteWidth = 26;
        int spriteHeight = 89;
        guiGraphics.blitSprite(WIRE_TERMINAL_SPRITE,
                mouseX - spriteWidth / 2,
                mouseY - spriteHeight / 7,
                spriteWidth,
                spriteHeight);
    }

    @Override
    protected void handleInputPortClick(@NotNull InputPort port) {
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

    @Override
    protected void handleOutputPortClick(@NotNull OutputPort port) {
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
}
