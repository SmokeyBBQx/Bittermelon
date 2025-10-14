package com.site21.bittermelon.content.items.wires.wire.client;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.items.wires.wire.WireItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.CORD_CONNECTION;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.PORT_ID;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class WireRenderer {
    @SubscribeEvent
    public static void onRenderGUILayer(RenderGuiLayerEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            ItemStack heldItem = minecraft.player.getMainHandItem();
            if (heldItem.getItem() instanceof WireItem) {
                BlockPos devicePos = heldItem.get(CORD_CONNECTION.get());
                String port = heldItem.get(PORT_ID.get());

                if (devicePos == null || port == null) return;
                MutableComponent deviceName = minecraft.player.level().getBlockState(devicePos).getBlock().getName();

                Component text = Component.literal("Wiring from " + deviceName);
                Component portID = Component.literal("(" + port + ")").withStyle(ChatFormatting.GRAY);
                GuiGraphics guiGraphics = event.getGuiGraphics();
                Window window = minecraft.getWindow();
                int width = window.getGuiScaledWidth();
                int height = window.getGuiScaledHeight();
                int x = width / 2;
                int y = height - 45;
                guiGraphics.drawCenteredString(minecraft.font, text, x, y, 0xFFFFFF);
                guiGraphics.drawCenteredString(minecraft.font, portID, x, y + 10, 0xFFFFFF);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderLevelStage(@NotNull RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;

        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null) return;

        ItemStack heldItem = player.getMainHandItem();
        if (!(heldItem.getItem() instanceof WireItem)) return;

        BlockPos devicePos = heldItem.get(CORD_CONNECTION.get());
        String port = heldItem.get(PORT_ID.get());

        if (devicePos == null || port == null) return;

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource bufferSource = minecraft.renderBuffers().bufferSource();
        Vec3 cameraPos = minecraft.gameRenderer.getMainCamera().getPosition();
        float partialTick = event.getCamera().getPartialTickTime();

        poseStack.pushPose();
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        renderWire(poseStack, bufferSource, player, devicePos, partialTick);

        poseStack.popPose();
    }

    /**
     * Adapted from net.minecraft.client.renderer.entity.EntityRenderer#renderLeash()
     */
    private static void renderWire(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource,
                                   @NotNull Player player, @NotNull BlockPos devicePos, float partialTick) {
        poseStack.pushPose();

        Vec3 playerHandPos = player.getRopeHoldPosition(partialTick);
        Vec3 deviceCenter = Vec3.atCenterOf(devicePos);

        poseStack.translate(deviceCenter.x, deviceCenter.y, deviceCenter.z);

        float deltaX = (float)(playerHandPos.x - deviceCenter.x);
        float deltaY = (float)(playerHandPos.y - deviceCenter.y);
        float deltaZ = (float)(playerHandPos.z - deviceCenter.z);
        float wireThickness = 0.025F;

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.leash());
        Matrix4f matrix4f = poseStack.last().pose();

        float horizontalDistance = Mth.invSqrt(deltaX * deltaX + deltaZ * deltaZ) * wireThickness / 2.0F;
        float offsetZ = deltaZ * horizontalDistance;
        float offsetX = deltaX * horizontalDistance;

        BlockPos playerBlockPos = BlockPos.containing(playerHandPos);
        Level level = player.level();

        int deviceBlockLight = level.getBrightness(LightLayer.BLOCK, devicePos);
        int playerBlockLight = level.getBrightness(LightLayer.BLOCK, playerBlockPos);
        int deviceSkyLight = level.getBrightness(LightLayer.SKY, devicePos);
        int playerSkyLight = level.getBrightness(LightLayer.SKY, playerBlockPos);

        for (int i = 0; i <= 24; i++) {
            addWireVertex(vertexConsumer, matrix4f, deltaX, deltaY, deltaZ,
                    deviceBlockLight, playerBlockLight, deviceSkyLight, playerSkyLight,
                    wireThickness, wireThickness, offsetZ, offsetX, i, false);
        }

        for (int j = 24; j >= 0; j--) {
            addWireVertex(vertexConsumer, matrix4f, deltaX, deltaY, deltaZ,
                    deviceBlockLight, playerBlockLight, deviceSkyLight, playerSkyLight,
                    wireThickness, 0.0F, offsetZ, offsetX, j, true);
        }

        poseStack.popPose();
    }

    private static void addWireVertex(
            @NotNull VertexConsumer buffer,
            Matrix4f pose,
            float deltaX,
            float deltaY,
            float deltaZ,
            int startBlockLight,
            int endBlockLight,
            int startSkyLight,
            int endSkyLight,
            float wireRadius,
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
        buffer.addVertex(pose, xPos + offsetX, yPos + wireRadius - taperRadius, zPos - offsetZ).setColor(gray, gray, gray, 1.0F).setLight(packedLight);
    }
}
