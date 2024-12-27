package com.site21.bittermelon.client.renderer.entity;

import com.site21.bittermelon.client.models.entity.ChickenModel;
import com.site21.bittermelon.entities.scps.chicken.Chicken;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class ChickenRenderer extends MobRenderer<Chicken, com.site21.bittermelon.client.models.entity.ChickenModel<Chicken>> {
    private static final ResourceLocation CHICKEN_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/chicken.png");

    public ChickenRenderer(EntityRendererProvider.Context context) {
        super(context, new ChickenModel<>(context.bakeLayer(ModelLayers.CHICKEN)), 0.3F);
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull Chicken entity) {
        return CHICKEN_LOCATION;
    }

    protected float getBob(Chicken livingBase, float partialTicks) {
        float f = Mth.lerp(partialTicks, livingBase.oFlap, livingBase.flap);
        float f1 = Mth.lerp(partialTicks, livingBase.oFlapSpeed, livingBase.flapSpeed);
        return (Mth.sin(f) + 1.0F) * f1;
    }
}
