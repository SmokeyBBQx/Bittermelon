package com.site21.bittermelon.common.content.entities.scp939.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SCP939Model extends EntityModel<SCP939RenderState> {

    public SCP939Model(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.2857F, 5.7174F, 11.3666F));

        PartDefinition upper_body = body.addOrReplaceChild("upper_body", CubeListBuilder.create().texOffs(34, 52).addBox(-3.7857F, -4.7174F, -7.3666F, 7.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition tail = upper_body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.2143F, -1.0507F, 0.3F));

        PartDefinition start = tail.addOrReplaceChild("start", CubeListBuilder.create().texOffs(34, 20).addBox(-2.5F, -2.5F, -1.0F, 5.0F, 5.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -0.1667F, 0.3333F));

        PartDefinition middle = start.addOrReplaceChild("middle", CubeListBuilder.create().texOffs(40, 0).addBox(-1.5F, -2.0F, -0.5F, 3.0F, 4.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 11.5F));

        PartDefinition end = middle.addOrReplaceChild("end", CubeListBuilder.create().texOffs(70, 79).addBox(0.0F, -1.5F, -1.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 11.5F));

        PartDefinition front = upper_body.addOrReplaceChild("front", CubeListBuilder.create().texOffs(0, 0).addBox(-5.1667F, -3.607F, -10.8372F, 9.0F, 9.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(22, 54).addBox(-0.6667F, -5.607F, -16.8372F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(14, 70).addBox(-0.6667F, -4.607F, -19.8372F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 54).addBox(-0.6667F, -8.607F, -10.8372F, 0.0F, 5.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.381F, -2.1104F, -7.5294F));

        PartDefinition head = front.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(-0.6667F, -1.286F, -20.8255F));

        PartDefinition lower_jaw = head.addOrReplaceChild("lower_jaw", CubeListBuilder.create().texOffs(0, 40).addBox(-2.5F, -0.7079F, -12.6796F, 5.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.1369F, 1.668F));

        PartDefinition cube_r1 = lower_jaw.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(62, 52).mirror().addBox(-0.5F, -2.0F, -6.5F, 0.0F, 3.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, -0.7079F, -6.1796F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r2 = lower_jaw.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(40, 15).addBox(-2.5F, -1.5F, 0.0F, 5.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.2459F, -12.9611F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r3 = lower_jaw.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(62, 52).addBox(0.5F, -2.0F, -6.5F, 0.0F, 3.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -0.7079F, -6.1796F, 0.0F, 0.0F, 0.3927F));

        PartDefinition upper_jaw = head.addOrReplaceChild("upper_jaw", CubeListBuilder.create().texOffs(34, 37).addBox(-2.5F, -1.4341F, -10.8437F, 5.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(68, 11).addBox(0.0F, -4.1841F, -7.8437F, 0.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.1369F, -0.168F));

        PartDefinition cube_r4 = upper_jaw.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(44, 66).mirror().addBox(-0.5F, -2.0F, -6.5F, 0.0F, 2.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.75F, 3.3159F, -4.3437F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r5 = upper_jaw.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(50, 15).addBox(-2.5F, -1.5F, 0.0F, 5.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.1278F, -11.1252F, -0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r6 = upper_jaw.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(44, 66).addBox(0.5F, -2.0F, -6.5F, 0.0F, 2.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.75F, 3.3159F, -4.3437F, 0.0F, 0.0F, -0.3927F));

        PartDefinition neck = front.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 20).addBox(-5.5F, -15.0F, 2.0F, 7.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(68, 0).addBox(-4.5F, -14.0F, -1.0F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(1.3333F, 12.393F, -18.8372F));

        PartDefinition left_front_leg = front.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(68, 38).addBox(-0.3327F, -2.0714F, 2.1593F, 2.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.1661F, 1.4644F, -17.9965F));

        PartDefinition left_front_lower_leg = left_front_leg.addOrReplaceChild("left_front_lower_leg", CubeListBuilder.create().texOffs(82, 23).addBox(-0.9829F, -0.5F, -0.9635F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.4002F, 7.4286F, 5.1228F));

        PartDefinition left_front_foot = left_front_lower_leg.addOrReplaceChild("left_front_foot", CubeListBuilder.create().texOffs(0, 85).addBox(-0.9658F, 0.0F, -2.927F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(60, 15).addBox(1.0342F, 0.0F, -4.927F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(86, 17).addBox(0.0342F, 0.0F, -4.927F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(38, 86).addBox(-0.9658F, 0.0F, -4.927F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.0171F, 9.5F, -0.0365F));

        PartDefinition cube_r7 = left_front_foot.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(64, 15).addBox(0.5F, -1.5F, -2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.4658F, 1.5F, -2.427F, 0.0F, 0.3927F, 0.0F));

        PartDefinition right_front_leg = front.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(68, 38).mirror().addBox(-1.6673F, -1.8214F, -1.3407F, 2.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.4994F, 1.2144F, -14.4965F));

        PartDefinition right_front_lower_leg = right_front_leg.addOrReplaceChild("right_front_lower_leg", CubeListBuilder.create().texOffs(82, 23).mirror().addBox(-1.0171F, -0.625F, 0.0365F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-0.4002F, 7.8036F, 0.6228F));

        PartDefinition right_front_foot = right_front_lower_leg.addOrReplaceChild("right_front_foot", CubeListBuilder.create().texOffs(38, 86).mirror().addBox(0.9658F, -0.25F, -3.927F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(86, 17).mirror().addBox(-0.0342F, -0.25F, -3.927F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(60, 15).mirror().addBox(-1.0342F, -0.25F, -3.927F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 85).mirror().addBox(-1.0342F, -0.25F, -1.927F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0171F, 9.625F, -0.0365F));

        PartDefinition cube_r8 = right_front_foot.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(64, 15).mirror().addBox(-0.5F, -1.5F, -2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.4658F, 1.25F, -1.427F, 0.0F, -0.3927F, 0.0F));

        PartDefinition frills = upper_body.addOrReplaceChild("frills", CubeListBuilder.create().texOffs(82, 79).addBox(-1.5F, -15.0F, 20.0F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(1.2143F, 6.2826F, -27.3666F));

        PartDefinition left_hind_leg = body.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(68, 23).addBox(-0.7613F, -1.9286F, -1.4835F, 3.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.9756F, -0.7888F, -0.8831F));

        PartDefinition left_hind_lower_leg = left_hind_leg.addOrReplaceChild("left_hind_lower_leg", CubeListBuilder.create().texOffs(14, 78).addBox(-1.5F, -1.25F, -1.25F, 3.0F, 12.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.4887F, 6.3214F, 2.7665F));

        PartDefinition left_hind_foot = left_hind_lower_leg.addOrReplaceChild("left_hind_foot", CubeListBuilder.create().texOffs(62, 86).addBox(0.3841F, 0.0F, -5.777F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(88, 0).addBox(1.3841F, 0.0F, -5.777F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(66, 86).addBox(-0.6159F, 0.0F, -5.777F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(38, 79).addBox(-1.1159F, 0.0F, -3.777F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.3841F, 10.75F, 0.527F));

        PartDefinition cube_r9 = left_hind_foot.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(58, 86).addBox(0.5F, -1.5F, -2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.6159F, 1.5F, -3.277F, 0.0F, 0.3927F, 0.0F));

        PartDefinition right_hind_leg = body.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(68, 23).mirror().addBox(-2.2387F, -1.9286F, -1.4835F, 3.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.547F, -0.7888F, -0.8831F));

        PartDefinition right_hind_lower_leg = right_hind_leg.addOrReplaceChild("right_hind_lower_leg", CubeListBuilder.create().texOffs(14, 78).mirror().addBox(-1.5F, -1.25F, -1.5F, 3.0F, 12.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-0.4887F, 6.3214F, 3.0165F));

        PartDefinition right_hind_foot = right_hind_lower_leg.addOrReplaceChild("right_hind_foot", CubeListBuilder.create().texOffs(62, 86).mirror().addBox(-0.3841F, 0.0F, -5.777F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(66, 86).mirror().addBox(0.6159F, 0.0F, -5.777F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(88, 0).mirror().addBox(-1.3841F, 0.0F, -5.777F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(38, 79).mirror().addBox(-1.8841F, 0.0F, -3.777F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.3841F, 10.75F, 0.277F));

        PartDefinition cube_r10 = right_hind_foot.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(58, 86).mirror().addBox(-0.5F, -1.5F, -2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.6159F, 1.5F, -3.277F, 0.0F, -0.3927F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}
