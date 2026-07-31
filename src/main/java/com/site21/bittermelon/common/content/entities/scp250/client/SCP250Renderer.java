package com.site21.bittermelon.common.content.entities.scp250.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.event.LayerDefinitions;
import com.site21.bittermelon.common.content.entities.scp250.SCP250;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class SCP250Renderer extends MobRenderer<SCP250, SCP250RenderState, SCP250Model> {
    public SCP250Renderer(EntityRendererProvider.Context context) {
        super(context, new SCP250Model(context.bakeLayer(LayerDefinitions.SCP_250_LAYER)), 1.0f);
    }

    @Override
    public @NotNull Identifier getTextureLocation(@NotNull SCP250RenderState scp548RenderState) {
        return Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/entity/scp_250.png");
    }

    @Override
    public @NotNull SCP250RenderState createRenderState() {
        return new SCP250RenderState();
    }
}