package com.site21.bittermelon.content.economy.bank.client.list;

import com.site21.bittermelon.content.economy.bank.Account;
import net.minecraft.client.gui.screens.Screen;

@FunctionalInterface
public interface AccountEntryFactory<E extends AccountEntry> {
    E create(Account account, Screen screen);
}
