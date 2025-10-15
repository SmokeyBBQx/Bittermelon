package com.site21.bittermelon.common.content.mobeffects.hallunication.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.shaders.ColorBleedShader;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterMobEffects.HALLUCINATION;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class HallucinationRenderer {
    @SubscribeEvent
    public static void onRenderLevelStage(@NotNull RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) return;

        Player player = Minecraft.getInstance().player;
        if (player == null || !player.hasEffect(HALLUCINATION)) return;

        Minecraft mc = Minecraft.getInstance();
        MobEffectInstance effect = player.getEffect(HALLUCINATION);
        int amplifier = effect.getAmplifier();
        int duration = effect.getDuration();

        ColorBleedShader.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());

        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.resetTextureMatrix();

        ColorBleedShader.processBlurShader(event.getPartialTick().getGameTimeDeltaTicks(), 1);
    }
}
