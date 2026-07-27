package com.site21.bittermelon.common.systems.medical.wound;

import com.mojang.blaze3d.vertex.PoseStack;
import com.site21.bittermelon.client.event.ClientSetup;
import com.site21.bittermelon.init.custom.BodyParts;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;

public class LimbRenderLayer<S extends EntityRenderState, M extends EntityModel<? super S>> extends RenderLayer<S, M> {
    public LimbRenderLayer(RenderLayerParent<S, M> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, S state, float yRot, float xRot) {
        PartInstance root = state.getRenderDataOrDefault(ClientSetup.ROOT_PART, BodyParts.EMPTY.get().toInstance());

        if (!root.getBodyPart().equals(BodyParts.EMPTY.get())) {
            renderPart(root, poseStack, collector, lightCoords);
        }
    }

    private void renderPart(PartInstance part, PoseStack poseStack, SubmitNodeCollector collector, int lightCoords) {
        collector.submitCustomGeometry(poseStack, RenderTypes.entitySolid(DefaultPlayerSkin.getDefaultTexture()), ((pose, buffer) -> {
            poseStack.pushPose();
            ModelPart modelPart = BodyPartModels.MODELS.get(part.getBodyPart().builtInRegistryHolder());
            modelPart.translateAndRotate(poseStack);
            modelPart.compile(
                    pose,
                    buffer,
                    lightCoords,
                    OverlayTexture.NO_OVERLAY,
                    -1
            );

            for (PartInstance attached : part.getAttachedParts().values()) {
                renderPart(attached, poseStack, collector, lightCoords);
            }

            poseStack.popPose();
        }
        ));

//        collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(clientWound.identifier()), ((pose, buffer) -> {
//            poseStack.pushPose();
//            poseStack.scale(1.25f, 1.25f, 1.25f);
//            ModelPart modelPart = BodyPartModels.MODELS.get(part.getBodyPart().builtInRegistryHolder());
//            modelPart.translateAndRotate(poseStack);
//            modelPart.compile(
//                    pose,
//                    buffer,
//                    lightCoords,
//                    OverlayTexture.NO_OVERLAY,
//                    -1
//            );
//
//            poseStack.popPose();
//        }
//        ));
    }
}
