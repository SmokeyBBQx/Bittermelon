package com.site21.bittermelon.common.content.entities.chicken.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class ChickenModel extends EntityModel<ChickenRenderState> {
    private final ModelPart head;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public ChickenModel(@NotNull ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.rightWing = root.getChild("right_wing");
        this.leftWing = root.getChild("left_wing");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
    }

    public static @NotNull LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(16, 3).addBox(0.0F, -2.2F, -2.6828F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 13).addBox(-0.5F, -3.2F, -1.6828F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(14, 0).addBox(-0.5F, -4.2F, -1.6828F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.offset(-0.5F, 18.2F, -2.3172F));

        PartDefinition head_fluff_extra_2_r1 = head.addOrReplaceChild("head_fluff_extra_2_r1", CubeListBuilder.create().texOffs(14, 12).addBox(-4.2426F, -10.5F, -1.4142F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 5.8F, 2.3172F, 0.0F, -0.7854F, 0.0F));

        PartDefinition head_fluff_extra_1_r1 = head.addOrReplaceChild("head_fluff_extra_1_r1", CubeListBuilder.create().texOffs(6, 11).addBox(0.7071F, -10.5F, -4.9497F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 5.8F, 2.3172F, 0.0F, 0.7854F, 0.0F));

        PartDefinition right_wing = partdefinition.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 18.0F, -1.0F));

        PartDefinition left_wing = partdefinition.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(14, 16).addBox(0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 18.0F, -1.0F));

        PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 0).addBox(0.25F, 0.25F, 0.5F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(12, 3).addBox(-0.75F, 1.25F, -1.5F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.75F, 22.75F, -0.5F));

        PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 1).addBox(-0.25F, 0.25F, 0.5F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 4).addBox(-0.25F, 1.25F, -1.5F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.75F, 22.75F, -0.5F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 10).addBox(-2.0F, -4.0F, 4.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 10).addBox(-2.0F, -4.0F, 6.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(19, 19).addBox(-1.5F, 1.0F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(18, 16).addBox(0.5F, 1.0F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.0F, -3.0F));

        PartDefinition left_leg_fluff_2_r1 = body.addOrReplaceChild("left_leg_fluff_2_r1", CubeListBuilder.create().texOffs(8, 16).addBox(-1.4142F, -2.0F, 0.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(4, 17).addBox(-2.8284F, -2.0F, 1.4142F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(8, 11).addBox(0.0F, -7.5F, 1.4142F, 0.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(8, 12).addBox(-2.1213F, -6.5F, -0.7071F, 0.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 3.0F, 3.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition left_leg_fluff_1_r1 = body.addOrReplaceChild("left_leg_fluff_1_r1", CubeListBuilder.create().texOffs(16, 13).addBox(-0.7071F, -2.0F, -2.1213F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 17).addBox(-2.1213F, -2.0F, -3.5355F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(6, 10).addBox(-3.5355F, -7.5F, -2.1213F, 0.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(8, 13).addBox(-1.4142F, -6.5F, -4.2426F, 0.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 3.0F, 3.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition tail_back_fluff_2_r1 = body.addOrReplaceChild("tail_back_fluff_2_r1", CubeListBuilder.create().texOffs(10, 18).addBox(2.1213F, -6.9497F, 4.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(12, 17).addBox(0.7071F, -5.5355F, -3.5F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 3.0F, 3.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition tail_back_fluff_1_r1 = body.addOrReplaceChild("tail_back_fluff_1_r1", CubeListBuilder.create().texOffs(0, 19).addBox(-5.6569F, -3.4142F, 4.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 18).addBox(-4.2426F, -2.0F, -3.5F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 3.0F, 3.0F, 0.0F, 0.0F, 0.7854F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(@NotNull ChickenRenderState renderState) {
        super.setupAnim(renderState);
        float f = (Mth.sin(renderState.flap) + 1.0F) * renderState.flapSpeed;
        head.xRot = renderState.xRot * (float) (Math.PI / 180.0);
        head.yRot = renderState.yRot * (float) (Math.PI / 180.0);
        float f1 = renderState.walkAnimationSpeed;
        float f2 = renderState.walkAnimationPos;
        rightLeg.xRot = Mth.cos(f2 * 0.6662F) * 1.4F * f1;
        leftLeg.xRot = Mth.cos(f2 * 0.6662F + (float) Math.PI) * 1.4F * f1;
        rightWing.zRot = f;
        leftWing.zRot = -f;
    }
}
