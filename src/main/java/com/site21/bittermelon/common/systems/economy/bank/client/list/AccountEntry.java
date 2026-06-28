package com.site21.bittermelon.common.systems.economy.bank.client.list;

import com.site21.bittermelon.common.systems.economy.bank.Account;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class AccountEntry extends ContainerObjectSelectionList.Entry<AccountEntry> {
    protected final Account account;
    protected final Screen parent;

    public AccountEntry(Account account, Screen parent) {
        this.account = account;
        this.parent = parent;
    }

    @Override
    public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
        if (hovered) {
            graphics.fill(getContentX(), getContentY(), getContentRight(), getContentBottom() - 2, 0x80808080);
        }

        graphics.text(
                Minecraft.getInstance().font,
                Component.literal("Account: " + account.getName()),
                getContentX() + 5,
                getContentY() + 5,
                0xFFFFFF
        );

        graphics.text(
                Minecraft.getInstance().font,
                Component.literal("ID: " + account.getId()),
                getContentX() + 5,
                getContentY() + 20,
                0xFFFFFF
        );
    }
}
