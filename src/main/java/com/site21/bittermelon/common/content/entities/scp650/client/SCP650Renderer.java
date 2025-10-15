package com.site21.bittermelon.common.content.entities.scp650.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.event.LayerDefinitions;
import com.site21.bittermelon.common.content.entities.scp650.SCP650;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SCP650Renderer extends LivingEntityRenderer<SCP650, SCP650RenderState, SCP650Model> {

    public SCP650Renderer(EntityRendererProvider.Context context) {
        super(context, new SCP650Model(context.bakeLayer(LayerDefinitions.SCP_131_LAYER)), 0.25f);
    }

    @Override
    public @NotNull SCP650RenderState createRenderState() {
        return new SCP650RenderState();
    }

    @Override
    public void extractRenderState(@NotNull SCP650 entity, @NotNull SCP650RenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.pose = entity.getCurrentPoseAnimation();
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SCP650RenderState renderState) {
        return ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/entity/scp_650.png");
    }
}
