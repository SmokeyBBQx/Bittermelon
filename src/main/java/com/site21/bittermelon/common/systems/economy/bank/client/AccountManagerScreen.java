package com.site21.bittermelon.common.systems.economy.bank.client;

import com.site21.bittermelon.common.systems.economy.bank.Account;
import com.site21.bittermelon.common.systems.economy.bank.AccountRegistry;
import com.site21.bittermelon.common.systems.economy.bank.client.list.AccountListWidget;
import com.site21.bittermelon.common.systems.economy.bank.client.list.AccountManagerEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;


public class AccountManagerScreen extends Screen {
    private AccountListWidget<AccountManagerEntry> accountList;
    private EditBox searchBox;

    private static final int SEARCH_HEIGHT = 20;

    public AccountManagerScreen() {
        super(Component.literal("Account Manager"));
    }

    @Override
    protected void init() {
        searchBox = new EditBox(
                font,
                5,
                45,
                150,
                SEARCH_HEIGHT,
                Component.literal("Search accounts...")
        );
        searchBox.setMaxLength(50);
        searchBox.setResponder(this::updateSearch);

        accountList = new AccountListWidget<>(
                minecraft,
                width / 2,
                height - 80,
                70,
                40,
                AccountManagerEntry::new
        );

        for (Account account : AccountRegistry.get(Minecraft.getInstance().level).getSortedAccounts()) {
            accountList.addEntry(account, new AccountManagerEntry(account, this));
        }

        addRenderableWidget(searchBox);
        addRenderableWidget(accountList);
    }

    private void updateSearch(String searchTerm) {
        accountList.updateSearch(searchTerm);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
