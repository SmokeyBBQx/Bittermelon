package com.site21.bittermelon.common.content.blocks.wallwriting.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.site21.bittermelon.common.content.blocks.wallwriting.WallWritingBlock;
import com.site21.bittermelon.common.content.blocks.wallwriting.WallWritingBlockEntity;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import static net.minecraft.client.renderer.blockentity.AbstractSignRenderer.getDarkColor;

public class WallWritingRenderer implements BlockEntityRenderer<WallWritingBlockEntity, WallWritingRenderState> {
    private static final float TEXT_SCALE = 0.010416667f;
    private final Font font;

    public WallWritingRenderer(BlockEntityRendererProvider.@NotNull Context context) {
        font = context.font();
    }

    @Override
    public void extractRenderState(WallWritingBlockEntity blockEntity, WallWritingRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.text = blockEntity.getText();
        state.facing = blockEntity.getBlockState().getValue(WallWritingBlock.FACING);
        state.face = blockEntity.getBlockState().getValue(WallWritingBlock.FACE);
    }

    @Override
    public WallWritingRenderState createRenderState() {
        return new WallWritingRenderState();
    }

    @Override
    public void submit(WallWritingRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector,
                       CameraRenderState cameraRenderState) {
        if (state.text == null) return;

        poseStack.pushPose();

        poseStack.translate(0.5, 0.5, 0.5);
        applyRotation(poseStack, state.facing, state.face);
        poseStack.translate(0, 0, -0.499);
        poseStack.scale(TEXT_SCALE, -TEXT_SCALE, TEXT_SCALE);

        submitText(state, poseStack, submitNodeCollector);

        poseStack.popPose();
    }

    private void submitText(WallWritingRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
        int color = state.text.getColor().getTextColor();
        int darkColor = getDarkColor(state.text);
        int lineCount = 4;
        int startY = -lineCount * 10 / 2;

        boolean glowing = state.text.hasGlowingText();
        int outlineColor = glowing ? darkColor : 0;

        for (int line = 0; line < lineCount; line++) {
            Component message = state.text.getMessage(line, false);

            if (!message.getString().isEmpty()) {
                FormattedCharSequence sequence = message.getVisualOrderText();
                float x = (float) (-font.width(sequence) / 2);
                float y = (float) (startY + line * 10);

                submitNodeCollector.submitText(
                        poseStack,
                        x,
                        y,
                        sequence,
                        false,
                        Font.DisplayMode.POLYGON_OFFSET,
                        state.lightCoords,
                        color,
                        0,
                        outlineColor
                );
            }
        }
    }

    private void applyRotation(PoseStack poseStack, Direction facing, @NotNull AttachFace attachFace) {
        switch (attachFace) {
            case WALL:
                switch (facing) {
                    case NORTH:
                        poseStack.mulPose(Axis.YP.rotationDegrees(0));
                        break;
                    case SOUTH:
                        poseStack.mulPose(Axis.YP.rotationDegrees(180));
                        break;
                    case WEST:
                        poseStack.mulPose(Axis.YP.rotationDegrees(90));
                        break;
                    case EAST:
                        poseStack.mulPose(Axis.YP.rotationDegrees(270));
                        break;
                }
                break;
            case FLOOR:
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
                poseStack.mulPose(Axis.ZP.rotationDegrees(180));
                switch (facing) {
                    case NORTH:
                        poseStack.mulPose(Axis.ZP.rotationDegrees(0));
                        break;
                    case SOUTH:
                        poseStack.mulPose(Axis.ZP.rotationDegrees(180));
                        break;
                    case WEST:
                        poseStack.mulPose(Axis.ZP.rotationDegrees(270));
                        break;
                    case EAST:
                        poseStack.mulPose(Axis.ZP.rotationDegrees(90));
                        break;
                }
                break;
            case CEILING:
                poseStack.mulPose(Axis.XP.rotationDegrees(270));
                switch (facing) {
                    case NORTH:
                        poseStack.mulPose(Axis.ZP.rotationDegrees(180));
                        break;
                    case SOUTH:
                        poseStack.mulPose(Axis.ZP.rotationDegrees(0));
                        break;
                    case WEST:
                        poseStack.mulPose(Axis.ZP.rotationDegrees(90));
                        break;
                    case EAST:
                        poseStack.mulPose(Axis.ZP.rotationDegrees(270));
                        break;
                }
                break;
        }

        poseStack.mulPose(Axis.YP.rotationDegrees(180));
    }
}
