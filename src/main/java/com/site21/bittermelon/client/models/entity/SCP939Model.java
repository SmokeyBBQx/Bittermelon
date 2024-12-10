package com.site21.bittermelon.client.models.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.entities.scps.SCP939.SCP939;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.html.parser.Entity;

public class SCP939Model<T extends SCP939> extends EntityModel<T> {
    private final ModelPart body;

    public SCP939Model(ModelPart root) {
        this.body = root.getChild("body");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(28, 29).addBox(0.5F, -7.0F, 12.0F, 0.0F, 5.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(40, 0).addBox(-2.0F, -3.0F, 10.0F, 5.0F, 9.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(13, 51).addBox(4.5F, 16.0F, -11.5F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(59, 51).addBox(4.0F, 21.0F, -11.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(57, 0).addBox(4.5F, 21.0F, 8.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 31).addBox(5.0F, 21.0F, 12.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 31).addBox(6.0F, 21.0F, 12.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(31, 10).addBox(7.0F, 21.0F, 12.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 31).addBox(-4.0F, 21.0F, 12.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(4, 31).addBox(-5.0F, 21.0F, 12.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(8, 31).addBox(-6.0F, 21.0F, 12.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(4, 10).addBox(-3.5F, 21.0F, -9.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(8, 10).addBox(-4.5F, 21.0F, -9.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(27, 9).addBox(-5.5F, 21.0F, -9.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(4, 9).addBox(4.5F, 21.0F, -9.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(8, 9).addBox(5.5F, 21.0F, -9.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 10).addBox(6.5F, 21.0F, -9.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(13, 51).mirror().addBox(-5.5F, 16.0F, -11.5F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(59, 51).mirror().addBox(-6.0F, 21.0F, -11.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(57, 0).mirror().addBox(-6.5F, 21.0F, 8.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 51).addBox(-1.0F, -2.0F, 17.0F, 3.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 2.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition posterior_r1 = body.addOrReplaceChild("posterior_r1", CubeListBuilder.create().texOffs(0, 34).addBox(-3.0F, -1.0F, 3.0F, 7.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -16.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition teeth_left_lower_r1 = body.addOrReplaceChild("teeth_left_lower_r1", CubeListBuilder.create().texOffs(0, 62).mirror().addBox(2.0F, -1.0F, -3.0F, 0.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 1.0F, 29.0F, -0.0436F, 0.0F, 0.4363F));

        PartDefinition teeth_right_lower_r1 = body.addOrReplaceChild("teeth_right_lower_r1", CubeListBuilder.create().texOffs(0, 62).addBox(-2.0F, -1.0F, -3.0F, 0.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 1.0F, 29.0F, -0.0436F, 0.0F, -0.4363F));

        PartDefinition teeth_front_lower_r1 = body.addOrReplaceChild("teeth_front_lower_r1", CubeListBuilder.create().texOffs(7, 76).addBox(-5.0F, -1.4063F, -4.5774F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 1.0F, 38.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition head_r1 = body.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(26, 24).addBox(-1.5F, -0.5F, -5.0F, 4.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 29.0F, -0.0436F, 0.0F, 0.0F));

        PartDefinition head_r2 = body.addOrReplaceChild("head_r2", CubeListBuilder.create().texOffs(27, 66).addBox(-1.5F, 3.2819F, -0.0048F, 4.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 24.0F, -0.0436F, 0.0F, 0.0F));

        PartDefinition teeth_right_upper_r1 = body.addOrReplaceChild("teeth_right_upper_r1", CubeListBuilder.create().texOffs(0, 57).addBox(-2.0F, -3.5F, -4.0F, 0.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 4.0F, 29.0F, -0.0436F, 0.0F, 0.4363F));

        PartDefinition teeth_left_upper_r1 = body.addOrReplaceChild("teeth_left_upper_r1", CubeListBuilder.create().texOffs(0, 57).mirror().addBox(2.0F, -3.5F, -4.0F, 0.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 4.0F, 29.0F, -0.0436F, 0.0F, -0.4363F));

        PartDefinition teeth_front_upper_r1 = body.addOrReplaceChild("teeth_front_upper_r1", CubeListBuilder.create().texOffs(7, 73).addBox(-5.0F, -4.4063F, -5.4226F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 7.0F, 38.0F, -0.4363F, 0.0F, 0.0F));

        PartDefinition left_arm_lower_r1 = body.addOrReplaceChild("left_arm_lower_r1", CubeListBuilder.create().texOffs(27, 62).mirror().addBox(1.0F, -2.0F, -4.0F, 2.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(27, 62).addBox(12.0F, -2.0F, -4.0F, 2.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 12.0F, 10.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition left_arm_upper_r1 = body.addOrReplaceChild("left_arm_upper_r1", CubeListBuilder.create().texOffs(55, 58).mirror().addBox(1.0F, -5.0F, -5.0F, 3.0F, 13.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, 3.0F, 11.0F, -0.0436F, 0.1745F, 0.1309F));

        PartDefinition left_leg_lower_r1 = body.addOrReplaceChild("left_leg_lower_r1", CubeListBuilder.create().texOffs(27, 0).mirror().addBox(1.0F, -4.0F, -4.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(27, 0).addBox(11.0F, -4.0F, -4.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 15.0F, -6.0F, -0.48F, 0.0F, 0.0F));

        PartDefinition left_leg_upper_r1 = body.addOrReplaceChild("left_leg_upper_r1", CubeListBuilder.create().texOffs(41, 46).mirror().addBox(0.0F, -5.0F, -5.0F, 4.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, 5.0F, -6.0F, 0.0436F, -0.1309F, 0.0873F));

        PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 9).addBox(2.0F, -1.0F, -9.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 22.0F, 18.0F, 0.0F, -0.4363F, 0.0F));

        PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(9, 0).addBox(1.5F, -1.0F, -9.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 22.0F, -2.0F, 0.0F, -0.4363F, 0.0F));

        PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(27, 10).addBox(-1.5F, -1.0F, -9.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 22.0F, -2.0F, 0.0F, 0.4363F, 0.0F));

        PartDefinition cube_r4 = body.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(31, 9).addBox(3.0F, -4.0F, -3.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 25.0F, 15.0F, 0.0F, 0.4363F, 0.0F));

        PartDefinition right_leg_upper_r1 = body.addOrReplaceChild("right_leg_upper_r1", CubeListBuilder.create().texOffs(41, 46).addBox(-4.0F, -5.0F, -5.0F, 4.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 5.0F, -6.0F, 0.1309F, 0.1309F, -0.0873F));

        PartDefinition right_arm_upper_r1 = body.addOrReplaceChild("right_arm_upper_r1", CubeListBuilder.create().texOffs(55, 58).addBox(-4.0F, -5.0F, -5.0F, 3.0F, 13.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 3.0F, 11.0F, -0.0436F, -0.1745F, -0.1309F));

        PartDefinition abdomen_r1 = body.addOrReplaceChild("abdomen_r1", CubeListBuilder.create().texOffs(44, 16).addBox(-3.0F, -2.0F, -6.0F, 7.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-3.0F, -3.0F, -2.0F, 7.0F, 11.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition back_spikes_r1 = body.addOrReplaceChild("back_spikes_r1", CubeListBuilder.create().texOffs(36, 41).addBox(-3.5F, 4.0F, 0.0F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -9.0F, -12.0F, 0.0436F, 0.0F, 0.0F));

        PartDefinition mid_spikes_r1 = body.addOrReplaceChild("mid_spikes_r1", CubeListBuilder.create().texOffs(0, 6).addBox(-3.5F, 0.0F, -7.0F, 0.0F, 9.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -11.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }




    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        body.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
