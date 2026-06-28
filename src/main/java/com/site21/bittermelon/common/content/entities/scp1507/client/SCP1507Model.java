package com.site21.bittermelon.common.content.entities.scp1507.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class SCP1507Model extends EntityModel<SCP1507RenderState> {

    public SCP1507Model(@NotNull ModelPart root) {
        super(root);
    }

    public static @NotNull LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 2).addBox(0.0F, -1.5F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 5.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(10, 14).addBox(-2.0F, -11.5F, -7.5F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 18).addBox(2.0F, -5.0F, -2.0F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(20, 16).addBox(-4.0F, -5.0F, -2.0F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(22, 23).addBox(0.5F, -1.0F, -1.0F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 23).addBox(-1.5F, -1.0F, -1.0F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 11).addBox(-2.0F, -6.0F, 3.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.0F, -1.5F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 19.0F, 0.75F));

        PartDefinition Beak_r1 = body.addOrReplaceChild("Beak_r1", CubeListBuilder.create().texOffs(16, 0).addBox(-1.5F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.5F, -8.75F, -0.6545F, 0.0F, 0.0F));

        PartDefinition UpperNeck_r1 = body.addOrReplaceChild("UpperNeck_r1", CubeListBuilder.create().texOffs(12, 21).addBox(-0.5F, -6.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(17, 6).addBox(-0.5F, -2.0F, -1.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -2.75F, -6.0F, -0.1745F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(@NotNull SCP1507RenderState renderState) {
        super.setupAnim(renderState);

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
