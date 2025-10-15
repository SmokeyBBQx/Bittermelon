package com.site21.bittermelon.systems.stumble.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.systems.stumble.StumbleHandler;
import com.site21.bittermelon.init.neoforge.BitterMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.systems.stumble.client.RiseKeyHandler.TICKS_REQUIRED;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class RiseProgressBar {
    private static final ResourceLocation PROGRESS_BAR_BACKGROUND = ResourceLocation.fromNamespaceAndPath("bittermelon", "textures/gui/sprites/hud/progress_bar_background.png");
    private static final ResourceLocation PROGRESS_BAR_PROGRESS = ResourceLocation.fromNamespaceAndPath("bittermelon", "textures/gui/sprites/hud/progress_bar_progress.png");
    private static final ResourceLocation STUN_BAR_BACKGROUND = ResourceLocation.fromNamespaceAndPath("bittermelon", "textures/gui/sprites/hud/stun_bar_background.png");
    private static final ResourceLocation STUN_BAR_PROGRESS = ResourceLocation.fromNamespaceAndPath("bittermelon", "textures/gui/sprites/hud/stun_bar_progress.png");

    private static void renderProgressBar(GuiGraphics guiGraphics, int x, int y) {
        float ticksHeld = RiseKeyHandler.getTicksHeld();

        RenderSystem.setShaderTexture(0, PROGRESS_BAR_BACKGROUND);
        RenderSystem.enableBlend();

        if (RiseKeyHandler.isKeyPressed()) {
            guiGraphics.blit(PROGRESS_BAR_BACKGROUND,
                    x, y,
                    0, 0,
                    182, 5,
                    182, 5);
        }

        if (ticksHeld > 0) {
            RenderSystem.setShaderTexture(0, PROGRESS_BAR_PROGRESS);
            int progressWidth = (int) ((ticksHeld / (TICKS_REQUIRED - 2)) * 182);
            progressWidth = Math.min(progressWidth, 182);

            guiGraphics.blit(PROGRESS_BAR_PROGRESS,
                    x, y,
                    0, 0,
                    progressWidth, 5,
                    182, 5);
        }

        RenderSystem.disableBlend();
    }

    private static void renderStunBar(@NotNull GuiGraphics guiGraphics, int x, int y, @NotNull Player player) {
        MobEffectInstance stumbleEffect = player.getEffect(BitterMobEffects.STUN);
        if (stumbleEffect == null) return;

        int stunTime = stumbleEffect.getDuration();
        int maxStunTime = player instanceof Player ? 40 : 100;

        guiGraphics.blit(PROGRESS_BAR_BACKGROUND,
                x, y,
                0, 0,
                182, 5,
                182, 5);

        RenderSystem.setShaderTexture(0, STUN_BAR_BACKGROUND);
        int progressWidth = (int) ((stunTime / (float) maxStunTime) * 182);
        progressWidth = Math.min(progressWidth, 182);

        guiGraphics.blit(STUN_BAR_PROGRESS,
                x, y,
                0, 0,
                progressWidth, 5,
                182, 5);

        RenderSystem.disableBlend();
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiEvent.@NotNull Post event) {
        GuiGraphics guiGraphics = event.getGuiGraphics();
        Player player = Minecraft.getInstance().player;
        int x = guiGraphics.guiWidth() / 2 - 91;
        int y = guiGraphics.guiHeight() - 32 + 3;

        if (player == null) return;
        if (!StumbleHandler.isStumbled(player)) return;

        if (RiseKeyHandler.isKeyPressed()) {
            renderProgressBar(guiGraphics, x, y);
        } else if (StumbleHandler.isStunned(player)) {
            renderStunBar(guiGraphics, x, y, player);
        }
    }
}