package com.site21.bittermelon.economy.screen;

import com.site21.bittermelon.database.PersonnelEntry;
import com.site21.bittermelon.economy.Account;
import com.site21.bittermelon.economy.AccountRegistry;
import com.site21.bittermelon.economy.screen.list.AccountListWidget;
import com.site21.bittermelon.economy.screen.list.AccountUserEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ATMScreen extends Screen {
    private final PersonnelEntry user;
    private final List<Account> accounts;
    private AccountListWidget<AccountUserEntry> accountList;
    private EditBox searchBox;

    private static final int SEARCH_HEIGHT = 20;

    public ATMScreen(PersonnelEntry user) {
        super(Component.literal("ATM"));
        this.user = user;
        AccountRegistry accountRegistry = AccountRegistry.getInstance();
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
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
       super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawString(
                Minecraft.getInstance().font,
                Component.literal("Welcome back, " + user.getName()),
                5,
                10,
                0xFFFFFF
        );

        guiGraphics.drawString(
                Minecraft.getInstance().font,
                Component.literal("Select Account"),
                5,
                30,
                0xFFFFFF
        );
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (searchBox.isMouseOver(mouseX, mouseY)) {
            searchBox.setFocused(true);
            if (button == 0) {
                searchBox.mouseClicked(mouseX, mouseY, button);
            }
            return true;
        } else {
            searchBox.setFocused(false);
        }

        return accountList.mouseClicked(mouseX, mouseY, button);
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (searchBox.isFocused()) {
            return searchBox.keyPressed(keyCode, scanCode, modifiers);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (searchBox.isFocused()) {
            return searchBox.charTyped(codePoint, modifiers);
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
