package com.site21.bittermelon.common.content.blocks.electronics.thermometer.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.site21.bittermelon.common.content.blocks.electronics.thermometer.ThermometerBlock;
import com.site21.bittermelon.common.content.blocks.electronics.thermometer.ThermometerBlockEntity;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;


public class ThermometerRenderer implements BlockEntityRenderer<ThermometerBlockEntity> {
    private final Font font;

    public ThermometerRenderer(BlockEntityRendererProvider.@NotNull Context context) {
        this.font = context.getFont();
    }

    @Override
    public void render(@NotNull ThermometerBlockEntity thermometer, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay, @NotNull Vec3 cameraPos) {
        BlockState blockState = thermometer.getBlockState();
        Direction facing = blockState.getValue(ThermometerBlock.FACING);

        poseStack.pushPose();

        float temperature = thermometer.getTemperature();
        String message = String.format("%.1f°C", temperature);

        switch (facing) {
            case NORTH -> poseStack.translate(0.61, 0.525, 0.875);
            case SOUTH -> {
                poseStack.translate(0.39, 0.525, 0.125);
                poseStack.mulPose(Axis.YN.rotationDegrees(180));
            }
            case EAST -> {
                poseStack.translate(0.125, 0.525, 0.61);
                poseStack.mulPose(Axis.YN.rotationDegrees(90));
            }
            case WEST -> {
                poseStack.translate(0.875, 0.525, 0.39);
                poseStack.mulPose(Axis.YN.rotationDegrees(270));
            }
        }

        poseStack.mulPose(Axis.ZN.rotationDegrees(180f));

        int length = message.length();
        float baseScale = 0.0075f;
        float scale = length > 6 ? baseScale * (1.0f - ((length - 6) * 0.2f)) : baseScale;

        poseStack.scale(scale, scale, scale);

        font.drawInBatch(
                message,
                0,
                0,
                0xFF000000,
                false,
                poseStack.last().pose(),
                bufferSource,
                Font.DisplayMode.POLYGON_OFFSET,
                0,
                15728880
        );

        poseStack.popPose();
    }
}
