package com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.privilegeeditor;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.BitterButton;
import com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.PersonnelTerminalScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class AddPrivilegeGroupWidget extends AbstractWidget {
    private final PrivilegeEditorScreen screen;

    private EditBox nameField;
    private Button addButton;
    private Button cancelButton;

    public AddPrivilegeGroupWidget(int x, int y, int width, int height, PrivilegeEditorScreen screen) {
        super(x, y, width, height, Component.literal("Privilege Group Creation"));
        this.screen = screen;
        init();
    }

    private void init() {
        int leftX = x + 10;
        int topY = y + 10;
        int fieldWidth = width / 2;
        int componentHeight = 20;
        int componentSpace = 5;

        nameField = new EditBox(Minecraft.getInstance().font, leftX, topY, fieldWidth, componentHeight, Component.literal("Name"));
        nameField.setHint(Component.literal("Name"));

        int addButtonX = leftX + fieldWidth + componentSpace;
        int addButtonWidth = 30;
        addButton = BitterButton.builder(Component.literal("Add"), this::onAdd, PersonnelTerminalScreen.BUTTON_SPRITES)
                .bounds(addButtonX, topY, addButtonWidth, componentHeight)
                .build();

        int cancelButtonX = addButtonX + addButtonWidth + componentSpace;
        int cancelButtonWidth = 50;
        cancelButton = BitterButton.builder(Component.literal("Cancel"), this::onCancel, PersonnelTerminalScreen.BUTTON_SPRITES)
                .bounds(cancelButtonX, topY, cancelButtonWidth, componentHeight)
                .build();
    }

    private void onAdd(Button button) {
        screen.addPrivilegeGroup(nameField.getValue());
        screen.setActiveWidget(null);
    }

    private void onCancel(Button button) {
        screen.setActiveWidget(null);
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blitSprite(RenderPipelines.GUI, ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/button"),
                x, y, width, height);

        nameField.render(guiGraphics, mouseX, mouseY, partialTick);
        addButton.render(guiGraphics, mouseX, mouseY, partialTick);
        cancelButton.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (nameField.mouseClicked(mouseX, mouseY, button)) {
            nameField.setFocused(true);
            return true;
        }
        if (addButton.mouseClicked(mouseX, mouseY, button)) return true;
        return cancelButton.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return nameField.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return nameField.charTyped(codePoint, modifiers);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {

    }
}
