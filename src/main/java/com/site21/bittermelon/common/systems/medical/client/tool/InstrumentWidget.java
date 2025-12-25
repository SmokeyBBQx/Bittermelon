package com.site21.bittermelon.common.systems.medical.client.tool;

import com.site21.bittermelon.common.systems.component.medical.MedicalInstrument;
import com.site21.bittermelon.common.systems.medical.client.HealthScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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

    protected Optional<ResourceLocation> getIcon() {
        return stack.get(componentType).icon();
    }

    public void renderTool(GuiGraphics guiGraphics, int x, int y) {
        if (getIcon().isPresent()) {
            guiGraphics.blit(
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
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(x, y);
            guiGraphics.pose().scale(width / 16f, height / 16f);
            guiGraphics.renderItem(stack, 0, 0);
            guiGraphics.pose().popMatrix();
        }
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderTool(guiGraphics, x, y);
    }
}
