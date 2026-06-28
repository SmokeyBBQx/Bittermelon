package com.site21.bittermelon.common.systems.economy.bank.client.list;

import com.site21.bittermelon.common.systems.economy.bank.Account;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AccountManagerEntry extends AccountEntry {
    public AccountManagerEntry(Account account, Screen parent) {
        super(account, parent);
    }

    @Override
    public @NotNull List<? extends NarratableEntry> narratables() {
        return List.of();
    }

    @Override
    public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
        super.extractContent(graphics, mouseX, mouseY, hovered, a);

        graphics.text(
                Minecraft.getInstance().font,
                Component.literal(String.format("| Balance: $%.2f", account.getBalance())),
                getContentX() + 60,
                getContentY() + 20,
                0xFFFFFF
        );
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return List.of();
    }
}
