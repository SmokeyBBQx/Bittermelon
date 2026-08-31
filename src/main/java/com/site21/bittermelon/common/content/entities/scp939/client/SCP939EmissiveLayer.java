package com.site21.bittermelon.common.content.entities.scp939.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

public class SCP939EmissiveLayer extends RenderLayer<SCP939RenderState, SCP939Model> {

    private static final Identifier EMISSIVE =
            Identifier.fromNamespaceAndPath("bittermelon", "textures/entity/scp_939_emissive.png");

    private static final float PULSE_SPEED = 0.1f;

    private static final int LOW_COLOR  = ARGB.color(255, 80, 65, 10);
    private static final int HIGH_COLOR = ARGB.color(255, 255, 255, 255);

    public SCP939EmissiveLayer(RenderLayerParent<SCP939RenderState, SCP939Model> parent) {
        super(parent);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int packedLight,
                       SCP939RenderState state, float yRot, float xRot) {
        if (!state.listeningAnimationState.isStarted()) return;

        float pulse = (Mth.sin(state.ageInTicks * PULSE_SPEED) + 1f) / 2f;

        int r = Mth.lerpInt(pulse, ARGB.red(LOW_COLOR), ARGB.red(HIGH_COLOR));
        int g = Mth.lerpInt(pulse, ARGB.green(LOW_COLOR), ARGB.green(HIGH_COLOR));
        int b = Mth.lerpInt(pulse, ARGB.blue(LOW_COLOR), ARGB.blue(HIGH_COLOR));

        int tint = ARGB.color(255, r, g, b);

        collector.submitModel(
                getParentModel(),
                state,
                poseStack,
                RenderTypes.entityTranslucentEmissive(EMISSIVE),
                packedLight,
                -1,
                tint,
                null
        );
    }
}