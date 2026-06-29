package com.site21.bittermelon.common.content.mobeffects;

import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

public class FaintingRenderer {
    public static final Identifier VIGNETTE_LOCATION = Identifier.withDefaultNamespace("textures/misc/vignette.png");

    @SubscribeEvent
    public static void onRenderLevelStage(@NotNull RenderLevelStageEvent.AfterLevel event) {
//        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) return;
//
//        Player player = Minecraft.getInstance().player;
//        if (player == null || !player.hasEffect(FAINTING)) return;
//
//        Minecraft mc = Minecraft.getInstance();
//        int amplifier = player.getEffect(FAINTING).getAmplifier();
//        long gameTime = mc.level.getGameTime();
//
//        BlurShader.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());
//
//        float wavePhase = (float) Math.sin(gameTime * 0.02 + amplifier * 0.3);
//        float consciousnessWave = Math.max(0, wavePhase);
//        consciousnessWave = consciousnessWave * consciousnessWave;
//        float blurIntensity = consciousnessWave * Math.min(amplifier * 2.0f, 8.0f);
//
//        RenderSystem.disableBlend();
//        RenderSystem.disableDepthTest();
//        RenderSystem.resetTextureMatrix();
//
//        if (blurIntensity > 0.1f) {
//            BlurShader.processBlurShader(event.getPartialTick().getGameTimeDeltaTicks(), blurIntensity);
//        }
    }

//    @SubscribeEvent
//    public static void onRenderOverlay(RenderGuiEvent.@NotNull Pre event) {
//        Minecraft mc = Minecraft.getInstance();
//        Player player = mc.player;
//        if (player == null) return;
//
//
//        if (player.hasEffect(FAINTING)) {
//            int amplifier = player.getEffect(FAINTING).getAmplifier();
//            float vignetteAmplifier = amplifier > 0 ? (float) amplifier / 10 : 0;
//            GuiGraphicsExtractor GuiGraphicsExtractor = event.getGuiGraphicsExtractor();
//
//            GuiGraphicsExtractor.blit(
//                    RenderPipelines.VIGNETTE,
//                    VIGNETTE_LOCATION,
//                    0,
//                    0,
//                    0.0F,
//                    0.0F,
//                    GuiGraphicsExtractor.guiWidth(),
//                    GuiGraphicsExtractor.guiHeight(),
//                    GuiGraphicsExtractor.guiWidth(),
//                    GuiGraphicsExtractor.guiHeight(),
//                    0xFF000000 | (int) (vignetteAmplifier * 255)
//            );
//        }
//    }
//
//    @SubscribeEvent
//    public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
//        Player player = Minecraft.getInstance().player;
//        if (player == null) return;
//
//        if (player.hasEffect(FAINTING)) {
//            float time = (float) (player.level().getGameTime() * 0.005);
//            float intensity = 5;
//            float roll = (float) (Math.sin(time * intensity) * intensity);
//
//            event.setRoll(roll);
//        }
//    }
}
