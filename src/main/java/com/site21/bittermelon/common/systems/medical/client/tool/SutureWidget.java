package com.site21.bittermelon.common.systems.medical.client.tool;

import com.site21.bittermelon.common.systems.medical.client.CompartmentWidget;
import com.site21.bittermelon.common.systems.medical.client.HealthScreen;
import com.site21.bittermelon.common.systems.medical.client.interaction.SuturingWidget;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SutureWidget extends InstrumentWidget {
    private SuturingWidget suturingWidget = null;

    public SutureWidget(@NotNull ItemStack stack, int x, int y, int width, int height, HealthScreen screen) {
        super(stack, BitterDataComponents.SUTURE.get(), x, y, width, height, screen);
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(GuiGraphicsExtractor, mouseX, mouseY, partialTick);

        if (suturingWidget != null) {
            suturingWidget.renderWidget(GuiGraphicsExtractor, mouseX, mouseY, partialTick);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        CompartmentWidget hoveredWidget = screen.getHoveredCompartmentWidget(mouseX, mouseY);
        if (hoveredWidget == null) return false;

        if (suturingWidget == null) {
            suturingWidget = new SuturingWidget((int) mouseX, (int) mouseY, screen, hoveredWidget);
            return true;
        }

        if (!suturingWidget.getCompartmentWidget().equals(hoveredWidget)) {
            suturingWidget = null;
            return true;
        }

        return suturingWidget.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (suturingWidget != null) {
            return suturingWidget.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (suturingWidget != null) {
            return suturingWidget.mouseReleased(mouseX, mouseY, button);
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
