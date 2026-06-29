package com.site21.bittermelon.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.site21.bittermelon.init.neoforge.BitterAttachmentTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class TypingIndicatorRenderer {

    public static boolean canRender(Entity entity) {
        if (!(entity instanceof Player)) return true;
        return entity.getExistingDataOrNull(BitterAttachmentTypes.LAST_TYPING_TIME) != null;
    }

    public static void renderTypingIcon(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords) {
        poseStack.pushPose();

        String[] typingFrames = {"[.]", "[..]", "[...]"};
        int frame = (int) ((System.currentTimeMillis() / 500) % typingFrames.length);
        String icon = typingFrames[frame];

        Font font = Minecraft.getInstance().font;
        int iconWidth = font.width(icon);
        float scale = 0.02f;

        poseStack.scale(scale, -scale, scale);

        float centerX = -iconWidth / 2f;

        collector.submitText(
                poseStack,
                centerX, 0,
                FormattedCharSequence.forward(icon, Style.EMPTY),
                false,
                Font.DisplayMode.NORMAL,
                lightCoords,
                0xFFFFFF,
                0,
                0
        );

        poseStack.popPose();
    }
}
