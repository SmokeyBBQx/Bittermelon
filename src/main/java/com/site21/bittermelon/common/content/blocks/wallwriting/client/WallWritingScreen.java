package com.site21.bittermelon.common.content.blocks.wallwriting.client;

import com.site21.bittermelon.common.content.blocks.wallwriting.WallWritingBlockEntity;
import com.site21.bittermelon.common.content.blocks.wallwriting.networking.UpdateWallWriting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.blockentity.AbstractSignRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.SignText;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.stream.IntStream;


public class WallWritingScreen extends Screen {
    private static final int MAX_TEXT_LINE_WIDTH = 90;
    private static final int TEXT_LINE_HEIGHT = 10;
    private static final Vector3f TEXT_SCALE = new Vector3f(0.9765628F, 0.9765628F, 0.9765628F);

    private final WallWritingBlockEntity wallWriting;
    private SignText text;
    private final String[] messages;
    private int frame;
    private int line;
    private @Nullable TextFieldHelper textField;

    public WallWritingScreen(@NotNull WallWritingBlockEntity wallWriting) {
        super(Component.literal("wallwriting.edit"));
        this.wallWriting = wallWriting;
        text = wallWriting.getText();
        messages = IntStream.range(0, 4).mapToObj((i) -> text.getMessage(i, false)).map(Component::getString).toArray(String[]::new);
    }

    @Override
    protected void init() {
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (_) -> onClose()).bounds(
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

    private boolean isValid() {
        return !wallWriting.isRemoved() && !wallWriting.playerIsTooFarAwayToEdit(minecraft.player.getUUID());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        extractBackground(graphics, mouseX, mouseY, partialTick);

        graphics.centeredText(font, title, width / 2, 50, 16777215);
        graphics.pose().pushMatrix();
        graphics.pose().translate(width / 2.0f, 90.0f);
        extractText(graphics);
        graphics.pose().popMatrix();
    }

    public void extractBackground(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int panelWidth = 100;
        int panelHeight = 50;
        int panelX = width / 2 - panelWidth / 2;
        int panelY = 90 - panelHeight / 2;
        graphics.fill(panelX, panelY, panelX + panelWidth, panelY + panelHeight, 0x55000000);
    }

    private void extractText(@NotNull GuiGraphicsExtractor graphics) {
        setupRenderTransform(graphics);

        int textColor = text.hasGlowingText() ? text.getColor().getTextColor() : AbstractSignRenderer.getDarkColor(text);
        boolean showCursor = frame / 6 % 2 == 0;
        int cursorPos = textField.getCursorPos();
        int selectionPos = textField.getSelectionPos();
        int textBlockHeight = 4 * TEXT_LINE_HEIGHT / 2;

        for (int lineIndex = 0; lineIndex < messages.length; ++lineIndex) {
            String message = messages[lineIndex];
            if (message == null) continue;

            message = formatMessageForDisplay(message);
            int lineY = lineIndex * TEXT_LINE_HEIGHT - textBlockHeight;

            renderTextLine(graphics, message, lineY, textColor);

            if (lineIndex == line && cursorPos >= 0) {
                int currentLineY = line * TEXT_LINE_HEIGHT - textBlockHeight;
                renderCursorAndSelection(graphics, message, currentLineY, cursorPos, selectionPos, textColor, showCursor);
            }
        }
    }

    private void setupRenderTransform(@NotNull GuiGraphicsExtractor graphics) {
        graphics.pose().translate(0.0F, 0.0F);
        Vector3f scale = TEXT_SCALE;
        graphics.pose().scale(scale.x(), scale.y());
    }

    private String formatMessageForDisplay(String message) {
        if (font.isBidirectional()) {
            return font.bidirectionalShaping(message);
        }
        return message;
    }

    private void renderTextLine(@NotNull GuiGraphicsExtractor graphics, String message, int lineY, int textColor) {
        int textX = -font.width(message) / 2;
        graphics.text(font, message, textX, lineY, textColor, false);
    }

    private void renderCursorAndSelection(@NotNull GuiGraphicsExtractor graphics, String message, int lineY,
                                          int cursorPos, int selectionPos, int textColor, boolean showCursor) {
        int messageWidth = font.width(message);
        int centerOffset = messageWidth / 2;
        int cursorOffset = font.width(message.substring(0, Math.min(cursorPos, message.length())));
        int cursorX = cursorOffset - centerOffset;

        if (showCursor) {
            renderCursor(graphics, message, cursorX, lineY, cursorPos, textColor);
        }

        if (selectionPos != cursorPos) {
            renderSelectionHighlight(graphics, message, lineY, cursorPos, selectionPos, centerOffset);
        }
    }

    private void renderCursor(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, @NotNull String message, int cursorX,
                              int lineY, int cursorPos, int textColor) {
        if (cursorPos >= message.length()) {
            GuiGraphicsExtractor.text(font, "_", cursorX, lineY, textColor, false);
        } else {
            GuiGraphicsExtractor.fill(cursorX, lineY - 1, cursorX + 1, lineY + TEXT_LINE_HEIGHT, -16777216 | textColor);
        }
    }

    private void renderSelectionHighlight(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, @NotNull String message, int lineY,
                                          int cursorPos, int selectionPos, int centerOffset) {
        int selectionStart = Math.min(cursorPos, selectionPos);
        int selectionEnd = Math.max(cursorPos, selectionPos);
        int startX = font.width(message.substring(0, selectionStart)) - centerOffset;
        int endX = font.width(message.substring(0, selectionEnd)) - centerOffset;
        int highlightLeft = Math.min(startX, endX);
        int highlightRight = Math.max(startX, endX);

        GuiGraphicsExtractor.fill(RenderPipelines.GUI_TEXT_HIGHLIGHT, highlightLeft, lineY, highlightRight,
                lineY + TEXT_LINE_HEIGHT, -16776961);
    }

    private void setMessage(String message) {
        messages[this.line] = message;
        text = text.setMessage(line, Component.literal(message));
        wallWriting.setText(text);
        ClientPacketDistributor.sendToServer(new UpdateWallWriting(wallWriting.getBlockPos(), messages, false));
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        assert textField != null;
        int keyCode = event.input();

        if (keyCode == 265) {
            line = line - 1 & 3;
            textField.setCursorToEnd();
            return true;
        } else if (keyCode != 264 && keyCode != 257 && keyCode != 335) {
            return textField.keyPressed(event) || super.keyPressed(event);
        } else {
            line = line + 1 & 3;
            textField.setCursorToEnd();
            return true;
        }
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        assert textField != null;
        textField.charTyped(event);
        return true;
    }

    @Override
    public void removed() {
        ClientPacketDistributor.sendToServer(new UpdateWallWriting(wallWriting.getBlockPos(), messages, true));
    }
}
