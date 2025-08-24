package com.site21.bittermelon.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.init.neoforge.BitterAttachmentTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.common.util.TriState;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class TypingIconRenderer {
    @SubscribeEvent
    public static void onRenderNameTag(@NotNull RenderNameTagEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        event.setCanRender(TriState.FALSE);
        if (Minecraft.getInstance().options.hideGui) return;

        if (player.getExistingDataOrNull(BitterAttachmentTypes.LAST_TYPING_TIME) != null) {
            renderTypingIcon(player, event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), event.getPartialTick());
        }
    }

    private static void renderTypingIcon(@NotNull Player player, @NotNull PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float partialTick) {
        Minecraft mc = Minecraft.getInstance();

        double distanceSqr = mc.getEntityRenderDispatcher().distanceToSqr(player);
        if (!ClientHooks.isNameplateInRenderDistance(player, distanceSqr)) {
            return;
        }

        Vec3 nameTagPos = player.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, player.getViewYRot(partialTick));
        if (nameTagPos == null) {
            return;
        }

        poseStack.pushPose();

        String[] typingFrames = {"[.]", "[..]", "[...]"};
        int frame = (int) ((System.currentTimeMillis() / 500) % typingFrames.length);
        String icon = typingFrames[frame];

        Font font = mc.font;
        int iconWidth = font.width(icon);
        float scale = 0.02f;

        poseStack.translate(nameTagPos.x, nameTagPos.y + 0.5 - 0.2, nameTagPos.z);
        poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
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
