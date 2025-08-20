package com.site21.bittermelon.content.mobeffects.eyeirritation.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.shaders.BlurShader;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.client.visualeffects.VignetteRenderer.renderVignette;
import static com.site21.bittermelon.init.neoforge.BitterMobEffects.*;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class EyeIrritationClientRenderer {
    @SubscribeEvent
    public static void onRenderLevelStage(@NotNull RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) return;

        Player player = Minecraft.getInstance().player;
        if (player == null || !player.hasEffect(EYE_IRRITATION)) return;

        Minecraft mc = Minecraft.getInstance();
        MobEffectInstance effect = player.getEffect(EYE_IRRITATION);
        int amplifier = effect.getAmplifier();
        int duration = effect.getDuration();

        BlurShader.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());

        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.resetTextureMatrix();

        BlurShader.processBlurShader(event.getPartialTick().getGameTimeDeltaTicks(),
                amplifier * (duration > 200 ? 1 : ((float) duration / 200)));
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiEvent.@NotNull Pre event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        if (player.hasEffect(EYE_IRRITATION)) {
            int duration =  player.getEffect(EYE_IRRITATION).getDuration();
            float vignetteAmplifier = duration > 200 ? 1 : (float) duration / 200;

            renderVignette(event.getGuiGraphics(), 0, vignetteAmplifier, vignetteAmplifier);
        }
    }
}
