package com.site21.bittermelon.content.medical.mobeffects.fainting;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.shaders.BlurShader;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import javax.annotation.Nullable;

import static com.site21.bittermelon.init.neoforge.BitterMobEffects.FAINTING;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class FaintingRenderer {
    private static final ResourceLocation VIGNETTE_LOCATION = ResourceLocation.withDefaultNamespace("textures/misc/vignette.png");

    @SubscribeEvent
    public static void onRenderLevelStage(@NotNull RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) return;

        Player player = Minecraft.getInstance().player;
        if (player == null || !player.hasEffect(FAINTING)) return;

        Minecraft mc = Minecraft.getInstance();
        int amplifier = player.getEffect(FAINTING).getAmplifier();
        long gameTime = mc.level.getGameTime();

        if (amplifier < 4 || amplifier >= 100) return;

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
        Minecraft mc = Minecraft.getInstance();
        Player player =mc.player;
        if (player == null) return;


        if (player.hasEffect(FAINTING)) {
            renderVignette(event.getGuiGraphics(), player);

            CameraType cameraType = mc.options.getCameraType();
            if (player.getEffect(FAINTING).getAmplifier() >= 100 && cameraType.isFirstPerson()) {
                GuiGraphics guiGraphics = event.getGuiGraphics();

                int screenWidth = mc.getWindow().getGuiScaledWidth();
                int screenHeight = mc.getWindow().getGuiScaledHeight();

                guiGraphics.fill(0, 0, screenWidth, screenHeight, 0xFF000000);
            }
        }
    }

    private static void renderVignette(@NotNull GuiGraphics guiGraphics, @Nullable Player player) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        float effectAmplifier = player.getEffect(FAINTING).getAmplifier();
        effectAmplifier = effectAmplifier > 0 ? effectAmplifier / 10 : 0;
        guiGraphics.setColor(effectAmplifier, effectAmplifier, effectAmplifier, 1.0f);

        guiGraphics.blit(VIGNETTE_LOCATION, 0, 0, -90, 0.0f, 0.0f, guiGraphics.guiWidth(), guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight());
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}
