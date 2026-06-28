package com.site21.bittermelon.common.content.entities.cage.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.site21.bittermelon.common.content.entities.cage.Cage;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class CageRenderer extends EntityRenderer<Cage, CageRenderState> {
    public CageRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void submit(CageRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        super.submit(state, poseStack, collector, camera);

        for (BlockInfo info : state.blocks) {
            poseStack.pushPose();
            poseStack.translate(
                    info.offset().getX(),
                    info.offset().getY(),
                    info.offset().getZ()
            );

            collector.submitMovingBlock(
                    poseStack,
                    state.movingBlocks.get(info.state())
            );

            poseStack.popPose();
        }
    }

    @Override
    public @NotNull CageRenderState createRenderState() {
        return new CageRenderState();
    }

    @Override
    public void extractRenderState(Cage entity, CageRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.pos = entity.getOnPos();
        reusedState.blocks = entity.getBlocks();
        for (BlockInfo info : reusedState.blocks) {
            reusedState.movingBlocks.put(info.state(),
                    createMovingBlock(reusedState.pos.offset(info.offset()), info.state(), (ClientLevel) entity.level()));
        }
    }

    private static MovingBlockRenderState createMovingBlock(BlockPos pos, BlockState blockState, ClientLevel level) {
        MovingBlockRenderState state = new MovingBlockRenderState();
        state.randomSeedPos = pos;
        state.blockPos = pos;
        state.blockState = blockState;
        state.biome = level.getBiome(pos);
        state.cardinalLighting = level.cardinalLighting();
        state.lightEngine = level.getLightEngine();
        return state;
    }
}
