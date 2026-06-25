package com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.event.LayerDefinitions;
import com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.LargeSlidingDoorBlock;
import com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.LargeSlidingDoorBlockEntity;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class LargeSlidingDoorRenderer implements BlockEntityRenderer<LargeSlidingDoorBlockEntity, LargeSlidingDoorState> {
    public static final SpriteId TEXTURE = Sheets.BLOCKS_MAPPER.apply(Bittermelon.identifier("large_sliding_door"));
    private final SpriteGetter sprites;
    private final LargeSlidingDoorModel model;

    public LargeSlidingDoorRenderer(BlockEntityRendererProvider.@NotNull Context context) {
        sprites = context.sprites();
        model = new LargeSlidingDoorModel(context.bakeLayer(LayerDefinitions.LARGE_SLIDING_DOOR_LAYER));
    }

    @Override
    public void extractRenderState(LargeSlidingDoorBlockEntity blockEntity, LargeSlidingDoorState state, float partialTicks,
                                   Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.openness = blockEntity.getDoorOpenAmount(partialTicks);
        state.zAxis = blockEntity.getBlockState().getValue(LargeSlidingDoorBlock.Z_AXIS);
    }

    @Override
    public LargeSlidingDoorState createRenderState() {
        return new LargeSlidingDoorState();
    }

    @Override
    public void submit(LargeSlidingDoorState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        if (state.zAxis) {
            poseStack.mulPose(Axis.YP.rotationDegrees(90));
            poseStack.translate(-0.5, -0.5, 0.5);
        } else {
            poseStack.translate(0.5, -0.5, 0.5);
        }

        poseStack.mulPose(Axis.XP.rotationDegrees(180));

        collector.submitModel(
                model,
                state.openness,
                poseStack,
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                -1,
                TEXTURE,
                sprites,
                0,
                state.breakProgress);

        poseStack.popPose();
    }

    @Override
    public @NotNull AABB getRenderBoundingBox(@NotNull LargeSlidingDoorBlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        boolean zAxis = blockEntity.getBlockState().getValue(LargeSlidingDoorBlock.Z_AXIS);

        if (zAxis) {
            return new AABB(pos.getX(), pos.getY() - 3, pos.getZ() - 1.3,
                    pos.getX() + 1, pos.getY() + 2, pos.getZ() + 2.3);
        } else {
            return new AABB(pos.getX() - 1.3, pos.getY() - 3, pos.getZ(),
                    pos.getX() + 2.3, pos.getY() + 2, pos.getZ() + 1);
        }
    }
}
