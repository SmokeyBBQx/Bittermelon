package com.site21.bittermelon.common.systems.medical.legacy.client.tool;

import com.site21.bittermelon.common.systems.medical.legacy.client.CompartmentWidget;
import com.site21.bittermelon.common.systems.medical.legacy.client.HealthScreen;
import com.site21.bittermelon.common.systems.medical.legacy.client.interaction.IncisionWidget;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ScalpelWidget extends InstrumentWidget {
    private IncisionWidget incisionWidget;
    private final float efficiency;
    private float rotation = 0;
    private double startX, startY = 0;

    public ScalpelWidget(@NotNull ItemStack stack, int x, int y, int width, int height, HealthScreen screen) {
        super(stack, BitterDataComponents.SCALPEL.get(), x, y, width, height, screen);
        efficiency = stack.get(BitterDataComponents.SCALPEL).efficiency();
    }

    @Override
    public void renderTool(@NotNull GuiGraphicsExtractor graphics, int x, int y) {
        graphics.pose().pushMatrix();
        graphics.pose().translate(x, y);
        graphics.pose().rotate(rotation);
        graphics.pose().translate(-16, -8);
        super.renderTool(graphics, 0, 0);
        graphics.pose().popMatrix();
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        if (incisionWidget != null) {
            incisionWidget.extractRenderState(graphics, mouseX, mouseY, a);
        }

        super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        startX = event.x();
        startY = event.y();

        if (incisionWidget == null) {
            CompartmentWidget hoveredWidget = screen.getHoveredCompartmentWidget(event.x(), event.y());
            if (hoveredWidget != null) {
                if (!hoveredWidget.isWithinContentArea((int) event.x(), (int) event.y())) return false;
                incisionWidget = new IncisionWidget((int) event.x(), (int) event.y(), efficiency, hoveredWidget, screen);
                return true;
            }
        }
        return super.mouseClicked(event,  doubleClick);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        rotation = (float) (Math.atan2(event.y() - startY, event.x() - startX) + Math.PI);

        if (incisionWidget != null) {
            return incisionWidget.mouseDragged(event, dx, dy);
        }

        return super.mouseDragged(event, dx, dy);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        rotation = 0;
        if (incisionWidget != null) {
            if (incisionWidget.mouseReleased(event)) {
                incisionWidget = null;
                return true;
            }
        }

        return super.mouseReleased(event);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {

    }
}
