package com.site21.bittermelon.common.systems.medical.client;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.function.Function;

public class AnatomyPictureInPictureRenderer extends PictureInPictureRenderer<AnatomyPictureInPictureRenderer.RenderState> {

    public AnatomyPictureInPictureRenderer(MultiBufferSource.BufferSource bufferSource) {
        super(bufferSource);
    }

    @Override
    public Class<RenderState> getRenderStateClass() {
        return RenderState.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void renderToTexture(RenderState renderState, PoseStack poseStack) {
        Minecraft mc = Minecraft.getInstance();
        mc.gameRenderer.getLighting().setupFor(Lighting.Entry.ENTITY_IN_UI);
        EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();

        applyTransformations(poseStack, renderState, dispatcher);

        EntityRenderer<?, ?> renderer = dispatcher.getRenderer(renderState.entity());
        renderEntityModel(renderState, poseStack, (LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?>) renderer);
    }

    private void applyTransformations(PoseStack poseStack, RenderState renderState, EntityRenderDispatcher dispatcher) {
        Vector3f translation = renderState.translation();
        poseStack.translate(translation.x, translation.y, translation.z);
        poseStack.mulPose(renderState.rotation());

        Quaternionf overrideCameraAngle = renderState.overrideCameraAngle();
        if (overrideCameraAngle != null) {
            dispatcher.overrideCameraOrientation(overrideCameraAngle.conjugate(new Quaternionf()).rotateY((float) Math.PI));
        }
    }

    @SuppressWarnings("unchecked")
    private void renderEntityModel(RenderState renderState, PoseStack poseStack,
                                   LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?> renderer) {
        EntityModel<LivingEntityRenderState> model = (EntityModel<LivingEntityRenderState>) renderer.getModel();
        ModelPart rootPart = model.root();

        RenderType renderType = model.renderType(renderer.getTextureLocation(renderState.entityRenderState()));
        Function<String, ModelPart> partLookup = rootPart.createPartLookup();
        for (ModelPart part : rootPart.children.values()) {
            int overlay = getPartOverlay(renderState, part, partLookup);
            renderPart(renderState, partLookup, part, poseStack, renderType, overlay);
        }
    }

    private void renderPart(RenderState renderState, Function<String, ModelPart> partLookup, ModelPart part,
                            PoseStack poseStack, RenderType renderType, int overlay) {
        // Copied from ModelPart.render with modifications to support part highlighting
        if (part.visible) {
            if (!part.cubes.isEmpty() || !part.children.isEmpty()) {
                poseStack.pushPose();
                part.translateAndRotate(poseStack);
                if (!part.skipDraw) {
                    part.compile(poseStack.last(), bufferSource.getBuffer(renderType), 255, overlay, -1);
                }

                for (ModelPart child : part.children.values()) {
                    // TODO: If part of anatomy model, use getPartOverlay, otherwise use parent part's overlay
                    int childOverlay = getPartOverlay(renderState, child, partLookup);
                    renderPart(renderState, partLookup, child, poseStack, renderType, childOverlay);
                }

                poseStack.popPose();
            }
        }
    }

    private int getPartOverlay(RenderState renderState, ModelPart part, Function<String, ModelPart> partLookup) {
        if (renderState.highlightedPart == null) {
            return OverlayTexture.NO_OVERLAY;
        }
        return part == partLookup.apply(renderState.highlightedPart) ?
                OverlayTexture.RED_OVERLAY_V :
                OverlayTexture.NO_OVERLAY;
    }

    @Override
    protected float getTranslateY(int height, int guiScale) {
        return height / 2.0F;
    }

    @Override
    protected String getTextureLabel() {
        return "bittermelon anatomy";
    }

    public record RenderState(
            LivingEntityRenderState entityRenderState,
            Entity entity,
            String highlightedPart,
            Vector3f translation,
            Quaternionf rotation,
            @Nullable Quaternionf overrideCameraAngle,
            int yRot,
            int x0,
            int y0,
            int x1,
            int y1,
            float scale,
            @Nullable ScreenRectangle scissorArea,
            @Nullable ScreenRectangle bounds
    ) implements PictureInPictureRenderState {

        public RenderState(
                LivingEntityRenderState entityRenderState,
                Entity entity,
                String highlightedPart,
                Vector3f translation,
                Quaternionf rotation,
                @Nullable Quaternionf overrideCameraAngle,
                int x,
                int y,
                int width,
                int height,
                float scale,
                @Nullable ScreenRectangle scissorArea
        ) {
            this(entityRenderState, entity, highlightedPart, translation, rotation, overrideCameraAngle,
                    0, x, y, x + width, y + height, scale, scissorArea,
                    PictureInPictureRenderState.getBounds(x, y, x + width, y + height, scissorArea)
            );
        }

        @Override
        public int x0() {
            return x0;
        }

        @Override
        public int x1() {
            return x1;
        }

        @Override
        public int y0() {
            return y0;
        }

        @Override
        public int y1() {
            return y1;
        }

        @Override
        public float scale() {
            return scale;
        }

        @Override
        public @Nullable ScreenRectangle scissorArea() {
            return scissorArea;
        }

        @Override
        public @Nullable ScreenRectangle bounds() {
            return bounds;
        }
    }
}
