package com.site21.bittermelon.common.systems.blockdamage.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.site21.bittermelon.common.systems.blockdamage.BlockDamageData;
import com.site21.bittermelon.common.systems.blockdamage.BlockDamageHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.IRenderableSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class BlockDamageRenderer {

    public static void renderDamaged(Level level, @Nullable PoseStack poseStack, @NotNull Camera camera, @NotNull Iterable<? extends IRenderableSection> renderableSections) {
        Minecraft minecraft = Minecraft.getInstance();
        Iterator<? extends IRenderableSection> iterator = renderableSections.iterator();
        while (iterator.hasNext()) {
            SectionRenderDispatcher.RenderSection renderSection = (SectionRenderDispatcher.RenderSection) iterator.next();
            BlockPos renderPos = renderSection.getRenderOrigin();
            BlockDamageData data = BlockDamageHelper.getBlockDamageData(level, renderPos);

            for (BlockPos damagedPos : data.getBlockDamages().keySet()) {
                if (!renderSection.getBoundingBox().intersects(damagedPos)) {
                    continue;
                }

                int breakStage = data.getVisualBlockDamage(damagedPos);

                // TODO: Implement render distance culling

                poseStack.pushPose();

                Vec3 cameraPos = camera.getPosition();
                poseStack.translate(
                        damagedPos.getX() - cameraPos.x,
                        damagedPos.getY() - cameraPos.y,
                        damagedPos.getZ() - cameraPos.z
                );

                VertexConsumer vertexConsumer = new SheetedDecalTextureGenerator(
                        Minecraft.getInstance().renderBuffers().crumblingBufferSource().getBuffer(
                                ModelBakery.DESTROY_TYPES.get(breakStage)
                        ),
                        poseStack.last(),
                        1.0f
                );

                minecraft.getBlockRenderer().renderBreakingTexture(level.getBlockState(damagedPos), damagedPos, level, poseStack, vertexConsumer);

                poseStack.popPose();
            }
        }
    }
}
