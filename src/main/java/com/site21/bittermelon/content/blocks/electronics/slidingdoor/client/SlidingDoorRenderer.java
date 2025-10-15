package com.site21.bittermelon.content.blocks.electronics.slidingdoor.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.site21.bittermelon.content.blocks.electronics.slidingdoor.SlidingDoorBlock;
import com.site21.bittermelon.content.blocks.electronics.slidingdoor.SlidingDoorBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SlidingDoorRenderer implements BlockEntityRenderer<SlidingDoorBlockEntity> {

    public SlidingDoorRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(@NotNull SlidingDoorBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay, @NotNull Vec3 cameraPos) {
        BlockState state = blockEntity.getBlockState();
        if (state.getValue(SlidingDoorBlock.VISIBLE)) return;

        poseStack.pushPose();

        float openAmount = blockEntity.getAnimationProgress(partialTick);
        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);

        Vec3 offset = calculateOffset(facing, openAmount);
        if (state.getValue(SlidingDoorBlock.HINGE) == DoorHingeSide.LEFT) {
            offset = offset.scale(-1);
        }

        poseStack.translate(offset.x, offset.y, offset.z);

        state = state.setValue(SlidingDoorBlock.VISIBLE, true);
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
                state,
                poseStack,
                bufferSource,
                packedLight,
                OverlayTexture.NO_OVERLAY
        );

        poseStack.popPose();
    }

    private Vec3 calculateOffset(@NotNull Direction facing, float openAmount) {
        return switch (facing) {
            case NORTH -> new Vec3(openAmount, 0, 0);
            case SOUTH -> new Vec3(-openAmount, 0, 0);
            case EAST -> new Vec3(0, 0, openAmount);
            case WEST -> new Vec3(0, 0, -openAmount);
            default -> Vec3.ZERO;
        };
    }
}
