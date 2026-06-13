package com.site21.bittermelon.common.content.entities.scp815snake.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class SCP815SnakeModel extends EntityModel<LivingEntityRenderState> {

    private final ModelPart bb_main;

    public SCP815SnakeModel(ModelPart root) {
        super(root);
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("bb_main",
                CubeListBuilder.create()
                        .texOffs(0, 8).addBox(-1.0F, -2.0F, -8.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 0).addBox(-1.0F, -2.0F, -3.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }
}