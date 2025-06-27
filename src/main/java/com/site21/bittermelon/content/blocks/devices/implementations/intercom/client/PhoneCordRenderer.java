package com.site21.bittermelon.content.blocks.devices.implementations.intercom.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.site21.bittermelon.content.blocks.devices.implementations.intercom.IntercomBlock;
import com.site21.bittermelon.content.blocks.devices.implementations.intercom.IntercomBlockEntity;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

/**
 * Adapted from net.minecraft.client.renderer.entity.EntityRenderer#renderLeash()
 */
@OnlyIn(Dist.CLIENT)
public class PhoneCordRenderer implements BlockEntityRenderer<IntercomBlockEntity> {
    public PhoneCordRenderer(BlockEntityRendererProvider.Context context) {
        super();
    }

    @Override
    public void render(@NotNull IntercomBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource bufferSource, int i, int i1) {
        if (blockEntity.isPhonePickedUp() && blockEntity.getPhoneUser() != null) {
            renderPhoneCord(blockEntity, partialTick, poseStack, bufferSource);
        }
    }

    private void renderPhoneCord(@NotNull IntercomBlockEntity blockEntity, float partialTick,
                                 @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource) {
        poseStack.pushPose();

        Vec3 playerHandPos = blockEntity.getPhoneUser().getRopeHoldPosition(partialTick);

        BlockPos phonePos = blockEntity.getBlockPos();
        Direction facing = blockEntity.getBlockState().getValue(IntercomBlock.FACING).getOpposite();

        Level level = blockEntity.getLevel();
        if (level == null) return;

        double phoneOffsetX = facing.getStepX() * 0.5;
        double phoneOffsetZ = facing.getStepZ() * 0.5;

        double phoneX = phonePos.getX() + 0.5 + phoneOffsetX;
        double phoneY = phonePos.getY() + 0.5;
        double phoneZ = phonePos.getZ() + 0.5 + phoneOffsetZ;

        poseStack.translate(phoneOffsetX + 0.5, 0.5, phoneOffsetZ + 0.5);

        float deltaX = (float) (playerHandPos.x - phoneX);
        float deltaY = (float) (playerHandPos.y - phoneY);
        float deltaZ = (float) (playerHandPos.z - phoneZ);
        float cordThickness = 0.025F;

        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.leash());
        Matrix4f matrix4f = poseStack.last().pose();
        float horizontalDistance = Mth.invSqrt(deltaX * deltaX + deltaZ * deltaZ) * cordThickness / 2.0F;
        float offsetZ = deltaZ * horizontalDistance;
        float offsetX = deltaX * horizontalDistance;

        BlockPos phoneBlockPos = BlockPos.containing(phoneX, phoneY, phoneZ);
        BlockPos playerBlockPos = BlockPos.containing(playerHandPos);

        int phoneBlockLight = level.getBrightness(LightLayer.BLOCK, phoneBlockPos);
        int playerBlockLight = level.getBrightness(LightLayer.BLOCK, playerBlockPos);
        int phoneSkyLight = level.getBrightness(LightLayer.SKY, phoneBlockPos);
        int playerSkyLight = level.getBrightness(LightLayer.SKY, playerBlockPos);

        for (int i = 0; i <= 24; i++) {
            addVertexPair(vertexconsumer, matrix4f, deltaX, deltaY, deltaZ, phoneBlockLight, playerBlockLight,
                    phoneSkyLight, playerSkyLight, cordThickness, cordThickness, offsetZ, offsetX, i, false);
        }

        for (int j = 24; j >= 0; j--) {
            addVertexPair(vertexconsumer, matrix4f, deltaX, deltaY, deltaZ, phoneBlockLight, playerBlockLight,
                    phoneSkyLight, playerSkyLight, cordThickness, 0.0F, offsetZ, offsetX, j, true);
        }

        poseStack.popPose();
    }

    private static void addVertexPair(
            @NotNull VertexConsumer buffer,
            Matrix4f pose,
            float deltaX,
            float deltaY,
            float deltaZ,
            int startBlockLight,
            int endBlockLight,
            int startSkyLight,
            int endSkyLight,
            float cordRadius,
            float taperRadius,
            float offsetX,
            float offsetZ,
            int segmentIndex,
            boolean isBackFace
    ) {
        float segmentProgress = (float)segmentIndex / 24.0F;
        int interpolatedBlockLight = (int)Mth.lerp(segmentProgress, (float)startBlockLight, (float)endBlockLight);
        int interpolatedSkyLight = (int)Mth.lerp(segmentProgress, (float)startSkyLight, (float)endSkyLight);
        int packedLight = LightTexture.pack(interpolatedBlockLight, interpolatedSkyLight);

        float brightnessMultiplier = segmentIndex % 2 == (isBackFace ? 1 : 0) ? 0.7F : 1.0F;
        float gray = 0.25F * brightnessMultiplier;

        float xPos = deltaX * segmentProgress;
        float yPos = deltaY > 0.0F ? deltaY * segmentProgress * segmentProgress : deltaY - deltaY * (1.0F - segmentProgress) * (1.0F - segmentProgress);
        float zPos = deltaZ * segmentProgress;

        buffer.addVertex(pose, xPos - offsetX, yPos + taperRadius, zPos + offsetZ).setColor(gray, gray, gray, 1.0F).setLight(packedLight);
        buffer.addVertex(pose, xPos + offsetX, yPos + cordRadius - taperRadius, zPos - offsetZ).setColor(gray, gray, gray, 1.0F).setLight(packedLight);
    }

    @Override
    public int getViewDistance() {
        return 12;
    }
}
