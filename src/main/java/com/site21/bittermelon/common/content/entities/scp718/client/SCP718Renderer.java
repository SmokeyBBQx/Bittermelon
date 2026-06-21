package com.site21.bittermelon.common.content.entities.scp718.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.event.LayerDefinitions;
import com.site21.bittermelon.common.content.entities.scp718.SCP718;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class SCP718Renderer extends MobRenderer<SCP718, SCP718RenderState, SCP718Model> {
    public SCP718Renderer(EntityRendererProvider.Context context) {
        super(context, new SCP718Model(context.bakeLayer(LayerDefinitions.SCP_718_LAYER)), 0.3f);
    }

    @Override
    public Identifier getTextureLocation(SCP718RenderState renderState) {
        return Bittermelon.identifier("textures/entity/scp_718.png");
    }

    @Override
    public SCP718RenderState createRenderState() {
        return new SCP718RenderState();
    }

    @Override
    public void extractRenderState(SCP718 entity, SCP718RenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.growth = entity.getGrowth();
    }
}
