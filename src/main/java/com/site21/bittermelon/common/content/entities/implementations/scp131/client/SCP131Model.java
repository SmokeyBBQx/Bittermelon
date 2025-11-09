package com.site21.bittermelon.common.content.entities.implementations.scp131.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.entities.implementations.scp131.SCP131;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SCP131Model extends HierarchicalModel<SCP131> {
    public static final ModelLayerLocation SCP131_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "scp131_layer"), "main");

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart wheel;

    public SCP131Model(@NotNull ModelPart root) {
        this.root = root.getChild("root");
        this.body = this.root.getChild("body");
        this.wheel = this.root.getChild("wheel");
    }

    public static @NotNull LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 14).addBox(-3.5F, 0.3019F, -4.284F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.2F))
                .texOffs(0, 0).addBox(-3.5F, 0.3019F, -4.284F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.8019F, 1.284F));

        PartDefinition top_bit_top_r1 = body.addOrReplaceChild("top_bit_top_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, -4.2958F, 2.0039F, -0.9163F, 0.0F, 0.0F));

        PartDefinition top_bit_middle_r1 = body.addOrReplaceChild("top_bit_middle_r1", CubeListBuilder.create().texOffs(26, 26).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.6552F, 0.216F, -0.7854F, 0.0F, 0.0F));

        PartDefinition top_bit_bottom_r1 = body.addOrReplaceChild("top_bit_bottom_r1", CubeListBuilder.create().texOffs(21, 14).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.6527F, -0.652F, -0.2182F, 0.0F, 0.0F));

        PartDefinition wheel = root.addOrReplaceChild("wheel", CubeListBuilder.create().texOffs(21, 0).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.5F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public @NotNull ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(@NotNull SCP131 scp131, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.body.xRot = headPitch * ((float)Math.PI / 180F);
        this.root.yRot = netHeadYaw * ((float)Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
