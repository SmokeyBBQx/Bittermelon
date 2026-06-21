package com.site21.bittermelon.common.content.mobeffects;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.common.content.mobeffects.FaintingRenderer.VIGNETTE_LOCATION;
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
            GuiGraphicsExtractor GuiGraphicsExtractor = event.getGuiGraphicsExtractor();

            GuiGraphicsExtractor.blit(
                    RenderPipelines.VIGNETTE,
                    VIGNETTE_LOCATION,
                    0,
                    0,
                    0.0F,
                    0.0F,
                    GuiGraphicsExtractor.guiWidth(),
                    GuiGraphicsExtractor.guiHeight(),
                    GuiGraphicsExtractor.guiWidth(),
                    GuiGraphicsExtractor.guiHeight(),
                    0xFF000000 | (int) (10 * 255)
            );

            if (mc.options.getCameraType().isFirstPerson()) {
                int screenWidth = mc.getWindow().getGuiScaledWidth();
                int screenHeight = mc.getWindow().getGuiScaledHeight();

                GuiGraphicsExtractor.fill(0, 0, screenWidth, screenHeight, 0xFF000000);
            }
        }
    }
}
