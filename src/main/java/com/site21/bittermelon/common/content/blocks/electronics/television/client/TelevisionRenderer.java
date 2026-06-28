package com.site21.bittermelon.common.content.blocks.electronics.television.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.site21.bittermelon.common.content.blocks.electronics.television.*;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

public class TelevisionRenderer implements BlockEntityRenderer<TelevisionBlockEntity, TelevisionRenderState> {
    public TelevisionRenderer(BlockEntityRendererProvider.@NotNull Context context) {}

    @Override
    public void extractRenderState(TelevisionBlockEntity blockEntity, TelevisionRenderState state, float partialTicks,
                                   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        BlockState blockState = blockEntity.getBlockState();
        state.powered = blockState.getValue(TelevisionBlock.POWERED);
        if (blockState.getBlock() instanceof StandingTelevisionBlock) {
            state.rotation = blockState.getValue(StandingTelevisionBlock.ROTATION);
            state.standing = true;
        } else {
            state.rotation = (int) blockState.getValue(WallTelevisionBlock.FACING).toYRot();
            state.standing = false;
        }

        state.media = blockEntity.getMedia();
    }

    @Override
    public TelevisionRenderState createRenderState() {
        return new TelevisionRenderState();
    }

    @Override
    public void submit(TelevisionRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (!state.powered || state.media == null) return;

        SpriteId material = MediaSheets.getMaterial(state.media);
        collector.submitCustomGeometry(
                poseStack,
                material.renderType(RenderTypes::entitySolid),
                (pose, buffer) -> {
                    if (state.standing) {
                        renderStandingTelevision(state, pose, buffer);
                    } else {
                        renderWallTelevision(state, pose, buffer);
                    }
                }
        );
    }

    private void renderStandingTelevision(TelevisionRenderState state, PoseStack.Pose pose, VertexConsumer buffer) {
        pose.translate(0.5f, 0.5f, 0.5f);
        pose.mulPose((Matrix4fc) Axis.YP.rotationDegrees(-state.rotation * 22.5f + 180f));
        pose.translate(0, -0.125f, 0.381f);

        Vector3f normal = pose.normal().transform(new Vector3f(0, 0, -1));
        float size = 0.33f;
        addVertex(buffer, pose, -size, -size + 0.125f, 0, 0f, 1f, normal);
        addVertex(buffer, pose, size, -size + 0.125f, 0, 1f, 1f, normal);
        addVertex(buffer, pose, size, size, 0, 1f, 0f, normal);
        addVertex(buffer, pose, -size, size, 0, 0f, 0f, normal);
    }

    private void renderWallTelevision(TelevisionRenderState state, PoseStack.Pose pose, VertexConsumer buffer) {
        pose.translate(0.5f, 0.5f, 0.5f);
        pose.mulPose((Matrix4fc) Axis.YP.rotationDegrees(-state.rotation));
        pose.translate(0, 0, 0.251f);

        Vector3f normal = pose.normal().transform(new Vector3f(0, 0, -1));

        float size = 0.33f;
//        float size = 0.28f;
        addVertex(buffer, pose, -size, -size + 0.128f, 0, 0f, 1f, normal);
        addVertex(buffer, pose, size, -size + 0.128f, 0, 1f, 1f, normal);
        addVertex(buffer, pose, size, size, 0, 1f, 0f, normal);
        addVertex(buffer, pose, -size, size, 0, 0f, 0f, normal);
    }

    private void addVertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, float u, float v, Vector3f normal) {
        consumer.addVertex(pose.pose(), x, y, z)
                .setColor(255, 255, 255, 255)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightCoordsUtil.FULL_BRIGHT)
                .setNormal(pose, normal.x, normal.y, normal.z);
    }
}
