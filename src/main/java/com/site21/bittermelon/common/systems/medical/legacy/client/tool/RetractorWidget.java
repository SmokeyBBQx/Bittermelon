package com.site21.bittermelon.common.systems.medical.legacy.client.tool;

import com.site21.bittermelon.common.systems.medical.legacy.client.CompartmentWidget;
import com.site21.bittermelon.common.systems.medical.legacy.client.HealthScreen;
import com.site21.bittermelon.common.systems.medical.legacy.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.legacy.compartment.layer.Point;
import com.site21.bittermelon.init.custom.Compartments;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RetractorWidget extends InstrumentWidget {
    private boolean horizontal = true;

    public RetractorWidget(@NotNull ItemStack stack, int x, int y, int width, int height, HealthScreen screen) {
        super(stack, BitterDataComponents.RETRACTOR.get(), x, y, width, height, screen);
    }

    @Override
    public void renderTool(@NotNull GuiGraphicsExtractor graphics, int x, int y) {
        graphics.pose().pushMatrix();
        graphics.pose().translate(x, y);
        graphics.pose().rotateAbout(horizontal ? 0.785f : -0.785f, 0, 0);
        super.renderTool(graphics, -16, -16);
        graphics.pose().popMatrix();
    }

    private void retract(@NotNull Point start, CompartmentWidget widget) {
        List<Point> retractPoints = horizontal ? floodFillHorizontal(widget, start) : floodFillVertical(widget, start);
        for (Point point : retractPoints) {
            CompartmentInstance slotInstance = screen.getMedicalStats().getCompartment(
                    widget.getLayer().getCompartmentAt(point.x(), point.y(), 0));
            if (slotInstance != null && slotInstance.getCompartment().equals(Compartments.CUT.get())) {
                slotInstance.set(BitterDataComponents.REVEAL_DISTANCE, 5);
            }
        }

        screen.sendMessage("*" + screen.getPlayerName() + " retracts " + screen.getTargetName() + "'s " +
                widget.getCompartment().getName() + ".*");
    }

    private @NotNull List<Point> floodFillHorizontal(@NotNull CompartmentWidget widget, @NotNull Point start) {
        List<Point> points = new ArrayList<>();
        int leftX = start.x();
        int rightX = start.x();

        while (isValidSlot(leftX, start.y(), widget)) {
            leftX--;
            points.add(new Point(leftX, start.y()));
        }

        while (isValidSlot(rightX, start.y(), widget)) {
            rightX++;
            points.add(new Point(leftX, start.y()));
        }

        return points;
    }

    private @NotNull List<Point> floodFillVertical(@NotNull CompartmentWidget widget, @NotNull Point start) {
        List<Point> points = new ArrayList<>();
        int topY = start.y();
        int bottomY = start.y();

        while (isValidSlot(start.x(), topY, widget)) {
            topY--;
            points.add(new Point(start.x(), topY));
        }

        while (isValidSlot(start.x(), bottomY, widget)) {
            bottomY++;
            points.add(new Point(start.x(), bottomY));
        }

        return points;
    }

    private boolean isValidSlot(int x, int y, @NotNull CompartmentWidget widget) {
        if (widget.getGrid()[y][x] == null) return false;

        CompartmentInstance slotInstance = screen.getMedicalStats().getCompartment(
                widget.getLayer().getCompartmentAt(x, y, 0));
        if (slotInstance == null) return false;

        return slotInstance.getCompartment().equals(Compartments.CUT.get());
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() == 1) {
            horizontal = !horizontal;
            return true;
        }

        CompartmentWidget hoveredWidget = screen.getHoveredCompartmentWidget(event.x(), event.y());
        if (hoveredWidget == null) return false;

        CompartmentInstance slotInstance = screen.getMedicalStats().getCompartment(hoveredWidget.getHoveredCompartment((int) event.x(), (int) event.y()));
        if (slotInstance == null) return false;
        if (!slotInstance.getCompartment().equals(Compartments.CUT.get())) return false;

        Point hoveredSlot = hoveredWidget.getHoveredSlot((int) event.x(), (int) event.y());
        if (hoveredSlot == null) return false;

        retract(hoveredSlot, hoveredWidget);

        return true;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
