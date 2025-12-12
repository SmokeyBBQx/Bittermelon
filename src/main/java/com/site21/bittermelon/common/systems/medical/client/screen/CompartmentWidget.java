package com.site21.bittermelon.common.systems.medical.client.screen;

import com.site21.bittermelon.common.systems.medical.client.screen.widget.MovableWidget;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartment.layer.LayerSlot;
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

public class CompartmentWidget extends MovableWidget {
    private static final int EDGE_MARGIN = 2;
    private static final int BUTTON_SIZE = 10;
    private static final int BUTTON_SPACING = 5;
    private static final int SLOT_SIZE = 16;

    private final CompartmentInstance compartment;
    private final HealthScreenV2 screen;
    private int layerIndex = 0;

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
        renderSlots(guiGraphics, mouseX, mouseY, partialTick);
        for (Button button : buttons) {
            button.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    private void renderSlots(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        LayerSlot[][] layerSlots = grid;
        if (layerSlots == null) {
            return;
        }

        int startX = x + EDGE_MARGIN;
        int startY = y + getHeaderHeight() + EDGE_MARGIN;

        for (int row = 0; row < layerSlots.length; row++) {
            for (int col = 0; col < layerSlots[row].length; col++) {
                LayerSlot slot = layerSlots[row][col];
                int slotX = startX + col * (SLOT_SIZE);
                int slotY = startY + row * (SLOT_SIZE);

                if (slot != null) {
                    renderSlot(slotX, slotY, slot, guiGraphics);
                } else {
                    guiGraphics.fill(slotX, slotY, slotX + SLOT_SIZE, slotY + SLOT_SIZE, 0xDD000000);
                }
            }
        }

        Point hoveredSlot = getHoveredSlot(mouseX, mouseY);
        if (hoveredSlot != null) {
            int slotX = startX + hoveredSlot.x * (SLOT_SIZE);
            int slotY = startY + hoveredSlot.y * (SLOT_SIZE);
            guiGraphics.fill(slotX, slotY, slotX + SLOT_SIZE, slotY + SLOT_SIZE, 0x80FFFFFF);
        }
    }

    private void renderSlot(int x, int y, @NotNull LayerSlot slot, @NotNull GuiGraphics guiGraphics) {
        ResourceLocation texture = slot.getType().getTexture();
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 0, 0, SLOT_SIZE, SLOT_SIZE, SLOT_SIZE, SLOT_SIZE);

        int bloodColor = ARGB.color(slot.getBloodLevel(), 0x900000);
        guiGraphics.fill(x, y, x + SLOT_SIZE, y + SLOT_SIZE, bloodColor);

        int fogColor = ARGB.color(1 - slot.getVisibility(), 0xDD000000);
        guiGraphics.fill(x, y, x + SLOT_SIZE, y + SLOT_SIZE, fogColor);
    }


    private void drawWindowFrame(@NotNull GuiGraphics guiGraphics) {

    }

    private @Nullable Point getHoveredSlot(int mouseX, int mouseY) {
        int startX = x + EDGE_MARGIN;
        int startY = y + getHeaderHeight() + EDGE_MARGIN;

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                LayerSlot slot = grid[row][col];
                if (slot == null) continue;

                if (slot.getVisibility() < 0.5f) continue;

                int slotX = startX + col * (SLOT_SIZE);
                int slotY = startY + row * (SLOT_SIZE);

                if (mouseX >= slotX && mouseX < slotX + SLOT_SIZE &&
                        mouseY >= slotY && mouseY < slotY + SLOT_SIZE) {
                    return new Point(col, row);
                }
            }
        }

        return null;
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {

    }
}
