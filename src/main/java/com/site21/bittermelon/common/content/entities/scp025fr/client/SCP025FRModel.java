package com.site21.bittermelon.common.content.entities.scp025fr.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class SCP025FRModel extends EntityModel<LivingEntityRenderState> {
    private final ModelPart leftAntenna;
    private final ModelPart leftEye;
    private final ModelPart rightAntenna;
    private final ModelPart rightMandible;
    private final ModelPart leftMandible;
    private final ModelPart rightEye;

    protected SCP025FRModel(ModelPart root) {
        super(root);
        this.leftAntenna = root.getChild("left_antenna");
        this.leftEye = root.getChild("left_eye");
        this.rightAntenna = root.getChild("right_antenna");
        this.rightMandible = root.getChild("right_mandible");
        this.leftMandible = root.getChild("left_mandible");
        this.rightEye = root.getChild("right_eye");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition left_antenna = partdefinition.addOrReplaceChild("left_antenna", CubeListBuilder.create().texOffs(54, 108).addBox(-4.0F, 0.0F, -1.0F, 5.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(6.5F, 21.5F, -4.0F));

        PartDefinition left_eye = partdefinition.addOrReplaceChild("left_eye", CubeListBuilder.create().texOffs(40, 26).addBox(-0.6F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(4.1F, 21.8F, 0.5F));

        PartDefinition right_antenna = partdefinition.addOrReplaceChild("right_antenna", CubeListBuilder.create().texOffs(34, 108).addBox(-4.0F, 0.0F, -1.0F, 5.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 21.5F, -4.0F));

        PartDefinition right_mandible = partdefinition.addOrReplaceChild("right_mandible", CubeListBuilder.create().texOffs(92, 108).addBox(-2.0F, 0.5F, -1.0F, 3.0F, 0.0F, 5.0F, new CubeDeformation(0.01F)), PartPose.offset(-1.5F, 22.5F, -3.0F));

        PartDefinition left_mandible = partdefinition.addOrReplaceChild("left_mandible", CubeListBuilder.create().texOffs(108, 108).addBox(-2.0F, 0.5F, -1.0F, 3.0F, 0.0F, 5.0F, new CubeDeformation(0.01F)), PartPose.offset(2.5F, 22.5F, -3.0F));

        PartDefinition right_eye = partdefinition.addOrReplaceChild("right_eye", CubeListBuilder.create().texOffs(46, 26).addBox(-1.4F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.1F, 21.8F, 0.5F));

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(108, 0).addBox(-4.0F, -3.0F, -1.0F, 8.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(22, 109).addBox(-2.0F, -2.0F, 0.0F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, 0.5F, 1.0F, 0.0F, 0.0F, 0.2182F));

        PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(22, 109).mirror().addBox(-3.0F, -2.0F, 0.0F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.5F, 0.5F, 1.0F, 0.0F, 0.0F, -0.2182F));

        PartDefinition cube_r3 = bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(108, 16).addBox(0.0F, 0.0F, -1.0F, 6.0F, 0.0F, 3.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(3.5F, -2.5F, 2.0F, 0.0F, 0.0F, 0.0873F));

        PartDefinition cube_r4 = bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(74, 108).addBox(-6.0F, 0.0F, -74.0F, 6.0F, 0.0F, 3.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-3.5F, -2.5F, 75.0F, 0.0F, 0.0F, -0.0873F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(LivingEntityRenderState renderState) {
        super.setupAnim(renderState);

        float age = renderState.ageInTicks;


    }
}
