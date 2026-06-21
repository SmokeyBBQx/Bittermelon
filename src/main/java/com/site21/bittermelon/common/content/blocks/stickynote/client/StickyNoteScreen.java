package com.site21.bittermelon.common.content.blocks.stickynote.client;

import com.site21.bittermelon.common.content.blocks.stickynote.StickyNoteBlockEntity;
import com.site21.bittermelon.common.content.blocks.stickynote.networking.UpdateStickyNote;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StickyNoteScreen extends Screen {
    private static final int TEXT_FIELD_WIDTH = 485;
    private static final int LINE_WIDTH = 120;
    private static final int LINE_HEIGHT = 10;

    private final StickyNoteBlockEntity stickyNote;
    private final int noteIndex;
    private String message;
    @Nullable
    private TextFieldHelper textField;

    public StickyNoteScreen(@NotNull StickyNoteBlockEntity stickyNote, int noteIndex) {
        super(Component.literal("Edit Sticky Note"));
        this.stickyNote = stickyNote;
        this.noteIndex = noteIndex;
        this.message = stickyNote.getNotes()[noteIndex] != null ? stickyNote.getNotes()[noteIndex] : "";
    }

    @Override
    protected void init() {
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> onClose())
                .bounds(width / 2 - 100, height / 3 + 60, 200, 20).build());
        textField = new TextFieldHelper(
                () -> message,
                this::setMessage,
                TextFieldHelper.createClipboardGetter(minecraft),
                TextFieldHelper.createClipboardSetter(minecraft),
                (text) -> minecraft.font.width(text) <= TEXT_FIELD_WIDTH
        );
    }

    @Override
    public void render(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, int mouseX, int mouseY, float partialTick) {
        super.render(GuiGraphicsExtractor, mouseX, mouseY, partialTick);
        GuiGraphicsExtractor.drawCenteredString(font, title, width / 2, height / 3 - 40, 0xFFFFFFFF);

        List<FormattedCharSequence> wrappedLines = font.split(Component.literal(message), LINE_WIDTH);
        int startY = height / 3 - LINE_HEIGHT / 2;

        for (int i = 0; i < wrappedLines.size(); i++) {
            FormattedCharSequence line = wrappedLines.get(i);
            int lineWidth = font.width(line);
            int x = width / 2 - lineWidth / 2;
            int y = startY + i * LINE_HEIGHT;
            GuiGraphicsExtractor.drawString(font, line, x, y, 0xFFFFFFFF, false);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return textField != null && textField.keyPressed(keyCode) || super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (textField != null) textField.charTyped(codePoint);
        return true;
    }

    private void setMessage(String message) {
        this.message = message;
        stickyNote.setNote(noteIndex, message);
    }

    @Override
    public void removed() {
        ClientPacketDistributor.sendToServer(new UpdateStickyNote(stickyNote.getBlockPos(), message, noteIndex));
    }
}
