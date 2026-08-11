package com.site21.bittermelon.common.systems.embeddedobject;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

import java.util.List;

public abstract class EmbeddedObjectLayer<M extends EntityModel<S>, S extends EntityRenderState> extends RenderLayer<S, M> {
    private final Model<S> model;
    private final S modelState;
    private final Identifier texture;

    public EmbeddedObjectLayer(
            RenderLayerParent<S, M> renderer,
            Model<S> model,
            S modelState,
            Identifier texture
    ) {
        super(renderer);
        this.model = model;
        this.modelState = modelState;
        this.texture = texture;
    }

    protected abstract List<EmbeddedObject> getEmbeddedObjects(S state);

    protected abstract int getOutlineColor(S state);

    private void submitEmbeddedObject(
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int lightCoords,
            float directionX,
            float directionY,
            float directionZ,
            int outlineColor
    ) {
        float directionXZ = Mth.sqrt(directionX * directionX + directionZ * directionZ);
        float yRot = (float)(Math.atan2(directionX, directionZ) * 180.0F / (float)Math.PI);
        float xRot = (float)(Math.atan2(directionY, directionXZ) * 180.0F / (float)Math.PI);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(xRot));
        submitNodeCollector.submitModel(model, modelState, poseStack, texture, lightCoords, OverlayTexture.NO_OVERLAY, outlineColor, null);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, S state, float yRot, float xRot) {
        List<EmbeddedObject> items = getEmbeddedObjects(state);
        if (items.isEmpty()) return;

        for (EmbeddedObject item : items) {
            poseStack.pushPose();
            ModelPart modelPart = getParentModel().root().getChild(item.modelPart());
            ModelPart.Cube cube = modelPart.cubes.get(item.cube());
            modelPart.translateAndRotate(poseStack);

            float midX = item.midX();
            float midY = item.midY();
            float midZ = item.midZ();

            poseStack.translate(
                    Mth.lerp(midX, cube.minX, cube.maxX) / 16.0F,
                    Mth.lerp(midY, cube.minY, cube.maxY) / 16.0F,
                    Mth.lerp(midZ, cube.minZ, cube.maxZ) / 16.0F
            );

            submitEmbeddedObject(
                    poseStack,
                    collector,
                    lightCoords,
                    -(midX * 2.0F - 1.0F),
                    -(midY * 2.0F - 1.0F),
                    -(midZ * 2.0F - 1.0F),
                    getOutlineColor(state)
            );

            poseStack.popPose();
        }
    }
}