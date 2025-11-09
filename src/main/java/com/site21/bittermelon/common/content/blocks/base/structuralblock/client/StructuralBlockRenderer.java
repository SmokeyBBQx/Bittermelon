package com.site21.bittermelon.common.content.blocks.base.structuralblock.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.site21.bittermelon.common.content.blocks.base.structuralblock.StructuralBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class StructuralBlockRenderer implements BlockEntityRenderer<StructuralBlockEntity> {
    public StructuralBlockRenderer(BlockEntityRendererProvider.Context context) {
        super();
    }

    @Override
    public void render(@NotNull StructuralBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity.getBreakProgress() > 0) {


            BlockPos pos = blockEntity.getBlockPos();
            Level level = blockEntity.getLevel();
            if (level == null) return;

            poseStack.pushPose();

            int breakStage = blockEntity.getBreakStage();

            PoseStack.Pose pose = poseStack.last();
            VertexConsumer vertexConsumer = new SheetedDecalTextureGenerator(
                    Minecraft.getInstance().renderBuffers().crumblingBufferSource().getBuffer(
                            ModelBakery.DESTROY_TYPES.get(breakStage)
                    ),
                    pose,
                    1.0F
            );

            // Render the breaking texture
            Minecraft.getInstance().getBlockRenderer().renderBreakingTexture(
                    blockEntity.getBlockState(),
                    pos,
                    level,
                    poseStack,
                    vertexConsumer,
                    ModelData.EMPTY
            );

            poseStack.popPose();
        }
    }
}
