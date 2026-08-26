package com.site21.bittermelon.common.content.entities.scp131.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.event.LayerDefinitions;
import com.site21.bittermelon.common.content.entities.scp131.SCP131;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;


public class SCP131Renderer extends MobRenderer<SCP131, SCP131RenderState, SCP131Model> {

    public SCP131Renderer(EntityRendererProvider.Context context) {
        super(context, new SCP131Model(context.bakeLayer(LayerDefinitions.SCP_131_LAYER)), 0.3f);
    }

    @Override
    public @NotNull Identifier getTextureLocation(@NotNull SCP131RenderState renderState) {
        return renderState.variant == 0 ?
                Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/entity/scp_131_a.png") :
                Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/entity/scp_131_b.png");
    }

    @Override
    public @NotNull SCP131RenderState createRenderState() {
        return new SCP131RenderState();
    }

    @Override
    public void extractRenderState(@NotNull SCP131 entity, @NotNull SCP131RenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.variant = entity.getVariant();
        reusedState.walkAnimationState = entity.walkAnimationState;
    }
}
