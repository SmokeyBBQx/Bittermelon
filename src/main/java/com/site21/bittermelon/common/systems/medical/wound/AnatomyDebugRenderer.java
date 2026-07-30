package com.site21.bittermelon.common.systems.medical.wound;

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
        double size = 0.1;

        PartInstance root = healthContainer.getRoot();
        for (Vec3 attachmentPoint : root.getBodyPart().attachmentPoints()) {
            attachmentPoint = attachmentPoint.scale(1 / 16.0);
            AABB aabb = new AABB(attachmentPoint.x, attachmentPoint.y, attachmentPoint.z,
                    attachmentPoint.x + size, attachmentPoint.y + size, attachmentPoint.z + size);
            Gizmos.cuboid(aabb.move(position), GizmoStyle.stroke(mainColor));
        }
    }
}
