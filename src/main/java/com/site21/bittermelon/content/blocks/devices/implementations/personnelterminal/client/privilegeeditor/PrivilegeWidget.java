package com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.client.privilegeeditor;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.client.BitterButton;
import com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.client.PersonnelTerminalScreen;
import com.site21.bittermelon.content.personnel.privilege.PrivilegeManager;
import com.site21.bittermelon.content.personnel.privilege.networking.AddPrivilege;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class PrivilegeWidget extends AbstractWidget {
    private final PrivilegeManager manager;
    private PrivilegeListWidget privilegeList;
    private EditBox searchField;
    private Button addPrivilegeButton;
    private String currentSearchTerm = "";
    private EditBox inputField;

    public PrivilegeWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Component.literal("Privileges"));
        manager = PrivilegeManager.get(Minecraft.getInstance().level);
        initializeComponents(x, y, width, height);
    }

    private void initializeComponents(int x, int y, int width, int height) {
        int leftX = x + 10;
        int rightX = getRight() - 10;
        int topY = y + 10;
        int bottomY = getBottom() - 30;
        int componentHeight = 20;
        int componentSpacing = 5;
        int componentWidth = width - 20;

        searchField = new EditBox(Minecraft.getInstance().font, leftX, topY, componentWidth, componentHeight, Component.literal("Search"));
        searchField.setHint(Component.literal("Search privileges..."));
        searchField.setResponder(this::onSearchChanged);
        searchField.setFGColor(0xFFFFFFFF);

        int privilegeButtonWidth = 30;
        int privilegeButtonX = rightX - privilegeButtonWidth;
        addPrivilegeButton = BitterButton.builder(Component.literal("Add"), this::onAddPrivilege, PersonnelTerminalScreen.BUTTON_SPRITES)
                .bounds(privilegeButtonX, bottomY, privilegeButtonWidth, componentHeight)
                .build();

        int inputWidth = componentWidth - privilegeButtonWidth - componentSpacing;
        inputField = new EditBox(Minecraft.getInstance().font, leftX, bottomY, inputWidth, componentHeight, Component.literal("Search"));
        inputField.setHint(Component.literal("Privilege name..."));
        inputField.setFGColor(0xFFFFFFFF);

        int listY = topY + componentHeight + componentSpacing;
        int listHeight = height - 40 - componentHeight - componentSpacing * 2;
        privilegeList = new PrivilegeListWidget(Minecraft.getInstance(), componentWidth, listHeight, listY, componentHeight);
        privilegeList.setX(leftX);

        refreshPrivileges();
    }

    private void onSearchChanged(@NotNull String searchTerm) {
        currentSearchTerm = searchTerm.toLowerCase();
        refreshPrivileges();
    }

    private void refreshPrivileges() {
        List<String> privileges = new ArrayList<>(manager.getPrivileges());

        if (!currentSearchTerm.isEmpty()) {
            privileges.removeIf(privilege -> !privilege.contains(currentSearchTerm));
        }

        privileges = privileges.stream().sorted(String.CASE_INSENSITIVE_ORDER).toList();

        privilegeList.refreshList(privileges);
    }

    private void onAddPrivilege(Button button) {
        String input = inputField.getValue();
        if (input.isEmpty()) return;
        ClientPacketDistributor.sendToServer(new AddPrivilege(input));
        inputField.setValue("");
        currentSearchTerm = "";
        manager.getPrivileges().add(input);
        refreshPrivileges();
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blitSprite(RenderPipelines.GUI, ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/generic_background"),
                getX(), getY(), getWidth(), getHeight());

        privilegeList.render(guiGraphics, mouseX, mouseY, partialTick);
        searchField.render(guiGraphics, mouseX, mouseY, partialTick);
        addPrivilegeButton.render(guiGraphics, mouseX, mouseY, partialTick);
        inputField.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (privilegeList.mouseClicked(mouseX, mouseY, button)
                || addPrivilegeButton.mouseClicked(mouseX, mouseY, button)) {
            searchField.setFocused(false);
            inputField.setFocused(false);
            return true;
        }

        if (searchField.mouseClicked(mouseX, mouseY, button))  {
            searchField.setFocused(true);
            inputField.setFocused(false);
            return true;
        }

        if (inputField.mouseClicked(mouseX, mouseY, button)) {
            inputField.setFocused(true);
            searchField.setFocused(false);
            return true;
        }

        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return searchField.keyPressed(keyCode, scanCode, modifiers) || inputField.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return searchField.charTyped(codePoint, modifiers) || inputField.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return privilegeList.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}
