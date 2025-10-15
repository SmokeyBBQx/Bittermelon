package com.site21.bittermelon.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.site21.bittermelon.init.neoforge.BitterAttachmentTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class TypingIndicatorRenderer {

    public static boolean canRender(Entity entity) {
        if (!(entity instanceof Player)) return true;
        return entity.getExistingDataOrNull(BitterAttachmentTypes.LAST_TYPING_TIME) != null;
    }

    public static void renderTypingIcon(@NotNull PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        String[] typingFrames = {"[.]", "[..]", "[...]"};
        int frame = (int) ((System.currentTimeMillis() / 500) % typingFrames.length);
        String icon = typingFrames[frame];

        Font font = Minecraft.getInstance().font;
        int iconWidth = font.width(icon);
        float scale = 0.02f;

        poseStack.scale(scale, -scale, scale);

        Matrix4f matrix = poseStack.last().pose();
        float centerX = -iconWidth / 2f;

        font.drawInBatch(
                icon,
                centerX, 0,
                0xFFFFFF,
                false,
                matrix,
                bufferSource,
                Font.DisplayMode.NORMAL,
                0,
                packedLight
        );

        poseStack.popPose();
    }
}
