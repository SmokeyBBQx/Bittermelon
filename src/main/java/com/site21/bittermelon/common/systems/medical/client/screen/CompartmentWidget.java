package com.site21.bittermelon.common.systems.medical.client.screen;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.medical.client.screen.widget.MovableWidget;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartment.layer.LayerSlot;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

public class CompartmentWidget extends MovableWidget {
    private static final ResourceLocation WINDOW_TEXTURE = Bittermelon.resource("textures/gui/healthscreen/surgery_window.png");
    private static final ResourceLocation WINDOW_SIDES_TEXTURE = Bittermelon.resource("textures/gui/healthscreen/surgery_window_sides.png");
    private static final int EDGE_MARGIN = 2;
    private static final int BUTTON_SIZE = 10;
    private static final int BUTTON_SPACING = 5;
    private static final int SLOT_SIZE = 16;

    private final CompartmentInstance compartment;
    private final HealthScreenV2 screen;
    private int layerIndex = 0;
    private int contentX;
    private int contentY;

    private Button closeWidgetButton;
    private Button collapseWidgetButton;
    private Button increaseLayerButton;
    private Button decreaseLayerButton;
    private final Button[] buttons;

    private final LayerSlot[][] grid;

    public CompartmentWidget(int x, int y, int width, int height, @NotNull CompartmentInstance compartment, HealthScreenV2 screen) {
        super(x, y, width, height, Component.literal(compartment.getName()));
        this.compartment = compartment;
        this.screen = screen;
        initializeButtons();
        buttons = new Button[]{closeWidgetButton, collapseWidgetButton, increaseLayerButton, decreaseLayerButton};
        grid = compartment.getLayers().getFirst().getGrid();
    }

    private void initializeButtons() {
        closeWidgetButton = Button.builder(
                        Component.literal("X"),
                        (button) -> screen.removeCompartmentSpace(this))
                .pos(getRight() - BUTTON_SIZE, y + BUTTON_SIZE)
                .size(BUTTON_SIZE, BUTTON_SIZE)
                .build();

        collapseWidgetButton = Button.builder(
                        Component.literal(isOpen ? "-" : "+"),
                        (button) -> toggleOpen())
                .pos(getRight() - BUTTON_SIZE * 2 - BUTTON_SPACING, y + BUTTON_SIZE)
                .size(BUTTON_SIZE, BUTTON_SIZE)
                .build();

        increaseLayerButton = Button.builder(
                        Component.literal("↑"),
                        (button) -> increaseLayer())
                .pos(getRight() - BUTTON_SIZE, y + 20)
                .size(BUTTON_SIZE, BUTTON_SIZE)
                .build();

        decreaseLayerButton = Button.builder(
                        Component.literal("↓"),
                        (button) -> decreaseLayer())
                .pos(getRight() - BUTTON_SIZE, y + 32)
                .size(BUTTON_SIZE, BUTTON_SIZE)
                .build();
    }

    private void increaseLayer() {
        if (layerIndex == 0) {
            layerIndex = compartment.getLayers().size() - 1;
            refreshCompartmentNodes();
            return;
        }
        layerIndex--;
        refreshCompartmentNodes();
    }

    private void decreaseLayer() {
        if (layerIndex >= compartment.getLayers().size() - 1) {
            layerIndex = 0;
            refreshCompartmentNodes();
            return;
        }
        layerIndex++;
        refreshCompartmentNodes();
    }

    private void refreshCompartmentNodes() {

    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderSlots(guiGraphics, mouseX, mouseY);
        renderFrame(guiGraphics);
        for (Button button : buttons) {
            button.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    private void renderSlots(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                LayerSlot slot = grid[row][col];
                int slotX = contentX + col * SLOT_SIZE;
                int slotY = contentY + row * SLOT_SIZE;

                if (slot != null) {
                    renderSlot(slotX, slotY, slot, guiGraphics);
                } else {
                    guiGraphics.fill(slotX, slotY, slotX + SLOT_SIZE, slotY + SLOT_SIZE, 0xDD000000);
                }
            }
        }

        renderPlacementIndicator(guiGraphics, mouseX, mouseY);
        renderHoveredSlot(guiGraphics, mouseX, mouseY);
    }

    private void renderSlot(int x, int y, @NotNull LayerSlot slot, @NotNull GuiGraphics guiGraphics) {
        ResourceLocation texture = slot.getType().getTexture();
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 0, 0, SLOT_SIZE, SLOT_SIZE, SLOT_SIZE, SLOT_SIZE, SLOT_SIZE, SLOT_SIZE);

        int bloodColor = ARGB.color(slot.getBloodLevel(), 0x900000);
        guiGraphics.fill(x, y, x + SLOT_SIZE, y + SLOT_SIZE, bloodColor);

        int fogColor = ARGB.color(1 - slot.getVisibility(), 0xDD000000);
        guiGraphics.fill(x, y, x + SLOT_SIZE, y + SLOT_SIZE, fogColor);
    }

    private void renderHoveredSlot(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Point hoveredSlot = getHoveredSlot(mouseX, mouseY);
        if (hoveredSlot != null) {
            int slotX = contentX + hoveredSlot.x * SLOT_SIZE;
            int slotY = contentY + hoveredSlot.y * SLOT_SIZE;
            guiGraphics.fill(slotX, slotY, slotX + SLOT_SIZE, slotY + SLOT_SIZE, 0x80FFFFFF);
        }
    }

    private void renderPlacementIndicator(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        HeldItemData heldItem = screen.getHeldItemData();
        if (heldItem == null) return;

        CompartmentInstance heldCompartment = heldItem.heldItem().get(BitterDataComponents.COMPARTMENT).toInstance();
        List<Point> shape = heldCompartment.getCompartment().getShape();

        Point hoveredSlot = getHoveredSlot(mouseX, mouseY);
        if (hoveredSlot == null) return;

        for (Point p : shape) {
            int targetX = hoveredSlot.x + p.x;
            int targetY = hoveredSlot.y + p.y;
            int slotX = contentX + targetX * SLOT_SIZE;
            int slotY = contentY + targetY * SLOT_SIZE;
            int color = compartment.getLayer(layerIndex).canFit(hoveredSlot.x, hoveredSlot.y, shape) ? 0x800000FF : 0x80FF0000;
            guiGraphics.fill(slotX, slotY, slotX + SLOT_SIZE, slotY + SLOT_SIZE, color);
        }
    }

    private void renderFrame(@NotNull GuiGraphics guiGraphics) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, WINDOW_TEXTURE, x, y, 0, 0, width / 2, 23, 256, 256);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, WINDOW_TEXTURE, x + width / 2, y, 252 - width / 2, 0, width / 2, 23, 256, 256);

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, WINDOW_SIDES_TEXTURE, x, y + 23, 0, 23, width / 2, height - 48, 256, 256);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, WINDOW_SIDES_TEXTURE, x + width / 2, y + 23, 256 - width / 2, 23, width / 2, height - 48, 256, 256);

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, WINDOW_TEXTURE, x, y + height - 25, 0, 130 - 5, width / 2, 15, 256, 256);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, WINDOW_TEXTURE, x + width / 2, y + height - 25, 252 - width / 2, 130 - 5, width / 2, 15, 256, 256);
    }

    private @Nullable Point getHoveredSlot(int mouseX, int mouseY) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                LayerSlot slot = grid[row][col];
                if (slot == null) continue;
                if (slot.getVisibility() < 0.5f) continue;

                int slotX = contentX + col * SLOT_SIZE;
                int slotY = contentY + row * SLOT_SIZE;

                if (mouseX >= slotX && mouseX < slotX + SLOT_SIZE &&
                        mouseY >= slotY && mouseY < slotY + SLOT_SIZE) {
                    return new Point(col, row);
                }
            }
        }

        return null;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Button btn : buttons) {
            if (btn.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        contentX = x + EDGE_MARGIN;
    }

    public void setY(int y) {
        super.setY(y);
        contentY = y + getHeaderHeight() + EDGE_MARGIN;
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {

    }
}
