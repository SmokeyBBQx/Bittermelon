package com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public abstract class BaseTerminalScreen extends Screen {
    protected int x, y, screenWidth, screenHeight;
    protected int widgetX, widgetY, widgetWidth, widgetHeight;
    protected EditBox searchField;
    protected AbstractWidget activeWidget;
    protected String currentSearchTerm = "";

    protected static final int MARGIN = 10;
    protected static final int COMPONENT_HEIGHT = 20;
    protected static final int COMPONENT_SPACE = 5;

    protected BaseTerminalScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        calculateLayout();
        initializeSearchField();
        initializeWidgets();
        refreshContent();
    }

    protected void calculateLayout() {
        screenWidth = width - 100 / 2;
        screenHeight = height - 10;
        x = 100 / 2;
        y = 10;

        widgetWidth = screenWidth / 2;
        widgetHeight = screenHeight - 80 - COMPONENT_SPACE;
        widgetX = screenWidth - widgetWidth - MARGIN;
        widgetY = y + 45;
    }

    protected void initializeSearchField() {
        int leftX = x + MARGIN;
        int topY = y + 20;
        int listWidth = (int) (screenWidth / 2.75);

        searchField = new EditBox(font, leftX, topY, listWidth, COMPONENT_HEIGHT, Component.literal("Search"));
        searchField.setHint(getSearchHint());
        searchField.setResponder(this::onSearchChanged);
        searchField.setFGColor(0xFFFFFFFF);
        addRenderableWidget(searchField);
    }

    protected abstract Component getSearchHint();
    protected abstract void initializeWidgets();
    protected abstract void refreshContent();

    private void onSearchChanged(@NotNull String searchTerm) {
        currentSearchTerm = searchTerm.toLowerCase();
        refreshContent();
    }

    public void setActiveWidget(AbstractWidget widget) {
        removeWidget(activeWidget);
        activeWidget = widget;
        if (widget != null) {
            addRenderableWidget(widget);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(x, y, screenWidth, screenHeight, 0xFFF9FDFF);
        guiGraphics.fill(x + 1, y + 1, screenWidth - 1, screenHeight - 1, 0xFFD6D6CE);

        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/scp_logo"),
                widgetX + widgetX / 6, screenHeight / 4, 200, 200);

        for (Renderable renderable : renderables) {
            renderable.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        guiGraphics.fillGradient(x + 2, y + 2, screenWidth - 2, 25, 0xFF2C02AC, 0xFF1084D0);
        guiGraphics.drawString(font, getTitle().getString(), x + MARGIN, y + 5, 0xFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
