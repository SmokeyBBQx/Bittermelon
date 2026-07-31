package com.site21.bittermelon.common.content.entities.scp250.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class SCP250Model extends EntityModel<SCP250RenderState> {
    private final ModelPart bb_main;

    public SCP250Model(ModelPart root) {
        super(root);
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -45.0F, -43.0F, 6.0F, 4.0F, 53.0F, new CubeDeformation(0.0F))
                .texOffs(108, 57).addBox(-8.0F, -44.0F, -42.0F, 16.0F, 18.0F, 37.0F, new CubeDeformation(-0.003F))
                .texOffs(118, 37).addBox(-3.5F, -52.0F, -84.0F, 7.0F, 8.0F, 12.0F, new CubeDeformation(-0.001F))
                .texOffs(208, 154).addBox(-3.5F, -44.0F, -72.0F, 7.0F, 4.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(0, 57).addBox(0.0F, -47.0F, -42.0F, 0.0F, 2.0F, 54.0F, new CubeDeformation(0.0F))
                .texOffs(196, 112).addBox(-4.5F, -44.0F, -5.0F, 9.0F, 10.0F, 17.0F, new CubeDeformation(-0.001F))
                .texOffs(108, 112).addBox(-3.0F, -45.0F, 10.0F, 6.0F, 4.0F, 38.0F, new CubeDeformation(0.0F))
                .texOffs(142, 154).addBox(-2.5F, -45.0F, 48.0F, 5.0F, 4.0F, 28.0F, new CubeDeformation(0.0F))
                .texOffs(156, 37).addBox(-2.0F, -45.0F, 76.0F, 4.0F, 4.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(106, 192).addBox(-1.5F, -45.0F, 91.0F, 3.0F, 4.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(70, 154).addBox(0.0F, -47.0F, 12.0F, 0.0F, 2.0F, 36.0F, new CubeDeformation(0.0F))
                .texOffs(0, 151).addBox(0.0F, -41.0F, 13.0F, 0.0F, 6.0F, 35.0F, new CubeDeformation(0.0F))
                .texOffs(142, 186).addBox(0.0F, -41.0F, 48.0F, 0.0F, 4.0F, 28.0F, new CubeDeformation(0.0F))
                .texOffs(198, 200).addBox(0.0F, -41.0F, 76.0F, 0.0F, 2.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(82, 135).addBox(0.0F, -41.0F, 91.0F, 0.0F, 1.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(0, 113).addBox(3.0F, -43.0F, 10.0F, 3.0F, 0.0F, 38.0F, new CubeDeformation(0.05F))
                .texOffs(118, 0).addBox(-6.0F, -43.0F, 11.0F, 3.0F, 0.0F, 37.0F, new CubeDeformation(0.05F))
                .texOffs(198, 23).addBox(2.0F, -43.0F, 48.0F, 3.0F, 0.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(198, 186).addBox(-5.0F, -43.0F, 48.0F, 3.0F, 0.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(194, 37).addBox(-4.1F, -53.0F, -72.0F, 8.0F, 9.0F, 9.0F, new CubeDeformation(0.05F))
                .texOffs(196, 139).addBox(-3.0F, -44.0F, -83.8F, 6.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(160, 218).addBox(2.5F, -46.0F, -83.5F, 0.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(178, 218).addBox(-2.5F, -46.0F, -83.5F, 0.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(70, 151).addBox(-2.5F, -46.0F, -83.5F, 5.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(214, 76).addBox(3.5F, -44.0F, -83.9F, 0.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(194, 55).addBox(-3.5F, -44.0F, -83.9F, 7.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(214, 90).addBox(-3.5F, -44.0F, -83.9F, 0.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(232, 50).addBox(6.0F, -3.0F, -5.1415F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(228, 213).addBox(6.0F, -8.0F, 5.0F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(228, 223).addBox(-6.0F, -8.0F, 5.0F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(232, 58).addBox(-6.0F, -3.0F, -5.1415F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(208, 167).addBox(-1.0F, -27.0F, -7.0F, 2.0F, 3.0F, 12.0F, new CubeDeformation(0.05F))
                .texOffs(82, 149).addBox(3.0F, -2.0F, -1.0F, 6.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(214, 104).addBox(-9.0F, -2.0F, -1.0F, 6.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bigTailBone_r1 = bb_main.addOrReplaceChild("Big Tail Bone_r1", CubeListBuilder.create().texOffs(20, 219).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 14.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -39.0F, 8.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition tailUnderBone_r1 = bb_main.addOrReplaceChild("Tail Under Bone_r1", CubeListBuilder.create().texOffs(198, 217).addBox(-1.0F, 5.0F, -4.0F, 2.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -44.0F, 8.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition rightClaw2_r1 = bb_main.addOrReplaceChild("Right Claw (2)_r1", CubeListBuilder.create().texOffs(86, 225).addBox(-1.0F, -7.0F, -1.0F, 0.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -12.0F, -50.0F, -1.6325F, 0.7844F, -1.6144F));

        PartDefinition rightClaw3_r1 = bb_main.addOrReplaceChild("Right Claw (3)_r1", CubeListBuilder.create().texOffs(74, 225).addBox(0.2554F, -0.7249F, -0.7819F, 0.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -15.0F, -43.0F, -1.6353F, 0.828F, -1.6184F));

        PartDefinition rightClaw1_r1 = bb_main.addOrReplaceChild("Right Claw (1)_r1", CubeListBuilder.create().texOffs(228, 200).addBox(-3.3818F, -2.4715F, -2.9545F, 0.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -15.0F, -46.0F, -1.6353F, 0.828F, -1.6184F));

        PartDefinition leftClaw2_r1 = bb_main.addOrReplaceChild("Left Claw (2)_r1", CubeListBuilder.create().texOffs(62, 225).addBox(1.0F, -7.0F, -1.0F, 0.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, -12.0F, -50.0F, -1.6325F, -0.7844F, 1.6144F));

        PartDefinition leftClaw3_r1 = bb_main.addOrReplaceChild("Left Claw (3)_r1", CubeListBuilder.create().texOffs(50, 225).addBox(-0.2554F, -0.7249F, -0.7819F, 0.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, -15.0F, -43.0F, -1.6353F, -0.828F, 1.6184F));

        PartDefinition leftClaw1_r1 = bb_main.addOrReplaceChild("Left Claw (1)_r1", CubeListBuilder.create().texOffs(228, 37).addBox(3.3818F, -2.4715F, -2.9545F, 0.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -15.0F, -46.0F, -1.6353F, -0.828F, 1.6184F));

        PartDefinition rightToeNail3_r1 = bb_main.addOrReplaceChild("Right Toe Nail (3)_r1", CubeListBuilder.create().texOffs(232, 23).addBox(0.0F, -4.0F, -5.0F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 1.0F, 0.0F, 0.0481F, 0.4359F, 0.0203F));

        PartDefinition rightToeNail1_r1 = bb_main.addOrReplaceChild("Right Toe Nail (1)_r1", CubeListBuilder.create().texOffs(180, 229).addBox(0.0F, -4.0F, -5.0F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 1.0F, 0.0F, 0.0472F, -0.3923F, -0.0181F));

        PartDefinition leftToeNail3_r1 = bb_main.addOrReplaceChild("Left Toe Nail (3)_r1", CubeListBuilder.create().texOffs(170, 229).addBox(0.0F, -4.0F, -5.0F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 1.0F, 0.0F, 0.0472F, 0.3923F, 0.0181F));

        PartDefinition leftToeNail1_r1 = bb_main.addOrReplaceChild("Left Toe Nail (1)_r1", CubeListBuilder.create().texOffs(160, 229).addBox(0.0F, -4.0F, -5.0F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 1.0F, 0.0F, 0.0481F, -0.4359F, -0.0203F));

        PartDefinition rightLowerFeet_r1 = bb_main.addOrReplaceChild("Right Lower Feet_r1", CubeListBuilder.create().texOffs(142, 218).addBox(-2.5F, -2.0F, -1.0F, 5.0F, 11.0F, 4.0F, new CubeDeformation(-0.001F))
                .texOffs(210, 217).addBox(9.5F, -2.0F, -1.0F, 5.0F, 11.0F, 4.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(-6.0F, -9.0F, 3.0F, -0.0436F, 0.0F, 0.0F));

        PartDefinition rightCrest_r1 = bb_main.addOrReplaceChild("Right Crest_r1", CubeListBuilder.create().texOffs(124, 232).addBox(0.5F, -3.0F, -4.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(-4.0F, -52.0F, -70.0F, 0.1745F, 0.0F, -0.1745F));

        PartDefinition rightHand_r1 = bb_main.addOrReplaceChild("Right hand_r1", CubeListBuilder.create().texOffs(232, 104).addBox(0.0F, -3.0F, -5.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.001F))
                .texOffs(232, 66).addBox(17.0F, -3.0F, -5.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(-9.0F, -12.0F, -42.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition leftWrist_r1 = bb_main.addOrReplaceChild("Left Wrist_r1", CubeListBuilder.create().texOffs(98, 225).addBox(-1.0F, -9.0F, -1.0F, 1.0F, 13.0F, 3.0F, new CubeDeformation(-0.001F))
                .texOffs(100, 113).addBox(-18.0F, -9.0F, -1.0F, 1.0F, 13.0F, 3.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(9.0F, -19.0F, -40.0F, -0.7418F, 0.0F, 0.0F));

        PartDefinition rightTriceps_r1 = bb_main.addOrReplaceChild("Right Triceps_r1", CubeListBuilder.create().texOffs(40, 219).addBox(-1.0F, -0.6114F, -0.4519F, 1.0F, 12.0F, 4.0F, new CubeDeformation(0.05F))
                .texOffs(30, 219).addBox(16.0F, -0.6114F, -0.4519F, 1.0F, 12.0F, 4.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-8.0F, -29.0F, -42.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition rightShoulderBone_r1 = bb_main.addOrReplaceChild("Right Shoulder Bone_r1", CubeListBuilder.create().texOffs(10, 219).addBox(0.0F, -7.0F, -2.0F, 1.0F, 14.0F, 4.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(-9.0F, -35.0F, -39.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition leftShoulderBone_r1 = bb_main.addOrReplaceChild("Left Shoulder Bone_r1", CubeListBuilder.create().texOffs(0, 219).addBox(0.0F, -7.0F, -2.0F, 1.0F, 14.0F, 4.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(8.0F, -35.0F, -38.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition rightLowerLeg_r1 = bb_main.addOrReplaceChild("Right Lower leg_r1", CubeListBuilder.create().texOffs(106, 211).addBox(-2.0F, 2.0F, -1.0F, 4.0F, 17.0F, 5.0F, new CubeDeformation(0.05F))
                .texOffs(82, 113).addBox(10.0F, 2.0F, -1.0F, 4.0F, 17.0F, 5.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-6.0F, -26.0F, -5.0F, 0.48F, 0.0F, 0.0F));

        PartDefinition rightUpperLeg_r1 = bb_main.addOrReplaceChild("Right Upper leg_r1", CubeListBuilder.create().texOffs(214, 55).addBox(-3.0F, 2.0F, -1.0F, 4.0F, 16.0F, 5.0F, new CubeDeformation(-0.001F))
                .texOffs(124, 211).addBox(9.0F, 2.0F, -1.0F, 4.0F, 16.0F, 5.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(-5.0F, -40.0F, 2.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition neckSpikesTop_r1 = bb_main.addOrReplaceChild("Neck Spikes Top_r1", CubeListBuilder.create().texOffs(198, 0).addBox(0.0F, -1.499F, -19.4431F, 0.0F, 2.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -46.0F, -44.0F, -0.3491F, 0.0F, 0.0F));

        PartDefinition leftCrest_r1 = bb_main.addOrReplaceChild("Left Crest_r1", CubeListBuilder.create().texOffs(190, 229).addBox(0.0152F, -2.8263F, -3.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(3.5F, -52.0F, -71.0F, 0.1745F, 0.0F, 0.1745F));

        PartDefinition neckRibsBottom_r1 = bb_main.addOrReplaceChild("Neck Ribs (Bottom)_r1", CubeListBuilder.create().texOffs(58, 192).addBox(9.0F, 10.0F, -19.0F, 0.0F, 9.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -52.0F, -42.0F, -0.3491F, 0.0F, 0.0F));

        PartDefinition neck_r1 = bb_main.addOrReplaceChild("Neck_r1", CubeListBuilder.create().texOffs(0, 192).addBox(-3.0F, -3.0F, -22.0F, 6.0F, 4.0F, 23.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, -42.0F, -43.0F, -0.3491F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(SCP250RenderState renderState) {
    }
}