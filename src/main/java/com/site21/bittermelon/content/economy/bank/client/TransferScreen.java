package com.site21.bittermelon.content.economy.bank.client;

import com.site21.bittermelon.content.economy.bank.Account;
import com.site21.bittermelon.content.economy.bank.AccountRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

@OnlyIn(Dist.CLIENT)
public class TransferScreen extends Screen {
    private final AccountScreen parent;
    private final Account account;
    private EditBox destinationBox;
    private EditBox amountBox;
    private EditBox descriptionBox;
    private Button makeTransfer;
    private Button cancelButton;
    private int destination;
    private float amount;
    private String description;
    private final AccountRegistry accountRegistry;

    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 20;
    private static final int PADDING = 10;

    public TransferScreen(AccountScreen parent, Account account) {
        super(Component.literal("Transfer"));
        this.parent = parent;
        this.account = account;
        accountRegistry = AccountRegistry.get(Minecraft.getInstance().level);
    }

    @Override
    protected void init() {
        int centerX = width / 2;
        int startY = height / 4;

        destinationBox = new EditBox(
                font,
                centerX - (BUTTON_WIDTH / 2),
                startY,
                BUTTON_WIDTH,
                BUTTON_HEIGHT,
                Component.literal("Account Number")
        );
        destinationBox.setHint(Component.literal("Account Number"));
        destinationBox.setMaxLength(6);
        destinationBox.setResponder(str -> {
            try {
                updateDestination(Integer.parseInt(str));
            } catch (NumberFormatException ignored) {
            }
        });

        amountBox = new EditBox(
                font,
                centerX - (BUTTON_WIDTH / 2),
                startY + BUTTON_HEIGHT + PADDING,
                BUTTON_WIDTH,
                BUTTON_HEIGHT,
                Component.literal("Amount")
        );
        amountBox.setHint(Component.literal("Amount"));
        amountBox.setMaxLength(10);
        amountBox.setResponder(str -> {
            try {
                updateAmount(Float.parseFloat(str));
            } catch (NumberFormatException ignored) {
            }
        });

        descriptionBox = new EditBox(
                font,
                centerX - (BUTTON_WIDTH / 2),
                startY + (BUTTON_HEIGHT + PADDING) * 2,
                BUTTON_WIDTH,
                BUTTON_HEIGHT,
                Component.literal("Description")
        );
        descriptionBox.setHint(Component.literal("Description"));
        descriptionBox.setResponder(this::updateDescription);

        makeTransfer = Button.builder(Component.literal("Make Transfer"), button -> makeTransfer())
                .pos(centerX - (BUTTON_WIDTH / 2), startY + (BUTTON_HEIGHT + PADDING) * 3)
                .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();

        cancelButton = Button.builder(Component.literal("Cancel"), button -> minecraft.setScreen(parent))
                .pos(centerX - (BUTTON_WIDTH / 2), startY + (BUTTON_HEIGHT + PADDING) * 4)
                .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();

        addRenderableWidget(destinationBox);
        addRenderableWidget(amountBox);
        addRenderableWidget(descriptionBox);
        addRenderableWidget(makeTransfer);
        addRenderableWidget(cancelButton);
    }

    private boolean isValidTransfer() {
        return destination > 0 && amount > 0 && amount <= account.getBalance() && accountRegistry.doesAccountExist(destination);
    }

    private void updateDestination(int destination) {
        this.destination = destination;
    }

    private void updateAmount(float amount) {
        this.amount = amount;
    }

    private void updateDescription(String description) {
        this.description = description;
    }

    private void makeTransfer() {
        if (isValidTransfer()) {
            accountRegistry.makeTransaction(account.getId(), destination, amount, new Date(), description);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.drawCenteredString(
                font,
                "Transfer Funds",
                width / 2,
                height / 4 - 30,
                0xFFFFFF
        );

        guiGraphics.drawCenteredString(
                font,
                "Current Balance: " + account.getBalance(),
                width / 2,
                height / 4 - 15,
                0xFFFFFF
        );
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
