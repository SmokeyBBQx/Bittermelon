package com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.LargeSlidingDoorBlock;
import com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.LargeSlidingDoorBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class LargeSlidingDoorRenderer implements BlockEntityRenderer<LargeSlidingDoorBlockEntity> {
    public static final ModelResourceLocation LEFT_DOOR_MODEL = new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/large_sliding_door_left"), "standalone");
    public static final ModelResourceLocation RIGHT_DOOR_MODEL = new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/large_sliding_door_right"), "standalone");
    public static final ModelResourceLocation FRAME_MODEL = new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/large_sliding_door_frame"), "standalone");
    private BakedModel leftDoorModel;
    private BakedModel rightDoorModel;
    private BakedModel frameModel;

    public LargeSlidingDoorRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(@NotNull LargeSlidingDoorBlockEntity blockEntity, float partialTick,
                       @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int light, int overlay) {
        if (leftDoorModel == null || rightDoorModel == null || frameModel == null) {
            leftDoorModel = Minecraft.getInstance().getModelManager().getModel(LEFT_DOOR_MODEL);
            rightDoorModel = Minecraft.getInstance().getModelManager().getModel(RIGHT_DOOR_MODEL);
            frameModel = Minecraft.getInstance().getModelManager().getModel(FRAME_MODEL);
        }

        // TODO: Lighting is unnatural - find fix

        float smoothProgress = blockEntity.getDoorOpenAmount(partialTick);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.solid());
        boolean zAxis = blockEntity.getBlockState().getValue(LargeSlidingDoorBlock.Z_AXIS);

        poseStack.pushPose();

        // Model exceeds size bounds - translate to prevent clipping.
        poseStack.translate(0.5, -1, 0.5);

        // Rotate according to axis.
        if (zAxis) {
            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(90));
        }

        poseStack.translate(-0.5, 0, -0.5);

        if (smoothProgress < 1) {
            // Right Door Model
            poseStack.pushPose();
            poseStack.translate(-smoothProgress * 1.3, 0, 0);
            Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
                    poseStack.last(), vertexConsumer, blockEntity.getBlockState(),
                    rightDoorModel, 1.0F, 1.0F, 1.0F, light, overlay);
            poseStack.popPose();

            // Left Door Model
            poseStack.pushPose();
            poseStack.translate(smoothProgress * 1.3, 0, 0);
            Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
                    poseStack.last(), vertexConsumer, blockEntity.getBlockState(),
                    leftDoorModel, 1.0F, 1.0F, 1.0F,  light, overlay);
            poseStack.popPose();
        }

        // Frame Model
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
                poseStack.last(), vertexConsumer, blockEntity.getBlockState(),
                frameModel, 1.0F, 1.0F, 1.0F,  light, overlay);

        poseStack.popPose();
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
