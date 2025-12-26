package com.site21.bittermelon.client.gui;

import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static net.minecraft.client.gui.screens.Screen.hasShiftDown;

public class ChatWidget extends AbstractWidget {
    public static final double MOUSE_SCROLL_SPEED = 7.0f;
    private static final Component USAGE_TEXT = Component.translatable("chat_screen.usage");
    private static final int TOOLTIP_MAX_WIDTH = 210;
    private final Screen screen;
    private String historyBuffer = "";
    private int historyPos = -1;
    public EditBox input;
    private String initial;
    CommandSuggestions commandSuggestions;

    public ChatWidget(int x, int y, int width, int height, @NotNull Screen screen) {
        super(x, y, width, height, Component.literal("Chat"));
        this.screen = screen;
        init(screen.getMinecraft());
    }

    protected void init(@NotNull Minecraft minecraft) {
        historyPos = minecraft.gui.getChat().getRecentChat().size();
        input = new EditBox(minecraft.fontFilterFishy, 4, this.height - 12, this.width - 4, 12,
                Component.translatable("chat.editBox")) {
            protected @NotNull MutableComponent createNarrationMessage() {
                return super.createNarrationMessage().append(commandSuggestions.getNarrationMessage());
            }
        };

        input.setMaxLength(256);
        input.setBordered(false);
        input.setValue(initial);
        input.setResponder(this::onEdited);
        input.setCanLoseFocus(false);
        commandSuggestions = new CommandSuggestions(minecraft, screen, input, minecraft.font,
                false, false, 1, 10,
                true, -805306368);
        commandSuggestions.setAllowHiding(false);
        commandSuggestions.updateCommandInfo();
    }

    public void resize(Minecraft minecraft) {
        init(minecraft);
        setChatLine(input.getValue());
        commandSuggestions.updateCommandInfo();
    }

    private void onEdited(String value) {
        String s = this.input.getValue();
        this.commandSuggestions.setAllowSuggestions(!s.equals(this.initial));
        this.commandSuggestions.updateCommandInfo();
    }


    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (commandSuggestions.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else if (keyCode != 257 && keyCode != 335) {
            if (keyCode == 265) {
                moveInHistory(-1);
                return true;
            } else if (keyCode == 264) {
                moveInHistory(1);
                return true;
            } else {
                return false;
            }
        } else {
            this.handleChatInput(input.getValue(), true);
            return true;
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        scrollY = Mth.clamp(scrollY, -1.0F, 1.0F);
        if (!this.commandSuggestions.mouseScrolled(scrollY)) {
            if (!hasShiftDown()) {
                scrollY *= MOUSE_SCROLL_SPEED;
            }

            screen.getMinecraft().gui.getChat().scrollChat((int)scrollY);
        }
        return true;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.commandSuggestions.mouseClicked((int)mouseX, (int)mouseY, button)) {
            return true;
        } else {
            if (button == 0) {
                ChatComponent chatcomponent = screen.getMinecraft().gui.getChat();
                if (chatcomponent.handleChatQueueClicked(mouseX, mouseY)) {
                    return true;
                }

                Style style = this.getComponentStyleAt(mouseX, mouseY);
                if (style != null && screen.handleComponentClicked(style)) {
                    this.initial = this.input.getValue();
                    return true;
                }
            }

            return input.mouseClicked(mouseX, mouseY, button) || super.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    protected void insertText(String text, boolean overwrite) {
        if (overwrite) {
            this.input.setValue(text);
        } else {
            this.input.insertText(text);
        }

    }

    public void moveInHistory(int msgPos) {
        int i = this.historyPos + msgPos;
        int j = screen.getMinecraft().gui.getChat().getRecentChat().size();
        i = Mth.clamp(i, 0, j);
        if (i != this.historyPos) {
            if (i == j) {
                this.historyPos = j;
                this.input.setValue(this.historyBuffer);
            } else {
                if (this.historyPos == j) {
                    this.historyBuffer = this.input.getValue();
                }

                this.input.setValue(screen.getMinecraft().gui.getChat().getRecentChat().get(i));
                this.commandSuggestions.setAllowSuggestions(false);
                this.historyPos = i;
            }
        }

    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(2, this.height - 14, this.width - 2, this.height - 2, screen.getMinecraft().options.getBackgroundColor(Integer.MIN_VALUE));
        screen.getMinecraft().gui.getChat().render(guiGraphics, screen.getMinecraft().gui.getGuiTicks(), mouseX, mouseY, true);
        this.commandSuggestions.render(guiGraphics, mouseX, mouseY);
        GuiMessageTag guimessagetag = screen.getMinecraft().gui.getChat().getMessageTagAt((double)mouseX, (double)mouseY);
        if (guimessagetag != null && guimessagetag.text() != null) {
            guiGraphics.setTooltipForNextFrame(screen.getMinecraft().font, screen.getMinecraft().font.split(guimessagetag.text(), TOOLTIP_MAX_WIDTH), mouseX, mouseY);
        } else {
            Style style = this.getComponentStyleAt(mouseX, mouseY);
            if (style != null && style.getHoverEvent() != null) {
                guiGraphics.renderComponentHoverEffect(screen.getMinecraft().font, style, mouseX, mouseY);
            }
        }

    }

    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    }

    public boolean isPauseScreen() {
        return false;
    }

    private void setChatLine(String chatLine) {
        this.input.setValue(chatLine);
    }

    @Nullable
    private Style getComponentStyleAt(double mouseX, double mouseY) {
        return screen.getMinecraft().gui.getChat().getClickedComponentStyleAt(mouseX, mouseY);
    }

    public void handleChatInput(String message, boolean addToRecentChat) {
        message = this.normalizeChatMessage(message);
        if (!message.isEmpty()) {
            if (addToRecentChat) {
                screen.getMinecraft().gui.getChat().addRecentChat(message);
            }

            if (message.startsWith("/")) {
                screen.getMinecraft().player.connection.sendCommand(message.substring(1));
            } else {
                screen.getMinecraft().player.connection.sendChat(message);
            }
        }

    }

    public String normalizeChatMessage(String message) {
        return StringUtil.trimChatMessage(StringUtils.normalizeSpace(message.trim()));
    }
}
