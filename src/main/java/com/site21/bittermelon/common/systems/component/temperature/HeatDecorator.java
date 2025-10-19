package com.site21.bittermelon.common.systems.component.temperature;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.BURN_TIME;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.TEMPERATURE;

public class HeatDecorator implements IItemDecorator {
    @Override
    public boolean render(@NotNull GuiGraphics guiGraphics, @NotNull Font font, @NotNull ItemStack stack, int x, int y) {
        if (stack.getOrDefault(TEMPERATURE, 0.0f) <= 273) return false;
        float temperature = stack.get(TEMPERATURE);

        float minTemp = 273f;
        float maxTemp = 506f;
        float intensity = Mth.clamp((temperature - minTemp) / (maxTemp - minTemp), 0f, 1f);

        int red = (int) (255 * intensity);
        int color = 0x55000000 | (red << 16);

        guiGraphics.fill(RenderPipelines.GUI, x, y, x + 16, y + 16, color);

        if (stack.get(BURN_TIME) != null) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, ResourceLocation.withDefaultNamespace("textures/block/fire_0.png"), x, y, 0, 16, 16, 16, 16, 16);
        }

        return true;
    }
}
