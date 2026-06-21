package com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.instanceprivilegeeditor;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.BitterButton;
import com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.PersonnelTerminalScreen;
import com.site21.bittermelon.common.systems.personnel.privilege.PrivilegeManager;
import com.site21.bittermelon.common.systems.personnel.privilege.PrivilegeOwner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class PrivilegeEditorWidget extends AbstractWidget {
    protected final PrivilegeManager privilegeManager;
    protected final PrivilegeOwner privilegeOwner;

    protected EditBox inputField;
    protected BitterButton toggleButton;
    protected BitterButton addButton;
    protected PrivilegeSearchList searchList;
    protected OwnedPrivilegeListWidget privilegeList;
    protected boolean privilegeValue = true;
    protected boolean searchMode = false;
    protected String currentSearchTerm = "";

    public PrivilegeEditorWidget(int x, int y, int width, int height, Component message, PrivilegeOwner privilegeOwner) {
        super(x, y, width, height, message);
        this.privilegeManager = PrivilegeManager.get(Minecraft.getInstance().player.level());
        this.privilegeOwner = privilegeOwner;
        initializeComponents(x, y, width, height);
    }

    private void initializeComponents(int x, int y, int width, int height) {
        int leftX = x + 10;
        int rightX = getRight() - 10;
        int endWidth = width - 20;
        int componentSpacing = 5;
        int topY = y + 10;
        int bottomY = getBottom() - 30;

        int componentHeight = 20;

        int addButtonWidth = 40;
        int addButtonX = rightX - addButtonWidth;
        addButton = BitterButton.builder(Component.literal("Add"),
                        button -> onAddButtonPressed(),
                        PersonnelTerminalScreen.BUTTON_SPRITES)
                .bounds(addButtonX, bottomY, addButtonWidth, componentHeight)
                .build();

        int toggleButtonSize = 20;
        int toggleButtonX = addButtonX - toggleButtonSize - componentSpacing;
        toggleButton = BitterButton.builder(Component.literal(""),
                        button -> togglePrivilegeValue(),
                        createToggleSprites(true))
                .bounds(toggleButtonX, bottomY, toggleButtonSize, toggleButtonSize)
                .tooltip(Tooltip.create(Component.literal("Grant Privilege")))
                .build();

        int inputFieldWidth = endWidth - addButtonWidth - toggleButtonSize - componentSpacing * 2;
        inputField = new EditBox(Minecraft.getInstance().font, leftX, bottomY, inputFieldWidth, componentHeight,
                Component.literal("Input"));
        inputField.setResponder(this::onSearchChanged);
        inputField.setHint(Component.literal("Type privilege..."));
        inputField.setFGColor(0xFFFFFFFF);

        int listHeight = height - 20 - componentHeight - componentSpacing;

        searchList = new PrivilegeSearchList(Minecraft.getInstance(), endWidth, listHeight, topY, componentHeight, this);
        searchList.setX(leftX);

        privilegeList = new OwnedPrivilegeListWidget(Minecraft.getInstance(), endWidth, listHeight, topY, componentHeight, this);
        privilegeList.setX(leftX);

        refreshSearchedPrivileges();
        refreshPrivileges();
    }

    @Contract("_ -> new")
    protected @NotNull WidgetSprites createToggleSprites(boolean value) {
        String state = value ? "true" : "false";
        return new WidgetSprites(
                Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/" + state + "_button"),
                Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/" + state + "_button_disabled"),
                Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/" + state + "_button_highlighted"),
                Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/" + state + "_button_focused")
        );
    }

    protected void togglePrivilegeValue() {
        privilegeValue = !privilegeValue;
        toggleButton.setSprites(createToggleSprites(privilegeValue));
        toggleButton.setTooltip(Tooltip.create(Component.literal(privilegeValue ? "Grant Privilege" : "Deny Privilege")));
    }

    protected void onSearchChanged(@NotNull String searchTerm) {
        String newSearchTerm = inputField.getValue().toLowerCase().trim();
        if (!newSearchTerm.equals(currentSearchTerm)) {
            currentSearchTerm = newSearchTerm;
            refreshSearchedPrivileges();
        }
    }

    public void setInput(String input) {
        inputField.setValue(input);
        onSearchChanged(input);
    }

    protected void enterSearchMode() {
        if (!searchMode) {
            searchMode = true;
            inputField.setFocused(true);
        }
    }

    protected void exitSearchMode() {
        searchMode = false;
        inputField.setFocused(false);
        currentSearchTerm = "";
    }

    protected void refreshSearchedPrivileges() {
        Map<String, Boolean> privileges = new LinkedHashMap<>();

        privilegeManager.getPrivilegeGroups().keySet()
                .forEach(group -> privileges.put(group, true));

        privilegeManager.getPrivileges()
                .forEach(privilege -> privileges.put(privilege, false));

        privileges.entrySet().removeIf(
                entry ->
                        entry.getKey().equals(privilegeOwner.getName())
                                || privilegeOwner.getPrivileges().containsKey(entry.getKey())
        );

        if (!currentSearchTerm.isEmpty()) {
            String searchLower = currentSearchTerm.toLowerCase();
            privileges.entrySet().removeIf(entry ->
                    !entry.getKey().toLowerCase().contains(searchLower));
        }

        searchList.refreshList(privileges);
    }

    protected void refreshPrivileges() {
        Map<String, Boolean> privileges = privilegeOwner.getPrivileges();

        privileges = privileges.entrySet().stream()
                .sorted((e1, e2) -> {
                    boolean e1HasGroup = e1.getKey().contains("group");
                    boolean e2HasGroup = e2.getKey().contains("group");
                    return Boolean.compare(e2HasGroup, e1HasGroup);
                })
                .collect(LinkedHashMap::new, (map, entry)
                        -> map.put(entry.getKey(), entry.getValue()), LinkedHashMap::putAll);

        privilegeList.refreshList(privileges);
    }

    public abstract void setPrivilege(String privilege, boolean value);

    public abstract void removePrivilege(String privilege);

    protected void onAddButtonPressed() {
        String privilege = inputField.getValue().trim();
        if (!privilege.isEmpty()) {
            privilegeOwner.getPrivileges().put(privilege, privilegeValue);
            setPrivilege(privilege, privilegeValue);
            inputField.setValue("");
            exitSearchMode();
            refreshSearchedPrivileges();
            refreshPrivileges();
        }
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, int mouseX, int mouseY, float partialTick) {
        GuiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/generic_background"),
                getX(), getY(), getWidth(), getHeight());

        if (searchMode) {
            searchList.render(GuiGraphicsExtractor, mouseX, mouseY, partialTick);
        } else {
            privilegeList.render(GuiGraphicsExtractor, mouseX, mouseY, partialTick);
        }

        inputField.render(GuiGraphicsExtractor, mouseX, mouseY, partialTick);
        toggleButton.render(GuiGraphicsExtractor, mouseX, mouseY, partialTick);
        addButton.render(GuiGraphicsExtractor, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (toggleButton.mouseClicked(mouseX, mouseY, button) ||
                addButton.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        if (inputField.mouseClicked(mouseX, mouseY, button)) {
            enterSearchMode();
            return true;
        }

        if (searchMode && searchList.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        if (!searchMode && privilegeList.mouseClicked(mouseX, mouseY, button)) {
            privilegeList.setFocused(true);
            return true;
        }

        exitSearchMode();
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (searchMode) {
            return inputField.keyPressed(keyCode, scanCode, modifiers) ||
                    searchList.keyPressed(keyCode, scanCode, modifiers);
        } else {
            return inputField.keyPressed(keyCode, scanCode, modifiers) ||
                    privilegeList.keyPressed(keyCode, scanCode, modifiers) ||
                    super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return inputField.charTyped(codePoint, modifiers) || super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (searchMode) {
            return searchList.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        }

        return privilegeList.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}
