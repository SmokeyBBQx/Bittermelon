package com.site21.bittermelon.content.items.taser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class TaserProjectileRenderer extends EntityRenderer<TaserProjectile> {
    public TaserProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@NotNull TaserProjectile entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.LINES);
        Matrix4f pose = poseStack.last().pose();

        float size = 0.02f;
        int color = 0xFF2e2e2e;

        vertexConsumer.addVertex(pose, -size, -size, -size).setColor(color).setNormal(1, 0, 0);
        vertexConsumer.addVertex(pose, size, size, size).setColor(color).setNormal(1, 0, 0);

        poseStack.popPose();
    }


    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull TaserProjectile taserProjectile) {
        return null;
    }
}
