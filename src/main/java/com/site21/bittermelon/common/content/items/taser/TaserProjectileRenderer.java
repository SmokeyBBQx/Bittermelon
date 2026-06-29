package com.site21.bittermelon.common.content.items.taser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;


public class TaserProjectileRenderer extends EntityRenderer<TaserProjectile, TaserProjectileRenderState> {
    public TaserProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull TaserProjectileRenderState createRenderState() {
        return new TaserProjectileRenderState();
    }

    @Override
    public void extractRenderState(@NotNull TaserProjectile entity, @NotNull TaserProjectileRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);

        if (entity.getOwner() instanceof LivingEntity shooter) {
            reusedState.shooterPos = shooter.getRopeHoldPosition(partialTick);
            reusedState.projectilePos = entity.getPosition(partialTick);
        }
    }

    @Override
    public void submit(TaserProjectileRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        super.submit(state, poseStack, collector, camera);

        poseStack.pushPose();

        Vec3 shooterPos = state.shooterPos;
        Vec3 projectilePos = state.projectilePos;

        float deltaX = (float) (shooterPos.x - projectilePos.x);
        float deltaY = (float) (shooterPos.y - projectilePos.y);
        float deltaZ = (float) (shooterPos.z - projectilePos.z);

        collector.submitCustomGeometry(
                poseStack,
                RenderTypes.lines(),
                (pose, buffer) -> {
                    int segments = 32;
                    for (int i = 0; i <= segments; i++) {
                        stringVertex(deltaX, deltaY, deltaZ, buffer, pose, (float) i / segments, (float) (i + 1) / segments);
                        stringVertex(deltaX + 0.1f, deltaY , deltaZ + 0.1f, buffer, pose, (float) i / segments, (float) (i + 1) / segments);
                    }
                }
        );

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
}
