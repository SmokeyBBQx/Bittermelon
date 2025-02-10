package com.site21.bittermelon.economy.screen;

import com.site21.bittermelon.economy.Account;
import com.site21.bittermelon.economy.AccountRegistry;
import com.site21.bittermelon.economy.screen.list.AccountListWidget;
import com.site21.bittermelon.economy.screen.list.AccountManagerEntry;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
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

        for (Account account : AccountRegistry.getInstance().getSortedAccounts()) {
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
