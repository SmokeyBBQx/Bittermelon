package com.site21.bittermelon.common.content.entities.seamonkey.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

import java.util.List;
import java.util.function.Function;

public class AppendagesLayer<S extends LivingEntityRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {
    private final List<Identifier> textures;

    private final AppendagesLayer.DrawSelector<S, M> drawSelector;
    private final Function<Identifier, RenderType> bufferProvider;
    private final boolean alwaysVisible;

    public AppendagesLayer(
            RenderLayerParent<S, M> renderer,
            List<Identifier> textures,
            AppendagesLayer.DrawSelector<S, M> drawSelector,
            Function<Identifier, RenderType> bufferProvider,
            boolean alwaysVisible
    ) {
        super(renderer);
        this.textures = textures;
        this.drawSelector = drawSelector;
        this.bufferProvider = bufferProvider;
        this.alwaysVisible = alwaysVisible;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, S renderState, float yRot, float xRot) {
        if (!renderState.isInvisible || alwaysVisible) {
            if (onlyDrawSelectedParts(renderState)) {
                int textureIndex = Mth.floor(renderState.ageInTicks / 5.0f) % textures.size();
                VertexConsumer consumer = bufferSource.getBuffer(bufferProvider.apply(textures.get(textureIndex)));
                getParentModel().renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
                resetDrawForAllParts();
            }
        }
    }

    private boolean onlyDrawSelectedParts(S renderState) {
        List<ModelPart> list = this.drawSelector.getPartsToDraw(this.getParentModel(), renderState);
        if (list.isEmpty()) {
            return false;
        } else {
            this.getParentModel().allParts().forEach(p_379465_ -> p_379465_.skipDraw = true);
            list.forEach(p_379767_ -> p_379767_.skipDraw = false);
            return true;
        }
    }

    private void resetDrawForAllParts() {
        this.getParentModel().allParts().forEach(p_379339_ -> p_379339_.skipDraw = false);
    }

    public interface DrawSelector<S extends LivingEntityRenderState, M extends EntityModel<S>> {
        List<ModelPart> getPartsToDraw(M model, S renderState);
    }
}
