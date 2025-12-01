package com.site21.bittermelon.common.content.entities.scp548.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class SCP548Model extends EntityModel<SCP548RenderState> {
    private final ModelPart body;
    private final ModelPart leftFrontLeg;
    private final ModelPart leftMiddleFrontLeg;
    private final ModelPart head;
    private final ModelPart leftMiddleHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart fangs;
    private final ModelPart rightPedipalp;
    private final ModelPart leftPedipalp;
    private final ModelPart rightFrontLeg;
    private final ModelPart rightHindLeg;
    private final ModelPart rightMiddleHindLeg;
    private final ModelPart rightMiddleFrontLeg;

    protected SCP548Model(ModelPart root) {
        super(root);
        this.body = root.getChild("body");
        this.leftFrontLeg = root.getChild("left_front_leg");
        this.leftMiddleFrontLeg = root.getChild("left_middle_front_leg");
        this.head = root.getChild("head");
        this.leftMiddleHindLeg = root.getChild("left_middle_hind_leg");
        this.leftHindLeg = root.getChild("left_hind_leg");
        this.fangs = root.getChild("fangs");
        this.rightPedipalp = root.getChild("right_pedipalp");
        this.leftPedipalp = root.getChild("left_pedipalp");
        this.rightFrontLeg = root.getChild("right_front_leg");
        this.rightHindLeg = root.getChild("right_hind_leg");
        this.rightMiddleHindLeg = root.getChild("right_middle_hind_leg");
        this.rightMiddleFrontLeg = root.getChild("right_middle_front_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 20.25F, 1.0F));

        PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(0, 10).addBox(-2.0F, -1.9583F, -0.0882F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0436F, 0.0F, 0.0F));

        PartDefinition left_front_leg = partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create(), PartPose.offset(2.0F, 21.75F, -3.0F));

        PartDefinition cube_r1 = left_front_leg.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 19).addBox(-0.75F, -1.0319F, -0.9819F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.8203F, 0.3752F));

        PartDefinition left_middle_front_leg = partdefinition.addOrReplaceChild("left_middle_front_leg", CubeListBuilder.create(), PartPose.offset(2.25F, 21.5F, -1.25F));

        PartDefinition cube_r2 = left_middle_front_leg.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(18, 14).addBox(-0.727F, -1.0384F, -1.0293F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1396F, 0.3665F, 0.2967F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -2.0F, -6.0F, 5.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.25F, 1.0F));

        PartDefinition left_middle_hind_leg = partdefinition.addOrReplaceChild("left_middle_hind_leg", CubeListBuilder.create(), PartPose.offset(2.0F, 21.5F, 0.5F));

        PartDefinition cube_r3 = left_middle_hind_leg.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(18, 18).addBox(-0.477F, -1.0384F, -0.9707F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1396F, -0.3665F, 0.2967F));

        PartDefinition left_hind_leg = partdefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create(), PartPose.offset(1.5F, 22.0F, 2.25F));

        PartDefinition cube_r4 = left_hind_leg.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(18, 22).addBox(-0.5F, -1.2819F, -1.0181F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, -0.8203F, 0.3752F));

        PartDefinition fangs = partdefinition.addOrReplaceChild("fangs", CubeListBuilder.create(), PartPose.offset(0.0F, 21.1312F, -5.4856F));

        PartDefinition cube_r5 = fangs.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(18, 3).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, 1.1188F, 0.4856F, 1.309F, 0.0F, 0.0F));

        PartDefinition cube_r6 = fangs.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(24, 3).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.75F, 1.1188F, 0.4856F, 1.3526F, 0.0F, 0.0F));

        PartDefinition right_pedipalp = partdefinition.addOrReplaceChild("right_pedipalp", CubeListBuilder.create(), PartPose.offset(-1.75F, 21.6F, -4.8F));

        PartDefinition cube_r7 = right_pedipalp.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(34, 17).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.389F, 0.467F, -1.0397F, 0.5236F, 0.3927F, 0.0F));

        PartDefinition left_pedipalp = partdefinition.addOrReplaceChild("left_pedipalp", CubeListBuilder.create(), PartPose.offset(1.75F, 22.0F, -5.0F));

        PartDefinition cube_r8 = left_pedipalp.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(34, 17).mirror().addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.389F, 0.067F, -0.8397F, 0.5236F, -0.3927F, 0.0F));

        PartDefinition right_front_leg = partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, 21.75F, -3.0F));

        PartDefinition cube_r9 = right_front_leg.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 19).mirror().addBox(-5.25F, -1.0319F, -0.9819F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, -0.8203F, -0.3752F));

        PartDefinition right_hind_leg = partdefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create(), PartPose.offset(-1.5F, 22.0F, 2.25F));

        PartDefinition cube_r10 = right_hind_leg.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(18, 22).mirror().addBox(-5.5F, -1.2819F, -1.0181F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.8203F, -0.3752F));

        PartDefinition right_middle_hind_leg = partdefinition.addOrReplaceChild("right_middle_hind_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, 21.5F, 0.5F));

        PartDefinition cube_r11 = right_middle_hind_leg.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(18, 18).mirror().addBox(-5.523F, -1.0384F, -0.9707F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1396F, 0.3665F, -0.2967F));

        PartDefinition right_middle_front_leg = partdefinition.addOrReplaceChild("right_middle_front_leg", CubeListBuilder.create(), PartPose.offset(-2.25F, 21.5F, -1.25F));

        PartDefinition cube_r12 = right_middle_front_leg.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(18, 14).mirror().addBox(-5.273F, -1.0384F, -1.0293F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1396F, -0.3665F, -0.2967F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(SCP548RenderState renderState) {
        super.setupAnim(renderState);
        float f = renderState.walkAnimationPos * 0.6662F;
        float f1 = renderState.walkAnimationSpeed;
        float f2 = -(Mth.cos(f * 2.0F + 0.0F) * 0.4F) * f1;
        float f3 = -(Mth.cos(f * 2.0F + (float)Math.PI) * 0.4F) * f1;
        float f4 = -(Mth.cos(f * 2.0F + ((float)Math.PI / 2F)) * 0.4F) * f1;
        float f5 = -(Mth.cos(f * 2.0F + ((float)Math.PI * 1.5F)) * 0.4F) * f1;
        float f6 = Math.abs(Mth.sin(f + 0.0F) * 0.4F) * f1;
        float f7 = Math.abs(Mth.sin(f + (float)Math.PI) * 0.4F) * f1;
        float f8 = Math.abs(Mth.sin(f + ((float)Math.PI / 2F)) * 0.4F) * f1;
        float f9 = Math.abs(Mth.sin(f + ((float)Math.PI * 1.5F)) * 0.4F) * f1;
        ModelPart var10000 = this.rightHindLeg;
        var10000.yRot += f2;
        var10000 = this.leftHindLeg;
        var10000.yRot -= f2;
        var10000 = this.rightMiddleHindLeg;
        var10000.yRot += f3;
        var10000 = this.leftMiddleHindLeg;
        var10000.yRot -= f3;
        var10000 = this.rightMiddleFrontLeg;
        var10000.yRot += f4;
        var10000 = this.leftMiddleFrontLeg;
        var10000.yRot -= f4;
        var10000 = this.rightFrontLeg;
        var10000.yRot += f5;
        var10000 = this.leftFrontLeg;
        var10000.yRot -= f5;
        var10000 = this.rightHindLeg;
        var10000.zRot += f6;
        var10000 = this.leftHindLeg;
        var10000.zRot -= f6;
        var10000 = this.rightMiddleHindLeg;
        var10000.zRot += f7;
        var10000 = this.leftMiddleHindLeg;
        var10000.zRot -= f7;
        var10000 = this.rightMiddleFrontLeg;
        var10000.zRot += f8;
        var10000 = this.leftMiddleFrontLeg;
        var10000.zRot -= f8;
        var10000 = this.rightFrontLeg;
        var10000.zRot += f9;
        var10000 = this.leftFrontLeg;
        var10000.zRot -= f9;
        root.xScale = 0.25f;
        root.yScale = 0.25f;
        root.zScale = 0.25f;
        root.setPos(0, 18f, 0);
    }
}
