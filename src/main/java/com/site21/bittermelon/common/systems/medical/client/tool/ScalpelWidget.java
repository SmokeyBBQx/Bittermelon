package com.site21.bittermelon.common.systems.medical.client.tool;

import com.site21.bittermelon.common.systems.medical.client.CompartmentWidget;
import com.site21.bittermelon.common.systems.medical.client.HealthScreen;
import com.site21.bittermelon.common.systems.medical.client.interaction.IncisionWidget;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
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
    public void renderTool(@NotNull GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(x, y);
        guiGraphics.pose().rotate(rotation);
        guiGraphics.pose().translate(-16, -8);
        super.renderTool(guiGraphics, 0, 0);
        guiGraphics.pose().popMatrix();
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (incisionWidget != null) {
            incisionWidget.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        }

        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        startX = mouseX;
        startY = mouseY;

        if (incisionWidget == null) {
            CompartmentWidget hoveredWidget = screen.getHoveredCompartmentWidget(mouseX, mouseY);
            if (hoveredWidget != null) {
                if (!hoveredWidget.isWithinContentArea((int) mouseX, (int) mouseY)) return false;
                incisionWidget = new IncisionWidget((int) mouseX, (int) mouseY, efficiency, hoveredWidget, screen);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        rotation = (float) (Math.atan2(mouseY - startY, mouseX - startX) + Math.PI);

        if (incisionWidget != null) {
            return incisionWidget.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        rotation = 0;
        if (incisionWidget != null) {
            if (incisionWidget.mouseReleased(mouseX, mouseY, button)) {
                incisionWidget = null;
                return true;
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {

    }
}
