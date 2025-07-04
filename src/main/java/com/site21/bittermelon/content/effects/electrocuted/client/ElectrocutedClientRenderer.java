package com.site21.bittermelon.content.effects.electrocuted.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static com.site21.bittermelon.init.neoforge.BitterMobEffects.*;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class ElectrocutedClientRenderer {
    private static final ResourceLocation VIGNETTE_LOCATION = ResourceLocation.withDefaultNamespace("textures/misc/vignette.png");

    @SubscribeEvent
    public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        MobEffectInstance instance = player.getEffect(ELECTROCUTED);
        if (instance == null) {
            instance = player.getEffect(TASERED);
        }

        if (instance != null) {
            RandomSource random = mc.player.getRandom();

            float intensity = instance.getAmplifier();
            float offsetYaw = (random.nextFloat() - 0.5f) * intensity;
            float offsetPitch = (random.nextFloat() - 0.5f) * intensity;
            float offsetRoll = (random.nextFloat() - 0.5f) * intensity;

            event.setYaw(event.getYaw() + offsetYaw);
            event.setPitch(event.getPitch() + offsetPitch);
            event.setRoll(event.getRoll() + offsetRoll);
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiEvent.@NotNull Pre event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        if (player.hasEffect(TASERED) || player.hasEffect(ELECTROCUTED)) {
            renderVignette(event.getGuiGraphics(), player);
        }
    }

    private static void renderVignette(@NotNull GuiGraphics guiGraphics, @Nullable Player player) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        MobEffectInstance instance = player.getEffect(ELECTROCUTED);
        if (instance == null) {
            instance = player.getEffect(TASERED);
        }

        float effectAmplifier = instance.getAmplifier();
//            effectAmplifier = effectAmplifier > 0 ? effectAmplifier / 30 : 0;
        guiGraphics.setColor(0, effectAmplifier, effectAmplifier, 1.0f);

        guiGraphics.blit(VIGNETTE_LOCATION, 0, 0, -90, 0.0f, 0.0f, guiGraphics.guiWidth(), guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight());
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}
