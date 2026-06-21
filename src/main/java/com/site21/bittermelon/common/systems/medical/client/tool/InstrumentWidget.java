package com.site21.bittermelon.common.systems.medical.client.tool;

import com.site21.bittermelon.common.systems.component.medical.MedicalInstrument;
import com.site21.bittermelon.common.systems.medical.client.CompartmentWidget;
import com.site21.bittermelon.common.systems.medical.client.HealthScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class InstrumentWidget extends AbstractWidget {
    protected final ItemStack stack;
    protected final DataComponentType<? extends MedicalInstrument> componentType;
    protected final HealthScreen screen;

    public InstrumentWidget(@NotNull ItemStack stack, DataComponentType<? extends MedicalInstrument> componentType,
                            int x, int y, int width, int height, HealthScreen screen) {
        super(x, y, width, height, Component.literal(stack.getHoverName().getString()));
        this.stack = stack;
        this.componentType = componentType;
        this.screen = screen;
    }

    protected Optional<Identifier> getIcon() {
        return stack.get(componentType).icon();
    }

    public void renderTool(GuiGraphicsExtractor GuiGraphicsExtractor, int x, int y) {
        if (getIcon().isPresent()) {
            GuiGraphicsExtractor.blit(
                    getIcon().get(),
                    x,
                    y,
                    width,
                    height,
                    0,
                    0,
                    16,
                    16
            );
        } else {
            GuiGraphicsExtractor.pose().pushMatrix();
            GuiGraphicsExtractor.pose().translate(x, y);
            GuiGraphicsExtractor.pose().scale(width / 16f, height / 16f);
            GuiGraphicsExtractor.renderItem(stack, 0, 0);
            GuiGraphicsExtractor.pose().popMatrix();
        }
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, int mouseX, int mouseY, float partialTick) {
        renderTool(GuiGraphicsExtractor, x, y);
    }

    public void onLayerChanged(CompartmentWidget widget) {
    }
}
