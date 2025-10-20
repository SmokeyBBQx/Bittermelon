package com.site21.bittermelon.common.systems.component.screwdriver;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;

public class ScrewdriverPose {
    public static final EnumProxy<HumanoidModel.ArmPose> SCREW_POSE = new EnumProxy<>(
            HumanoidModel.ArmPose.class,
            false,
            (IArmPoseTransformer) ScrewdriverPose::applyCustomModelPose
    );

    private static void applyCustomModelPose(HumanoidModel<?> model, HumanoidRenderState state, HumanoidArm arm) {
        if (arm == HumanoidArm.RIGHT) {
            model.rightArm.xRot = -1f;
        } else {
            model.leftArm.xRot = -1f;
        }
    }
}
