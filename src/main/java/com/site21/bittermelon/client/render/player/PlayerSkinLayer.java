package com.site21.bittermelon.client.render.player;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public abstract class PlayerSkinLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    private final Identifier TEXTURE;
    private final int order;

    public PlayerSkinLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer, Identifier texture, int order) {
        super(renderer);
        this.TEXTURE = texture;
        this.order = order;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
        // modification of RenderLayer::renderColoredCutoutModel
        collector.order(order)
                .submitModel(
                        getParentModel(),
                        state,
                        poseStack,
                        RenderTypes.entityTranslucent(TEXTURE), // see other render types if bugs occur
                        lightCoords,
                        LivingEntityRenderer.getOverlayCoords(state, 0.0F),
                        -1,
                        null,
                        state.outlineColor,
                        null
                );
    }
}
