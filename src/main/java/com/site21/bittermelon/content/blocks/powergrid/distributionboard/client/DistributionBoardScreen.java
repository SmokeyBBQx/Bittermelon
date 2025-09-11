package com.site21.bittermelon.content.blocks.powergrid.distributionboard.client;

import com.site21.bittermelon.content.blocks.devices.wiring.OutputPort;
import com.site21.bittermelon.content.blocks.powergrid.distributionboard.DistributionBoardBlockEntity;
import com.site21.bittermelon.content.blocks.powergrid.distributionboard.networking.ToggleBreaker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.components.MultilineTextField;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DistributionBoardScreen extends Screen {
    private final DistributionBoardBlockEntity board;
    private final List<BreakerButton> breakers;

    public DistributionBoardScreen(DistributionBoardBlockEntity board) {
        super(Component.literal("Distribution Board"));
        this.board = board;
        breakers = new ArrayList<>();
    }

    @Override
    protected void init() {
        int x = width / 2 - (60 * board.getOutputPorts().size()) / 2;
        int y = height / 3 + 50 / 2;

        for (int i = 1; i <= board.getOutputPorts().size(); i++) {
            int finalI = i;
            BreakerButton breaker = new BreakerButton(x + i * 50, y, 50, 50, button -> toggleBreaker("WAY_" + finalI));
            breaker.setOn(board.isBreakerOn("WAY_" + i));
            breakers.add(breaker);

            MultiLineEditBox nameField = new MultiLineEditBox(minecraft.font, x + i * 50, y + 60, 50,
                    30, Component.literal(""), Component.literal(""));

            addRenderableWidget(breaker);
            addRenderableWidget(nameField);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        for (int i = 1; i <= breakers.size(); i++) {
            String name = "WAY " + i;
            BreakerButton breaker = breakers.get(i - 1);
            int x = breaker.getX() + (breaker.getWidth() - minecraft.font.width(name)) / 2;
            int y = breaker.getY() - 20;

            guiGraphics.drawString(minecraft.font, name, x, y, 0xFFFFFF);
        }
    }

    private void toggleBreaker(String breaker) {
        PacketDistributor.sendToServer(new ToggleBreaker(breaker, board.getBlockPos()));
    }
}
