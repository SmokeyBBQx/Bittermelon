package com.site21.bittermelon.common.systems.medical.client;

import com.site21.bittermelon.common.systems.character.networking.SetCharactersChanged;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentUtil;
import com.site21.bittermelon.common.systems.medical.compartment.VisualData;
import com.site21.bittermelon.common.systems.medical.compartment.layer.LayerData;
import com.site21.bittermelon.common.systems.medical.compartment.layer.LayerSlot;
import com.site21.bittermelon.common.systems.medical.compartment.layer.Point;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.common.systems.medical.networking.AddAndInsertCompartment;
import com.site21.bittermelon.common.systems.medical.networking.ExtractCompartment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.*;

public class CompartmentWidget extends MovableResizableWidget {
    private static final int EDGE_MARGIN = 2;
    private static final int BUTTON_SIZE = 10;

    private final CompartmentInstance compartment;
    private final HealthScreen screen;
    private int layerIndex = 0;
    private int depth = 0;
    private int contentX;
    private int contentY;
    private int slotSize = 16;

    private Button closeWidgetButton;
    private Button collapseWidgetButton;
    private Button increaseLayerButton;
    private Button decreaseLayerButton;
    private final Button[] buttons;

    private LayerSlot[][] grid;
    private float[][] light;

    public CompartmentWidget(int x, int y, int width, int height, @NotNull CompartmentInstance compartment, HealthScreen screen) {
        super(x, y, width, height, Component.literal(compartment.getName()));
        this.compartment = compartment;
        this.screen = screen;
        initializeButtons();
        buttons = new Button[]{closeWidgetButton, collapseWidgetButton, increaseLayerButton, decreaseLayerButton};
        grid = CompartmentUtil.getLayerGrid(compartment, 0);
        updatePositions();
        updateLight();
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

        boolean hasLayers = CompartmentUtil.getLayers(compartment).size() > 1;
        increaseLayerButton.visible = hasLayers;
        decreaseLayerButton.visible = hasLayers;
    }

    private void increaseLayer() {
        if (layerIndex == 0) {
            layerIndex = CompartmentUtil.getLayers(compartment).size() - 1;
        } else {
            layerIndex--;
        }

        grid = getLayer().getGrid();
        updateLight();
        screen.onLayerChanged(this);
    }

    private void decreaseLayer() {
        if (layerIndex >= CompartmentUtil.getLayers(compartment).size() - 1) {
            layerIndex = 0;
        } else {
            layerIndex++;
        }

        grid = getLayer().getGrid();
        updateLight();
        screen.onLayerChanged(this);
    }

    private void updateLight() {
        light = new float[grid.length][grid[0].length];
        for (float[] floats : light) {
            Arrays.fill(floats, layerIndex == 0 ? 1.0f : 0.0f);
        }

        LayerData previousLayer = layerIndex > 0 ? CompartmentUtil.getLayer(getCompartment(), layerIndex - 1) : null;
        if (previousLayer == null) return;

        for (Map.Entry<Point, UUID> entry : previousLayer.getCompartmentsAt(0).entrySet()) {
            CompartmentInstance instance = screen.getMedicalStats().getCompartment(entry.getValue());
            int revealDistance = instance.getOrDefault(REVEAL_DISTANCE, 0);
            if (revealDistance <= 0) continue;

            Point slotPos = entry.getKey();
            lightSlots(slotPos.x(), slotPos.y(), compartment.getOrDefault(SHAPE, List.of(new Point(0, 0))), revealDistance);
        }
    }

    private void lightSlots(int x, int y, @NotNull List<Point> shape, int visibility) {
        for (Point p : shape) {
            int targetX = x + p.x();
            int targetY = y + p.y();
            if (targetX < 0 || targetX >= grid[0].length || targetY < 0 || targetY >= grid.length) continue;

            if (grid[targetY][targetX] != null) {
                light[targetY][targetX] = 1;

                for (Direction ignored : Direction.Plane.HORIZONTAL) {
                    spreadLight(targetX, targetY, 0, visibility);
                }
            }
        }
    }

    private void spreadLight(int x, int y, int dist, int visibility) {
        if (dist >= visibility) return;

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            int adjacentX = x + direction.getStepX();
            int adjacentY = y + direction.getStepZ();
            if (adjacentX < 0 || adjacentX >= grid[0].length || adjacentY < 0 || adjacentY >= grid.length)
                continue;
            if (grid[adjacentY][adjacentX] != null) {
                light[adjacentY][adjacentX] = Math.max(light[adjacentY][adjacentX], (float) 1 / (dist + 2));
                spreadLight(adjacentX, adjacentY, dist + 1, visibility);
            }
        }
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
        LayerData layer = getLayer();
        if (layer.getTexture() != null) {
            int width = layer.getWidth() * slotSize;
            int height = layer.getHeight() * slotSize;
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, layer.getTexture(), contentX, contentY, 0, 0,
                    width, height, width, height);
        }

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
        if (light[v][u] <= 0) {
            guiGraphics.fill(x, y, x + slotSize, y + slotSize, 0xDD000000);
            return;
        }

        // Slot texture
        if (getLayer().getTexture() == null) {
            ResourceLocation texture = slot.getType().getTexture();
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, slotSize, slotSize, 1, 1, 16, 16);
        }

        // Blood level overlay
        if (slot.getBloodLevel() > 0) {
            int bloodColor = ARGB.color(slot.getBloodLevel(), 0x900000);
            guiGraphics.fill(x, y, x + slotSize, y + slotSize, bloodColor);
        }

        // Fog of war overlay
        if (light[v][u] < 1) {
            int fogColor = ARGB.color(1 - light[v][u], 0xDD000000);
            guiGraphics.fill(x, y, x + slotSize, y + slotSize, fogColor);
        }
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

        List<Point> shape = heldCompartment.getOrDefault(SHAPE, List.of());
        if (shape.isEmpty()) return;

        Point hoveredSlot = getHoveredSlot(mouseX, mouseY);
        if (hoveredSlot == null) return;

        for (Point p : shape) {
            int targetX = hoveredSlot.x() + p.x();
            int targetY = hoveredSlot.y() + p.y();
            int slotX = contentX + targetX * slotSize;
            int slotY = contentY + targetY * slotSize;
            int color = getLayer().canFit(hoveredSlot.x(), hoveredSlot.y(), depth, shape) ? 0x80008000 : 0x80FF0000;
            guiGraphics.fill(slotX, slotY, slotX + slotSize, slotY + slotSize, color);
        }
    }

    private void renderCompartments(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        MedicalStats medicalStats = screen.getMedicalStats();
        UUID hoveredCompartmentId = getHoveredCompartment(mouseX, mouseY);

        for (Map<Point, UUID> compartments : getLayer().getCompartments()) {
            // TODO: Could this be smarter? Either by caching or making layer data immutable to prevent concurrent modification exceptions
            for (Map.Entry<Point, UUID> entry : List.copyOf(compartments.entrySet())) {
                Point slotPos = entry.getKey();
                UUID compartmentId = entry.getValue();
                CompartmentInstance instance = medicalStats.getCompartment(compartmentId);

                if (instance == null || !instance.has(VISUAL_DATA)) continue;
                VisualData visualData = instance.get(VISUAL_DATA);

                int slotX = contentX + slotPos.x() * slotSize;
                int slotY = contentY + slotPos.y() * slotSize;
                int compartmentWidth = slotSize * visualData.width();
                int compartmentHeight = slotSize * visualData.height();
                int color = visualData.color();

                if (compartmentId.equals(hoveredCompartmentId)) {
                    float pulse = (float) (Math.sin(System.currentTimeMillis() / 500.0) * 0.2f + 0.8f);
                    int alpha = (int) (ARGB.alpha(color) * pulse);
                    color = ARGB.color(alpha, color);
                }

                ResourceLocation icon = visualData.icon();
                if (icon != null) {
                    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, icon, slotX, slotY, 0, 0, compartmentWidth,
                            compartmentHeight, compartmentWidth, compartmentHeight, color);
                }
            }
        }
    }

    private void renderCompartmentTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }

    protected void renderDragHandle(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderDragHandle(guiGraphics, mouseX, mouseY, partialTick);

        String name = screen.getMainCompartmentWidget().equals(this)
                ? screen.getTargetName()
                : compartment.getOrDefault(DISPLAY_NAME, compartment.getName()) + " " + getLayer().getName();
        guiGraphics.drawCenteredString(screen.getFont(), name, x + width / 2, y + 4, 0xFFFFFFFF);
    }

    public @Nullable UUID getHoveredCompartment(int mouseX, int mouseY) {
        Point hoveredSlot = getHoveredSlot(mouseX, mouseY);
        if (hoveredSlot == null) return null;

        return getLayer().getCompartmentAt(hoveredSlot.x(), hoveredSlot.y(), depth);
    }

    public @Nullable Point getHoveredSlot(int mouseX, int mouseY) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                LayerSlot slot = grid[row][col];
                if (slot == null) continue;
                if (light[row][col] <= 0) continue;

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
        // TODO: Compartment becomes stale, need to fetch from medical stats again
        return CompartmentUtil.getLayer(screen.getMedicalStats().getCompartment(compartment.getId()), layerIndex);
//        return CompartmentUtil.getLayer(compartment, layerIndex);
    }

    public int getLayerIndex() {
        return layerIndex;
    }

    public LayerSlot[][] getGrid() {
        return grid;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Button btn : buttons) {
            if (btn.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }

        CompartmentInstance hoveredCompartment = screen.getMedicalStats().getCompartment(getHoveredCompartment((int) mouseX, (int) mouseY));
        if (hoveredCompartment != null && hoveredCompartment.getCompartment().canExtract(hoveredCompartment, screen.getMedicalStats())) {
            if (button == 0) {
                ClientPacketDistributor.sendToServer(new ExtractCompartment(screen.getEntity().getUUID(),
                        compartment.getId(), hoveredCompartment.getId(), layerIndex));
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

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        depth = Mth.clamp(depth + (scrollY > 0 ? 1 : -1), 0, getLayer().getDepth() - 1);
        return true;
    }

    public boolean tryToPlace(int x, int y, @NotNull CompartmentInstance placingCompartment) {
        Point hoveredSlot = getHoveredSlot(x, y);
        if (hoveredSlot == null) return false;
        if (!getLayer().canFit(hoveredSlot.x(), hoveredSlot.y(), depth, placingCompartment)) return false;

        // Compartment should always be able to fit here
        ClientPacketDistributor.sendToServer(new AddAndInsertCompartment(screen.getEntity().getUUID(),
                compartment.getId(), placingCompartment, layerIndex, hoveredSlot.x(), hoveredSlot.y(), depth));
        ClientPacketDistributor.sendToServer(new SetCharactersChanged());
        return true;
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
        return screen.getMedicalStats().getCompartment(compartment.getId());
    }

    public int getSlotSize() {
        return slotSize;
    }

    public int getContentX() {
        return contentX;
    }

    public int getContentY() {
        return contentY;
    }

    public int getDepth() {
        return depth;
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {

    }
}
