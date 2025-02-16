package com.site21.bittermelon.content.economy.client.list;

import com.site21.bittermelon.content.economy.Account;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
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
    public void render(@NotNull GuiGraphics guiGraphics, int index, int top, int left, int width, int height,
                       int mouseX, int mouseY, boolean hovered, float partialTick) {
        super.render(guiGraphics, index, top, left, width, height, mouseX, mouseY, hovered, partialTick);

                    guiGraphics.drawString(
                    Minecraft.getInstance().font,
                    Component.literal(String.format("| Balance: $%.2f", account.getBalance())),
                    left + 60,
                    top + 20,
                    0xFFFFFF
            );
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return List.of();
    }
}
