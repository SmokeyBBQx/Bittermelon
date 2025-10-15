package com.site21.bittermelon.common.content.mobeffects.hallunication.client;

import com.site21.bittermelon.Bittermelon;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class HallucinationRenderer {
    @SubscribeEvent
    public static void onRenderLevelStage(@NotNull RenderLevelStageEvent.AfterLevel event) {
//        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) return;
//
//        Player player = Minecraft.getInstance().player;
//        if (player == null || !player.hasEffect(HALLUCINATION)) return;
//
//        Minecraft mc = Minecraft.getInstance();
//        MobEffectInstance effect = player.getEffect(HALLUCINATION);
//        int amplifier = effect.getAmplifier();
//        int duration = effect.getDuration();
//
//        ColorBleedShader.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());
//
//        RenderSystem.disableBlend();
//        RenderSystem.disableDepthTest();
//        RenderSystem.resetTextureMatrix();
//
//        ColorBleedShader.processBlurShader(event.getPartialTick().getGameTimeDeltaTicks(), 1);
    }
}
