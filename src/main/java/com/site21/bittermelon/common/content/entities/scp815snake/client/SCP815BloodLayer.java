package com.site21.bittermelon.common.content.entities.scp815snake.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.Identifier;

import static com.site21.bittermelon.client.event.ClientSetup.BROKEN_JAW;


public class SCP815BloodLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    private static final Identifier SCP815_BLOOD = Bittermelon.identifier("textures/entity/815_bloody_layer.png");

    public SCP815BloodLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
        if (!Boolean.TRUE.equals(state.getRenderData(BROKEN_JAW))) return;
}
