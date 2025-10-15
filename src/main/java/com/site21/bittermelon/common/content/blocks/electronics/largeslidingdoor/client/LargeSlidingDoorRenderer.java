package com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.LargeSlidingDoorBlock;
import com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.LargeSlidingDoorBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class LargeSlidingDoorRenderer implements BlockEntityRenderer<LargeSlidingDoorBlockEntity> {
    private BlockStateModel leftDoorModel;
    private BlockStateModel rightDoorModel;
    private BlockStateModel frameModel;

    public LargeSlidingDoorRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(@NotNull LargeSlidingDoorBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay, @NotNull Vec3 cameraPos) {

//        float smoothProgress = blockEntity.getDoorOpenAmount(partialTick);
//        boolean zAxis = blockEntity.getBlockState().getValue(LargeSlidingDoorBlock.Z_AXIS);
//        BlockPos pos = blockEntity.getBlockPos();
//        BlockState state = blockEntity.getBlockState();
//
//        poseStack.pushPose();
//        poseStack.translate(0.5, -1, 0.5);
//
//        if (zAxis) {
//            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(90));
//        }
//
//        poseStack.translate(-0.5, 0, -0.5);
//
//        if (smoothProgress < 1) {
//            // Right Door Model
//            poseStack.pushPose();
//            poseStack.translate(-smoothProgress * 1.3, 0, 0);
//            ModelBlockRenderer.renderModel(
//                    poseStack.last(), bufferSource, rightDoorModel,
//                    1.0F, 1.0F, 1.0F, packedLight, packedOverlay,
//                    blockEntity.getLevel(), pos, state);
//            poseStack.popPose();
//
//            // Left Door Model
//            poseStack.pushPose();
//            poseStack.translate(smoothProgress * 1.3, 0, 0);
//            ModelBlockRenderer.renderModel(
//                    poseStack.last(), bufferSource, leftDoorModel,
//                    1.0F, 1.0F, 1.0F, packedLight, packedOverlay,
//                    blockEntity.getLevel(), pos, state);
//            poseStack.popPose();
//        }
//
//        // Frame Model
//        ModelBlockRenderer.renderModel(
//                poseStack.last(), bufferSource, frameModel,
//                1.0F, 1.0F, 1.0F, packedLight, packedOverlay,
//                blockEntity.getLevel(), pos, state);
//
//        poseStack.popPose();
    }


    @Override
    public @NotNull AABB getRenderBoundingBox(@NotNull LargeSlidingDoorBlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        boolean zAxis = blockEntity.getBlockState().getValue(LargeSlidingDoorBlock.Z_AXIS);

        // Rotate according to axis.
        if (zAxis) {
            return new AABB(pos.getX(), pos.getY() - 3, pos.getZ() - 1.3,
                    pos.getX() + 1, pos.getY() + 2, pos.getZ() + 2.3);
        } else {
            return new AABB(pos.getX() - 1.3, pos.getY() - 3, pos.getZ(),
                    pos.getX() + 2.3, pos.getY() + 2, pos.getZ() + 1);
        }
    }
}
