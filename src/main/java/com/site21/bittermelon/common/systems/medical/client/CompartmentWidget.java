package com.site21.bittermelon.common.systems.medical.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartment.VisualData;
import com.site21.bittermelon.common.systems.medical.compartment.layer.LayerData;
import com.site21.bittermelon.common.systems.medical.compartment.layer.LayerSlot;
import com.site21.bittermelon.common.systems.medical.compartment.layer.Point;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CompartmentWidget extends MovableResizableWidget {
    private static final ResourceLocation WINDOW_TEXTURE = Bittermelon.resource("textures/gui/healthscreen/surgery_window.png");
    private static final ResourceLocation WINDOW_SIDES_TEXTURE = Bittermelon.resource("textures/gui/healthscreen/surgery_window_sides.png");
    private static final int EDGE_MARGIN = 2;
    private static final int BUTTON_SIZE = 10;
    private static final int BUTTON_SPACING = 5;

    private final CompartmentInstance compartment;
    private final HealthScreen screen;
    private int layerIndex = 0;
    private int contentX;
    private int contentY;
    private int slotSize = 16;

    private Button closeWidgetButton;
    private Button collapseWidgetButton;
    private Button increaseLayerButton;
    private Button decreaseLayerButton;
    private final Button[] buttons;

    private LayerSlot[][] grid;

    public CompartmentWidget(int x, int y, int width, int height, @NotNull CompartmentInstance compartment, HealthScreen screen) {
        super(x, y, width, height, Component.literal(compartment.getName()));
        this.compartment = compartment;
        this.screen = screen;
        initializeButtons();
        buttons = new Button[]{closeWidgetButton, collapseWidgetButton, increaseLayerButton, decreaseLayerButton};
        grid = compartment.getLayers().getFirst().getGrid();
        updatePositions();
    }

    private void initializeButtons() {
        closeWidgetButton = Button.builder(
                        Component.literal("X"),
                        (button) -> screen.removeCompartmentSpace(this))
                .size(BUTTON_SIZE, BUTTON_SIZE)
                .build();

        collapseWidgetButton = Button.builder(
                        Component.literal(isOpen ? "-" : "+"),
                        (button) -> toggleOpen())
                .size(BUTTON_SIZE, BUTTON_SIZE)
                .build();

        increaseLayerButton = Button.builder(
                        Component.literal("↑"),
                        (button) -> increaseLayer())
                .size(BUTTON_SIZE, BUTTON_SIZE)
                .build();

        decreaseLayerButton = Button.builder(
                        Component.literal("↓"),
                        (button) -> decreaseLayer())
                .size(BUTTON_SIZE, BUTTON_SIZE)
                .build();
    }

    private void increaseLayer() {
        if (layerIndex == 0) {
            layerIndex = compartment.getLayers().size() - 1;
        } else {
            --layerIndex;
        }

        grid = getLayer().getGrid();
    }

    private void decreaseLayer() {
        if (layerIndex >= compartment.getLayers().size() - 1) {
            layerIndex = 0;
        } else {
            ++layerIndex;
        }

        grid = getLayer().getGrid();
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (isOpen) {
            guiGraphics.fill(x, y + getHeaderHeight(), getRight(), getBottom(), 0xDD000000);
            renderSlots(guiGraphics, mouseX, mouseY);
            renderCompartments(guiGraphics, mouseX, mouseY);
            renderHoveredSlot(guiGraphics, mouseX, mouseY);
            renderResizeHandle(guiGraphics, mouseX, mouseY, partialTick);
            increaseLayerButton.render(guiGraphics, mouseX, mouseY, partialTick);
            decreaseLayerButton.render(guiGraphics, mouseX, mouseY, partialTick);
        }
        renderDragHandle(guiGraphics, mouseX, mouseY, partialTick);
        collapseWidgetButton.render(guiGraphics, mouseX, mouseY, partialTick);
        closeWidgetButton.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void renderSlots(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                LayerSlot slot = grid[row][col];
                if (slot == null) continue;
                int slotX = contentX + col * slotSize;
                int slotY = contentY + row * slotSize;

                renderSlot(slotX, slotY, col, row, slot, guiGraphics);
            }
        }

        renderPlacementIndicator(guiGraphics, mouseX, mouseY);
    }

    private void renderSlot(int x, int y, int u, int v, @NotNull LayerSlot slot, @NotNull GuiGraphics guiGraphics) {
        // Slot texture
        ResourceLocation texture = slot.getType().getTexture();
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, slotSize, slotSize, 1, 1, 16, 16);

        // Blood level overlay
        int bloodColor = ARGB.color(slot.getBloodLevel(), 0x900000);
        guiGraphics.fill(x, y, x + slotSize, y + slotSize, bloodColor);

        // Fog of war overlay
        int fogColor = ARGB.color(1 - slot.getVisibility(), 0xDD000000);
        guiGraphics.fill(x, y, x + slotSize, y + slotSize, fogColor);
    }

    private void renderHoveredSlot(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Point hoveredSlot = getHoveredSlot(mouseX, mouseY);
        if (hoveredSlot != null) {
            int slotX = contentX + hoveredSlot.x() * slotSize;
            int slotY = contentY + hoveredSlot.y() * slotSize;
            guiGraphics.fill(slotX, slotY, slotX + slotSize, slotY + slotSize, 0x80FFFFFF);
        }
    }

    private void renderPlacementIndicator(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        CompartmentInstance heldCompartment = screen.getHeldCompartment();
        if (heldCompartment == null) return;

        List<Point> shape = heldCompartment.getCompartment().getShape();
        Point hoveredSlot = getHoveredSlot(mouseX, mouseY);
        if (hoveredSlot == null) return;

        for (Point p : shape) {
            int targetX = hoveredSlot.x() + p.x();
            int targetY = hoveredSlot.y() + p.y();
            int slotX = contentX + targetX * slotSize;
            int slotY = contentY + targetY * slotSize;
            int color = getLayer().canFit(hoveredSlot.x(), hoveredSlot.y(), shape) ? 0x80008000 : 0x80FF0000;
            guiGraphics.fill(slotX, slotY, slotX + slotSize, slotY + slotSize, color);
        }
    }

    private void renderCompartments(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        MedicalStats medicalStats = screen.getMedicalStats();
        UUID hoveredCompartmentId = getHoveredCompartment(mouseX, mouseY);

        for (Map.Entry<Point, UUID> entry : getLayer().getCompartments().entrySet()) {
            Point slotPos = entry.getKey();
            UUID compartmentId = entry.getValue();
            CompartmentInstance instance = medicalStats.getCompartment(compartmentId);

            VisualData visualData = instance.getVisualData();

            int slotX = contentX + slotPos.x() * slotSize;
            int slotY = contentY + slotPos.y() * slotSize;
            int compartmentWidth = slotSize * visualData.getWidth();
            int compartmentHeight = slotSize * visualData.getHeight();
            int color = visualData.color;

            // TODO: Buggy
            float pulse = (float) (Math.sin(System.currentTimeMillis() / 500.0) * 0.4f + 0.95f);
            color = compartmentId.equals(hoveredCompartmentId) ? ARGB.color(pulse, color) : color;

            ResourceLocation icon = visualData.getIcon();
            if (icon != null) {
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, icon, slotX, slotY, 0, 0, compartmentWidth, compartmentHeight, compartmentWidth, compartmentHeight, color);
            }
        }
    }

    private void renderCompartmentTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }

    private @Nullable UUID getHoveredCompartment(int mouseX, int mouseY) {
        Point hoveredSlot = getHoveredSlot(mouseX, mouseY);
        if (hoveredSlot == null) return null;

        return compartment.getLayer(layerIndex).getCompartmentAt(hoveredSlot.x(), hoveredSlot.y());
    }

    public @Nullable Point getHoveredSlot(int mouseX, int mouseY) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                LayerSlot slot = grid[row][col];
                if (slot == null) continue;
                if (slot.getVisibility() < 0.5f) continue;

                int slotX = contentX + col * slotSize;
                int slotY = contentY + row * slotSize;

                if (mouseX >= slotX && mouseX < slotX + slotSize &&
                        mouseY >= slotY && mouseY < slotY + slotSize) {
                    return new Point(col, row);
                }
            }
        }

        return null;
    }

    public LayerData getLayer() {
        return compartment.getLayer(layerIndex);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Button btn : buttons) {
            if (btn.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }

        CompartmentInstance hoveredCompartment = screen.getMedicalStats().getCompartment(getHoveredCompartment((int) mouseX, (int) mouseY));
        if (hoveredCompartment != null) {
            if (button == 0) {
                getLayer().removeInstance(hoveredCompartment.getId());
                screen.setHeldCompartment(hoveredCompartment);
                return true;
            } else {
                return screen.addCompartmentSpace(hoveredCompartment);
            }
        }

        Point hoveredSlot = getHoveredSlot((int) mouseX, (int) mouseY);
        if (hoveredSlot != null) {
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean tryToPlace(int x, int y, @NotNull CompartmentInstance compartment) {
        Point hoveredSlot = getHoveredSlot(x, y);
        if (hoveredSlot == null) return false;

        return getLayer().tryToPlace(hoveredSlot.x(), hoveredSlot.y(), compartment);
    }

    public boolean isWithinContentArea(int mouseX, int mouseY) {
        return mouseX >= contentX && mouseX < contentX + getLayer().getWidth() * slotSize &&
                mouseY >= contentY && mouseY < contentY + getLayer().getHeight() * slotSize;
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        updatePositions();
    }

    public void setY(int y) {
        super.setY(y);
        updatePositions();
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        updatePositions();
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        updatePositions();
    }

    private void updatePositions() {
        updateSlotSize();
        updateContentPosition();
        updateButtonPositions();
    }

    private void updateContentPosition() {
        contentX = x + (width - getLayer().getWidth() * slotSize) / 2;
        contentY = y + getHeaderHeight() + (height - getHeaderHeight() - getLayer().getHeight() * slotSize) / 2;
    }

    private void updateSlotSize() {
        int availableWidth = width - 10;
        int availableHeight = height - getHeaderHeight() - 5;
        LayerData layer = getLayer();
        slotSize = Math.min(availableWidth / layer.getWidth(), availableHeight / layer.getHeight());
    }

    private void updateButtonPositions() {
        int x = getRight() - BUTTON_SIZE - EDGE_MARGIN;

        closeWidgetButton.setX(x);
        closeWidgetButton.setY(y + EDGE_MARGIN);

        collapseWidgetButton.setX(getRight() - BUTTON_SIZE * 2 - 4);
        collapseWidgetButton.setY(y + EDGE_MARGIN);

        increaseLayerButton.setX(x);
        increaseLayerButton.setY(y + 18);

        decreaseLayerButton.setX(x);
        decreaseLayerButton.setY(y + 30);
    }

    public CompartmentInstance getCompartment() {
        return compartment;
    }

    public int getSlotSize() {
        return slotSize;
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {

    }
}
