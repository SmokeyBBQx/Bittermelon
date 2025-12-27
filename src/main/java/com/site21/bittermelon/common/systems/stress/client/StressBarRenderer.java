package com.site21.bittermelon.common.systems.stress.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.init.neoforge.BitterAttachmentTypes;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.contextualbar.ContextualBarRenderer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class StressBarRenderer implements ContextualBarRenderer {
    private static final ResourceLocation STRESS_BAR_BACKGROUND = Bittermelon.resource("hud/stress_bar_background");
    private static final ResourceLocation STRESS_BAR_PROGRESS = Bittermelon.resource("hud/stress_bar_progress");
    private final Minecraft minecraft;

    public StressBarRenderer(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        LocalPlayer player = minecraft.player;
        int i = left(minecraft.getWindow());
        int j = top(minecraft.getWindow());
        int l = (int) ((float) (player.getData(BitterAttachmentTypes.STRESS) % 100) / 100 * 183.0F);
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, STRESS_BAR_BACKGROUND, i, j, 182, 5);
        if (l > 0) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, STRESS_BAR_PROGRESS, 182, 5, 0, 0, i, j, l, 5);
        }
    }

    public void render(@NotNull GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        LocalPlayer player = minecraft.player;
        int level = player.getData(BitterAttachmentTypes.STRESS) / 100;
        Component component = Component.literal(String.valueOf(level));
        int i = (guiGraphics.guiWidth() - minecraft.font.width(component)) / 2;
        int j = guiGraphics.guiHeight() - 24 - 9 - 2;
        guiGraphics.drawString(minecraft.font, component, i, j, 0xFFFFFFFF, true);
    }
}
