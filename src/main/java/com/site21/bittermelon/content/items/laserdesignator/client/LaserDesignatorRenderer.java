package com.site21.bittermelon.content.items.laserdesignator.client;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.items.laserdesignator.LaserDesignatorItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.BitterDataComponents.POSITION_1;
import static com.site21.bittermelon.init.BitterDataComponents.POSITION_2;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class LaserDesignatorRenderer {

    @SubscribeEvent
    public static void onRenderGUILayer(RenderGuiLayerEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            ItemStack heldItem = minecraft.player.getMainHandItem();
            if (heldItem.getItem() instanceof LaserDesignatorItem) {
                BlockPos pos1 = heldItem.get(POSITION_1.get());
                BlockPos pos2 = heldItem.get(POSITION_2.get());

                if (pos1 == null || pos2 == null) return;

                int xSize = Math.abs(pos2.getX() - pos1.getX());
                int ySize = Math.abs(pos2.getY() - pos1.getY());
                int zSize = Math.abs(pos2.getZ() - pos1.getZ());
                Component text = Component.literal("(" + xSize + ", " + ySize + ", " + zSize + ")");
                GuiGraphics guiGraphics = event.getGuiGraphics();
                Window window = minecraft.getWindow();
                int width = window.getGuiScaledWidth();
                int height = window.getGuiScaledHeight();
                int x = (width - minecraft.font.width(text)) / 2;
                int y = height - 35;
                guiGraphics.drawString(minecraft.font, text, x, y, 0xFFFFFF);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderLevel(@NotNull RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        assert mc.player != null;
        ItemStack mainHand = mc.player.getMainHandItem();

        if (!(mainHand.getItem() instanceof LaserDesignatorItem)) {
            return;
        }

        BlockPos pos1 = mainHand.get(POSITION_1.get());
        BlockPos pos2 = mainHand.get(POSITION_2.get());

        if (pos1 == null || pos2 == null) {
            return;
        }

        AABB box = AABB.encapsulatingFullBlocks(pos1, pos2);

        PoseStack poseStack = event.getPoseStack();
        poseStack.pushPose();

        var camera = mc.gameRenderer.getMainCamera();
        poseStack.translate(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z);

        VertexConsumer builder = mc.renderBuffers().outlineBufferSource()
                .getBuffer(RenderType.lines());

        LevelRenderer.renderLineBox(
                poseStack,
                builder,
                box,
                1.0f,
                0.0f,
                0.0f,
                1.0f
        );

        renderGlowingBlocks(mc, poseStack, box);

        poseStack.popPose();
    }

    private static void renderGlowingBlocks(@NotNull Minecraft mc, PoseStack poseStack, @NotNull AABB box) {
        int minX = (int) box.minX;
        int minY = (int) box.minY;
        int minZ = (int) box.minZ;
        int maxX = (int) box.maxX - 1;
        int maxY = (int) box.maxY - 1;
        int maxZ = (int) box.maxZ - 1;

        VertexConsumer glowBuilder = mc.renderBuffers().outlineBufferSource()
                .getBuffer(RenderType.LINES);

        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos currentPos = new BlockPos(x, y, z);

                    if (mc.level.getBlockState(currentPos).isAir()) {
                        continue;
                    }

                    AABB blockBox = new AABB(
                            currentPos.getX() + 0.01, currentPos.getY() + 0.01, currentPos.getZ() + 0.01,
                            currentPos.getX() + 0.99, currentPos.getY() + 0.99, currentPos.getZ() + 0.99
                    );

                    LevelRenderer.renderLineBox(
                            poseStack,
                            glowBuilder,
                            blockBox,
                            1.0f,
                            0.8f,
                            0.0f,
                            0.8f
                    );
                }
            }
        }

        RenderSystem.depthMask(true);
        RenderSystem.disableDepthTest();
    }
}
