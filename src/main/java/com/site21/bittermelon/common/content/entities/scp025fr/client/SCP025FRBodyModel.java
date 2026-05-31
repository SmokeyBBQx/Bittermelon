package com.site21.bittermelon.common.content.entities.scp025fr.client;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class SCP025FRBodyModel extends SCP025FRPartModel<SCP025FRPartRenderState> {
    private final ModelPart leftAnteriorLegs;
    private final ModelPart leftPosteriorLegs;
    private final ModelPart rightAnteriorLegs;
    private final ModelPart rightPosteriorLegs;

    public SCP025FRBodyModel(ModelPart root) {
        super(root);
        ModelPart leftLegs = root.getChild("left_legs");
        leftAnteriorLegs = leftLegs.getChild("left_anterior_legs");
        leftPosteriorLegs = leftLegs.getChild("left_posterior_legs");
        ModelPart rightLegs = root.getChild("right_legs");
        rightAnteriorLegs = rightLegs.getChild("right_anterior_legs");
        rightPosteriorLegs = rightLegs.getChild("right_posterior_legs");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition left_legs = partdefinition.addOrReplaceChild("left_legs", CubeListBuilder.create(), PartPose.offset(5.5F, 24.5F, -4.0F));

        PartDefinition left_anterior_legs = left_legs.addOrReplaceChild("left_anterior_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_anterior_legs_r1 = left_anterior_legs.addOrReplaceChild("left_anterior_legs_r1", CubeListBuilder.create().texOffs(22, 109).addBox(-2.0F, -2.0F, 0.0F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2182F));

        PartDefinition left_posterior_legs = left_legs.addOrReplaceChild("left_posterior_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 4.0F));

        PartDefinition left_posterior_legs_r1 = left_posterior_legs.addOrReplaceChild("left_posterior_legs_r1", CubeListBuilder.create().texOffs(22, 109).addBox(-2.0F, -2.0F, 0.0F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2182F));

        PartDefinition right_legs = partdefinition.addOrReplaceChild("right_legs", CubeListBuilder.create(), PartPose.offset(-5.5F, 24.5F, -4.0F));

        PartDefinition right_anterior_legs = right_legs.addOrReplaceChild("right_anterior_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_anterior_legs_r1 = right_anterior_legs.addOrReplaceChild("right_anterior_legs_r1", CubeListBuilder.create().texOffs(22, 109).mirror().addBox(-3.0F, -2.0F, 0.0F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2182F));

        PartDefinition right_posterior_legs = right_legs.addOrReplaceChild("right_posterior_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 4.0F));

        PartDefinition right_posterior_legs_r1 = right_posterior_legs.addOrReplaceChild("right_posterior_legs_r1", CubeListBuilder.create().texOffs(22, 109).mirror().addBox(-3.0F, -2.0F, 0.0F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2182F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 28).addBox(-4.0F, -3.0F, -5.0F, 8.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition left_carapace_r1 = body.addOrReplaceChild("left_carapace_r1", CubeListBuilder.create().texOffs(32, 87).addBox(0.0F, 0.0F, 2.0F, 6.0F, 0.0F, 10.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(3.5F, -2.5F, -7.0F, 0.0F, 0.0F, 0.0873F));

        PartDefinition right_carapace_r1 = body.addOrReplaceChild("right_carapace_r1", CubeListBuilder.create().texOffs(0, 67).addBox(-6.0F, 0.0F, -71.0F, 6.0F, 0.0F, 10.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-3.5F, -2.5F, 66.0F, 0.0F, 0.0F, -0.0873F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(SCP025FRPartRenderState renderState) {
        super.setupAnim(renderState);

        float speed = Math.min(renderState.walkAnimationSpeed, 1.0f);
        float basePhase = renderState.walkAnimationPos * 1.2f;
        float segmentLag = 0.6f;
        float phase = basePhase + renderState.index * segmentLag;
        float withinSegmentLag = 0.3f;

        animateLeg(leftAnteriorLegs, phase, speed, 1.0f);
        animateLeg(rightAnteriorLegs, phase + Mth.PI, speed, -1.0f);
        animateLeg(leftPosteriorLegs, phase - withinSegmentLag, speed, 1.0f);
        animateLeg(rightPosteriorLegs, phase + Mth.PI - withinSegmentLag, speed, -1.0f);
    }
}
