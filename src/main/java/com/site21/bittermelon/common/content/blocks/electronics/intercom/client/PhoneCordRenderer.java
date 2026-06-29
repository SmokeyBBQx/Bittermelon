package com.site21.bittermelon.common.content.blocks.electronics.intercom.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.site21.bittermelon.common.content.blocks.electronics.intercom.IntercomBlock;
import com.site21.bittermelon.common.content.blocks.electronics.intercom.IntercomBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * Adapted from net.minecraft.client.renderer.entity.EntityRenderer#renderLeash()
 */
public class PhoneCordRenderer implements BlockEntityRenderer<IntercomBlockEntity, PhoneCordRenderState> {

    public PhoneCordRenderer(BlockEntityRendererProvider.Context context) {
        super();
    }

    @Override
    public PhoneCordRenderState createRenderState() {
        return new PhoneCordRenderState();
    }

    @Override
    public void extractRenderState(IntercomBlockEntity blockEntity, PhoneCordRenderState state, float partialTicks,
                                   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        Player player = blockEntity.getPhoneUser();
        state.playerPos = player.getRopeHoldPosition(partialTicks);
        state.facing = blockEntity.getBlockState().getValue(IntercomBlock.FACING).getOpposite();

        assert blockEntity.getLevel() != null;

        BlockPos eyePos = BlockPos.containing(player.getEyePosition(partialTicks));
        state.startBlockLight = player.isOnFire()
                ? 15
                : player.level().getBrightness(LightLayer.BLOCK, eyePos);
        state.endBlockLight = blockEntity.getLevel().getBrightness(LightLayer.BLOCK, blockEntity.getBlockPos());
        state.startSkyLight = player.level().getBrightness(LightLayer.SKY, eyePos);
        state.endSkyLight = blockEntity.getLevel().getBrightness(LightLayer.SKY, blockEntity.getBlockPos());
    }

    @Override
    public void submit(PhoneCordRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
        collector.submitCustomGeometry(poseStack, RenderTypes.leash(), (pose, buffer) -> renderCord(state, pose, buffer));
    }

    private void renderCord(PhoneCordRenderState state, PoseStack.Pose pose, VertexConsumer buffer) {
        BlockPos phonePos = state.blockPos;
        Direction facing = state.facing;

        float phoneOffsetX = facing.getStepX() * 0.5f;
        float phoneOffsetZ = facing.getStepZ() * 0.5f;

        float phoneX = phonePos.getX() + 0.5f + phoneOffsetX;
        float phoneY = phonePos.getY() + 0.5f;
        float phoneZ = phonePos.getZ() + 0.5f + phoneOffsetZ;

        pose.translate(phoneOffsetX + 0.5f, 0.5f, phoneOffsetZ + 0.5f);

        float dx = (float) (state.playerPos.x - phoneX);
        float dy = (float) (state.playerPos.y - phoneY);
        float dz = (float) (state.playerPos.z - phoneZ);
        float cordThickness = 0.025f;

        float horizontalDistance = Mth.invSqrt(dx * dx + dz * dz) * cordThickness / 2.0f;
        float offsetZ = dz * horizontalDistance;
        float offsetX = dx * horizontalDistance;

        for (int i = 0; i <= 24; i++) {
            addVertexPair(buffer, pose, dx, dy, dz, cordThickness, 0.05f, offsetZ, offsetX, i, false, state);
        }

        for (int j = 24; j >= 0; j--) {
            addVertexPair(buffer, pose, dx, dy, dz, cordThickness, 0.0f, offsetZ, offsetX, j, true, state);
        }
    }

    private static void addVertexPair(
            VertexConsumer buffer,
            PoseStack.Pose pose,
            float dx,
            float dy,
            float dz,
            float cordRadius,
            float taperRadius,
            float offsetX,
            float offsetZ,
            int segmentIndex,
            boolean isBackFace,
            PhoneCordRenderState state
    ) {
        float segmentProgress = segmentIndex / 24.0f;
        int blockLight = (int) Mth.lerp(segmentProgress, (float) state.startBlockLight, (float) state.endBlockLight);
        int skyLight = (int) Mth.lerp(segmentProgress, (float) state.startSkyLight, (float) state.endSkyLight);
        int packedLight = LightCoordsUtil.pack(blockLight, skyLight);

        float brightnessMultiplier = segmentIndex % 2 == (isBackFace ? 1 : 0) ? 0.7f : 1.0f;
        float gray = 0.25f * brightnessMultiplier;

        float x = dx * segmentProgress;
        float y = dy > 0.0f ? dy * segmentProgress * segmentProgress : dy - dy * (1.0f - segmentProgress) * (1.0f - segmentProgress);
        float zPos = dz * segmentProgress;

        buffer.addVertex(pose, x - offsetX, y + taperRadius, zPos + offsetZ).setColor(gray, gray, gray, 1.0f).setLight(packedLight);
        buffer.addVertex(pose, x + offsetX, y + cordRadius - taperRadius, zPos - offsetZ).setColor(gray, gray, gray, 1.0f).setLight(packedLight);
    }

    @Override
    public int getViewDistance() {
        return 12;
    }

    @Override
    public boolean shouldRender(IntercomBlockEntity blockEntity, Vec3 cameraPosition) {
        return blockEntity.isPhonePickedUp();
    }
}
