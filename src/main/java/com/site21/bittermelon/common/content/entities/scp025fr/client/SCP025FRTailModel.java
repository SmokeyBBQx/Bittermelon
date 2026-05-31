package com.site21.bittermelon.common.content.entities.scp025fr.client;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class SCP025FRTailModel extends SCP025FRPartModel<SCP025FRPartRenderState> {
    private final ModelPart rightBackLegs;
    private final ModelPart rightBackMiddleLegs;
    private final ModelPart rightFrontMiddleLegs;
    private final ModelPart rightFrontLegs;
    private final ModelPart leftBackLegs;
    private final ModelPart leftBackMiddleLegs;
    private final ModelPart leftFrontMiddleLegs;
    private final ModelPart leftFrontLegs;

    public SCP025FRTailModel(ModelPart root) {
        super(root);
        ModelPart rightLegs = root.getChild("right_legs");
        this.rightBackLegs = rightLegs.getChild("right_back_legs");
        this.rightBackMiddleLegs = rightLegs.getChild("right_back_middle_legs");
        this.rightFrontMiddleLegs = rightLegs.getChild("right_front_middle_legs");
        this.rightFrontLegs = rightLegs.getChild("right_front_legs");
        ModelPart leftLegs = root.getChild("left_legs");
        this.leftBackLegs = leftLegs.getChild("left_back_legs");
        this.leftBackMiddleLegs = leftLegs.getChild("left_back_middle_legs");
        this.leftFrontMiddleLegs = leftLegs.getChild("left_front_middle_legs");
        this.leftFrontLegs = leftLegs.getChild("left_front_legs");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition right_legs = partdefinition.addOrReplaceChild("right_legs", CubeListBuilder.create(), PartPose.offset(-5.5F, 24.5F, 4.0F));

        PartDefinition right_back_legs = right_legs.addOrReplaceChild("right_back_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_back_legs_r1 = right_back_legs.addOrReplaceChild("right_back_legs_r1", CubeListBuilder.create().texOffs(22, 109).mirror().addBox(-3.0F, -2.0F, 0.0F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2182F));

        PartDefinition right_back_middle_legs = right_legs.addOrReplaceChild("right_back_middle_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -4.0F));

        PartDefinition right_back_middle_legs_r1 = right_back_middle_legs.addOrReplaceChild("right_back_middle_legs_r1", CubeListBuilder.create().texOffs(22, 109).mirror().addBox(-3.0F, -2.0F, 0.0F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2182F));

        PartDefinition right_front_middle_legs = right_legs.addOrReplaceChild("right_front_middle_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -8.0F));

        PartDefinition right_front_middle_legs_r1 = right_front_middle_legs.addOrReplaceChild("right_front_middle_legs_r1", CubeListBuilder.create().texOffs(22, 109).mirror().addBox(-3.0F, -2.0F, 0.0F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2182F));

        PartDefinition right_front_legs = right_legs.addOrReplaceChild("right_front_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -12.0F));

        PartDefinition right_front_legs_r1 = right_front_legs.addOrReplaceChild("right_front_legs_r1", CubeListBuilder.create().texOffs(22, 109).mirror().addBox(-3.0F, -2.0F, 0.0F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2182F));

        PartDefinition left_legs = partdefinition.addOrReplaceChild("left_legs", CubeListBuilder.create(), PartPose.offset(5.5F, 24.5F, 4.0F));

        PartDefinition left_back_legs = left_legs.addOrReplaceChild("left_back_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_back_legs_r1 = left_back_legs.addOrReplaceChild("left_back_legs_r1", CubeListBuilder.create().texOffs(22, 109).addBox(-2.0F, -2.0F, 0.0F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2182F));

        PartDefinition left_back_middle_legs = left_legs.addOrReplaceChild("left_back_middle_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -4.0F));

        PartDefinition left_back_middle_legs_r1 = left_back_middle_legs.addOrReplaceChild("left_back_middle_legs_r1", CubeListBuilder.create().texOffs(22, 109).addBox(-2.0F, -2.0F, 0.0F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2182F));

        PartDefinition left_front_middle_legs = left_legs.addOrReplaceChild("left_front_middle_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -8.0F));

        PartDefinition left_front_middle_legs_r1 = left_front_middle_legs.addOrReplaceChild("left_front_middle_legs_r1", CubeListBuilder.create().texOffs(22, 109).addBox(-2.0F, -2.0F, 0.0F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2182F));

        PartDefinition left_front_legs = left_legs.addOrReplaceChild("left_front_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -12.0F));

        PartDefinition left_front_legs_r1 = left_front_legs.addOrReplaceChild("left_front_legs_r1", CubeListBuilder.create().texOffs(22, 109).addBox(-2.0F, -2.0F, 0.0F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2182F));

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(40, 13).addBox(-4.0F, -3.0F, -7.0F, 8.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(108, 8).addBox(-3.0F, -3.0F, 3.0F, 6.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 14).addBox(0.0F, 0.0F, 72.0F, 6.0F, 0.0F, 14.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(2.5F, -2.5F, -79.0F, 0.0F, 0.0F, 0.0873F));

        PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, 0.0F, -1.0F, 6.0F, 0.0F, 14.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-2.5F, -2.5F, -6.0F, 0.0F, 0.0F, -0.0873F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(SCP025FRPartRenderState renderState) {
        super.setupAnim(renderState);

        float speed = Math.min(renderState.walkAnimationSpeed, 1.0f);
        float basePhase = renderState.walkAnimationPos * 1.2f;
        float segmentLag = 0.6f;
        float withinSegmentLag = 0.3f;
        int index = renderState.index;

        float phase1 = basePhase + index * segmentLag;
        animateLeg(leftFrontMiddleLegs, phase1, speed, 1.0f);
        animateLeg(rightFrontMiddleLegs, phase1 + Mth.PI, speed, -1.0f);
        animateLeg(leftBackMiddleLegs, phase1 - withinSegmentLag, speed, 1.0f);
        animateLeg(rightBackMiddleLegs, phase1 + Mth.PI - withinSegmentLag, speed, -1.0f);

        float phase2 = basePhase + (index + 1) * segmentLag;
        animateLeg(leftFrontLegs, phase2, speed, 1.0f);
        animateLeg(rightFrontLegs, phase2 + Mth.PI, speed, -1.0f);
        animateLeg(leftBackLegs, phase2 - withinSegmentLag, speed, 1.0f);
        animateLeg(rightBackLegs, phase2 + Mth.PI - withinSegmentLag, speed, -1.0f);
    }
}
