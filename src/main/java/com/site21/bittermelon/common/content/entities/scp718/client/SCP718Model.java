package com.site21.bittermelon.common.content.entities.scp718.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class SCP718Model extends EntityModel<SCP718RenderState> {
    private final ModelPart body;
    private final ModelPart eye;
    private final ModelPart stalk;

    public SCP718Model(ModelPart root) {
        super(root);
        this.body = root.getChild("body");
        this.eye = this.body.getChild("eye");
        this.stalk = this.body.getChild("stalk");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition eye = body.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(24, 0).addBox(-1.6F, -3.0F, -2.6F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(24, 6).addBox(-1.6F, -3.0F, -2.6F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offset(0.6F, -23.0F, -0.4F));

        PartDefinition stalk = body.addOrReplaceChild("stalk", CubeListBuilder.create().texOffs(16, 6).addBox(0.0F, -23.0F, -1.0F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 6).addBox(0.0F, -23.0F, -1.0F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.2F))
                .texOffs(0, 0).addBox(-2.5F, -0.1F, -3.5F, 6.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition base_veins_2_r1 = stalk.addOrReplaceChild("base_veins_2_r1", CubeListBuilder.create().texOffs(0, 6).addBox(0.2929F, -23.0F, -3.1213F, 0.0F, 23.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, 2.0F, 0.009F, -0.6983F, -0.0123F));

        PartDefinition base_veins_1_r1 = stalk.addOrReplaceChild("base_veins_1_r1", CubeListBuilder.create().texOffs(4, 6).addBox(0.2929F, -23.0F, -3.1213F, 0.0F, 23.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.009F, -0.6983F, -0.0123F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(SCP718RenderState renderState) {
        super.setupAnim(renderState);

        float scale = Math.min(1.0f, 0.2f + renderState.growth);
        eye.y = -23.0F * scale;
        stalk.yScale = scale;

        eye.xRot = renderState.xRot * (float) (Math.PI / 180.0);
        eye.yRot = renderState.yRot * (float) (Math.PI / 180.0);
        eye.visible = renderState.deathTime == 0;
    }
}
