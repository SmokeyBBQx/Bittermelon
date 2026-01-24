package com.site21.bittermelon.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.site21.bittermelon.common.systems.carry.CarryHandler;
import com.site21.bittermelon.init.neoforge.BitterAttachmentTypes;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.PlayerModelPart;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
    @Shadow
    @Final
    private EntityRenderDispatcher entityRenderDispatcher;

    @Inject(method = "renderHandsWithItems", at = @At("HEAD"), cancellable = true)
    private void onRenderHandsWithItems(float partialTick, PoseStack poseStack,
                                        MultiBufferSource.BufferSource buffer, LocalPlayer player, int packedLight, CallbackInfo ci) {
        if (player.hasData(BitterAttachmentTypes.CARRIED_PASSENGER)) {
            bittermelon$renderCarryingHands(poseStack, buffer, partialTick, packedLight, player);
            buffer.endBatch();
            ci.cancel();
        }
    }

    @Unique
    private void bittermelon$renderCarryingHands(PoseStack poseStack, MultiBufferSource buffer, float partialTick,
                                                 int packedLight, LocalPlayer player) {
        PlayerRenderer playerRenderer = (PlayerRenderer) entityRenderDispatcher.getRenderer(player);
        ResourceLocation skin = player.getSkin().texture();

        Entity carried = CarryHandler.getCarried(player);
        if (carried == null) return;

        // Make gap between hands based on carried entity width
        float entityWidth = carried.getBbWidth();
        float zRotation = entityWidth > 0 ? (entityWidth - 1.0f) * 0.2f : 0f;
        float xRotation = -2.0f;

        // Make hand rotation adjust after body rotation, not camera rotation
        float bodyYaw = player.yBodyRotO + (player.yBodyRot - player.yBodyRotO) * partialTick;
        float cameraYaw = player.getViewYRot(partialTick);
        float yawDifference = bodyYaw - cameraYaw;

        // Make hands not move up and down with camera pitch
        float cameraPitch = player.getViewXRot(partialTick);

        float yOffset = -0.4f;
        float zOffset = 0.3f;

        // Right hand
        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(cameraPitch));
        poseStack.translate(0.0f, yOffset, zOffset);
        poseStack.mulPose(Axis.YP.rotationDegrees(-yawDifference));
        poseStack.mulPose(Axis.XP.rotation(xRotation));
        poseStack.mulPose(Axis.ZP.rotation(zRotation));
        playerRenderer.renderRightHand(poseStack, buffer, packedLight, skin,
                player.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE), player);
        poseStack.popPose();

        // Left hand
        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(cameraPitch));
        poseStack.translate(0.0f, yOffset, zOffset);
        poseStack.mulPose(Axis.YP.rotationDegrees(-yawDifference));
        poseStack.mulPose(Axis.XP.rotation(xRotation));
        poseStack.mulPose(Axis.ZP.rotation(-zRotation));
        playerRenderer.renderLeftHand(poseStack, buffer, packedLight, skin,
                player.isModelPartShown(PlayerModelPart.LEFT_SLEEVE), player);
        poseStack.popPose();
    }
}
