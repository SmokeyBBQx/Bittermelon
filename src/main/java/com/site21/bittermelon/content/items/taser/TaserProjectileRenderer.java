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

        float deltaX = (float)(shooterPos.x - projectilePos.x);
        float deltaY = (float)(shooterPos.y - projectilePos.y);
        float deltaZ = (float)(shooterPos.z - projectilePos.z);

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.lineStrip());
        PoseStack.Pose pose = poseStack.last();

        int segments = 16;
        for (int i = 0; i <= segments; i++) {
            stringVertex(deltaX, deltaY, deltaZ, vertexConsumer, pose, (float) i / segments, (float) (i + 1) / segments);
        }

        poseStack.popPose();
    }

    private static void stringVertex(float x, float y, float z, @NotNull VertexConsumer consumer, PoseStack.Pose pose, float stringFraction, float nextStringFraction) {
        float f = x * stringFraction;
        float f1 = y * (stringFraction * stringFraction + stringFraction) * 0.5F;
        float f2 = z * stringFraction;
        float f3 = x * nextStringFraction - f;
        float f4 = y * (nextStringFraction * nextStringFraction + nextStringFraction) * 0.5F + 0.25F - f1;
        float f5 = z * nextStringFraction - f2;
        float f6 = Mth.sqrt(f3 * f3 + f4 * f4 + f5 * f5);
        f3 /= f6;
        f4 /= f6;
        f5 /= f6;
        consumer.addVertex(pose, f, f1, f2).setColor(0xFF444444).setNormal(pose, f3, f4, f5);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull TaserProjectile taserProjectile) {
        return null;
    }
}
