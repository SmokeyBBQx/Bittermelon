package com.site21.bittermelon.content.items.wires.wire.client;

import com.site21.bittermelon.content.blocks.devices.IElectronic;
import com.site21.bittermelon.content.blocks.devices.wiring.InputPort;
import com.site21.bittermelon.content.blocks.devices.wiring.OutputPort;
import com.site21.bittermelon.content.items.wires.wire.networking.MakeWireConnection;
import com.site21.bittermelon.content.items.wires.wire.networking.WiringDataUpdate;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.CORD_CONNECTION;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.PORT_ID;

@OnlyIn(Dist.CLIENT)
public class WiringScreen extends Screen {
    private final IElectronic electronic;
    private final ItemStack wireItem;

    private static final int PORT_BUTTON_WIDTH = 100;
    private static final int PORT_BUTTON_HEIGHT = 20;
    private static final int PORT_SPACING = 10;
    private static final int LEFT_MARGIN = 50;
    private static final int RIGHT_MARGIN = 50;
    private static final int TOP_MARGIN = 40;

    public WiringScreen(@NotNull IElectronic electronic, ItemStack wireItem) {
        super(Component.literal("Wiring"));
        this.electronic = electronic;
        this.wireItem = wireItem;
    }

    @Override
    protected void init() {
        final int centerX = this.width / 2;

        int yPos = TOP_MARGIN;
        for (InputPort port : electronic.getInputPorts().values()) {
            Button inputButton = new Button.Builder(Component.literal(port.id), (button) -> {
                handleInputPortClick(port);
            })
                    .pos(LEFT_MARGIN, yPos)
                    .size(PORT_BUTTON_WIDTH, PORT_BUTTON_HEIGHT)
                    .build();

            this.addRenderableWidget(inputButton);
            yPos += PORT_BUTTON_HEIGHT + PORT_SPACING;
        }

        yPos = TOP_MARGIN;

        for (OutputPort port : electronic.getOutputPorts().values()) {
            Button outputButton = new Button.Builder(Component.literal(port.id), (button) -> {
                handleOutputPortClick(port);
            })
                    .pos(this.width - RIGHT_MARGIN - PORT_BUTTON_WIDTH, yPos)
                    .size(PORT_BUTTON_WIDTH, PORT_BUTTON_HEIGHT)
                    .build();

            this.addRenderableWidget(outputButton);
            yPos += PORT_BUTTON_HEIGHT + PORT_SPACING;
        }


    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);


    }

    private void handleInputPortClick(@NotNull InputPort port) {
        if (wireItem.get(CORD_CONNECTION) == null) {
            PacketDistributor.sendToServer(new WiringDataUpdate(port.pos, port.id, wireItem));
            wireItem.set(CORD_CONNECTION, port.pos);
            wireItem.set(PORT_ID, port.id);
        } else {
            PacketDistributor.sendToServer(new MakeWireConnection(port.pos, wireItem.get(CORD_CONNECTION), port.id, wireItem.get(PORT_ID)));
        }
        onClose();
    }

    private void handleOutputPortClick(@NotNull OutputPort port) {
        if (wireItem.get(CORD_CONNECTION) == null) {
            PacketDistributor.sendToServer(new WiringDataUpdate(port.pos, port.id, wireItem));
            wireItem.set(CORD_CONNECTION, port.pos);
            wireItem.set(PORT_ID, port.id);
        } else {
            PacketDistributor.sendToServer(new MakeWireConnection(wireItem.get(CORD_CONNECTION), port.pos, wireItem.get(PORT_ID), port.id));
        }
        onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
