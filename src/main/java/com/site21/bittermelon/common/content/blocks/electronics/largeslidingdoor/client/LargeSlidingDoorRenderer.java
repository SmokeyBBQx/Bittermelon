package com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.event.LayerDefinitions;
import com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.LargeSlidingDoorBlock;
import com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.LargeSlidingDoorBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class LargeSlidingDoorRenderer implements BlockEntityRenderer<LargeSlidingDoorBlockEntity> {
    private final LargeSlidingDoorModel model;

    public LargeSlidingDoorRenderer(BlockEntityRendererProvider.@NotNull Context context) {
        model = new LargeSlidingDoorModel(context.bakeLayer(LayerDefinitions.LARGE_SLIDING_DOOR_LAYER));
    }

    @Override
    public void render(@NotNull LargeSlidingDoorBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay, @NotNull Vec3 cameraPos) {
        float smoothProgress = blockEntity.getDoorOpenAmount(partialTick);
        boolean zAxis = blockEntity.getBlockState().getValue(LargeSlidingDoorBlock.Z_AXIS);

        poseStack.pushPose();

        if (zAxis) {
            poseStack.mulPose(Axis.YP.rotationDegrees(90));
            poseStack.translate(-0.5, -0.5, 0.5);
        } else {
            poseStack.translate(0.5, -0.5, 0.5);
        }

        poseStack.mulPose(Axis.XP.rotationDegrees(180));

        Material material = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/large_sliding_door"));
        VertexConsumer vertexConsumer = material.buffer(buffer, RenderType::entityCutout);

        model.setupAnim(smoothProgress);
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay);

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
