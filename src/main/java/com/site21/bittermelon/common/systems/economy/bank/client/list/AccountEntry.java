package com.site21.bittermelon.common.systems.economy.bank.client.list;

import com.site21.bittermelon.common.systems.economy.bank.Account;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public abstract class AccountEntry extends ContainerObjectSelectionList.Entry<AccountEntry> {
    protected final Account account;
    protected final Screen parent;

    public AccountEntry(Account account, Screen parent) {
        this.account = account;
        this.parent = parent;
    }

    @Override
    public void render(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, int index, int top, int left, int width, int height,
                       int mouseX, int mouseY, boolean hovered, float partialTick) {
        if (hovered) {
            GuiGraphicsExtractor.fill(left, top, left + width, top + height - 2, 0x80808080);
        }

        GuiGraphicsExtractor.drawString(
                Minecraft.getInstance().font,
                Component.literal("Account: " + account.getName()),
                left + 5,
                top + 5,
                0xFFFFFF
        );

        GuiGraphicsExtractor.drawString(
                Minecraft.getInstance().font,
                Component.literal("ID: " + account.getId()),
                left + 5,
                top + 20,
                0xFFFFFF
        );
    }
}
