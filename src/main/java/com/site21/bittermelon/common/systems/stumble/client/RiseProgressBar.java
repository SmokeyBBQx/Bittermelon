package com.site21.bittermelon.common.systems.stumble.client;

import com.site21.bittermelon.common.systems.stumble.StumbleHandler;
import com.site21.bittermelon.init.neoforge.BitterMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.common.systems.stumble.client.RiseKeyHandler.TICKS_REQUIRED;

public class RiseProgressBar {
    private static final Identifier PROGRESS_BAR_BACKGROUND = Identifier.fromNamespaceAndPath("bittermelon", "hud/progress_bar_background");
    private static final Identifier PROGRESS_BAR_PROGRESS = Identifier.fromNamespaceAndPath("bittermelon", "hud/progress_bar_progress");
    private static final Identifier STUN_BAR_PROGRESS = Identifier.fromNamespaceAndPath("bittermelon", "hud/stun_bar_progress");

    private static final int BAR_WIDTH = 182;
    private static final int BAR_HEIGHT = 5;
    private static final int MAX_STUN_TIME = 40;

    private static void renderProgressBar(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, int x, int y) {
        float ticksHeld = RiseKeyHandler.getTicksHeld();

        GuiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_BAR_BACKGROUND, x, y, BAR_WIDTH, BAR_HEIGHT);

        if (ticksHeld > 0) {
            int progressWidth = Math.min((int) ((ticksHeld / (TICKS_REQUIRED - 2)) * BAR_WIDTH), BAR_WIDTH);
            GuiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_BAR_PROGRESS, BAR_WIDTH, BAR_HEIGHT, 0, 0, x, y, progressWidth, BAR_HEIGHT);
        }
    }

    private static void renderStunBar(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, int x, int y, @NotNull Player player) {
        MobEffectInstance stumbleEffect = player.getEffect(BitterMobEffects.STUN);
        if (stumbleEffect == null) return;

        GuiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_BAR_BACKGROUND, x, y, BAR_WIDTH, BAR_HEIGHT);

        int progressWidth = Math.min((int) ((stumbleEffect.getDuration() / (float) MAX_STUN_TIME) * BAR_WIDTH), BAR_WIDTH);
        GuiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, STUN_BAR_PROGRESS, BAR_WIDTH, BAR_HEIGHT, 0, 0, x, y, progressWidth, BAR_HEIGHT);
    }

    public static void extract(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor) {
        Player player = Minecraft.getInstance().player;
        if (player == null || !StumbleHandler.isStumbled(player)) return;

        int x = GuiGraphicsExtractor.guiWidth() / 2 - 91;
        int y = GuiGraphicsExtractor.guiHeight() - 32 + 3;

        if (RiseKeyHandler.isKeyPressed()) {
            renderProgressBar(GuiGraphicsExtractor, x, y);
        } else if (StumbleHandler.isStunned(player)) {
            renderStunBar(GuiGraphicsExtractor, x, y, player);
        }
    }
}