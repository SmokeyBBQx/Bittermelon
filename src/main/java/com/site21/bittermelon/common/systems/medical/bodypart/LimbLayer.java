package com.site21.bittermelon.common.systems.medical.bodypart;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.site21.bittermelon.client.event.ClientSetup;
import com.site21.bittermelon.common.systems.medical.bodypart.client.BodyPartModel;
import com.site21.bittermelon.common.systems.medical.bodypart.client.BodyPartModels;
import com.site21.bittermelon.common.systems.medical.wound.Wound;
import com.site21.bittermelon.common.systems.medical.wound.client.ClientWound;
import com.site21.bittermelon.common.systems.medical.wound.client.ClientWoundCache;
import com.site21.bittermelon.init.custom.BodyParts;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
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
        BodyPart bodyPart = part.getBodyPart();
        BodyPartModel bodyPartModel = BodyPartModels.MODELS.get(bodyPart.builtInRegistryHolder());
        ModelPart modelPart = bodyPartModel.modelPart();
        Vec3 restOffset = bodyPartModel.restOffset();

        poseStack.pushPose();

        float x = (float) (equivalentPart.x - restOffset.x + bodyPart.offset().x);
        float y = (float) (equivalentPart.y - restOffset.y + bodyPart.offset().y);
        float z = (float) (equivalentPart.z - restOffset.z + bodyPart.offset().z);

        poseStack.translate(x / 16.0, y / 16.0, z / 16.0);

        poseStack.mulPose(Axis.ZP.rotation(equivalentPart.zRot));
        poseStack.mulPose(Axis.YP.rotation(equivalentPart.yRot));
        poseStack.mulPose(Axis.XP.rotation(equivalentPart.xRot));
        poseStack.scale(equivalentPart.xScale, equivalentPart.yScale, equivalentPart.zScale);

        collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(bodyPartModel.texture()),
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
