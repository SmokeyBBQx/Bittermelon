package com.site21.bittermelon.content.economy.bank.client.list;

import com.site21.bittermelon.content.economy.bank.Account;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AccountListWidget<E extends AccountEntry> extends ContainerObjectSelectionList<AccountEntry> {
    private final List<Account> allAccounts = new ArrayList<>();
    private final AccountEntryFactory<E> entryFactory;
    private String searchTerm = "";

    public AccountListWidget(Minecraft minecraft, int width, int height, int y, int itemHeight, AccountEntryFactory<E> entryFactory) {
        super(minecraft, width, height, y, itemHeight);
        this.entryFactory = entryFactory;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.width - 6;
    }

    @Override
    public int getRowWidth() {
        return this.width - 12;
    }

    public void addEntry(Account account, E entry) {
        allAccounts.add(account);
        addEntry(entry);
    }

    public void updateSearch(@NotNull String searchTerm) {
        this.searchTerm = searchTerm.toLowerCase();
        clearEntries();

        for (Account account : allAccounts) {
            if (matchesSearch(account)) {
                addEntry(entryFactory.create(account, minecraft.screen));
            }
        }
    }

    private boolean matchesSearch(Account account) {
        if (searchTerm.isEmpty()) return true;

        return account.getName().toLowerCase().contains(searchTerm) ||
                String.valueOf(account.getId()).contains(searchTerm);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        AccountEntry entry = getEntryAtPosition(mouseX, mouseY);
        if (entry == null) return false;

        return entry.mouseClicked(mouseX, mouseY, button);
    }
}

