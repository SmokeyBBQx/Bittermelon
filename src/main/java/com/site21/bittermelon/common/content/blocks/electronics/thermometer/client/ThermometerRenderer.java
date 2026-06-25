package com.site21.bittermelon.common.content.blocks.electronics.thermometer.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.site21.bittermelon.common.content.blocks.electronics.thermometer.ThermometerBlock;
import com.site21.bittermelon.common.content.blocks.electronics.thermometer.ThermometerBlockEntity;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;


public class ThermometerRenderer implements BlockEntityRenderer<ThermometerBlockEntity, ThermometerRenderState> {
    public ThermometerRenderer(BlockEntityRendererProvider.@NotNull Context context) {
    }

    @Override
    public void extractRenderState(ThermometerBlockEntity blockEntity, ThermometerRenderState state, float partialTicks,
                                   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        state.temperature = String.format("%.1f°C", blockEntity.getTemperature());
        switch (blockEntity.getBlockState().getValue(ThermometerBlock.FACING)) {
            case NORTH -> {
                state.offset = new Vec3(0.61, 0.525, 0.875);
                state.rotation = 0.0f;
            }
            case SOUTH -> {
                state.offset = new Vec3(0.39, 0.525, 0.125);
                state.rotation = 180.0f;
            }
            case EAST -> {
                state.offset = new Vec3(0.125, 0.525, 0.61);
                state.rotation = 90.0f;
            }
            case WEST -> {
                state.offset = new Vec3(0.875, 0.525, 0.39);
                state.rotation = 270.0f;
            }
        }
    }

    @Override
    public ThermometerRenderState createRenderState() {
        return new ThermometerRenderState();
    }

    @Override
    public void submit(ThermometerRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(state.offset);
        poseStack.mulPose(Axis.YN.rotationDegrees(state.rotation));
        poseStack.mulPose(Axis.ZN.rotationDegrees(180f));

        int length = state.temperature.length();
        float baseScale = 0.0075f;
        float scale = length > 6 ? baseScale * (1.0f - ((length - 6) * 0.2f)) : baseScale;

        poseStack.scale(scale, scale, scale);
        collector.submitText(
                poseStack,
                0,
                0,
                FormattedCharSequence.forward(state.temperature, Style.EMPTY),
                false,
                Font.DisplayMode.POLYGON_OFFSET,
                state.lightCoords,
                0xFF000000,
                0,
                0
        );

        poseStack.popPose();
    }
}
