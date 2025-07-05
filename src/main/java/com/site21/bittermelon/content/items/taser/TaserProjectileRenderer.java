package com.site21.bittermelon.content.items.taser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class TaserProjectileRenderer extends EntityRenderer<TaserProjectile> {
    public TaserProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@NotNull TaserProjectile entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (!(entity.getOwner() instanceof LivingEntity shooter)) return;

        poseStack.pushPose();

        Vec3 shooterPos = shooter.getRopeHoldPosition(partialTick);
        Vec3 projectilePos = entity.getPosition(partialTick);

        float deltaX = (float) (shooterPos.x - projectilePos.x);
        float deltaY = (float) (shooterPos.y - projectilePos.y);
        float deltaZ = (float) (shooterPos.z - projectilePos.z);

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.lineStrip());
        PoseStack.Pose pose = poseStack.last();

        int segments = 32;
        for (int i = 0; i <= segments; i++) {
            stringVertex(deltaX, deltaY, deltaZ, vertexConsumer, pose, (float) i / segments, (float) (i + 1) / segments);
            stringVertex(deltaX + 0.1f, deltaY , deltaZ + 0.1f, vertexConsumer, pose, (float) i / segments, (float) (i + 1) / segments);
        }

        poseStack.popPose();
    }

    private static void stringVertex(float x, float y, float z, @NotNull VertexConsumer consumer, PoseStack.Pose pose, float stringFraction, float nextStringFraction) {
        float currentX = x * stringFraction;
        float currentY = y * (stringFraction * stringFraction + stringFraction) * 0.5F;
        float currentZ = z * stringFraction;
        float deltaX = x * nextStringFraction + currentX;
        float deltaY = y * (nextStringFraction * nextStringFraction + nextStringFraction) * 0.5F + 0.25F - currentY;
        float deltaZ = z * nextStringFraction + currentZ;
        float normalLength = Mth.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
        deltaX /= normalLength;
        deltaY /= normalLength;
        deltaZ /= normalLength;
        consumer.addVertex(pose, currentX, currentY, currentZ).setColor(0xFF444444).setNormal(pose, deltaX, deltaY, deltaZ);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull TaserProjectile taserProjectile) {
        return null;
    }
}
