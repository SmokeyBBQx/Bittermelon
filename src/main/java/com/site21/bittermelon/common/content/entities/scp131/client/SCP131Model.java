package com.site21.bittermelon.common.content.entities.scp131.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SCP131Model extends EntityModel<SCP131RenderState> {
    private final ModelPart body;

    protected SCP131Model(ModelPart root) {
        super(root);
        body = root.getChild("body");
    }

    public static @NotNull LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        PartDefinition root = part.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 14).addBox(-3.5F, 0.3019F, -4.284F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.2F))
                .texOffs(0, 0).addBox(-3.5F, 0.3019F, -4.284F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.8019F, 1.284F));

        PartDefinition top_bit_top_r1 = body.addOrReplaceChild("top_bit_top_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, -4.2958F, 2.0039F, -0.9163F, 0.0F, 0.0F));

        PartDefinition top_bit_middle_r1 = body.addOrReplaceChild("top_bit_middle_r1", CubeListBuilder.create().texOffs(26, 26).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.6552F, 0.216F, -0.7854F, 0.0F, 0.0F));

        PartDefinition top_bit_bottom_r1 = body.addOrReplaceChild("top_bit_bottom_r1", CubeListBuilder.create().texOffs(21, 14).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.6527F, -0.652F, -0.2182F, 0.0F, 0.0F));

        PartDefinition wheel = root.addOrReplaceChild("wheel", CubeListBuilder.create().texOffs(21, 0).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.5F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(@NotNull SCP131RenderState renderState) {
        super.setupAnim(renderState);

        body.xRot = renderState.xRot * ((float)Math.PI / 180F);
        body.yRot = renderState.yRot * ((float)Math.PI / 180F);
    }
}
