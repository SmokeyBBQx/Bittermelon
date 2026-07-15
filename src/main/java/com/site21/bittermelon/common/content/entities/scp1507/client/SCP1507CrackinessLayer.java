package com.site21.bittermelon.common.content.entities.scp1507.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.site21.bittermelon.client.event.HealthStages;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.Identifier;


public class SCP1507CrackinessLayer extends RenderLayer<SCP1507RenderState, SCP1507Model> {
    private static final Identifier DAMAGE_TEXTURE =
            Identifier.fromNamespaceAndPath("bittermelon", "textures/entity/1507_cracked_overlay.png");

    public SCP1507CrackinessLayer(RenderLayerParent<SCP1507RenderState, SCP1507Model> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SCP1507RenderState state, float yRot, float xRot) {
        if (!state.isInvisible && state.crackiness == HealthStages.Level.HURT) {
            renderColoredCutoutModel(this.getParentModel(), DAMAGE_TEXTURE, poseStack, submitNodeCollector, lightCoords, state, -1, 1);
        }
    }
}
