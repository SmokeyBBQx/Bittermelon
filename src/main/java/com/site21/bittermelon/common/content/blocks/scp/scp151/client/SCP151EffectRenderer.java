package com.site21.bittermelon.common.content.blocks.scp.scp151.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.render.shaders.BlurShader;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.client.render.VignetteRenderer.renderVignette;
import static com.site21.bittermelon.init.neoforge.BitterMobEffects.DROWNING;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class SCP151EffectRenderer {
    @SubscribeEvent
    public static void onRenderLevelStage(@NotNull RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) return;

        Player player = Minecraft.getInstance().player;
        if (player == null || !player.hasEffect(DROWNING)) return;

        Minecraft mc = Minecraft.getInstance();
        int amplifier = player.getEffect(DROWNING).getAmplifier();
        long gameTime = mc.level.getGameTime();

        if (amplifier < 4) return;

        BlurShader.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());

        float wavePhase = (float) Math.sin(gameTime * 0.02 + amplifier * 0.3);
        float consciousnessWave = Math.max(0, wavePhase);
        consciousnessWave = consciousnessWave * consciousnessWave;
        float blurIntensity = consciousnessWave * Math.min(amplifier * 2.0f, 8.0f);

        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.resetTextureMatrix();

        if (blurIntensity > 0.1f) {
            BlurShader.processBlurShader(event.getPartialTick().getGameTimeDeltaTicks(), blurIntensity);
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiEvent.@NotNull Pre event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        if (player.hasEffect(DROWNING)) {
            float effectAmplifier = (player.getMaxAirSupply() - player.getAirSupply());
            renderVignette(event.getGuiGraphics(), effectAmplifier > 0 ? effectAmplifier / 30 : 0);
        }
    }
}
