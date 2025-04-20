package com.site21.bittermelon.content.medical.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.medical.client.screen.deprecated.HealthScreen;
import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.compartments.CompartmentSpace;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.VisualData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CompartmentSpaceWidget extends MovableResizableWidget {
    public static final ResourceLocation WINDOW_TEXTURE = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/healthscreen/surgery_window.png");
    public static final ResourceLocation WINDOW_SIDES_TEXTURE = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/healthscreen/surgery_window_sides.png");

    public static final ResourceLocation LAYER_TEXTURE = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/healthscreen/layer.png");
    public static final ResourceLocation SELECTED_LAYER_TEXTURE = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/healthscreen/selected_layer.png");
    public static final ResourceLocation INJURED_LAYER_TEXTURE = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/healthscreen/injured_layer.png");
    public static final ResourceLocation INJURED_SELECTED_LAYER_TEXTURE = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/healthscreen/injured_selected_layer.png");

    private static final int HEADER_HEIGHT = 15;

    private double scrollX = 0;
    private double scrollY = 0;
    private boolean isContentDragging = false;

    private float zoomLevel = 1.0f;
    private static final float MIN_ZOOM = 0.5f;
    private static final float MAX_ZOOM = 2.0f;
    private static final float ZOOM_STEP = 0.25f;

    private final CompartmentSpace compartmentSpace;
    private final HealthScreenV2 healthScreen;
    private final List<CompartmentNodeWidget> compartmentWidgets;
    private int layer = 0;

    private Button closeWidgetButton;
    private Button collapseWidgetButton;
    private Button increaseLayerButton;
    private Button decreaseLayerButton;
    private Button recenterButton;
    private Button zoomInButton;
    private Button zoomOutButton;

    private final int CLOSE_RIGHT_MARGIN = 15;
    private final int COLLAPSE_RIGHT_MARGIN = 28;
    private final int LAYER_RIGHT_MARGIN = 23;
    private final int TOP_MARGIN = 5;
    private final int INCREASE_TOP_MARGIN = 20;
    private final int DECREASE_TOP_MARGIN = 32;

    private int getCloseButtonRelX() { return width - CLOSE_RIGHT_MARGIN; }
    private int getCollapseButtonRelX() { return width - COLLAPSE_RIGHT_MARGIN; }
    private int getIncreaseLayerButtonRelX() { return width - LAYER_RIGHT_MARGIN; }
    private int getDecreaseLayerButtonRelX() { return width - LAYER_RIGHT_MARGIN; }

    private int getCloseButtonRelY() { return TOP_MARGIN; }
    private int getCollapseButtonRelY() { return TOP_MARGIN; }
    private int getIncreaseLayerButtonRelY() { return INCREASE_TOP_MARGIN; }
    private int getDecreaseLayerButtonRelY() { return DECREASE_TOP_MARGIN; }

    private final Button[] buttons;
    private ResourceLocation backgroundTexture;
    private final Component name;

    public CompartmentSpaceWidget(int x, int y, int width, int height, Component title, @NotNull CompartmentSpace compartmentSpace, HealthScreenV2 healthScreen) {
        super(x, y, width, height, title);
        this.compartmentSpace = compartmentSpace;
        this.healthScreen = healthScreen;
        this.compartmentWidgets = new ArrayList<>();
        this.name = title;
        updateButtons();
        buttons = new Button[]{closeWidgetButton, collapseWidgetButton, increaseLayerButton, decreaseLayerButton,
                recenterButton, zoomInButton, zoomOutButton};
        refreshCompartmentNodes();
        backgroundTexture = compartmentSpace.getBackgroundTexture();
    }

    private void updateButtons() {
        if (closeWidgetButton == null) {
            closeWidgetButton = Button.builder(
                            Component.literal("X"),
                            (button) -> healthScreen.removeCompartmentSpace(this))
                    .pos(x + getCloseButtonRelX(), y + getCloseButtonRelY())
                    .size(10, 10)
                    .build();
        } else {
            closeWidgetButton.setX(x + getCloseButtonRelX());
            closeWidgetButton.setY(y + getCloseButtonRelY());
        }

        if (collapseWidgetButton == null) {
            collapseWidgetButton = Button.builder(
                            Component.literal(isOpen ? "-" : "+"),
                            (button) -> toggleOpen())
                    .pos(x + getCollapseButtonRelX(), y + getCollapseButtonRelY())
                    .size(10, 10)
                    .build();
        } else {
            collapseWidgetButton.setX(x + getCollapseButtonRelX());
            collapseWidgetButton.setY(y + getCollapseButtonRelY());
            collapseWidgetButton.setMessage(Component.literal(isOpen ? "-" : "+"));
        }

        if (increaseLayerButton == null) {
            increaseLayerButton = Button.builder(
                            Component.literal("↑"),
                            (button) -> increaseLayer())
                    .pos(x + getIncreaseLayerButtonRelX(), y + getIncreaseLayerButtonRelY())
                    .size(10, 10)
                    .build();
        } else {
            increaseLayerButton.setX(x + getIncreaseLayerButtonRelX());
            increaseLayerButton.setY(y + getIncreaseLayerButtonRelY());
        }

        if (decreaseLayerButton == null) {
            decreaseLayerButton = Button.builder(
                            Component.literal("↓"),
                            (button) -> decreaseLayer())
                    .pos(x + getDecreaseLayerButtonRelX(), y + getDecreaseLayerButtonRelY())
                    .size(10, 10)
                    .build();
        } else {
            decreaseLayerButton.setX(x + getDecreaseLayerButtonRelX());
            decreaseLayerButton.setY(y + getDecreaseLayerButtonRelY());
        }
        if (recenterButton == null) {
            recenterButton = Button.builder(
                            Component.literal("⊕"),
                            (button) -> recenterView())
                    .pos(x + 12, y + getIncreaseLayerButtonRelY())
                    .size(10, 10)
                    .build();
        } else {
            recenterButton.setX(x + 12);
            recenterButton.setY(y + getIncreaseLayerButtonRelY());
        }
        if (zoomInButton == null) {
            zoomInButton = Button.builder(
                            Component.literal("+"),
                            (button) -> zoomIn())
                    .pos(x + getIncreaseLayerButtonRelX() - 12, y + height - 30)
                    .size(10, 10)
                    .build();
        } else {
            zoomInButton.setX(x + getIncreaseLayerButtonRelX() - 12);
            zoomInButton.setY(y + height - 30);
        }

        if (zoomOutButton == null) {
            zoomOutButton = Button.builder(
                            Component.literal("-"),
                            (button) -> zoomOut())
                    .pos(x + getDecreaseLayerButtonRelX(), y + height - 30)
                    .size(10, 10)
                    .build();
        } else {
            zoomOutButton.setX(x + getDecreaseLayerButtonRelX());
            zoomOutButton.setY(y + height - 30);
        }
    }

    private void increaseLayer() {
        if (layer == 0) {
            layer = compartmentSpace.getLayers().size() - 1;
            refreshCompartmentNodes();
            return;
        }
        layer--;
        refreshCompartmentNodes();
    }

    private void decreaseLayer() {
        if (layer >= compartmentSpace.getLayers().size() - 1) {
            layer = 0;
            refreshCompartmentNodes();
            return;
        }
        layer++;
        refreshCompartmentNodes();
    }

    private void recenterView() {
        scrollX = 0;
        scrollY = 0;
        updateCompartmentWidgetPositions();
    }

    private void zoomIn() {
        if (zoomLevel < MAX_ZOOM) {
            zoomLevel += ZOOM_STEP;
            refreshCompartmentNodes();
        }
    }

    private void zoomOut() {
        if (zoomLevel > MIN_ZOOM) {
            zoomLevel -= ZOOM_STEP;
            refreshCompartmentNodes();
        }
    }

    protected void refreshCompartmentNodes() {
        compartmentWidgets.clear();
        List<UUID> list = new ArrayList<>(compartmentSpace.getLayers().get(layer));

        if (isSingleBodyPart(list)) {
            CompartmentInstance firstCompartment = healthScreen.getMedicalStats().getCompartment(list.getFirst());
            backgroundTexture = firstCompartment.getCompartmentSpace().getBackgroundTexture();
            setMessage(Component.literal(name.getString() + " (" + firstCompartment.getName() + ")"));
            list = new ArrayList<>(firstCompartment.getCompartmentSpace().getCompartments());
        } else {
            backgroundTexture = compartmentSpace.getBackgroundTexture();
            setMessage(name);
        }

        createCompartmentWidgets(list);
    }

    private boolean isSingleBodyPart(@NotNull List<UUID> list) {
        if (list.size() != 1) {
            return false;
        }
        CompartmentInstance compartment = healthScreen.getMedicalStats().getCompartment(list.getFirst());
        return compartment.hasTag(CompartmentTag.BODY_PART) ||
                compartment.hasTag(CompartmentTag.MAJOR_BODY_PART);
    }

    private void createCompartmentWidgets(@NotNull List<UUID> compartmentList) {
        for (UUID uuid : compartmentList) {
            CompartmentInstance compartment = healthScreen.getMedicalStats().getCompartment(uuid);
            if (compartment == null) continue;

            CompartmentNodeWidget widget = getCompartmentNodeWidget(compartment);
            compartmentWidgets.add(widget);
        }
    }

    private @NotNull CompartmentNodeWidget getCompartmentNodeWidget(@NotNull CompartmentInstance compartment) {
        VisualData visualData = compartment.getVisualData();

        CompartmentNodeWidget widget = new CompartmentNodeWidget(
                x + visualData.x,
                y + visualData.y,
                26, 26,
                Component.literal(compartment.getName()),
                healthScreen,
                compartment
        );

        widget.setRelativeX(visualData.x);
        widget.setRelativeY(visualData.y);
        return widget;
    }

    @Override
    protected int getHeaderHeight() {
        return HEADER_HEIGHT;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int contentX = x + 8;
        int contentY = y + 26 - 11;
        int contentWidth = width - 17;
        int contentHeight = height - 26 - 8;

        if (isOpen) {
            guiGraphics.enableScissor(contentX, contentY, contentX + contentWidth, contentY + contentHeight);

            drawTiledBackground(guiGraphics, contentX, contentY, contentWidth, contentHeight);

            for (CompartmentNodeWidget widget : compartmentWidgets) {
                widget.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
            }


            renderLayerIndicators(guiGraphics);

            increaseLayerButton.render(guiGraphics, mouseX, mouseY, partialTick);
            decreaseLayerButton.render(guiGraphics, mouseX, mouseY, partialTick);
            recenterButton.render(guiGraphics, mouseX, mouseY, partialTick);
            zoomInButton.render(guiGraphics, mouseX, mouseY, partialTick);
            zoomOutButton.render(guiGraphics, mouseX, mouseY, partialTick);

            String zoomPercentage = String.format("%.0f%%", zoomLevel * 100);
            int zoomTextLength = Minecraft.getInstance().font.width(zoomPercentage);

            guiGraphics.drawString(
                    Minecraft.getInstance().font,
                    Component.literal(zoomPercentage),
                    x + getIncreaseLayerButtonRelX() - zoomTextLength - 15,
                    y + height - 29,
                    0xFFFFFF
            );

            guiGraphics.disableScissor();
        }

        drawWindowFrame(guiGraphics);
        closeWidgetButton.render(guiGraphics, mouseX, mouseY, partialTick);
        collapseWidgetButton.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.drawString(
                Minecraft.getInstance().font,
                getMessage(),
                x + 8,
                y + 6,
                0xFFFFFF
        );

        if (isOpen) {
            if (isMouseInContentArea(mouseX, mouseY, contentX, contentY, contentWidth, contentHeight)) {
                boolean isMouseOverButton = false;
                for (Button button : buttons) {
                    if (button.isMouseOver(mouseX, mouseY)) {
                        isMouseOverButton = true;
                    }
                }
                CompartmentNodeWidget hoveredWidget = getHoveredWidget(mouseX, mouseY);
                if (hoveredWidget != null && !isMouseOverButton) {
                    hoveredWidget.drawHover(guiGraphics, mouseX, mouseY, partialTick, width, height);
                }
            }
            guiGraphics.fill(x + width - 3, y + height - 1, x + width - 3, y + height, 0xFFAAAAAA);
        }
    }

    private void renderLayerIndicators(GuiGraphics guiGraphics) {
        for (int i = 0; i < compartmentSpace.getLayers().size(); i++) {
            int indicatorX = x + width - 24;
            int indicatorY = y + 50 + i * 6;

            if (layer == i) {
                if (isLayerInjured(i)) {
                    guiGraphics.blit(
                            INJURED_SELECTED_LAYER_TEXTURE, indicatorX, indicatorY, 0, 0, 11, 5, 11, 5);
                } else {
                    guiGraphics.blit(SELECTED_LAYER_TEXTURE, indicatorX, indicatorY, 0, 0, 11, 5, 11, 5);
                }
            } else if (isLayerInjured(i)) {
                guiGraphics.blit(INJURED_LAYER_TEXTURE, indicatorX, indicatorY, 0, 0, 11, 5, 11, 5);
            } else {
                guiGraphics.blit(LAYER_TEXTURE, indicatorX, indicatorY, 0, 0, 11, 5, 11, 5);
            }
        }
    }

    private boolean isLayerInjured(int layerIndex) {
        List<UUID> instances = new ArrayList<>(compartmentSpace.getLayers().get(layerIndex));

        if (compartmentSpace.getLayers().get(layerIndex).stream()
                .anyMatch(uuid -> healthScreen.getMedicalStats().getCompartment(uuid).hasTag(CompartmentTag.CONDITION))) {
            return true;
        }

        if (instances.size() == 1) {
            CompartmentInstance instance = healthScreen.getMedicalStats().getCompartment(instances.getFirst());
            return instance.getCompartmentSpace().getCompartments().stream()
                    .anyMatch(uuid -> healthScreen.getMedicalStats().getCompartment(uuid).hasTag(CompartmentTag.CONDITION));
        }

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

    private void drawTiledBackground(@NotNull GuiGraphics guiGraphics, int contentX, int contentY, int contentWidth, int contentHeight) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(contentX, contentY, 0.0F);

        int tileOffsetX = -(int)scrollX;
        int tileOffsetY = -(int)scrollY;

        int tilesX = (contentWidth / 16) + 2;
        int tilesY = (contentHeight / 16) + 2;

        int startX = (tileOffsetX % 16);
        int startY = (tileOffsetY % 16);

        for (int i = -1; i <= tilesX; i++) {
            for (int j = -1; j <= tilesY; j++) {
                guiGraphics.blit(
                        backgroundTexture,
                        startX + (16 * i),
                        startY + (16 * j),
                        0, 0, 16, 16, 16, 16
                );
            }
        }

        guiGraphics.pose().popPose();
    }

    private void drawWindowFrame(@NotNull GuiGraphics guiGraphics) {
        RenderSystem.enableBlend();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        guiGraphics.blit(WINDOW_TEXTURE, x, y, 0, 0, width / 2, 23);
        guiGraphics.blit(WINDOW_TEXTURE, x + width / 2, y, 252 - width / 2, 0, width / 2, 23);

        guiGraphics.blit(WINDOW_SIDES_TEXTURE, x, y + 23, 0, 23, width / 2, height - 48);
        guiGraphics.blit(WINDOW_SIDES_TEXTURE, x + width / 2, y + 23, 252 - width / 2, 23, width / 2, height - 48);

        guiGraphics.blit(WINDOW_TEXTURE, x, y + height - 25, 0, 130 - 5, width / 2, 15);
        guiGraphics.blit(WINDOW_TEXTURE, x + width / 2, y + height - 25, 252 - width / 2, 130 - 5, width / 2, 15);
        RenderSystem.disableBlend();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isDragging || isResizing) {
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }

        if (isContentDragging && isOpen) {
            scrollX -= dragX;
            scrollY -= dragY;

            updateCompartmentWidgetPositions();
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    private void updateCompartmentWidgetPositions() {
        for (CompartmentNodeWidget widget : compartmentWidgets) {
            widget.setX(x + widget.getRelativeX() - Mth.floor(scrollX));
            widget.setY(y + widget.getRelativeY() - Mth.floor(scrollY));
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
        for (Button uiButton : buttons) {
            if (uiButton.isMouseOver(mouseX, mouseY)) {
                return uiButton.mouseClicked(mouseX, mouseY, button);
            }
        }

        if (mouseY >= getY() && mouseY < getY() + HEADER_HEIGHT &&
                mouseX >= getX() && mouseX < getX() + width) {
            isDragging = true;
            dragOffsetX = (int) mouseX - getX();
            dragOffsetY = (int) mouseY - getY();
            return true;
        }

        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        if (isOpen &&
                mouseX >= getX() && mouseX < getX() + width &&
                mouseY >= getY() + HEADER_HEIGHT && mouseY < getY() + height) {
            return onContentAreaClicked(mouseX, mouseY, button);
        }

        return false;
    }

    protected boolean onContentAreaClicked(double mouseX, double mouseY, int button) {
        for (CompartmentNodeWidget widget : compartmentWidgets) {
            if (widget.isMouseOver(mouseX, mouseY)) {
                return widget.mouseClicked(mouseX, mouseY, button);
            }
        }

        if (button == 0) {
            isContentDragging = true;
            return true;
        }

        return false;
    }


    @Override
    protected boolean isInResizeArea(double mouseX, double mouseY) {
        if (!isOpen) return false;
        return super.isInResizeArea(mouseX, mouseY);
    }

    private @Nullable CompartmentNodeWidget getHoveredWidget(int mouseX, int mouseY) {
        for (CompartmentNodeWidget widget : compartmentWidgets) {
            if (widget.isMouseOver(mouseX, mouseY)) {
                return widget;
            }
        }
        return null;
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        updateButtons();
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        updateButtons();
    }

    @Override
    public void setX(int x) {
        super.setX(x);

        for (CompartmentNodeWidget widget : compartmentWidgets) {
            widget.setX(x + widget.getRelativeX() - (int)scrollX);
        }

        updateButtons();
    }

    @Override
    public void setY(int y) {
        super.setY(y);

        for (CompartmentNodeWidget widget : compartmentWidgets) {
            widget.setY(y + widget.getRelativeY() - (int)scrollY);
        }

        updateButtons();
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {

    }
}
