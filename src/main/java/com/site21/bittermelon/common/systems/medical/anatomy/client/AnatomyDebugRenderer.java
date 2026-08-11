package com.site21.bittermelon.common.systems.medical.anatomy.client;

import com.site21.bittermelon.common.systems.medical.bodypart.HealthContainer;
import com.site21.bittermelon.common.systems.medical.bodypart.PartInstance;
import com.site21.bittermelon.init.neoforge.BitterAttachmentTypes;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.debug.DebugValueAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class AnatomyDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
    private final Minecraft minecraft;

    public AnatomyDebugRenderer(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    @Override
    public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
        if (minecraft.level == null) return;

        for (Entity entity : minecraft.level.entitiesForRendering()) {
            if (!entity.isInvisible()
                    && entity.hasData(BitterAttachmentTypes.HEALTH_CONTAINER)
                    && frustum.isVisible(entity.getBoundingBox())
                    && (entity != minecraft.getCameraEntity() || minecraft.options.getCameraType() != CameraType.FIRST_PERSON)) {
                float entityPartialTicks = minecraft
                        .getDeltaTracker()
                        .getGameTimeDeltaPartialTick(!minecraft.level.tickRateManager().isEntityFrozen(entity));
//                showHitboxes(entity, entity.getData(BitterAttachmentTypes.HEALTH_CONTAINER), entityPartialTicks);
            }
        }
    }

    private void showHitboxes(Entity entity, HealthContainer healthContainer, float partialTicks) {
        Vec3 position = entity.getPosition(partialTicks);
        int mainColor = -16711936;

        PartInstance root = healthContainer.getRoot();
        float eyeHeight = entity.getEyeHeight() - 0.475f;
        Gizmos.cuboid(
                root.getBodyPart().boundingBox()
                        .move(position)
                        .move(root.getBodyPart().offset().add(0, eyeHeight, 0)),
                GizmoStyle.stroke(mainColor)
        );

        for (Map.Entry<Vec3, PartInstance> entry : root.getAttachedParts().entrySet()) {
            Vec3 attachmentPoint = entry.getKey();
            AABB boundingBox = entry.getValue().getBodyPart().boundingBox();
            attachmentPoint = attachmentPoint.scale(1 / 16.0).reverse();
            Vec3 pivot = entry.getValue().getBodyPart().offset().scale(1 / 16.0).reverse();
            AABB aabb = boundingBox
                    .move(attachmentPoint)
                    .move(pivot)
                    .move(position)
                    .move(0, eyeHeight, 0);
            Gizmos.cuboid(aabb, GizmoStyle.stroke(mainColor));
            Gizmos.point(
                    attachmentPoint
                            .add(position)
                            .add(0, eyeHeight, 0),
                    0xFFFF0000,
                    5.0f
            );
            Gizmos.point(
                    pivot
                            .add(attachmentPoint)
                            .add(position)
                            .add(0, eyeHeight, 0),
                    0xFF0000FF,
                    5.0f
            );
        }
    }
}
