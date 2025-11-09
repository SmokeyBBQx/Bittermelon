package com.site21.bittermelon.common.content.blocks.wallwriting.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.site21.bittermelon.common.content.blocks.wallwriting.WallWritingBlock;
import com.site21.bittermelon.common.content.blocks.wallwriting.WallWritingBlockEntity;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.client.renderer.blockentity.SignRenderer.getDarkColor;

@OnlyIn(Dist.CLIENT)
public class WallWritingRenderer implements BlockEntityRenderer<WallWritingBlockEntity> {
    private final Font font;
    private static final int TEXT_COLOR = 0x000000;
    private static final float TEXT_SCALE = 0.010416667F;

    public WallWritingRenderer(BlockEntityRendererProvider.@NotNull Context context) {
        font = context.getFont();
    }

    @Override
    public void render(@NotNull WallWritingBlockEntity wallWritingBlockEntity, float partialTick, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (wallWritingBlockEntity.getText() == null) return;

        BlockState state = wallWritingBlockEntity.getBlockState();
        poseStack.pushPose();

        poseStack.translate(0.5, 0.5, 0.5);
        applyRotation(poseStack, state.getValue(WallWritingBlock.FACING), state.getValue(WallWritingBlock.FACE));
        poseStack.translate(0, 0, -0.499);
        poseStack.scale(TEXT_SCALE, -TEXT_SCALE, TEXT_SCALE);
        int lineCount = 4;
        int startY = -lineCount * 10 / 2;

        for (int line = 0; line < lineCount; line++) {
            SignText text = wallWritingBlockEntity.getText();
            Component message = text.getMessage(line, false);

            if (!message.getString().isEmpty()) {
                FormattedCharSequence sequence = message.getVisualOrderText();
                int x = -font.width(sequence) / 2;
                int y = startY + line * 10;
                int color = text.getColor().getTextColor();

                if (text.hasGlowingText()) {
                    font.drawInBatch8xOutline(
                            sequence,
                            x,
                            y,
                            color,
                            getDarkColor(text),
                            poseStack.last().pose(),
                            bufferSource,
                            packedLight);
                } else {
                    font.drawInBatch(
                            sequence,
                            x,
                            y,
                            color,
                            false,
                            poseStack.last().pose(),
                            bufferSource,
                            Font.DisplayMode.POLYGON_OFFSET,
                            0,
                            packedLight
                    );
                }
            }
        }

        poseStack.popPose();
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
