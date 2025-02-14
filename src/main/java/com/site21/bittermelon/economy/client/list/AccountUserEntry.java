package com.site21.bittermelon.economy.client.list;

import com.site21.bittermelon.economy.Account;
import com.site21.bittermelon.economy.client.AccountScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
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
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Minecraft.getInstance().setScreen(new AccountScreen(account, parent));
        return true;
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return List.of();
    }
}
