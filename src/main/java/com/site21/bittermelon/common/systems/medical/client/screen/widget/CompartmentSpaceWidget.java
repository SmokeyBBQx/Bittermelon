package com.site21.bittermelon.common.systems.medical.client.screen.widget;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.medical.client.screen.HealthScreenV2;
import com.site21.bittermelon.common.systems.medical.client.screen.HeldItemData;
import com.site21.bittermelon.common.systems.medical.client.screen.networking.MoveCompartment;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentTag;
import com.site21.bittermelon.common.systems.medical.compartment.LayerData;
import com.site21.bittermelon.common.systems.medical.compartment.VisualData;
import com.site21.bittermelon.init.custom.Compartments;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.site21.bittermelon.client.render.BitterRenderPipelines.*;

public class CompartmentSpaceWidget extends MovableResizableWidget {
    public static final ResourceLocation WINDOW_TEXTURE = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/healthscreen/surgery_window.png");
    public static final ResourceLocation WINDOW_SIDES_TEXTURE = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/healthscreen/surgery_window_sides.png");

    public static final ResourceLocation LAYER_TEXTURE = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/healthscreen/layer.png");
    public static final ResourceLocation SELECTED_LAYER_TEXTURE = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/healthscreen/selected_layer.png");
    public static final ResourceLocation INJURED_LAYER_TEXTURE = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/healthscreen/injured_layer.png");
    public static final ResourceLocation INJURED_SELECTED_LAYER_TEXTURE = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/healthscreen/injured_selected_layer.png");

    private static final int HEADER_HEIGHT = 15;
    private static final int TILE_SIZE = 80;
    private static final int EDGE_MARGIN = 20;

    private Button closeWidgetButton;
    private Button collapseWidgetButton;
    private Button increaseLayerButton;
    private Button decreaseLayerButton;
    private Button recenterButton;

    private final int CLOSE_RIGHT_MARGIN = 15;
    private final int COLLAPSE_RIGHT_MARGIN = 28;
    private final int LAYER_RIGHT_MARGIN = 23;
    private final int TOP_MARGIN = 5;
    private final int INCREASE_TOP_MARGIN = 20;
    private final int DECREASE_TOP_MARGIN = 32;

    private int contentX = 0;
    private int contentY = 0;
    private int contentWidth = 0;
    private int contentHeight = 0;
    private static final int CONTENT_TOP_PADDING = 16;

    private double scrollX = 0;
    private double scrollY = 0;
    private boolean isContentDragging = false;

    private CompartmentInstance compartment;
    private final HealthScreenV2 healthScreen;
    private final List<CompartmentNodeWidget> compartmentWidgets;
    private int layerIndex = 0;
    private final LayerData[] layers;
    private List<CompartmentNodeWidget> sortedWidgets = null;

    private final Button[] buttons;
    private ResourceLocation backgroundTexture;

    public CompartmentSpaceWidget(int x, int y, int width, int height, @NotNull CompartmentInstance compartment, HealthScreenV2 healthScreen) {
        super(x, y, width, height, Component.literal(compartment.getName()));
        this.compartment = compartment;
        this.healthScreen = healthScreen;
        this.compartmentWidgets = new ArrayList<>();
        this.layers = compartment.getCompartment().getLayers();
        initializeButtons();
        buttons = new Button[]{closeWidgetButton, collapseWidgetButton, increaseLayerButton, decreaseLayerButton, recenterButton};
        refreshCompartmentNodes();
        backgroundTexture = layers[0].backgroundTexture();

        setContentX(x);
        setContentY(y);
        setContentWidth(width);
        setContentHeight(height);
    }

    private void initializeButtons() {
        closeWidgetButton = Button.builder(
                        Component.literal("X"),
                        (button) -> healthScreen.removeCompartmentSpace(this))
                .pos(x + width - CLOSE_RIGHT_MARGIN, y + TOP_MARGIN)
                .size(10, 10)
                .build();

        collapseWidgetButton = Button.builder(
                        Component.literal(isOpen ? "-" : "+"),
                        (button) -> toggleOpen())
                .pos(x + width - COLLAPSE_RIGHT_MARGIN, y + TOP_MARGIN)
                .size(10, 10)
                .build();

        increaseLayerButton = Button.builder(
                        Component.literal("↑"),
                        (button) -> increaseLayer())
                .pos(x + width - LAYER_RIGHT_MARGIN, y + INCREASE_TOP_MARGIN)
                .size(10, 10)
                .build();

        decreaseLayerButton = Button.builder(
                        Component.literal("↓"),
                        (button) -> decreaseLayer())
                .pos(x + width - LAYER_RIGHT_MARGIN, y + DECREASE_TOP_MARGIN)
                .size(10, 10)
                .build();

        recenterButton = Button.builder(
                        Component.literal("⊕"),
                        (button) -> recenterView())
                .pos(x + 12, y + INCREASE_TOP_MARGIN)
                .size(10, 10)
                .build();
    }

    private void updateButtons() {
        closeWidgetButton.setX(x + width - CLOSE_RIGHT_MARGIN);
        closeWidgetButton.setY(y + TOP_MARGIN);

        collapseWidgetButton.setX(x + width - COLLAPSE_RIGHT_MARGIN);
        collapseWidgetButton.setY(y + TOP_MARGIN);
        collapseWidgetButton.setMessage(Component.literal(isOpen ? "-" : "+"));

        increaseLayerButton.setX(x + width - LAYER_RIGHT_MARGIN);
        increaseLayerButton.setY(y + INCREASE_TOP_MARGIN);

        decreaseLayerButton.setX(x + width - LAYER_RIGHT_MARGIN);
        decreaseLayerButton.setY(y + DECREASE_TOP_MARGIN);

        recenterButton.setX(x + 12);
        recenterButton.setY(y + INCREASE_TOP_MARGIN);
    }

    private void increaseLayer() {
        if (layerIndex == 0) {
            layerIndex = layers.length - 1;
            refreshCompartmentNodes();
            return;
        }
        layerIndex--;
        refreshCompartmentNodes();
    }

    private void decreaseLayer() {
        if (layerIndex >= layers.length - 1) {
            layerIndex = 0;
            refreshCompartmentNodes();
            return;
        }
        layerIndex++;
        refreshCompartmentNodes();
    }

    private void recenterView() {
        scrollX = 0;
        scrollY = 0;
        updateCompartmentWidgetPositions();
    }

    public void refreshCompartmentNodes() {
        clearCompartmentNodes();
        sortedWidgets = null;

        LayerData layerData = layers[layerIndex];
        backgroundTexture = layerData.backgroundTexture();
        setMessage(Component.literal(layerData.name()));

        createCompartmentWidgets(compartment.getLayer(layerIndex));
    }

    private void createCompartmentWidgets(@NotNull Set<UUID> compartmentList) {
        for (UUID uuid : compartmentList) {
            CompartmentInstance instance = healthScreen.getMedicalStats().getCompartment(uuid);
            if (instance == null) continue;

            CompartmentNodeWidget widget = getCompartmentNodeWidget(instance);
            compartmentWidgets.add(widget);
        }
    }

    private @NotNull CompartmentNodeWidget getCompartmentNodeWidget(@NotNull CompartmentInstance compartment) {
        VisualData visualData = compartment.getVisualData();

        CompartmentNodeWidget widget = new CompartmentNodeWidget(
                contentX + visualData.x,
                contentY + visualData.y,
                26, 26,
                Component.literal(compartment.getName()),
                healthScreen,
                compartment
        );

        widget.setRelativeX(visualData.x);
        widget.setRelativeY(visualData.y);

        widget.setX(contentX + widget.getRelativeX() - Mth.floor(scrollX));
        widget.setY(contentY + widget.getRelativeY() - Mth.floor(scrollY));

        return widget;
    }

    @Override
    protected int getHeaderHeight() {
        return HEADER_HEIGHT;
    }

    @Override
    protected int getMinWidth() {
        return Minecraft.getInstance().font.width(getMessage()) + CLOSE_RIGHT_MARGIN + COLLAPSE_RIGHT_MARGIN;
    }

    @Override
    protected int getMinHeight() {
        return INCREASE_TOP_MARGIN * 2 + 5;
    }

    @Override
    protected int getMaxWidth() {
        return layers[layerIndex].width() + EDGE_MARGIN;
    }

    @Override
    protected int getMaxHeight() {
        return layers[layerIndex].height() + EDGE_MARGIN;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        List<CompartmentInstance> revealingCompartments = findRevealingCompartments();

        CompartmentNodeWidget hoveredWidget = null;

        if (isOpen) {
            hoveredWidget = getHoveredWidget(revealingCompartments, mouseX, mouseY, contentX, contentY);

            // Render only within content area
            guiGraphics.enableScissor(contentX, contentY, contentX + contentWidth, contentY + contentHeight);

            drawTiledBackground(guiGraphics, RenderPipelines.GUI_TEXTURED, backgroundTexture, 0xFFFFFFFF);
            renderWidgets(guiGraphics, mouseX, mouseY, partialTick, hoveredWidget);
            drawHoveredWidget(guiGraphics, mouseX, mouseY, partialTick, hoveredWidget);
            renderFog(guiGraphics, revealingCompartments);

            renderLayerIndicators(guiGraphics);
            renderButtons(guiGraphics, mouseX, mouseY, partialTick);

            guiGraphics.disableScissor();
        }

        drawWindowFrame(guiGraphics);
        closeWidgetButton.render(guiGraphics, mouseX, mouseY, partialTick);
        collapseWidgetButton.render(guiGraphics, mouseX, mouseY, partialTick);

        // Draw title
        guiGraphics.drawString(
                Minecraft.getInstance().font,
                getMessage(),
                x + 8,
                y + 6,
                0xFFFFFFFF
        );

        // Draw hovered widget tooltip
        if (hoveredWidget != null && hoveredWidget.visible && isOpen) {
            hoveredWidget.drawHover(guiGraphics, mouseX, mouseY, partialTick, width, height);
        }
    }

    private @NotNull List<CompartmentInstance> findRevealingCompartments() {
        List<CompartmentInstance> revealingCompartments = new ArrayList<>();

        if (layerIndex > 0) {
            for (UUID uuid : compartment.getLayer(layerIndex - 1)) {
                CompartmentInstance instance = healthScreen.getMedicalStats().getCompartment(uuid);
                if (instance.hasTag(CompartmentTag.CUT) || instance.getCompartment().equals(Compartments.LIVER.get()) || instance.getCompartment().equals(Compartments.COLON.get())) {
                    revealingCompartments.add(instance);
                }
            }
        }

        return revealingCompartments;
    }

    private void renderButtons(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        increaseLayerButton.render(guiGraphics, mouseX, mouseY, partialTick);
        decreaseLayerButton.render(guiGraphics, mouseX, mouseY, partialTick);
        recenterButton.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void renderWidgets(GuiGraphics guiGraphics, int mouseX, int mouseY,
                               float partialTick, CompartmentNodeWidget hoveredWidget) {
        sortWidgets();

        for (CompartmentNodeWidget widget : sortedWidgets) {
            // Skip hovered widget to render it on top later
            if (hoveredWidget != null && hoveredWidget.equals(widget)) continue;
            widget.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    private void sortWidgets() {
        if (sortedWidgets == null) {
            sortedWidgets = new ArrayList<>(compartmentWidgets);
            sortedWidgets.sort((a, b) ->
                    Float.compare(a.getCompartment().getVisualData().z, b.getCompartment().getVisualData().z));
        }
    }

    private void drawHoveredWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CompartmentNodeWidget hoveredWidget) {
        if (hoveredWidget == null || !hoveredWidget.visible) return;

        for (Button button : buttons) {
            if (button.isMouseOver(mouseX, mouseY)) {
                return;
            }
        }

        hoveredWidget.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void renderLayerIndicators(GuiGraphics guiGraphics) {
        for (int i = 0; i < layers.length; i++) {
            int indicatorX = x + width - 24;
            int indicatorY = y + 50 + i * 6;

            ResourceLocation sprite = layerIndex == i ?
                    isLayerInjured(i) ? INJURED_SELECTED_LAYER_TEXTURE : SELECTED_LAYER_TEXTURE :
                    isLayerInjured(i) ? INJURED_LAYER_TEXTURE : LAYER_TEXTURE;

            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprite, indicatorX, indicatorY, 0, 0,
                    11, 5, 11, 5);
        }
    }

    private void drawTiledBackground(@NotNull GuiGraphics guiGraphics, RenderPipeline pipeline, ResourceLocation texture, int color) {
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(contentX, contentY);

        int tileOffsetX = (int) -scrollX / 5;
        int tileOffsetY = (int) -scrollY / 5;

        int startTileX = (tileOffsetX / TILE_SIZE) - 1;
        int startTileY = (tileOffsetY / TILE_SIZE) - 1;
        int endTileX = ((tileOffsetX + contentWidth) / TILE_SIZE) + 2;
        int endTileY = ((tileOffsetY + contentHeight) / TILE_SIZE) + 2;

        int startX = (startTileX * TILE_SIZE) - tileOffsetX;
        int startY = (startTileY * TILE_SIZE) - tileOffsetY;

        for (int i = startTileX; i <= endTileX; i++) {
            int x = startX + ((i - startTileX) * TILE_SIZE);
            for (int j = startTileY; j <= endTileY; j++) {
                int y = startY + ((j - startTileY) * TILE_SIZE);

                if (x + TILE_SIZE >= 0 && x <= contentWidth && y + TILE_SIZE >= 0 && y <= contentHeight) {
                    guiGraphics.blit(
                            pipeline,
                            texture,
                            x, y,
                            0, 0,
                            TILE_SIZE, TILE_SIZE,
                            TILE_SIZE, TILE_SIZE,
                            color
                    );
                }
            }
        }

        guiGraphics.pose().popMatrix();
    }

    private void drawWindowFrame(@NotNull GuiGraphics guiGraphics) {
        // TODO: Ugly code but works

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, WINDOW_TEXTURE, x, y, 0, 0, width / 2, 23, 256, 256);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, WINDOW_TEXTURE, x + width / 2, y, 252 - width / 2, 0, width / 2, 23, 256, 256);

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, WINDOW_SIDES_TEXTURE, x, y + 23, 0, 23, width / 2, height - 48, 256, 256);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, WINDOW_SIDES_TEXTURE, x + width / 2, y + 23, 256 - width / 2, 23, width / 2, height - 48, 256, 256);

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, WINDOW_TEXTURE, x, y + height - 25, 0, 130 - 5, width / 2, 15, 256, 256);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, WINDOW_TEXTURE, x + width / 2, y + height - 25, 252 - width / 2, 130 - 5, width / 2, 15, 256, 256);
    }

    private void renderFog(@NotNull GuiGraphics guiGraphics, @NotNull List<CompartmentInstance> revealingCompartments) {
        if (layerIndex == 0) return;

        int lightFogColor = 0xAA000000;
        int darkFogColor = 0xF2000000;

        if (revealingCompartments.isEmpty()) {
            guiGraphics.fill(contentX, contentY, contentX + contentWidth, contentY + contentHeight, darkFogColor);
            return;
        }

        // Draw revealing compartments to stencil buffer (value = 1)
        for (CompartmentInstance instance : revealingCompartments) {
            VisualData visualData = instance.getVisualData();
            float scaleFactor = visualData.getScale();
            float minRevealX = (float) (contentX + visualData.getX() - scrollX);
            float minRevealY = (float) (contentY + visualData.getY() - scrollY);

            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(minRevealX, minRevealY);
            guiGraphics.pose().rotate(visualData.rotation);
            guiGraphics.pose().scale(scaleFactor, scaleFactor);

            guiGraphics.blit(
                    STENCIL_TEST_TEXTURED,
                    visualData.icon,
                    1, 1,
                    0, 0,
                    visualData.getWidth(), visualData.getHeight(),
                    visualData.getWidth(), visualData.getHeight(),
                    0x01FFFFFF
            );

            guiGraphics.pose().popMatrix();
        }

        // Render dark fog where stencil is 0 (everywhere else)
        guiGraphics.fill(STENCIL_FOG, contentX, contentY, contentX + contentWidth, contentY + contentHeight, lightFogColor);

        // Draw expanded areas around revealing compartments
        for (CompartmentInstance instance : revealingCompartments) {
            VisualData visualData = instance.getVisualData();
            float scaleFactor = visualData.getScale();
            int minRevealX = contentX + visualData.getX() - Mth.floor(scrollX);
            int minRevealY = contentY + visualData.getY() - Mth.floor(scrollY);
            int revealWidth = (int) (visualData.getWidth() * scaleFactor);
            int revealHeight = (int) (visualData.getHeight() * scaleFactor);

            guiGraphics.fill(
                    STENCIL_TEST,
                    minRevealX,
                    minRevealY,
                    minRevealX + revealWidth,
                    minRevealY + revealHeight,
                    0x01000000);
        }

        // Render light fog where stencil is 2 (expanded area)
        guiGraphics.fill(STENCIL_FOG, contentX, contentY, contentX + contentWidth, contentY + contentHeight, lightFogColor);

        // Alternatively, using previous layer's tiled background for fog
//        ResourceLocation texture = layerIndex > 0 ? layers[layerIndex - 1].backgroundTexture() : layers[0].backgroundTexture();
//        drawTiledBackground(guiGraphics, contentX, contentY, contentWidth, contentHeight, lightFogPipeline, texture, 0x55FFFFFF);
    }

    private boolean isLayerInjured(int layerIndex) {
//        List<UUID> instances = new ArrayList<>(layers.get(layer).getCompartments());

//        if (layers.stream()
//                .anyMatch(uuid -> healthScreen.getMedicalStats().getCompartment(uuid).hasTag(CompartmentTag.CONDITION))) {
//            return true;
//        }

//        if (instances.size() == 1) {
//            CompartmentInstance instance = healthScreen.getMedicalStats().getCompartment(instances.getFirst());
//            return instance.getCompartmentSpace().getCompartments().stream()
//                    .anyMatch(uuid -> healthScreen.getMedicalStats().getCompartment(uuid).hasTag(CompartmentTag.CONDITION));
//        }

        return false;
    }

    private boolean isMouseInContentArea(int mouseX, int mouseY, int contentX, int contentY, int contentWidth, int contentHeight) {
        int contentRight = contentX + contentWidth;
        int contentBottom = contentY + contentHeight;

        int hoverMargin = 5;
        int hoverX = contentX + hoverMargin;
        int hoverY = contentY + hoverMargin;
        int hoverRight = contentRight - hoverMargin;
        int hoverBottom = contentBottom - hoverMargin;

        return mouseX >= hoverX && mouseX <= hoverRight &&
                mouseY >= hoverY && mouseY <= hoverBottom;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isDragging || isResizing) {
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }

        if (isContentDragging && isOpen) {
            scrollX -= dragX;
            scrollY -= dragY;

//            int contentWidth = width - 17;
//            int contentHeight = height - 18;
//
//            int layerWidth = layers[layerIndex].width();
//            int layerHeight = layers[layerIndex].height();
//
//            double maxScrollX = Math.max(0, layerWidth - contentWidth);
//            double maxScrollY = Math.max(0, layerHeight - contentHeight);
//
//            scrollX = Mth.clamp(scrollX, 0, maxScrollX);
//            scrollY = Mth.clamp(scrollY, 0, maxScrollY);

            updateCompartmentWidgetPositions();
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    private void updateCompartmentWidgetPositions() {
        int contentX = x + 8;
        int contentY = y + CONTENT_TOP_PADDING + 2;

        for (CompartmentNodeWidget widget : compartmentWidgets) {
            widget.setX(contentX + widget.getRelativeX() - Mth.floor(scrollX));
            widget.setY(contentY + widget.getRelativeY() - Mth.floor(scrollY));
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean result = isContentDragging;
        if (isContentDragging) {
            isContentDragging = false;
        }

        return result || super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Check buttons first
        for (Button uiButton : buttons) {
            if (uiButton.isMouseOver(mouseX, mouseY)) {
                return uiButton.mouseClicked(mouseX, mouseY, button);
            }
        }

        // Check header for dragging
        if (mouseY >= getY() && mouseY < getY() + HEADER_HEIGHT &&
                mouseX >= getX() && mouseX < getX() + width) {
            isDragging = true;
            dragOffsetX = (int) mouseX - getX();
            dragOffsetY = (int) mouseY - getY();
            return true;
        }

        // Check resize area
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        // Check content area
        if (isOpen &&
                mouseX >= getX() && mouseX < getX() + width &&
                mouseY >= getY() + HEADER_HEIGHT && mouseY < getY() + height) {
            return onContentAreaClicked(mouseX, mouseY, button);
        }

        return false;
    }

    protected boolean onContentAreaClicked(double mouseX, double mouseY, int button) {
        int contentX = x + 8;
        int contentY = y + CONTENT_TOP_PADDING + 2;
        List<CompartmentInstance> revealingCompartments = findRevealingCompartments();

        // Check compartment widgets
        for (CompartmentNodeWidget widget : compartmentWidgets) {
            if (widget.isMouseOver(mouseX, mouseY) && isWithinRevealedArea(revealingCompartments, mouseX, mouseY, contentX, contentY)) {
                return widget.mouseClicked(mouseX, mouseY, button, this);
            }
        }

        // Start dragging content
        if (button == 0) {
            isContentDragging = true;
        }

        return true;
    }

    public void handleCompartmentInteraction(double mouseX, double mouseY, int button) {
        CompartmentInstance heldCompartment = healthScreen.getHeldItemData().heldItem().get(BitterDataComponents.COMPARTMENT).toInstance();

        for (CompartmentNodeWidget widget : compartmentWidgets) {
            if (widget.isMouseOver(mouseX, mouseY)) {
                heldCompartment.getCompartment().performActionOn(this, widget.getCompartment(), heldCompartment, mouseX, mouseY, button);
                return;
            }
        }

        heldCompartment.getCompartment().performAction(this, mouseX, mouseY, button);
    }

    public void handleCompartmentPlacement(double mouseX, double mouseY) {
        // TODO: Prevent from placing compartments outside the layer area

        HeldItemData heldItem = healthScreen.getHeldItemData();
        CompartmentInstance target = heldItem.heldItem().get(BitterDataComponents.COMPARTMENT).toInstance();

        VisualData visualData = target.getVisualData();
        int offsetX = visualData.getWidth() * 2;
        int offsetY = visualData.getHeight() * 2;
        int newX = (int) (mouseX - x + scrollX) - offsetX;
        int newY = (int) (mouseY - y + scrollY) - offsetY;
        visualData.x(newX).y(newY);

        ClientPacketDistributor.sendToServer(new MoveCompartment(
                healthScreen.getMedicalStats().getCharacterID(),
                compartment.getId(),
                target.getId(),
                heldItem.parent().compartment.getId(),
                layerIndex,
                visualData
        ));

        healthScreen.setHeldItemData(null);
    }

    @Override
    protected boolean isInResizeArea(double mouseX, double mouseY) {
        if (!isOpen) return false;
        return super.isInResizeArea(mouseX, mouseY);
    }

    private @Nullable CompartmentNodeWidget getHoveredWidget(List<CompartmentInstance> revealingCompartments, int mouseX, int mouseY, int contentX, int contentY) {
        for (CompartmentNodeWidget widget : compartmentWidgets) {
            if (widget.isMouseOver(mouseX, mouseY) && isWithinRevealedArea(revealingCompartments, mouseX, mouseY, contentX, contentY)) {
                return widget;
            }
        }
        return null;
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        setContentWidth(width);
        updateButtons();
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        setContentHeight(height);
        updateButtons();
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        setContentX(x);

        for (CompartmentNodeWidget widget : compartmentWidgets) {
            widget.setX(x + widget.getRelativeX() - (int) scrollX);
        }

        updateButtons();
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        setContentY(y);

        for (CompartmentNodeWidget widget : compartmentWidgets) {
            widget.setY(y + widget.getRelativeY() - (int) scrollY);
        }

        updateButtons();
    }

    private boolean isWithinRevealedArea(@NotNull List<CompartmentInstance> revealingCompartments, double mouseX,
                                         double mouseY, int contentX, int contentY) {
        if (revealingCompartments.isEmpty()) {
            return true;
        }

        for (CompartmentInstance instance : revealingCompartments) {
            VisualData visualData = instance.getVisualData();
            double revealX = contentX + visualData.getX() - scrollX;
            double revealY = contentY + visualData.getY() - scrollY;
            double revealWidth = visualData.getWidth() * visualData.getScale();
            double revealHeight = visualData.getHeight() * visualData.getScale();

            boolean withinX = mouseX >= revealX && mouseX <= revealX + revealWidth;
            boolean withinY = mouseY >= revealY && mouseY <= revealY + revealHeight;

            if (withinX && withinY) {
                return true;
            }
        }

        return false;
    }

    public void clearCompartmentNodes() {
        for (CompartmentNodeWidget widget : compartmentWidgets) {
            widget.cleanup();
        }
        compartmentWidgets.clear();
    }

    public CompartmentInstance getCompartment() {
        return compartment;
    }

    public void setCompartment(CompartmentInstance compartment) {
        this.compartment = compartment;
    }

    public HealthScreenV2 getHealthScreen() {
        return healthScreen;
    }

    private void setContentX(int x) {
        this.contentX = x + 8;
    }

    private void setContentY(int y) {
        this.contentY = y + CONTENT_TOP_PADDING + 2;
    }

    private void setContentWidth(int width) {
        this.contentWidth = width - 8 * 2 - 2;
    }

    private void setContentHeight(int height) {
        this.contentHeight = height - CONTENT_TOP_PADDING * 2 - 4;
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {

    }
}
