package com.site21.bittermelon.common.systems.component.screwdriver;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

public class ScrewdriverUseAnimation implements IClientItemExtensions {
    private static final float HAND_OFFSET_X = 0.56f;
    private static final float HAND_OFFSET_Y = -0.52f;
    private static final float HAND_OFFSET_Z = -0.72f;
    private static final float EQUIP_Y_OFFSET = -0.6f;

    private static final float ANIMATION_SPEED = 0.4f;
    private static final float ANIMATION_AMPLITUDE = 0.5f;
    private static final float ANIMATION_OFFSET = 0.5f;

    private static final float POSITION_OFFSET_X = -0.25f;
    private static final float POSITION_OFFSET_Y = 0.22f;
    private static final float POSITION_OFFSET_Z = 0.35f;

    private static final float BASE_ROTATION_X = -90.0f;
    private static final float BASE_ROTATION_Y = -90.0f;
    private static final float MIN_ROTATION_Y = -15.0f;
    private static final float MAX_ROTATION_Y_RANGE = 75.0f;

    @Override
    public boolean applyForgeHandTransform(@NotNull PoseStack poseStack, @NotNull LocalPlayer player, @NotNull HumanoidArm arm,
                                           @NotNull ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
        if (itemInHand.getUseAnimation() != Screwdriver.SCREW_ANIMATION || !player.isUsingItem()) return false;
        int armDirection = arm == HumanoidArm.RIGHT ? 1 : -1;
        poseStack.translate(armDirection * HAND_OFFSET_X, HAND_OFFSET_Y + equipProcess * EQUIP_Y_OFFSET, HAND_OFFSET_Z);

        float usageTicks = player.getTicksUsingItem() + partialTick;
        float animationProgress = Mth.sin(usageTicks * ANIMATION_SPEED) * ANIMATION_AMPLITUDE + ANIMATION_OFFSET;
        float additionalRotationY = MIN_ROTATION_Y + MAX_ROTATION_Y_RANGE * animationProgress;

        if (arm == HumanoidArm.RIGHT) {
            poseStack.translate(POSITION_OFFSET_X, POSITION_OFFSET_Y, POSITION_OFFSET_Z);
            poseStack.mulPose(Axis.XP.rotationDegrees(BASE_ROTATION_X));
            poseStack.mulPose(Axis.YP.rotationDegrees(BASE_ROTATION_Y + additionalRotationY * animationProgress));
        } else {
            poseStack.translate(-POSITION_OFFSET_X, POSITION_OFFSET_Y, POSITION_OFFSET_Z);
            poseStack.mulPose(Axis.XP.rotationDegrees(BASE_ROTATION_X));
            poseStack.mulPose(Axis.YP.rotationDegrees(-(BASE_ROTATION_Y + additionalRotationY * animationProgress)));
        }

        return true;
    }

    @Override
    public HumanoidModel.ArmPose getArmPose(@NotNull LivingEntity entity, @NotNull InteractionHand hand, @NotNull ItemStack stack) {
        return (stack.getUseAnimation() == Screwdriver.SCREW_ANIMATION && entity.isUsingItem())
                ? ScrewdriverPose.SCREW_POSE.getValue() : null;
    }
}
