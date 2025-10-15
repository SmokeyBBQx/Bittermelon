package com.site21.bittermelon.common.content.entities.chicken.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.entities.chicken.Chicken;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.common.content.entities.client.ModelLayers.CHICKEN_LAYER;

public class ChickenRenderer extends MobRenderer<Chicken, ChickenRenderState, ChickenModel> {

    public ChickenRenderer(EntityRendererProvider.Context context) {
        super(context, new ChickenModel(context.bakeLayer(CHICKEN_LAYER)), 0.2F);
    }

    @Override
    public @NotNull ChickenRenderState createRenderState() {
        return new ChickenRenderState();
    }

    public void extractRenderState(@NotNull Chicken entity, @NotNull ChickenRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.flap = Mth.lerp(partialTick, entity.oFlap, entity.flap);
        reusedState.flapSpeed = Mth.lerp(partialTick, entity.oFlapSpeed, entity.flapSpeed);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ChickenRenderState renderState) {
        return ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/entity/silkie_white.png");
    }
}
