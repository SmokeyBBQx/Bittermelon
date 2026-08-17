package com.site21.bittermelon.common.content.entities.ragdoll.client;

import com.jme3.math.Vector3f;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.site21.bittermelon.common.content.entities.ragdoll.RagdollEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import org.joml.Quaternionf;

import java.util.List;

public class RagdollRenderer extends EntityRenderer<RagdollEntity, RagdollRenderState> {
    private final ModelPart head, torso, leftArm, rightArm, leftLeg, rightLeg;

    public RagdollRenderer(EntityRendererProvider.Context context) {
        super(context);
        PlayerModel model = new PlayerModel(context.bakeLayer(ModelLayers.PLAYER), false);
        head = model.head;
        torso = model.body;
        leftArm = model.leftArm;
        rightArm = model.rightArm;
        leftLeg = model.leftLeg;
        rightLeg = model.rightLeg;
    }

    @Override
    public RagdollRenderState createRenderState() {
        return new RagdollRenderState();
    }

    @Override
    public void extractRenderState(RagdollEntity entity, RagdollRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        List<RagdollTransformation> transformations = entity.getPartTransformations();
        for (int i = 0; i < 6; i++) {
            state.partPositions[i] = transformations.get(i).interpolatedPos(partialTicks, state.partPositions[i]).subtract(new Vector3f((float) entity.position().x, (float) entity.position().y, (float) entity.position().z));
            state.partRotations[i] = transformations.get(i).interpolatedRot(partialTicks, state.partRotations[i]);
        }
    }

    @Override
    public void submit(RagdollRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        super.submit(state, poseStack, collector, camera);
        submitPart(state, 0, head, poseStack, collector);
        submitPart(state, 1, torso, poseStack, collector);
        submitPart(state, 2, leftArm, poseStack, collector);
        submitPart(state, 3, rightArm, poseStack, collector);
        submitPart(state, 4, leftLeg, poseStack, collector);
        submitPart(state, 5, rightLeg, poseStack, collector);

    }

    private void submitPart(RagdollRenderState state, int i, ModelPart part, PoseStack poseStack, SubmitNodeCollector collector) {
        poseStack.pushPose();

        Vector3f pos = state.partPositions[i];
        poseStack.translate(pos.x, pos.y, pos.z);

        Quaternionf rotation = new Quaternionf(
                state.partRotations[i].getX(),
                state.partRotations[i].getY(),
                state.partRotations[i].getZ(),
                state.partRotations[i].getW()
        );
        poseStack.mulPose(rotation);
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0f));

        poseStack.translate(-part.x / 16.0f, -part.y / 16.0f, -part.z / 16.0f);

        float offsetY = switch (i) {
            case 0 -> 4.0f / 16.0f;
            case 1, 4, 5 -> -6.0f / 16.0f;
            case 2, 3 -> -4.0f / 16.0f;
            default -> 0.0f;
        };
        poseStack.translate(0.0f, offsetY, 0.0f);

        collector.submitModelPart(part, poseStack, RenderTypes.entityCutout(DefaultPlayerSkin.getDefaultTexture()), state.lightCoords, OverlayTexture.NO_OVERLAY, null);

        poseStack.popPose();
    }
}
