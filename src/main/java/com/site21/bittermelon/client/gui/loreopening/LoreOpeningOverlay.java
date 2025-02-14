package com.site21.bittermelon.client.gui.loreopening;

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
import java.util.Objects;

import static com.site21.bittermelon.Bittermelon.shouldDisplayText;

@EventBusSubscriber(value = Dist.CLIENT)
public class LoreOpeningOverlay {
    public static long displayStartTime;
    private static final int DISPLAY_DURATION = 10000;
    private static final int FADE_DURATION = 500;
    private static final float TITLE_SCALE = 1.2f;
    private static final float TEXT_SCALE = 1.1f;

    @SubscribeEvent
    public static void onRenderGUI(RenderGuiEvent.Post event) {
        if (shouldDisplayText) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - displayStartTime;

            if (elapsedTime >= DISPLAY_DURATION + FADE_DURATION) {
                shouldDisplayText = false;
                return;
            }

            GuiGraphics guiGraphics = event.getGuiGraphics();
            Minecraft minecraft = Minecraft.getInstance();

            String title = getRealWorldDate();
            String text = "";

            if (minecraft.level != null && minecraft.level.isClientSide) {
                ServerLevel serverLevel = Objects.requireNonNull(minecraft.getSingleplayerServer()).getLevel(Level.OVERWORLD);
                assert serverLevel != null;
                LoreOpeningData storage = LoreOpeningData.get(serverLevel);
                 text = storage.getMessage();
            }

            int width = minecraft.getWindow().getGuiScaledWidth();
            int height = minecraft.getWindow().getGuiScaledHeight();

            float alpha = getAlpha(elapsedTime);

            alpha = Math.max(0.0f, Math.min(1.0f, alpha));
            int alphaInt = (int) (alpha * 255);

            int x = width / 2;
            int y = height / 3;

            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            poseStack.scale(TITLE_SCALE, TITLE_SCALE, TITLE_SCALE);
            int scaledTitleX = (int) (x / TITLE_SCALE);
            int scaledTitleY = (int) (y / TITLE_SCALE);

            guiGraphics.drawCenteredString(
                    minecraft.font,
                    title,
                    scaledTitleX,
                    scaledTitleY,
                    0xFFAA00 | (alphaInt << 24)
            );
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.scale(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);
            int scaledTextX = (int) (x / TEXT_SCALE);
            int scaledTextY = (int) ((y + 2 * TITLE_SCALE) / TEXT_SCALE);

            int maxWidth = (int) ((width * 0.5) / TEXT_SCALE);
            int textX = scaledTextX - (maxWidth / 2);

            guiGraphics.drawWordWrap(
                    minecraft.font,
                    Component.literal(text),
                    textX + 5,
                    scaledTextY + 15,
                    maxWidth,
                    0xFFFFFF | (alphaInt << 24)
            );

            poseStack.popPose();
        }
    }

    private static float getAlpha(long elapsedTime) {
        float alpha;
        if (elapsedTime < FADE_DURATION) {
            // Fade in
            alpha = elapsedTime / (float) FADE_DURATION;
        } else if (elapsedTime > DISPLAY_DURATION) {
            // Fade out
            alpha = 1.0f - ((elapsedTime - DISPLAY_DURATION) / (float) FADE_DURATION);
        } else {
            alpha = 1.0f;
        }
        return alpha;
    }

    public static @NotNull String getRealWorldDate() {
        LocalDateTime now = LocalDateTime.now();
        int day = now.getDayOfMonth();
        String month = now.getMonth().toString().charAt(0) +
                now.getMonth().toString().substring(1).toLowerCase();
        String year = String.format(String.valueOf(now.getYear()));

        String suffix;
        if (day >= 11 && day <= 13) {
            suffix = "th";
        } else {
            suffix = switch (day % 10) {
                case 1 -> "st";
                case 2 -> "nd";
                case 3 -> "rd";
                default -> "th";
            };
        }

        return String.format("%d%s of %s, %s", day, suffix, month, year);
    }

}
