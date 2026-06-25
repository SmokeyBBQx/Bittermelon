package com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.privilegeeditor;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.BitterButton;
import com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.PersonnelTerminalScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
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
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/button"),
                x, y, width, height);

        nameField.extractRenderState(graphics, mouseX, mouseY, partialTick);
        addButton.extractRenderState(graphics, mouseX, mouseY, partialTick);
        cancelButton.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (nameField.mouseClicked(event, doubleClick)) {
            nameField.setFocused(true);
            return true;
        }
        if (addButton.mouseClicked(event, doubleClick)) return true;
        return cancelButton.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        return nameField.keyPressed(event);
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        return nameField.charTyped(event);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {

    }
}
