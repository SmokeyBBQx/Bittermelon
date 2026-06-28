package com.site21.bittermelon.common.systems.economy.bank.client.list;

import com.site21.bittermelon.common.systems.economy.bank.Account;
import com.site21.bittermelon.common.systems.economy.bank.client.AccountScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AccountUserEntry extends AccountEntry {
    public AccountUserEntry(Account account, Screen parent) {
        super(account, parent);
    }

    @Override
    public @NotNull List<? extends NarratableEntry> narratables() {
        return List.of();
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        Minecraft.getInstance().setScreen(new AccountScreen(account, parent));
        return true;
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return List.of();
    }
}
