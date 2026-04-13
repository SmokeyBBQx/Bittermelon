package com.site21.bittermelon.common.systems.rage;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.gui.GuiLayer;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.RAGE;

public class RageRenderer implements GuiLayer {
    private static final ResourceLocation VIGNETTE_LOCATION = ResourceLocation.withDefaultNamespace("textures/misc/vignette.png");

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!Minecraft.useFancyGraphics()) return;
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            int rageValue = player.getData(RAGE);
            float rage = (float) rageValue / 100;
            if (rage > 0) {
                int beatInterval = RageHandler.getHeartbeatDelay(rageValue);
                long time = player.level().getGameTime();
                int color = getColor(rage, beatInterval, time);

                guiGraphics.blit(
                        RenderPipelines.VIGNETTE,
                        VIGNETTE_LOCATION,
                        0,
                        0,
                        0.0F,
                        0.0F,
                        guiGraphics.guiWidth(),
                        guiGraphics.guiHeight(),
                        guiGraphics.guiWidth(),
                        guiGraphics.guiHeight(),
                        color
                );
            }
        }
    }

    private static int getColor(float rage, int beatInterval, long time) {
        int base = (int) Math.min(200, (rage * 255.0f));

        int pulseWindow = Math.max(2, beatInterval / 3);
        int phase = (int) (time % beatInterval);

        float pulseStrength = 0.0f;
        if (phase < pulseWindow) {
            float t = phase / (float) Math.max(1, pulseWindow - 1);
            pulseStrength = 1.0f - Math.abs((t * 2.0f) - 1.0f);
        }

        int amount = (int) (base * (1.0f + rage * 0.75f * pulseStrength));
        amount = Math.min(255, amount);

        return ARGB.color(amount, 0, amount, amount);
    }
}
