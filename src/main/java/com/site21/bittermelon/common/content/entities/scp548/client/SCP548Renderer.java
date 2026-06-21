package com.site21.bittermelon.common.content.entities.scp548.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.event.LayerDefinitions;
import com.site21.bittermelon.common.content.entities.scp548.SCP548;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class SCP548Renderer extends MobRenderer<SCP548, SCP548RenderState, SCP548Model> {
    public SCP548Renderer(EntityRendererProvider.Context context) {
        super(context, new SCP548Model(context.bakeLayer(LayerDefinitions.SCP_548_LAYER)), 0.05f);
    }

    @Override
    public @NotNull Identifier getTextureLocation(@NotNull SCP548RenderState scp548RenderState) {
        return Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/entity/scp_548.png");
    }

    @Override
    public @NotNull SCP548RenderState createRenderState() {
        return new SCP548RenderState();
    }
}
