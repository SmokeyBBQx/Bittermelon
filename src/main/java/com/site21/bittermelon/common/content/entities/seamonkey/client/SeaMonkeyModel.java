package com.site21.bittermelon.common.content.entities.seamonkey.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

import java.util.List;
import java.util.Set;

public class SeaMonkeyModel extends EntityModel<LivingEntityRenderState> {
    public final ModelPart body;
    public final ModelPart appendages;

    protected SeaMonkeyModel(ModelPart root) {
        super(root);
        this.body = root.getChild("body");
        this.appendages = this.body.getChild("appendages");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 20).addBox(-4.0F, -1.0F, 2.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.0F, -1.0F, 3.0F, 2.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(14, 20).addBox(-3.0F, -1.0F, 0.0F, 4.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(-2.0F, 0.0F, 13.0F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 24.0F, -7.0F));

        PartDefinition appendages = body.addOrReplaceChild("appendages", CubeListBuilder.create().texOffs(-5, 11).addBox(-5.0F, 0.0F, -5.0F, 10.0F, 0.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 0.0F, 8.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    public static LayerDefinition createAppendagesLayer() {
        return createBodyLayer().apply(mesh -> {
            mesh.getRoot().retainExactParts(Set.of("appendages"));
            return mesh;
        });
    }

    @Override
    public void setupAnim(LivingEntityRenderState renderState) {
        super.setupAnim(renderState);
    }

    public List<ModelPart> getAppendages(LivingEntityRenderState renderState) {
        return appendages.getAllParts();
    }
}
