package com.site21.bittermelon.common.content.blocks.electronics.television.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.site21.bittermelon.common.content.blocks.electronics.television.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class TelevisionRenderer implements BlockEntityRenderer<TelevisionBlockEntity> {
    public TelevisionRenderer(BlockEntityRendererProvider.@NotNull Context context) {

    }

    @Override
    public void render(@NotNull TelevisionBlockEntity tv, float partialTick, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay, Vec3 cameraPos) {
        BlockState state = tv.getBlockState();

        if (!state.getValue(TelevisionBlock.POWERED)) return;

        if (state.getBlock() instanceof StandingTelevisionBlock) {
            renderStandingTelevision(tv, state, partialTick, poseStack, buffer);
        } else {
            renderWallTelevision(tv, state, partialTick, poseStack, buffer);
        }
    }

    private void renderStandingTelevision(@NotNull TelevisionBlockEntity tv, BlockState state, float partialTick, @NotNull PoseStack poseStack,
                                          @NotNull MultiBufferSource buffer) {
        int rotation = state.getValue(StandingTelevisionBlock.ROTATION);
        Holder<Media> media = tv.getMedia();
        if (media == null) return;

        Material material = MediaSheets.getMaterial(media);
        VertexConsumer vertexConsumer = material.buffer(buffer, RenderType::entitySolid);

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(-rotation * 22.5f + 180f));
        poseStack.translate(0, -0.125, 0.381);

        PoseStack.Pose last = poseStack.last();
        Vector3f normal = last.normal().transform(new Vector3f(0, 0, -1));
        float size = 0.33f;
        addVertex(vertexConsumer, last, -size, -size + 0.125f, 0, 0f, 1f, normal);
        addVertex(vertexConsumer, last, size, -size + 0.125f, 0, 1f, 1f, normal);
        addVertex(vertexConsumer, last, size, size, 0, 1f, 0f, normal);
        addVertex(vertexConsumer, last, -size, size, 0, 0f, 0f, normal);
        poseStack.popPose();
    }

    private void renderWallTelevision(@NotNull TelevisionBlockEntity tv, @NotNull BlockState state, float partialTick, @NotNull PoseStack poseStack,
                                      @NotNull MultiBufferSource buffer) {
        Direction facing = state.getValue(WallTelevisionBlock.FACING);
        Holder<Media> media = tv.getMedia();
        if (media == null) return;

        Material material = MediaSheets.getMaterial(media);
        VertexConsumer vertexConsumer = material.buffer(buffer, RenderType::entitySolid);

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
        poseStack.translate(0, 0, 0.251);

        PoseStack.Pose last = poseStack.last();
        Vector3f normal = last.normal().transform(new Vector3f(0, 0, -1));

        float size = 0.33f;
//        float size = 0.28f;
        addVertex(vertexConsumer, last, -size, -size + 0.128f, 0, 0f, 1f, normal);
        addVertex(vertexConsumer, last, size, -size + 0.128f, 0, 1f, 1f, normal);
        addVertex(vertexConsumer, last, size, size, 0, 1f, 0f, normal);
        addVertex(vertexConsumer, last, -size, size, 0, 0f, 0f, normal);

        poseStack.popPose();
    }

    private void addVertex(@NotNull VertexConsumer consumer, PoseStack.@NotNull Pose pose, float x, float y, float z, float u, float v, @NotNull Vector3f normal) {
        consumer.addVertex(pose.pose(), x, y, z)
                .setColor(255, 255, 255, 255)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setNormal(pose, normal.x, normal.y, normal.z);
    }


}
