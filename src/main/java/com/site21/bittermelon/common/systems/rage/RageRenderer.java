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
                boolean pulse = player.level().getGameTime() % beatInterval == 0;

                int base = (int) (rage * 255.0f);
                float pulseFactor = 1.0f + (rage * 0.75f);

                int amount = pulse ? (int) (base * pulseFactor) : base;
                amount = Math.min(255, amount);

                int color = ARGB.color(amount, 0, amount, amount);

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
}
