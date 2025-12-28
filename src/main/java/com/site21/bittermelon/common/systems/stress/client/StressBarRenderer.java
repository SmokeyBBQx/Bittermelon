package com.site21.bittermelon.common.systems.stress.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.stress.StressUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.contextualbar.ContextualBarRenderer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.STRESS;
import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.STRESS_RELIEF;

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
        int x = left(minecraft.getWindow());
        int y = top(minecraft.getWindow());
        int stress = player.getData(STRESS);
        int level = stress / 100;
        int width;

        if (level >= StressUtil.MAX_STRESS || stress % 100 == 0) {
            width = 182;
        } else {
            width = (stress % 100) * 182 / 100;
        }

        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, STRESS_BAR_BACKGROUND, x, y, 182, 5);

        if (width > 0) {
            int color = ARGB.color(1 - player.getData(STRESS_RELIEF), 0xFFFFFF);

            guiGraphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    getStressBar(level),
                    182, 5,
                    0, 0,
                    x, y,
                    width, 5,
                    color
            );
        }
    }

    @Contract("_ -> new")
    private @NotNull ResourceLocation getStressBar(int level) {
        return level < 1 ? STRESS_BAR_PROGRESS : Bittermelon.resource("hud/stress_bar_progress_" + level);
    }

    public void render(@NotNull GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        LocalPlayer player = minecraft.player;
        int level = player.getData(STRESS) / 100;
        Component component = Component.literal("!".repeat(level));
        int x = (guiGraphics.guiWidth() - minecraft.font.width(component)) / 2;
        int y = guiGraphics.guiHeight() - 24 - 9 - 2;
        int color = ARGB.color(1 - player.getData(STRESS_RELIEF), 0xFFFFFF);
        guiGraphics.drawString(minecraft.font, component, x, y, color, true);
    }
}
