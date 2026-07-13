package com.site21.bittermelon.common.systems.carry;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.site21.bittermelon.init.neoforge.BitterAttachmentTypes;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.level.LightLayer;

public class CarryRenderer {
    public static void renderCarriedEntity(CameraRenderState cameraRenderState, PoseStack poseStack, SubmitNodeCollector collector) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;
        if (!player.hasData(BitterAttachmentTypes.CARRIED_PASSENGER)) return;
        if (mc.options.getCameraType() != CameraType.FIRST_PERSON) return;

        Identifier skin = player.getSkin().body().texturePath();

        Entity carried = CarryHandler.getCarried(player);
        if (carried == null) return;

        float entityWidth = carried.getBbWidth();
        float zRotation = entityWidth > 0 ? (entityWidth - 1.0f) * 0.2f : 0f;
        float xRotation = 105.0f;

        float partialTick = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);
        float cameraYaw = cameraRenderState.yRot;
        float angle = player.getPreciseBodyRotation(partialTick) - cameraYaw;

        float yOffset = -0.4f;
        float zOffset = -0.25f;

        BlockPos eyePos = BlockPos.containing(player.getEyePosition(partialTick));
        int block = player.isOnFire()
                ? 15
                : player.level().getBrightness(LightLayer.BLOCK, eyePos);
        int sky = player.level().getBrightness(LightLayer.SKY, eyePos);
        int lightCoords = LightCoordsUtil.pack(block, sky);
        AvatarRenderer<AbstractClientPlayer> avatarRenderer = Minecraft.getInstance().getEntityRenderDispatcher().getPlayerRenderer(player);

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-cameraYaw));
        poseStack.translate(0, yOffset, zOffset);
        poseStack.mulPose(Axis.YP.rotationDegrees(-angle));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRotation));
        poseStack.mulPose(Axis.ZP.rotation(zRotation));
        avatarRenderer.renderRightHand(
                poseStack,
                collector,
                lightCoords,
                skin,
                player.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE),
                player
        );
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-cameraYaw));
        poseStack.translate(0, yOffset, zOffset);
        poseStack.mulPose(Axis.YP.rotationDegrees(-angle));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRotation));
        poseStack.mulPose(Axis.ZP.rotation(-zRotation));
        avatarRenderer.renderLeftHand(
                poseStack,
                collector,
                lightCoords,
                skin,
                player.isModelPartShown(PlayerModelPart.LEFT_SLEEVE),
                player
        );
        poseStack.popPose();
    }
}
