package com.site21.bittermelon.common.content.entities.scp025fr.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.util.Mth;

public class SCP025FRPartModel<T extends EntityRenderState> extends EntityModel<T> {
    protected SCP025FRPartModel(ModelPart root) {
        super(root);
    }

    protected void animateLeg(ModelPart leg, float phase, float speed, float side) {
        leg.yRot = -Mth.cos(phase) * 0.45f * speed * side;
        float lift = Math.max(0.0f, Mth.sin(phase));
        leg.zRot = -lift * 0.85f * speed * side;
    }
}
