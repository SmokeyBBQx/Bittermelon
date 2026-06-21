package com.site21.bittermelon.common.content.entities.scp025fr.client;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.event.LayerDefinitions;
import com.site21.bittermelon.common.content.entities.scp025fr.SCP025FR;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.HitboxRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.PartEntity;

public class SCP025FRRenderer extends MobRenderer<SCP025FR, SCP025FRRenderState, SCP025FRModel> {
    private static final Identifier TEXTURE = Bittermelon.identifier("textures/entity/scp_025_fr.png");
    private final SCP025FRBodyModel bodyModel;
    private final SCP025FRTailModel tailModel;

    public SCP025FRRenderer(EntityRendererProvider.Context context) {
        super(context, new SCP025FRModel(context.bakeLayer(LayerDefinitions.SCP_025_FR_LAYER)), 0.25f);
        bodyModel = new SCP025FRBodyModel(context.bakeLayer(LayerDefinitions.SCP_025_FR_BODY_LAYER));
        tailModel = new SCP025FRTailModel(context.bakeLayer(LayerDefinitions.SCP_025_FR_TAIL_LAYER));
    }

    @Override
    public Identifier getTextureLocation(SCP025FRRenderState renderState) {
        return TEXTURE;
    }

    @Override
    public SCP025FRRenderState createRenderState() {
        return new SCP025FRRenderState();
    }

    @Override
    public void extractRenderState(SCP025FR entity, SCP025FRRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);

        Vec3 entityPos = new Vec3(
                Mth.lerp(partialTick, entity.xOld, entity.getX()),
                Mth.lerp(partialTick, entity.yOld, entity.getY()),
                Mth.lerp(partialTick, entity.zOld, entity.getZ())
        );

        state.partPoses.clear();
        for (PartEntity<?> part : entity.getParts()) {
            Vec3 partPos = new Vec3(
                    Mth.lerp(partialTick, part.xOld, part.getX()),
                    Mth.lerp(partialTick, part.yOld, part.getY()),
                    Mth.lerp(partialTick, part.zOld, part.getZ())
            );
            state.partPoses.add(new SCP025FRRenderState.PartPose(
                    partPos.subtract(entityPos),
                    Mth.rotLerp(partialTick, part.yRotO, part.getYRot()),
                    Mth.lerp(partialTick, part.xRotO, part.getXRot())
            ));
        }
    }

    @Override
    public void render(SCP025FRRenderState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(state, poseStack, bufferSource, packedLight);

        VertexConsumer consumer = bufferSource.getBuffer(bodyModel.renderType(TEXTURE));
        for (int i = 0; i < state.partPoses.size(); i++) {
            SCP025FRRenderState.PartPose pose = state.partPoses.get(i);
            poseStack.pushPose();

            poseStack.translate(pose.offset().x, pose.offset().y + 1.5, pose.offset().z);
            poseStack.mulPose(Axis.YP.rotationDegrees(-pose.yRot()));
            poseStack.mulPose(Axis.XP.rotationDegrees(-pose.xRot()));
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));

            SCP025FRPartRenderState partState = new SCP025FRPartRenderState();
            partState.walkAnimationSpeed = state.walkAnimationSpeed;
            partState.walkAnimationPos = state.walkAnimationPos;
            partState.index = i;

            if (i == state.partPoses.size() - 1) {
                renderTail(partState, poseStack, bufferSource.getBuffer(tailModel.renderType(TEXTURE)), packedLight);
            } else {
                renderBody(partState, poseStack, consumer, packedLight);
            }

            poseStack.popPose();
        }
    }

    private void renderBody(SCP025FRPartRenderState partState, PoseStack poseStack, VertexConsumer consumer, int packedLight) {
        bodyModel.setupAnim(partState);
        bodyModel.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
    }

    private void renderTail(SCP025FRPartRenderState partState, PoseStack poseStack, VertexConsumer consumer, int packedLight) {
        tailModel.setupAnim(partState);
        tailModel.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
    }

    @Override
    protected void extractAdditionalHitboxes(SCP025FR entity, ImmutableList.Builder<HitboxRenderState> hitboxes, float partialTick) {
        super.extractAdditionalHitboxes(entity, hitboxes, partialTick);

        double dx = -Mth.lerp(partialTick, entity.xOld, entity.getX());
        double dy = -Mth.lerp(partialTick, entity.yOld, entity.getY());
        double dz = -Mth.lerp(partialTick, entity.zOld, entity.getZ());

        for (PartEntity<?> part : entity.getParts()) {
            AABB aabb = part.getBoundingBox();
            HitboxRenderState hitbox = new HitboxRenderState(
                    aabb.minX - part.getX(),
                    aabb.minY - part.getY(),
                    aabb.minZ - part.getZ(),
                    aabb.maxX - part.getX(),
                    aabb.maxY - part.getY(),
                    aabb.maxZ - part.getZ(),
                    (float)(dx + Mth.lerp(partialTick, part.xOld, part.getX())),
                    (float)(dy + Mth.lerp(partialTick, part.yOld, part.getY())),
                    (float)(dz + Mth.lerp(partialTick, part.zOld, part.getZ())),
                    0.25f,
                    1.0f,
                    0.0f
            );
            hitboxes.add(hitbox);
        }
    }

    @Override
    protected boolean affectedByCulling(SCP025FR display) {
        return false;
    }
}
