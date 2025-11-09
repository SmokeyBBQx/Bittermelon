package com.site21.bittermelon.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.site21.bittermelon.Bittermelon.shouldDisplayText;
import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.LORE_OPENING;

@EventBusSubscriber(value = Dist.CLIENT)
public class LoreOpeningOverlay {
    public static long displayStartTime;
    private static final int DISPLAY_DURATION = 10000;
    private static final int FADE_DURATION = 500;
    private static final float TITLE_SCALE = 1.2f;
    private static final float TEXT_SCALE = 1.1f;

    @SubscribeEvent
    public static void onRenderGUI(RenderGuiEvent.Post event) {
        if (!shouldDisplayText) return;

        long elapsed = System.currentTimeMillis() - displayStartTime;
        if (elapsed >= DISPLAY_DURATION + FADE_DURATION) {
            shouldDisplayText = false;
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        String text = mc.level != null ? mc.level.getExistingData(LORE_OPENING).orElse("") : "";

        int centerX = mc.getWindow().getGuiScaledWidth() / 2;
        int centerY = mc.getWindow().getGuiScaledHeight() / 3;
        int alpha = (int) (calculateAlpha(elapsed) * 255) << 24;

        GuiGraphics graphics = event.getGuiGraphics();
        graphics.drawCenteredString(mc.font, getFormattedDate(), centerX, centerY, 0xFFAA00 | alpha);
        graphics.drawCenteredString(mc.font, Component.literal(text), centerX, centerY + 20, 0xFFFFFF | alpha);
    }

    private static float calculateAlpha(long elapsed) {
        if (elapsed < FADE_DURATION) return elapsed / (float) FADE_DURATION;
        if (elapsed > DISPLAY_DURATION) return 1.0f - ((elapsed - DISPLAY_DURATION) / (float) FADE_DURATION);
        return 1.0f;
    }

    private static @NotNull String getFormattedDate() {
        LocalDateTime now = LocalDateTime.now();
        int day = now.getDayOfMonth();
        String suffix = (day >= 11 && day <= 13) ? "th" :
                switch (day % 10) {
                    case 1 -> "st";
                    case 2 -> "nd";
                    case 3 -> "rd";
                    default -> "th";
                };
        return now.format(DateTimeFormatter.ofPattern("d'" + suffix + "' 'of' MMMM, yyyy"));
    }

}
