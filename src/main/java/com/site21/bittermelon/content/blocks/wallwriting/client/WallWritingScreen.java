package com.site21.bittermelon.content.blocks.wallwriting.client;

import com.mojang.blaze3d.platform.Lighting;
import com.site21.bittermelon.content.blocks.wallwriting.WallWritingBlockEntity;
import com.site21.bittermelon.content.blocks.wallwriting.networking.UpdateWallWriting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.SignText;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

@OnlyIn(Dist.CLIENT)
public class WallWritingScreen extends Screen {
    private static final int MAX_TEXT_LINE_WIDTH = 90;
    private static final int TEXT_LINE_HEIGHT = 10;
    private static final Vector3f TEXT_SCALE = new Vector3f(0.9765628F, 0.9765628F, 0.9765628F);

    private final WallWritingBlockEntity wallWriting;
    private SignText text;
    private final String[] messages;
    private int frame;
    private int line;
    @Nullable
    private TextFieldHelper textField;

    public WallWritingScreen(@NotNull WallWritingBlockEntity wallWriting) {
        super(Component.literal("Write On Wall"));
        this.wallWriting = wallWriting;
        text = wallWriting.getText();
        messages = IntStream.range(0, 4).mapToObj((i) -> text.getMessage(i, false)).map(Component::getString).toArray(String[]::new);
    }

    @Override
    protected void init() {
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> onClose()).bounds(
                width / 2 - 100, height / 4 + 144, 200, 20).build());
        textField = new TextFieldHelper(() ->
                messages[line],
                this::setMessage,
                TextFieldHelper.createClipboardGetter(minecraft),
                TextFieldHelper.createClipboardSetter(minecraft),
                (text) -> minecraft.font.width(text) <= MAX_TEXT_LINE_WIDTH);
    }

    @Override
    public void tick() {
        ++frame;
        if (!isValid()) {
            onClose();
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        Lighting.setupForFlatItems();
        guiGraphics.drawCenteredString(font, title, width / 2, 50, 16777215);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(width / 2.0f, 90.0f, 50.0f);
        renderText(guiGraphics);
        guiGraphics.pose().popPose();

        Lighting.setupFor3DItems();
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        int panelWidth = 100;
        int panelHeight = 50;
        int panelX = width / 2 - panelWidth / 2;
        int panelY = 90 - panelHeight / 2;
        guiGraphics.fill(panelX, panelY, panelX + panelWidth, panelY + panelHeight, 0x55000000);
    }

    private void renderText(@NotNull GuiGraphics guiGraphics) {
        setupRenderTransform(guiGraphics);

        int textColor = text.hasGlowingText() ? text.getColor().getTextColor() : SignRenderer.getDarkColor(text);
        boolean showCursor = frame / 6 % 2 == 0;
        int cursorPos = textField.getCursorPos();
        int selectionPos = textField.getSelectionPos();
        int textBlockHeight = 4 * TEXT_LINE_HEIGHT / 2;

        for (int lineIndex = 0; lineIndex < messages.length; ++lineIndex) {
            String message = messages[lineIndex];
            if (message == null) continue;

            message = formatMessageForDisplay(message);
            int lineY = lineIndex * TEXT_LINE_HEIGHT - textBlockHeight;

            renderTextLine(guiGraphics, message, lineY, textColor);

            if (lineIndex == line && cursorPos >= 0) {
                int currentLineY = line * TEXT_LINE_HEIGHT - textBlockHeight;
                renderCursorAndSelection(guiGraphics, message, currentLineY, cursorPos, selectionPos, textColor, showCursor);
            }
        }
    }

    private void setupRenderTransform(@NotNull GuiGraphics guiGraphics) {
        guiGraphics.pose().translate(0.0F, 0.0F, 4.0F);
        Vector3f scale = TEXT_SCALE;
        guiGraphics.pose().scale(scale.x(), scale.y(), scale.z());
    }

    private String formatMessageForDisplay(String message) {
        if (font.isBidirectional()) {
            return font.bidirectionalShaping(message);
        }
        return message;
    }

    private void renderTextLine(@NotNull GuiGraphics guiGraphics, String message, int lineY, int textColor) {
        int textX = -font.width(message) / 2;
        guiGraphics.drawString(font, message, textX, lineY, textColor, false);
    }

    private void renderCursorAndSelection(@NotNull GuiGraphics guiGraphics, String message, int lineY,
                                          int cursorPos, int selectionPos, int textColor, boolean showCursor) {
        int messageWidth = font.width(message);
        int centerOffset = messageWidth / 2;
        int cursorOffset = font.width(message.substring(0, Math.min(cursorPos, message.length())));
        int cursorX = cursorOffset - centerOffset;

        if (showCursor) {
            renderCursor(guiGraphics, message, cursorX, lineY, cursorPos, textColor);
        }

        if (selectionPos != cursorPos) {
            renderSelectionHighlight(guiGraphics, message, lineY, cursorPos, selectionPos, centerOffset);
        }
    }

    private void renderCursor(@NotNull GuiGraphics guiGraphics, @NotNull String message, int cursorX,
                              int lineY, int cursorPos, int textColor) {
        if (cursorPos >= message.length()) {
            guiGraphics.drawString(font, "_", cursorX, lineY, textColor, false);
        } else {
            guiGraphics.fill(cursorX, lineY - 1, cursorX + 1, lineY + TEXT_LINE_HEIGHT, -16777216 | textColor);
        }
    }

    private void renderSelectionHighlight(@NotNull GuiGraphics guiGraphics, @NotNull String message, int lineY,
                                          int cursorPos, int selectionPos, int centerOffset) {
        int selectionStart = Math.min(cursorPos, selectionPos);
        int selectionEnd = Math.max(cursorPos, selectionPos);
        int startX = font.width(message.substring(0, selectionStart)) - centerOffset;
        int endX = font.width(message.substring(0, selectionEnd)) - centerOffset;
        int highlightLeft = Math.min(startX, endX);
        int highlightRight = Math.max(startX, endX);

        guiGraphics.fill(RenderPipelines.GUI_TEXT_HIGHLIGHT, highlightLeft, lineY, highlightRight,
                lineY + TEXT_LINE_HEIGHT, -16776961);
    }

    private boolean isValid() {
        return !wallWriting.isRemoved() && !wallWriting.playerIsTooFarAwayToEdit(minecraft.player.getUUID());
    }

    private void setMessage(String message) {
        messages[this.line] = message;
        text = text.setMessage(line, Component.literal(message));
        wallWriting.setText(text);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 265) {
            line = line - 1 & 3;
            textField.setCursorToEnd();
            return true;
        } else if (keyCode != 264 && keyCode != 257 && keyCode != 335) {
            return textField.keyPressed(keyCode) || super.keyPressed(keyCode, scanCode, modifiers);
        } else {
            line = line + 1 & 3;
            textField.setCursorToEnd();
            return true;
        }
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        textField.charTyped(codePoint);
        return true;
    }

    @Override
    public void removed() {
        ClientPacketDistributor.sendToServer(new UpdateWallWriting(wallWriting.getBlockPos(), messages));
    }
}
