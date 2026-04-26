package com.site21.bittermelon.common.systems.medical.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.client.renderstate.RenderStateExtensions;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class AnatomyModelWidget {

    public static void renderEntityInInventoryFollowsAngle(@NotNull GuiGraphics graphics, int x1, int y1, int x2, int y2, int scale, float yOffset, float horizontalRotation, float verticalRotation, @NotNull LivingEntity entity) {
        float centerX = (float) (x1 + x2) / 2.0F;
        float centerY = (float) (y1 + y2) / 2.0F;

        graphics.enableScissor(x1, y1, x2, y2);

        Quaternionf baseRotation = (new Quaternionf()).rotateZ((float) Math.PI);
        Quaternionf verticalTiltRotation = (new Quaternionf()).rotateX(verticalRotation * 20.0F * ((float) Math.PI / 180F));
        baseRotation.mul(verticalTiltRotation);

        float originalBodyRotation = entity.yBodyRot;
        float originalYaw = entity.getYRot();
        float originalPitch = entity.getXRot();
        float originalHeadRotationOld = entity.yHeadRotO;
        float originalHeadRotation = entity.yHeadRot;

        float newBodyRotation = 180.0F + horizontalRotation * 20.0F;
        float newYawRotation = 180.0F + horizontalRotation * 20.0F;
        float newPitchRotation = -verticalRotation * 20.0F;

        entity.yBodyRot = newBodyRotation;
        entity.setYRot(newYawRotation);
        entity.setXRot(newPitchRotation);
        entity.yHeadRot = newBodyRotation;
        entity.yHeadRotO = newBodyRotation;

        float entityScale = entity.getScale();
        Vector3f translation = new Vector3f(0.0F, entity.getBbHeight() / 2.0F + yOffset * entityScale, 0.0F);
        float adjustedScale = (float) scale / entityScale;

        renderEntityInInventory(graphics, x1, y1, x2, y2, adjustedScale, translation, baseRotation, verticalTiltRotation, entity);

        entity.yBodyRot = originalBodyRotation;
        entity.setYRot(originalYaw);
        entity.setXRot(originalPitch);
        entity.yHeadRotO = originalHeadRotationOld;
        entity.yHeadRot = originalHeadRotation;

        graphics.disableScissor();
    }

    public static void renderEntityInInventory(
            GuiGraphics guiGraphics,
            int x1,
            int y1,
            int x2,
            int y2,
            float scale,
            Vector3f translation,
            Quaternionf rotation,
            @Nullable Quaternionf overrideCameraAngle,
            LivingEntity entity
    ) {
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer entityrenderer = entityrenderdispatcher.getRenderer(entity);
        // Neo: use fresh render state to support multiple entities of the same type within a single frame
        EntityRenderState entityrenderstate = entityrenderer.createRenderState();
        entityrenderer.extractRenderState(entity, entityrenderstate, 1.0F);
        RenderStateExtensions.onUpdateEntityRenderState(entityrenderer, entity, entityrenderstate);
        entityrenderstate.hitboxesRenderState = null;
        guiGraphics.submitEntityRenderState(entityrenderstate, scale, translation, rotation, overrideCameraAngle, x1, y1, x2, y2);
    }
}
