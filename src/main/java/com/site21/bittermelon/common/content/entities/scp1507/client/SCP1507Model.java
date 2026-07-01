package com.site21.bittermelon.common.content.entities.scp1507.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class SCP1507Model extends EntityModel<SCP1507RenderState> {
    ModelPart leftLeg;
    ModelPart rightLeg;

    public SCP1507Model(@NotNull ModelPart root) {
        super(root);
        leftLeg = root.getChild("left_leg");
        rightLeg = root.getChild("right_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition root = meshdefinition.getRoot();

        root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 25).addBox(1.5F, -7.0F, -1.0F, 0.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 24.0F, 1.75F));
        root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(2, 25).addBox(2.5F, -7.0F, -1.0F, 0.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 24.0F, 1.75F));
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -11.0F, -2.25F, 5.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 10).addBox(-1.5F, -17.5F, -6.5F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 17).addBox(2.5F, -10.0F, -1.25F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(12, 17).addBox(-3.5F, -10.0F, -1.25F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(22, 8).addBox(0.5F, -7.5F, 0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(14, 10).addBox(-1.5F, -10.0F, 3.75F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(4, 25).addBox(-1.5F, -7.5F, 0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        body.addOrReplaceChild("beak", CubeListBuilder.create().texOffs(24, 22).addBox(-1.5F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -13.5F, -7.75F, -0.6545F, 0.0F, 0.0F));
        body.addOrReplaceChild("upper_neck", CubeListBuilder.create().texOffs(22, 0).addBox(-0.5F, -8.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -6.85F, -5.35F, -0.1745F, 0.0F, 0.0F));body.addOrReplaceChild("Base Neck_r1", CubeListBuilder.create().texOffs(24, 17).addBox(-0.5F, -1.9725F, 1.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -8.85F, -5.0F, -0.1745F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(@NotNull SCP1507RenderState renderState) {
        super.setupAnim(renderState);

        root.visible = renderState.deathTime == 0;
        leftLeg.visible = renderState.leftLegAttached;
        rightLeg.visible = renderState.rightLegAttached;

        if (renderState.onGround) {
            float hopHeight = 8f;
            float hopSpeed = 1.5f;

            float f = renderState.walkAnimationPos;
            float f1 = renderState.walkAnimationSpeed;
            float hopOffset = Math.abs(Mth.sin(f * hopSpeed)) * f1 * hopHeight;

            root().y -= hopOffset;
        }

        float attackTime = renderState.attackTime;
        float totalAttackDuration = 20f;
        float attackPhase = 0.5f;

        if (attackTime > 0) {
            float progress = 1 - (attackTime / totalAttackDuration);

            if (progress < attackPhase) {
                float attackProgress = progress / attackPhase;
                float easedAngle = 1 - (float)Math.pow(1 - attackProgress, 3);
                root().xRot = -easedAngle;
            } else {
                float recoveryProgress = (progress - attackPhase) / (1 - attackPhase);
                float easedAngle = (float)Math.pow(1 - recoveryProgress, 3);
                root().xRot = -easedAngle;
            }
        } else {
            root().xRot = 0;
        }
    }
}
