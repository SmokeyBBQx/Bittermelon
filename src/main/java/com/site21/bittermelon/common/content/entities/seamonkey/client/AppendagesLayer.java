package com.site21.bittermelon.common.content.entities.seamonkey.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

import java.util.List;
import java.util.function.Function;

public class AppendagesLayer<S extends LivingEntityRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {
    private final List<Identifier> textures;
    private final M model;
    private final Function<Identifier, RenderType> bufferProvider;
    private final boolean alwaysVisible;

    public AppendagesLayer(
            RenderLayerParent<S, M> renderer,
            List<Identifier> textures,
            M model,
            Function<Identifier, RenderType> bufferProvider,
            boolean alwaysVisible
    ) {
        super(renderer);
        this.textures = textures;
        this.model = model;
        this.bufferProvider = bufferProvider;
        this.alwaysVisible = alwaysVisible;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, S state, float yRot, float xRot) {
        if (!state.isInvisible || alwaysVisible) {
            int textureIndex = Mth.floor(state.ageInTicks / 5.0f) % textures.size();
            RenderType renderType = bufferProvider.apply(textures.get(textureIndex));
            collector.order(1)
                    .submitModel(
                            model,
                            state,
                            poseStack,
                            renderType,
                            lightCoords,
                            LivingEntityRenderer.getOverlayCoords(state, 0.0F),
                            0,
                            null,
                            state.outlineColor,
                            null
                    );
        }
    }
}
