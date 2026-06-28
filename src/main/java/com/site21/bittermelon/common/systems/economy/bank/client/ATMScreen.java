package com.site21.bittermelon.common.systems.economy.bank.client;

import com.site21.bittermelon.common.systems.economy.bank.Account;
import com.site21.bittermelon.common.systems.economy.bank.AccountRegistry;
import com.site21.bittermelon.common.systems.economy.bank.client.list.AccountListWidget;
import com.site21.bittermelon.common.systems.economy.bank.client.list.AccountUserEntry;
import com.site21.bittermelon.common.systems.personnel.registry.PersonnelEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.List;


public class ATMScreen extends Screen {
    private final PersonnelEntry user;
    private final List<Account> accounts;
    private AccountListWidget<AccountUserEntry> accountList;
    private EditBox searchBox;

    private static final int SEARCH_HEIGHT = 20;

    public ATMScreen(PersonnelEntry user) {
        super(Component.literal("ATM"));
        this.user = user;
        AccountRegistry accountRegistry = AccountRegistry.get(Minecraft.getInstance().level);
        accounts = accountRegistry.getPermittedAccounts(this.user);
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
                AccountUserEntry::new
        );

        for (Account account : accounts) {
            accountList.addEntry(account, new AccountUserEntry(account, this));
        }

        addRenderableWidget(searchBox);
        addRenderableWidget(accountList);
    }

    private void updateSearch(String searchTerm) {
        accountList.updateSearch(searchTerm);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);

        graphics.text(
                Minecraft.getInstance().font,
                Component.literal("Welcome back, " + user.getName()),
                5,
                10,
                0xFFFFFF
        );

        graphics.text(
                Minecraft.getInstance().font,
                Component.literal("Select Account"),
                5,
                30,
                0xFFFFFF
        );
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double mouseX = event.x();
        double mouseY = event.y();

        if (searchBox.isMouseOver(mouseX, mouseY)) {
            searchBox.setFocused(true);
            if (event.button() == 0) {
                searchBox.mouseClicked(event, doubleClick);
            }
            return true;
        } else {
            searchBox.setFocused(false);
        }

        return accountList.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (searchBox.isFocused()) {
            return searchBox.keyPressed(event);
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        if (searchBox.isFocused()) {
            return searchBox.charTyped(event);
        }
        return super.charTyped(event);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
