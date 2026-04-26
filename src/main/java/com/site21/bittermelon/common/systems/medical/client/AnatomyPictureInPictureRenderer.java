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

public class AnatomyPictureInPictureRenderer extends PictureInPictureRenderer<AnatomyPictureInPictureRenderer.RenderState> {

    public AnatomyPictureInPictureRenderer(MultiBufferSource.BufferSource bufferSource) {
        super(bufferSource);
    }

    @Override
    public Class<RenderState> getRenderStateClass() {
        return RenderState.class;
    }

    @Override
    protected void renderToTexture(RenderState renderState, PoseStack poseStack) {
        Minecraft.getInstance().gameRenderer.getLighting().setupFor(Lighting.Entry.ENTITY_IN_UI);

        Minecraft mc = Minecraft.getInstance();
        EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();

        Vector3f vector3f = renderState.translation();
        poseStack.translate(vector3f.x, vector3f.y, vector3f.z);
        poseStack.mulPose(renderState.rotation());
        Quaternionf quaternionf = renderState.overrideCameraAngle();
        if (quaternionf != null) {
            dispatcher.overrideCameraOrientation(quaternionf.conjugate(new Quaternionf()).rotateY((float) Math.PI));
        }


        EntityRenderer<?, ?> renderer = dispatcher.getRenderer(renderState.entity);

        if (!(renderer instanceof LivingEntityRenderer<?, ?, ?> livingRenderer)) return;

        LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?> castRenderer =
                (LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?>) livingRenderer;

        EntityModel<LivingEntityRenderState> model =
                (EntityModel<LivingEntityRenderState>) castRenderer.getModel();

//        model.root().getChild(renderState.highlightedPart).visible = true;
//        model.root().getChild(renderState.highlightedPart).render(poseStack,
//                bufferSource.getBuffer(model.renderType(castRenderer.getTextureLocation(renderState.entityRenderState))),
//                255, OverlayTexture.RED_OVERLAY_V);

        for (ModelPart part : model.root().getAllParts()) {
            int packedLight = part.visible ? 255 : 0;
            boolean wasVisible = part.visible;
            part.visible = true;
            int overlay = renderState.highlightedPart == null ? OverlayTexture.NO_OVERLAY :
                    part == model.root().getChild(renderState.highlightedPart) ? OverlayTexture.RED_OVERLAY_V : OverlayTexture.NO_OVERLAY;
            part.render(poseStack,
                    bufferSource.getBuffer(model.renderType(castRenderer.getTextureLocation(renderState.entityRenderState))),
                    packedLight, overlay);
            part.visible = wasVisible;
        }


//        model.root().getChild(renderState.highlightedPart).visible = false;
//        dispatcher.render(renderState.entityRenderState, 0.0, 0.0, 0.0, poseStack, bufferSource, 15728880);
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
