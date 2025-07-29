package com.site21.bittermelon.content.effects.eyeirritation.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.shaders.BlurShader;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterMobEffects.DROWNING;
import static com.site21.bittermelon.init.neoforge.BitterMobEffects.EYE_IRRITATION;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class EyeIrritationClientRenderer {
    @SubscribeEvent
    public static void onRenderLevelStage(@NotNull RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) return;

        Player player = Minecraft.getInstance().player;
        if (player == null || !player.hasEffect(EYE_IRRITATION)) return;

        Minecraft mc = Minecraft.getInstance();
        int amplifier = player.getEffect(EYE_IRRITATION).getAmplifier();

        BlurShader.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());

        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.resetTextureMatrix();

        if (amplifier > 0.1f) {
            BlurShader.processBlurShader(event.getPartialTick().getGameTimeDeltaTicks(), amplifier);
        }
    }
}
