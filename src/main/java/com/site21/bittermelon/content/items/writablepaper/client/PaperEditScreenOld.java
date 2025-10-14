package com.site21.bittermelon.content.items.writablepaper.client;

import com.site21.bittermelon.Bittermelon;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.Util;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3x2f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class PaperEditScreenOld extends Screen {
    public static final ResourceLocation PAPER_LOCATION = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/paper.png");
    private static final int TEXT_WIDTH = 114;
    private static final int TEXT_HEIGHT = 128;
    private static final int IMAGE_WIDTH = 192;
    private static final int IMAGE_HEIGHT = 192;
    private static final int LINES = 20;
    private static final float BASE_SCALE = 0.75f;
    private static final float SCALE_INCREMENT = 0.25f;
    private static final int BASE_LINE_HEIGHT = 9;

    private String text = "";
    private Component content;
    private int frameTick;
    private int lastIndex = -1;
    private long lastClickTime;
    private final String[] lineContents = new String[LINES];
    private DisplayCache displayCache;


    private final TextFieldHelper pageEdit = new TextFieldHelper(
            this::getText,
            this::setText,
            this::getClipboard,
            this::setClipboard,
            (string) -> string.length() < 1024 && this.font.wordWrapHeight(string, 210) <= 128);

    private Button doneButton;
    private Button signButton;

    private final ItemStack paper;

    public PaperEditScreenOld(ItemStack paper) {
        super(GameNarrator.NO_TITLE);
        this.paper = paper;
        this.displayCache = DisplayCache.EMPTY;
    }

    @Override
    protected void init() {

    }

    @Override
    public void tick() {
        super.tick();
        ++this.frameTick;
    }

    private void setClipboard(String clipboardValue) {
        if (this.minecraft != null) {
            TextFieldHelper.setClipboardContents(this.minecraft, clipboardValue);
        }
    }

    private @NotNull String getClipboard() {
        return this.minecraft != null ? TextFieldHelper.getClipboardContents(this.minecraft) : "";
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        DisplayCache displayCache = this.getDisplayCache();

        for (LineInfo lineInfo : displayCache.lines) {
            guiGraphics.pose().pushMatrix();
            float scale = lineInfo.scale;
            guiGraphics.pose().scale(scale, scale, new Matrix3x2f());
            guiGraphics.drawString(this.font, lineInfo.asComponent,
                    (int) (lineInfo.x / scale),
                    (int) (lineInfo.y / scale),
                    -16777216, false);
            guiGraphics.pose().popMatrix();
        }

        this.renderHighlight(guiGraphics, displayCache.selection);
        this.renderCursor(guiGraphics, displayCache.cursor, displayCache.cursorAtEnd);
    }

    public void renderBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderTransparentBackground(guiGraphics);
        guiGraphics.blit(PAPER_LOCATION, (width - 250) / 2, 20, 2, 2, 0, 0, 250, 256);
    }

    private void renderCursor(GuiGraphics guiGraphics, Pos2i cursorPos, boolean isEndOfText) {
        if (this.frameTick / 6 % 2 == 0) {
            cursorPos = this.convertLocalToScreen(cursorPos);
            if (!isEndOfText) {
                guiGraphics.fill(cursorPos.x, cursorPos.y - 1, cursorPos.x + 1, cursorPos.y + 9, -16777216);
            } else {
                guiGraphics.drawString(this.font, "_", cursorPos.x, cursorPos.y, 0, false);
            }
        }
    }

    private void renderHighlight(GuiGraphics guiGraphics, Rect2i[] highlightAreas) {
        for(Rect2i rect2i : highlightAreas) {
            int i = rect2i.getX();
            int j = rect2i.getY();
            int k = i + rect2i.getWidth();
            int l = j + rect2i.getHeight();
            guiGraphics.fill(RenderPipelines.GUI_TEXT_HIGHLIGHT, i, j, k, l, -16776961);
        }

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else {
            if (Screen.isSelectAll(keyCode)) {
                this.pageEdit.selectAll();
                return true;
            } else if (Screen.isCopy(keyCode)) {
                this.pageEdit.copy();
                return true;
            } else if (Screen.isPaste(keyCode)) {
                this.pageEdit.paste();
                return true;
            } else if (Screen.isCut(keyCode)) {
                this.pageEdit.cut();
                return true;
            } else {
                TextFieldHelper.CursorStep cursorStep = Screen.hasControlDown() ? TextFieldHelper.CursorStep.WORD : TextFieldHelper.CursorStep.CHARACTER;
                return switch (keyCode) {
                    case 257, 335 -> {
                        this.pageEdit.insertText("\n");
                        yield true;
                    }
                    case 259 -> {
                        this.pageEdit.removeFromCursor(-1, cursorStep);
                        yield true;
                    }
                    case 261 -> {
                        this.pageEdit.removeFromCursor(1, cursorStep);
                        yield true;
                    }
                    case 262 -> {
                        this.pageEdit.moveBy(1, Screen.hasShiftDown(), cursorStep);
                        yield true;
                    }
                    case 263 -> {
                        this.pageEdit.moveBy(-1, Screen.hasShiftDown(), cursorStep);
                        yield true;
                    }
                    case 264 -> {
                        this.keyDown();
                        yield true;
                    }
                    case 265 -> {
                        this.keyUp();
                        yield true;
                    }
                    default -> false;
                };
            }
        }
    }

    public boolean charTyped(char codePoint, int modifiers) {
        if (super.charTyped(codePoint, modifiers)) {
            return true;
        } else if (StringUtil.isAllowedChatCharacter(codePoint)) {
            this.pageEdit.insertText(Character.toString(codePoint));
            this.clearDisplayCache();
            return true;
        } else {
            return false;
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!super.mouseClicked(mouseX, mouseY, button)) {
            if (button == 0) {
                long i = Util.getMillis();
                DisplayCache displayCache = this.getDisplayCache();
                int j = displayCache.getIndexAtPosition(this.font, this.convertScreenToLocal(new Pos2i((int) mouseX, (int) mouseY)));
                if (j >= 0) {
                    if (j == this.lastIndex && i - this.lastClickTime < 250L) {
                        if (!this.pageEdit.isSelecting()) {
                            this.selectWord(j);
                        } else {
                            this.pageEdit.selectAll();
                        }
                    } else {
                        this.pageEdit.setCursorPos(j, Screen.hasShiftDown());
                    }

                    this.clearDisplayCache();
                }

                this.lastIndex = j;
                this.lastClickTime = i;
            }

        }
        return true;
    }

    private void selectWord(int index) {
        String s = this.getText();
        this.pageEdit.setSelectionRange(StringSplitter.getWordPosition(s, -1, index, false), StringSplitter.getWordPosition(s, 1, index, false));
    }

    private String getText() {
        return text;
    }

    private void setText(String text) {
        this.text = text;
        this.clearDisplayCache();
    }

    private void updateLineContents() {
        Arrays.fill(lineContents, "");
        String[] lines = text.split("\n", LINES);
        System.arraycopy(lines, 0, lineContents, 0, Math.min(lines.length, LINES));
    }

    private void keyUp() {
        this.changeLine(-1);
    }

    private void keyDown() {
        this.changeLine(1);
    }

    private void changeLine(int yChange) {
        int i = this.pageEdit.getCursorPos();
        int j = this.getDisplayCache().changeLine(i, yChange);
        this.pageEdit.setCursorPos(j, Screen.hasShiftDown());
    }

    static int findLineFromPos(int[] lineStarts, int find) {
        int i = Arrays.binarySearch(lineStarts, find);
        return i < 0 ? -(i + 2) : i;
    }

    private Rect2i createPartialLineSelection(String input, StringSplitter splitter, int startPos, int endPos, int y, int lineStart, float lineScale) {
        String s = input.substring(lineStart, startPos);
        String s1 = input.substring(lineStart, endPos);
        Pos2i bookeditscreen$pos2i = new Pos2i((int)splitter.stringWidth(s), y);
        Pos2i bookeditscreen$pos2i1 = new Pos2i((int)splitter.stringWidth(s1), y + (int)(9 * lineScale));
        return this.createSelection(bookeditscreen$pos2i, bookeditscreen$pos2i1);
    }

    private Rect2i createSelection(Pos2i corner1, Pos2i corner2) {
        Pos2i bookeditscreen$pos2i = this.convertLocalToScreen(corner1);
        Pos2i bookeditscreen$pos2i1 = this.convertLocalToScreen(corner2);
        int i = Math.min(bookeditscreen$pos2i.x, bookeditscreen$pos2i1.x);
        int j = Math.max(bookeditscreen$pos2i.x, bookeditscreen$pos2i1.x);
        int k = Math.min(bookeditscreen$pos2i.y, bookeditscreen$pos2i1.y);
        int l = Math.max(bookeditscreen$pos2i.y, bookeditscreen$pos2i1.y);
        return new Rect2i(i, k, j - i, l - k);
    }

    private Pos2i convertScreenToLocal(Pos2i screenPos) {
        return new Pos2i(screenPos.x - (this.width - 192) / 2 - 36, screenPos.y - 32);
    }

    private Pos2i convertLocalToScreen(Pos2i localScreenPos) {
        return new Pos2i(localScreenPos.x + (this.width - 192) / 2 - 5, localScreenPos.y + 32);
    }

    private DisplayCache getDisplayCache() {
        if (this.displayCache == null) {
            this.displayCache = this.rebuildDisplayCache();
        }

        return this.displayCache;
    }

    private void clearDisplayCache() {
        this.displayCache = null;
    }

    private DisplayCache rebuildDisplayCache() {
        String pageText = getText();
        if (pageText.isEmpty()) return DisplayCache.EMPTY;

        int cursorPos = pageEdit.getCursorPos();
        int selectionPos = pageEdit.getSelectionPos();
        IntList lineStartPositions = new IntArrayList();
        List<LineInfo> lines = new ArrayList<>();
        MutableInt lineIndex = new MutableInt();
        MutableBoolean endsWithNewline = new MutableBoolean();

        final float[] cumulativeYOffset = {0};

        StringSplitter splitter = font.getSplitter();
        splitter.splitLines(pageText, 280, Style.EMPTY, true, (style, startPos, endPos) -> {
            int currentLineIndex = lineIndex.getAndIncrement();
            String lineText = pageText.substring(startPos, endPos);
            endsWithNewline.setValue(lineText.endsWith("\n"));
            String trimmedLine = StringUtils.stripEnd(lineText, " \n");

            float lineScale = 1.0f;
            String displayText = trimmedLine;

            if (trimmedLine.startsWith("++")) {
                lineScale = 1.5f;
                displayText = trimmedLine.substring(2);
            } else if (trimmedLine.startsWith("--")) {
                lineScale = 0.5f;
                displayText = trimmedLine.substring(2);
            } else if (trimmedLine.startsWith("+")) {
                lineScale = 1.25f;
                displayText = trimmedLine.substring(1);
            } else if (trimmedLine.startsWith("-")) {
                lineScale = 0.75f;
                displayText = trimmedLine.substring(1);
            }

            int yPosition = (int) cumulativeYOffset[0];
            cumulativeYOffset[0] += 9 * lineScale;

            Pos2i screenPos = convertLocalToScreen(new Pos2i(0, yPosition));
            lineStartPositions.add(startPos);
            lines.add(new LineInfo(style, displayText, screenPos.x, screenPos.y, lineScale));
        });

        int[] lineStarts = lineStartPositions.toIntArray();
        Pos2i cursorScreenPos = calculateCursorPosition(pageText, cursorPos, lineStarts, lines, endsWithNewline.isTrue());
        List<Rect2i> selectionRects = createSelectionRects(pageText, cursorPos, selectionPos, lineStarts, lines, splitter);

        return new DisplayCache(
                pageText,
                cursorScreenPos,
                cursorPos == pageText.length(),
                findLineFromPos(lineStarts, cursorPos),
                lineStarts,
                lines.toArray(new LineInfo[0]),
                selectionRects.toArray(new Rect2i[0])
        );
    }

    private Pos2i calculateCursorPosition(String text, int cursorPos, int[] lineStarts, List<LineInfo> lines, boolean endsWithNewline) {
        boolean cursorAtEnd = cursorPos == text.length();

        if (cursorAtEnd && endsWithNewline) {
            float totalHeight = 0;
            for (LineInfo line : lines) {
                totalHeight += 9 * line.scale;
            }
            return new Pos2i(0, (int) totalHeight);
        }

        int lineIndex = findLineFromPos(lineStarts, cursorPos);

        float yOffset = 0;
        for (int i = 0; i < lineIndex; i++) {
            yOffset += 9 * lines.get(i).scale;
        }

        int xOffset = font.width(text.substring(lineStarts[lineIndex], cursorPos));
        return new Pos2i(xOffset, (int) yOffset);
    }

    private List<Rect2i> createSelectionRects(String text, int cursorPos, int selectionPos, int[] lineStarts, List<LineInfo> lines, StringSplitter splitter) {
        List<Rect2i> selectionRects = new ArrayList<>();

        if (cursorPos == selectionPos) {
            return selectionRects;
        }

        int selectionStart = Math.min(cursorPos, selectionPos);
        int selectionEnd = Math.max(cursorPos, selectionPos);
        int startLine = findLineFromPos(lineStarts, selectionStart);
        int endLine = findLineFromPos(lineStarts, selectionEnd);

        if (startLine == endLine) {
            float yPos = 0;
            for (int i = 0; i < startLine; i++) {
                yPos += 9 * lines.get(i).scale;
            }
            selectionRects.add(createPartialLineSelection(text, splitter, selectionStart, selectionEnd, (int) yPos, lineStarts[startLine], lines.get(startLine).scale));
        } else {
            addMultiLineSelection(text, splitter, selectionStart, selectionEnd, startLine, endLine, lineStarts, lines, selectionRects);
        }

        return selectionRects;
    }

    private void addMultiLineSelection(String text, StringSplitter splitter, int selectionStart, int selectionEnd,
                                       int startLine, int endLine, int[] lineStarts, List<LineInfo> lines, List<Rect2i> selectionRects) {
        float yPos = 0;
        for (int i = 0; i < startLine; i++) {
            yPos += 9 * lines.get(i).scale;
        }

        int firstLineEnd = (startLine + 1 < lineStarts.length) ? lineStarts[startLine + 1] : text.length();
        selectionRects.add(createPartialLineSelection(text, splitter, selectionStart, firstLineEnd, (int) yPos, lineStarts[startLine], lines.get(startLine).scale));
        yPos += 9 * lines.get(startLine).scale;

        for (int line = startLine + 1; line < endLine; line++) {
            String lineText = text.substring(lineStarts[line], lineStarts[line + 1]);
            int lineWidth = (int) splitter.stringWidth(lineText);
            selectionRects.add(createSelection(new Pos2i(0, (int) yPos), new Pos2i(lineWidth, (int) yPos + (int)(9 * lines.get(line).scale))));
            yPos += 9 * lines.get(line).scale;
        }

        selectionRects.add(createPartialLineSelection(text, splitter, lineStarts[endLine], selectionEnd, (int) yPos, lineStarts[endLine], lines.get(endLine).scale));
    }

    @OnlyIn(Dist.CLIENT)
    static class DisplayCache {
        static final DisplayCache EMPTY;
        private final String fullText;
        final Pos2i cursor;
        final boolean cursorAtEnd;
        final int cursorLine;
        private final int[] lineStarts;
        final LineInfo[] lines;
        final Rect2i[] selection;

        public DisplayCache(String fullText, Pos2i cursor, boolean cursorAtEnd, int cursorLine, int[] lineStarts, LineInfo[] lines, Rect2i[] selection) {
            this.fullText = fullText;
            this.cursor = cursor;
            this.cursorAtEnd = cursorAtEnd;
            this.cursorLine = cursorLine;
            this.lineStarts = lineStarts;
            this.lines = lines;
            this.selection = selection;
        }

        public int getIndexAtPosition(Font font, @NotNull Pos2i cursorPosition) {
            float cumulativeY = 0;
            int lineIndex = -1;

            for (int i = 0; i < this.lines.length; i++) {
                float lineHeight = 9 * this.lines[i].scale;
                if (cursorPosition.y >= cumulativeY && cursorPosition.y < cumulativeY + lineHeight) {
                    lineIndex = i;
                    break;
                }
                cumulativeY += lineHeight;
            }

            if (lineIndex < 0) {
                return 0;
            } else {
                LineInfo lineInfo = this.lines[lineIndex];
                float adjustedX = cursorPosition.x / lineInfo.scale;
                return this.lineStarts[lineIndex] + font.getSplitter().plainIndexAtWidth(lineInfo.contents, (int) adjustedX, lineInfo.style);
            }
        }

        public int changeLine(int xChange, int yChange) {
            int i = findLineFromPos(this.lineStarts, xChange);
            int j = i + yChange;
            int k;
            if (0 <= j && j < this.lineStarts.length) {
                int l = xChange - this.lineStarts[i];
                int i1 = this.lines[j].contents.length();
                k = this.lineStarts[j] + Math.min(l, i1);
            } else {
                k = xChange;
            }

            return k;
        }

        public int findLineStart(int line) {
            int i = findLineFromPos(this.lineStarts, line);
            return this.lineStarts[i];
        }

        public int findLineEnd(int line) {
            int i = findLineFromPos(this.lineStarts, line);
            return this.lineStarts[i] + this.lines[i].contents.length();
        }

        static {
            EMPTY = new DisplayCache("", new Pos2i(0, 0),  true, 0, new int[]{0},
                    new LineInfo[]{new LineInfo(Style.EMPTY, "", 0, 0, 1.0f)}, new Rect2i[0]);
        }
    }

    @OnlyIn(Dist.CLIENT)
    static class LineInfo {
        final Style style;
        final String contents;
        final Component asComponent;
        final int x;
        final int y;
        final float scale;

        public LineInfo(Style style, String contents, int x, int y, float scale) {
            this.style = style;
            this.contents = contents;
            this.x = x;
            this.y = y;
            this.asComponent = Component.literal(contents).setStyle(style);
            this.scale = scale;
        }
    }

    @OnlyIn(Dist.CLIENT)
    record Pos2i(int x, int y) {
    }
}
