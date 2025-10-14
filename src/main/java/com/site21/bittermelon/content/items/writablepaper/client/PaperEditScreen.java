package com.site21.bittermelon.content.items.writablepaper.client;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.ChatFormatting;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class PaperEditScreen extends Screen {
    public static final ResourceLocation PAPER_LOCATION = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/paper.png");
    private String text;
    private DisplayCache displayCache;
    private final ItemStack paper;

    private final TextFieldHelper pageEdit;

    public PaperEditScreen(ItemStack paper) {
        super(GameNarrator.NO_TITLE);
        this.paper = paper;
        text = "";
        displayCache = DisplayCache.EMPTY;
        pageEdit = new TextFieldHelper(
                this::getText,
                this::setText,
                this::getClipboard,
                this::setClipboard,
                (string) -> string.length() < 1024);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        for (LineInfo lineInfo : getDisplayCache().lines) {
            guiGraphics.pose().pushMatrix();
            float scale = lineInfo.scale;
            guiGraphics.pose().scale(scale, scale, new Matrix3x2f());

            float xPos = lineInfo.centered ?
                    (width / 2f) - font.width(lineInfo.text) / 2f :
                    (width - 200) / 2f;

            guiGraphics.drawString(font, lineInfo.text, (int) (xPos / scale), (int) (lineInfo.y / scale), 0, false);
            guiGraphics.pose().popMatrix();
        }
    }

    public void renderBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderTransparentBackground(guiGraphics);
        guiGraphics.blit(PAPER_LOCATION, (this.width - 250) / 2, 20, 0, 0, 0, 0, 250, 256);
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
                    // ENTER KEY
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
                    default -> false;
                };
            }
        }
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (super.charTyped(codePoint, modifiers)) {
            return true;
        } else if (StringUtil.isAllowedChatCharacter(codePoint)) {
            pageEdit.insertText(Character.toString(codePoint));
            clearDisplayCache();
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    public void setText(String text) {
        this.text = text;
        clearDisplayCache();
    }

    public String getText() {
        return text;
    }

    private void setClipboard(String clipboardValue) {
        if (minecraft != null) {
            TextFieldHelper.setClipboardContents(minecraft, clipboardValue);
        }
    }

    private @NotNull String getClipboard() {
        return minecraft != null ? TextFieldHelper.getClipboardContents(minecraft) : "";
    }

    public DisplayCache getDisplayCache() {
        return displayCache == null || displayCache == DisplayCache.EMPTY ? rebuildDisplayCache() : displayCache;
    }

    public void clearDisplayCache() {
        displayCache = DisplayCache.EMPTY;
    }

    public DisplayCache rebuildDisplayCache() {
        int cursorPos = pageEdit.getCursorPos();
        int selectionPos = pageEdit.getSelectionPos();
        List<LineInfo> lines = new ArrayList<>();
        MutableBoolean endsWithNewline = new MutableBoolean();
        final float[] cumulativeYOffset = {0};

        ScaledStringSplitter stringSplitter = new ScaledStringSplitter(font);
        stringSplitter.splitLinesWithScaling(text, 205, Style.EMPTY, true, ((style, startPos, endPos, scale) -> {
            String lineText = text.substring(startPos, endPos);
            lineText = StringUtils.stripEnd(lineText, " \n");

            MarkdownFormat format = parseMarkdown(lineText);

            int yPosition = (int) cumulativeYOffset[0];
            cumulativeYOffset[0] += 9 * scale;

            lines.add(new LineInfo(format.component, yPosition + 25, scale, format.centered));
        }));

        return new DisplayCache(
                lines.toArray(new LineInfo[0])
        );
    }

    private boolean tempCentered = false;

    @Contract("_ -> new")
    private @NotNull MarkdownFormat parseMarkdown(@NotNull String line) {
        tempCentered = false;
        Style baseStyle = Style.EMPTY;

        if (line.startsWith("++")) {
            line = line.substring(2);
        } else if (line.startsWith("--")) {
            line = line.substring(2);
        } else if (line.startsWith("+")) {
            line = line.substring(1);
        } else if (line.startsWith("-")) {
            line = line.substring(1);
        }

        Component component = parseStyledText(line, baseStyle);

        return new MarkdownFormat(component, tempCentered);
    }

    private Component parseStyledText(@NotNull String text, Style baseStyle) {
        MutableComponent result = Component.empty();
        Style currentStyle = baseStyle;
        int i = 0;

        while (i < text.length()) {
            String delimiter = findDelimiterAt(text, i);

            if (delimiter != null) {
                result = result.append(Component.literal(delimiter).setStyle(getDelimiterStyle(currentStyle)));
                currentStyle = toggleStyle(currentStyle, baseStyle, delimiter);
                i += delimiter.length();
            } else {
                result = result.append(Component.literal(String.valueOf(text.charAt(i))).setStyle(currentStyle));
                i++;
            }
        }

        return result;
    }

    @Contract(pure = true)
    private @Nullable String findDelimiterAt(String text, int pos) {
        String[] delimiters = {"**", "~~", "__", ">>", "<<", "*"};

        for (String delim : delimiters) {
            if (text.startsWith(delim, pos)) {
                return delim;
            }
        }
        return null;
    }

    private @NotNull Style getDelimiterStyle(@NotNull Style currentStyle) {
        return currentStyle.withColor(ChatFormatting.GRAY);
    }

    private Style toggleStyle(Style current, Style base, @NotNull String delimiter) {
        return switch (delimiter) {
            case "**" -> current.withBold(!current.isBold());
            case "*" -> current.withItalic(!current.isItalic());
            case "~~" -> current.withStrikethrough(!current.isStrikethrough());
            case "__" -> current.withUnderlined(!current.isUnderlined());
            case ">>" -> {
                this.tempCentered = true;
                yield current;
            }
            case "<<" -> base;
            default -> current;
        };
    }

    @OnlyIn(Dist.CLIENT)
    record DisplayCache(LineInfo[] lines) {
        static DisplayCache EMPTY = new DisplayCache(new LineInfo[0]);
    }

    @OnlyIn(Dist.CLIENT)
    record LineInfo(Component text, int y, float scale, boolean centered) {
    }

    @OnlyIn(Dist.CLIENT)
    record MarkdownFormat(Component component, boolean centered) {
    }
}
