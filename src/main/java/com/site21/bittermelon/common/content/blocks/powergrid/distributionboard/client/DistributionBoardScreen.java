package com.site21.bittermelon.common.content.blocks.powergrid.distributionboard.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.powergrid.distributionboard.DistributionBoardBlockEntity;
import com.site21.bittermelon.common.content.blocks.powergrid.distributionboard.networking.ToggleBreaker;
import com.site21.bittermelon.common.content.blocks.powergrid.distributionboard.networking.ToggleMainSwitch;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class DistributionBoardScreen extends Screen {
    private final DistributionBoardBlockEntity board;
    private final List<BreakerButton> breakers;
    private BreakerButton mainSwitch;

    public DistributionBoardScreen(DistributionBoardBlockEntity board) {
        super(Component.literal("Distribution Board"));
        this.board = board;
        breakers = new ArrayList<>();
    }

    @Override
    protected void init() {
        int x = 20 + width / 2 - (60 * board.getOutputPorts().size()) / 2;
        int y = height / 3 + 50 / 2;

        for (int i = 1; i <= board.getOutputPorts().size(); i++) {
            int finalI = i;
            BreakerButton breaker = new BreakerButton(x + i * 50, y, 50, 50, button -> toggleBreaker("WAY_" + finalI));
            breaker.setOn(board.isBreakerOn("WAY_" + i));
            breakers.add(breaker);

            MultiLineEditBox nameField = MultiLineEditBox.builder()
                    .setX(x + i * 50)
                    .setY(y + 60)
                    .build(minecraft.font, 50, 30, Component.literal(""));

            addRenderableWidget(breaker);
            addRenderableWidget(nameField);
        }

        mainSwitch = new BreakerButton(x - 20, y, 50, 50, button -> toggleMainSwitch(),
                Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "distributionboard/main_switch_on"),
                Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "distributionboard/main_switch_on_highlighted"),
                Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "distributionboard/main_switch_off"),
                Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "distributionboard/main_switch_off_highlighted")
        );

        mainSwitch.setOn(board.isMainSwitchOn());

        addRenderableWidget(mainSwitch);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        for (int i = 1; i <= breakers.size(); i++) {
            String name = "WAY " + i;
            BreakerButton breaker = breakers.get(i - 1);
            int x = breaker.getX() + (breaker.getWidth() - minecraft.font.width(name)) / 2;
            int y = breaker.getY() - 20;

            graphics.text(minecraft.font, name, x, y, 0xFFFFFF);
        }

        graphics.text(minecraft.font, "Main Switch", mainSwitch.getX() + (mainSwitch.getWidth() - minecraft.font.width("Main Switch")) / 2, mainSwitch.getY() - 20, 0xFFFFFF);
    }

    private void toggleBreaker(String breaker) {
        ClientPacketDistributor.sendToServer(new ToggleBreaker(breaker, board.getBlockPos()));
    }

    private void toggleMainSwitch() {
        ClientPacketDistributor.sendToServer(new ToggleMainSwitch(board.getBlockPos()));
    }
}
