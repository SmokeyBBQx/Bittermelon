package com.site21.bittermelon.common.systems.medical.legacy.client.tool;

import com.site21.bittermelon.common.systems.medical.legacy.client.CompartmentWidget;
import com.site21.bittermelon.common.systems.medical.legacy.client.HealthScreen;
import com.site21.bittermelon.common.systems.medical.legacy.client.interaction.SuturingWidget;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SutureWidget extends InstrumentWidget {
    private SuturingWidget suturingWidget = null;

    public SutureWidget(@NotNull ItemStack stack, int x, int y, int width, int height, HealthScreen screen) {
        super(stack, BitterDataComponents.SUTURE.get(), x, y, width, height, screen);
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractWidgetRenderState(graphics, mouseX, mouseY, a);

        if (suturingWidget != null) {
            suturingWidget.extractRenderState(graphics, mouseX, mouseY, a);
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        CompartmentWidget hoveredWidget = screen.getHoveredCompartmentWidget(event.x(), event.y());
        if (hoveredWidget == null) return false;

        if (suturingWidget == null) {
            suturingWidget = new SuturingWidget((int) event.x(), (int) event.y(), screen, hoveredWidget);
            return true;
        }

        if (!suturingWidget.getCompartmentWidget().equals(hoveredWidget)) {
            suturingWidget = null;
            return true;
        }

        return suturingWidget.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        if (suturingWidget != null) {
            return suturingWidget.mouseDragged(event, dx, dy);
        }
        return false;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (suturingWidget != null) {
            return suturingWidget.mouseReleased(event);
        }
        return false;
    }

    @Override
    public void onLayerChanged(CompartmentWidget widget) {
        suturingWidget = null;
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {

    }
}
