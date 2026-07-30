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
import net.minecraft.core.Holder;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class LimbLayer<S extends EntityRenderState, M extends EntityModel<? super S>> extends RenderLayer<S, M> {
    public LimbLayer(RenderLayerParent<S, M> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, S state, float yRot, float xRot) {
        PartInstance root = state.getRenderDataOrDefault(ClientSetup.ROOT_PART, BodyParts.EMPTY.get().toInstance());

        Function<String, ModelPart> partLookup = getParentModel().root().createPartLookup();

        if (!root.getBodyPart().equals(BodyParts.EMPTY.get())) {
            renderPart(partLookup, root, poseStack, collector, lightCoords);
        }
    }

    private void renderPart(Function<String, ModelPart> partLookup, PartInstance part, PoseStack poseStack, SubmitNodeCollector collector, int lightCoords) {
        ModelPart equivalentPart = partLookup.apply(part.getLimbSlot().identifier);
        if (equivalentPart == null) return;
        Holder<BodyPart> bodyPart = part.getBodyPart().builtInRegistryHolder();
        ModelPart modelPart = BodyPartModels.MODELS.get(bodyPart);
        Vec3 restOffset = BodyPartModels.REST_OFFSETS.get(bodyPart);

        poseStack.pushPose();

        modelPart.x = (float) (equivalentPart.x - restOffset.x);
        modelPart.y = (float) (equivalentPart.y - restOffset.y);
        modelPart.z = (float) (equivalentPart.z - restOffset.z);
        modelPart.xRot = equivalentPart.xRot;
        modelPart.yRot = equivalentPart.yRot;
        modelPart.zRot = equivalentPart.zRot;
        modelPart.xScale = equivalentPart.xScale;
        modelPart.yScale = equivalentPart.yScale;
        modelPart.zScale = equivalentPart.zScale;

        modelPart.translateAndRotate(poseStack);

        collector.submitCustomGeometry(poseStack, RenderTypes.entitySolid(DefaultPlayerSkin.getDefaultTexture()),
                ((pose, buffer) -> modelPart.compile(
                        pose,
                        buffer,
                        lightCoords,
                        OverlayTexture.NO_OVERLAY,
                        -1
                ))
        );

        for (Wound wound : part.getWounds()) {
            ClientWound clientWound = ClientWoundCache.INSTANCE.get(wound, UUID.fromString("00000000-0000-0000-0000-000000000000"), part.getBodyPart());
            poseStack.pushPose();
            poseStack.scale(1.01f, 1.01f, 1.01f);
            collector.submitCustomGeometry(poseStack, RenderTypes.entityTranslucent(clientWound.identifier()), ((pose, buffer) ->
                    modelPart.compile(pose, buffer, lightCoords, OverlayTexture.NO_OVERLAY, -1)
            ));
            poseStack.popPose();
        }

        poseStack.popPose();

        for (Map.Entry<Vec3, PartInstance> entry : part.getAttachedParts().entrySet()) {
            poseStack.pushPose();
            Vec3 attachmentPoint = entry.getKey();
            poseStack.translate(attachmentPoint.x / 16, attachmentPoint.y / 16, attachmentPoint.z / 16);
            renderPart(partLookup, entry.getValue(), poseStack, collector, lightCoords);
            poseStack.popPose();
        }
    }
}
