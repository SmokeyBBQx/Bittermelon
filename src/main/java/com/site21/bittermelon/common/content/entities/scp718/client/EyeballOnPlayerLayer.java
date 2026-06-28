package com.site21.bittermelon.common.content.entities.scp718.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

import static com.site21.bittermelon.client.event.ClientSetup.EYEBALL_GROWTH;
import static com.site21.bittermelon.client.event.LayerDefinitions.SCP_718_SMALL_LAYER;

public class EyeballOnPlayerLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    private static final Identifier TEXTURE = Bittermelon.identifier("textures/entity/scp_718.png");
    private final SCP718SmallModel model;
    private final SCP718RenderState eyeballState;

    public EyeballOnPlayerLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        model = new SCP718SmallModel(modelSet.bakeLayer(SCP_718_SMALL_LAYER));
        eyeballState = new SCP718RenderState();
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
        if (!Boolean.TRUE.equals(state.getRenderData(EYEBALL_GROWTH))) return;

        submitEyeballOnShoulder(poseStack, collector, lightCoords, state, yRot, xRot, 0.8f, 0.35f);
        submitEyeballOnShoulder(poseStack, collector, lightCoords, state, yRot, xRot, 0.9f, -0.4f);
        renderEyeballOnHead(poseStack, collector, lightCoords, state, yRot, xRot, 1.0f, -0.075f);
    }

    private void submitEyeball(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords,
                               AvatarRenderState state, float yRot, float xRot, float scale, float y, float x) {
        float newY = state.isCrouching ? y + 0.3f : y;
        poseStack.translate(x, newY * scale, 0.0f);
        poseStack.scale(scale, scale, scale);
        eyeballState.yRot = yRot;
        eyeballState.xRot = xRot;
        eyeballState.scale = scale;

        collector.submitModel(
                model,
                eyeballState,
                poseStack,
                model.renderType(TEXTURE),
                lightCoords,
                OverlayTexture.NO_OVERLAY,
                state.outlineColor,
                null
        );
    }

    private void renderEyeballOnHead(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords,
                                     AvatarRenderState state, float yRot, float xRot, float scale, float x) {
        poseStack.pushPose();
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(xRot));

        submitEyeball(poseStack, collector, lightCoords, state, yRot, xRot, scale,-2.0f, x);
        poseStack.popPose();
    }

    private void submitEyeballOnShoulder(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords,
                                     AvatarRenderState state, float yRot, float xRot, float scale, float x) {
        poseStack.pushPose();
        submitEyeball(poseStack, collector, lightCoords, state, yRot, xRot, scale, -1.5f, x);
        poseStack.popPose();
    }
}
