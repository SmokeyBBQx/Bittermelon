package com.site21.bittermelon.common.content.entities.scp718.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import static com.site21.bittermelon.client.event.LayerDefinitions.SCP_718_SMALL_LAYER;

public class EyeballOnPlayerLayer extends RenderLayer<PlayerRenderState, PlayerModel> {
    private static final ResourceLocation TEXTURE = Bittermelon.resource("textures/entity/scp_718.png");
    private final SCP718SmallModel model;
    private final SCP718RenderState eyeballState;

    public EyeballOnPlayerLayer(RenderLayerParent<PlayerRenderState, PlayerModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        model = new SCP718SmallModel(modelSet.bakeLayer(SCP_718_SMALL_LAYER));
        eyeballState = new SCP718RenderState();
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                       PlayerRenderState renderState, float yRot, float xRot) {
        renderEyeballOnShoulder(poseStack, bufferSource, packedLight, renderState, yRot, xRot, 0.4f);
        renderEyeballOnShoulder(poseStack, bufferSource, packedLight, renderState, yRot, xRot, -0.4f);
        renderEyeballOnHead(poseStack, bufferSource, packedLight, renderState, yRot, xRot, -0.075f);
    }

    private void renderEyeball(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                               PlayerRenderState renderState, float yRot, float xRot, float y, float x) {
        float newY = renderState.isCrouching ? y + 0.2f : y;
        poseStack.translate(x, newY, 0.0f);
        eyeballState.yRot = yRot;
        eyeballState.xRot = xRot;

        model.setupAnim(eyeballState);
        model.renderToBuffer(poseStack, buffer.getBuffer(model.renderType(TEXTURE)), packedLight, OverlayTexture.NO_OVERLAY);
    }

    private void renderEyeballOnHead(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                     PlayerRenderState renderState, float yRot, float xRot, float x) {
        poseStack.pushPose();
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(xRot));

        renderEyeball(poseStack, buffer, packedLight, renderState, yRot, xRot, -2.0f, x);
        poseStack.popPose();
    }

    private void renderEyeballOnShoulder(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                     PlayerRenderState renderState, float yRot, float xRot, float x) {
        poseStack.pushPose();
        renderEyeball(poseStack, buffer, packedLight, renderState, yRot, xRot, -1.2f, x);
        poseStack.popPose();
    }
}
