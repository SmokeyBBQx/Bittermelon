package com.site21.bittermelon.common.content.entities.scp1507.client;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.site21.bittermelon.client.event.HealthStages;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.Identifier;

import java.util.Map;


public class SCP1507CrackinessLayer extends RenderLayer<SCP1507RenderState, SCP1507Model> {
    private static final Map<HealthStages.Level, Identifier> TEXTURES = Map.of(
            HealthStages.Level.HURT, Identifier.fromNamespaceAndPath("bittermelon", "textures/entity/1507_cracked_overlay_hurt.png"),
            HealthStages.Level.DAMAGED, Identifier.fromNamespaceAndPath("bittermelon", "textures/entity/1507_cracked_overlay_damaged.png")
    );

    public SCP1507CrackinessLayer(RenderLayerParent<SCP1507RenderState, SCP1507Model> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SCP1507RenderState state, float yRot, float xRot) {
        Identifier texture = TEXTURES.get(state.crackiness);
        if (!state.isInvisible && texture != null) {
            renderColoredCutoutModel(this.getParentModel(), texture, poseStack, submitNodeCollector, lightCoords, state, -1, 1);
        }
    }
}
