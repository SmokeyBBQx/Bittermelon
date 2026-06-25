package com.site21.bittermelon.common.content.blocks.electronics.slidingdoor.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.site21.bittermelon.common.content.blocks.electronics.slidingdoor.SlidingDoorBlock;
import com.site21.bittermelon.common.content.blocks.electronics.slidingdoor.SlidingDoorBlockEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import static net.minecraft.world.level.block.DecoratedPotBlock.HORIZONTAL_FACING;


public class SlidingDoorRenderer implements BlockEntityRenderer<SlidingDoorBlockEntity, SlidingDoorState> {

    public SlidingDoorRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void extractRenderState(SlidingDoorBlockEntity blockEntity, SlidingDoorState state, float partialTicks,
                                   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        BlockState blockState = blockEntity.getBlockState();
        if (blockState.getValue(SlidingDoorBlock.VISIBLE)) return;

        Vec3 offset = calculateOffset(blockState.getValue(HORIZONTAL_FACING), blockEntity.getAnimationProgress(partialTicks));
        if (blockState.getValue(SlidingDoorBlock.HINGE) == DoorHingeSide.LEFT) {
            offset.scale(-1);
        }
        state.offset = offset;

        if (blockEntity.getLevel() instanceof ClientLevel level) {
            Holder<Biome> biome = level.getBiome(blockEntity.getBlockPos());
            state.movingBlock = createMovingBlock(blockEntity.getBlockPos(), blockState, biome, level);
        }
    }

    @Override
    public SlidingDoorState createRenderState() {
        return new SlidingDoorState();
    }

    private static MovingBlockRenderState createMovingBlock(BlockPos pos, BlockState blockState, Holder<Biome> biome, ClientLevel level) {
        MovingBlockRenderState movingBlockRenderState = new MovingBlockRenderState();
        movingBlockRenderState.randomSeedPos = pos;
        movingBlockRenderState.blockPos = pos;
        movingBlockRenderState.blockState = blockState;
        movingBlockRenderState.biome = biome;
        movingBlockRenderState.cardinalLighting = level.cardinalLighting();
        movingBlockRenderState.lightEngine = level.getLightEngine();
        return movingBlockRenderState;
    }

    @Override
    public void submit(SlidingDoorState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(state.offset);
        collector.submitMovingBlock(poseStack, state.movingBlock);
        poseStack.popPose();
    }

    private static Vec3 calculateOffset(@NotNull Direction facing, float openAmount) {
        return switch (facing) {
            case NORTH -> new Vec3(openAmount, 0, 0);
            case SOUTH -> new Vec3(-openAmount, 0, 0);
            case EAST -> new Vec3(0, 0, openAmount);
            case WEST -> new Vec3(0, 0, -openAmount);
            default -> Vec3.ZERO;
        };
    }
}
