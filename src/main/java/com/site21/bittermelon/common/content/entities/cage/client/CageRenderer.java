package com.site21.bittermelon.common.content.entities.cage.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.site21.bittermelon.common.content.entities.cage.Cage;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class CageRenderer extends EntityRenderer<Cage, CageRenderState> {
    private final BlockRenderDispatcher blockRenderer;

    public CageRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(@NotNull CageRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        for (BlockInfo info : renderState.blocks) {
            poseStack.pushPose();
            poseStack.translate(
                    info.offset().getX(),
                    info.offset().getY(),
                    info.offset().getZ()
            );

            blockRenderer.renderSingleBlock(
                    Blocks.OAK_WOOD.defaultBlockState(),
                    poseStack,
                    bufferSource,
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    renderState.level,
                    renderState.pos.offset(info.offset())
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
        reusedState.level = entity.level();
        reusedState.pos = entity.getOnPos();
        reusedState.blocks = entity.getBlocks();
    }
}
