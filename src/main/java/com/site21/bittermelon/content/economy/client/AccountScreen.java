package com.site21.bittermelon.content.economy.client;

import com.site21.bittermelon.content.economy.Account;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class AccountScreen extends Screen {
    private final Account account;
    private final Screen parent;
    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 20;
    private static final int PADDING = 10;

    public AccountScreen(@NotNull Account account, Screen parent) {
        super(Component.literal("Account: " + account.getName()));
        this.account = account;
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = width / 2;
        int startY = height / 4;

        addRenderableWidget(Button.builder(Component.literal("Withdraw Money"), button -> openWithdrawScreen())
                .pos(centerX - (BUTTON_WIDTH / 2), startY)
                .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());

        addRenderableWidget(Button.builder(Component.literal("Deposit Money"), button -> openDepositScreen())
                .pos(centerX - (BUTTON_WIDTH / 2), startY + BUTTON_HEIGHT + PADDING)
                .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());

        addRenderableWidget(Button.builder(Component.literal("Transfer Money"), button -> openTransferScreen())
                .pos(centerX - (BUTTON_WIDTH / 2), startY + (BUTTON_HEIGHT + PADDING) * 2)
                .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());

        addRenderableWidget(Button.builder(Component.literal("Transaction History"), button -> openTransactionHistory())
                .pos(centerX - (BUTTON_WIDTH / 2), startY + (BUTTON_HEIGHT + PADDING) * 3)
                .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());

        addRenderableWidget(Button.builder(Component.literal("Back"), button -> minecraft.setScreen(parent))
                .pos(centerX - (BUTTON_WIDTH / 2), height - BUTTON_HEIGHT - PADDING)
                .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());
    }


    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.drawCenteredString(
                font,
                getTitle(),
                width / 2,
                20,
                0xFFFFFF
        );

        guiGraphics.drawCenteredString(
                font,
                Component.literal(String.format("Balance: $%.2f", account.getBalance())),
                width / 2,
                40,
                0xFFFFFF
        );
    }

    private void openWithdrawScreen() {
    }

    private void openDepositScreen() {
    }

    private void openTransferScreen() {
        minecraft.setScreen(new TransferScreen(this, account));
    }

    private void openTransactionHistory() {
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
