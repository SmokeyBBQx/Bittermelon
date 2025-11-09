package com.site21.bittermelon.common.content.mobeffects;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.client.render.VignetteRenderer.renderVignette;
import static com.site21.bittermelon.init.neoforge.BitterMobEffects.UNCONSCIOUS;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class UnconsciousRenderer {
    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiEvent.@NotNull Pre event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        // TODO: Gray shader

        if (player.hasEffect(UNCONSCIOUS)) {
            renderVignette(event.getGuiGraphics(), 10);

            if (mc.options.getCameraType().isFirstPerson()) {
                GuiGraphics guiGraphics = event.getGuiGraphics();

                int screenWidth = mc.getWindow().getGuiScaledWidth();
                int screenHeight = mc.getWindow().getGuiScaledHeight();

                guiGraphics.fill(0, 0, screenWidth, screenHeight, 0xFF000000);
            }
        }
    }
}
