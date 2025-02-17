package com.site21.bittermelon.content.entities.implementations.chicken.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.entities.implementations.chicken.Chicken;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.content.entities.client.ModelLayers.CHICKEN_LAYER;

public class ChickenRenderer extends MobRenderer<Chicken, ChickenModel<Chicken>> {
    private static final ResourceLocation CHICKEN_LOCATION = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/entity/silkie_white.png");

    public ChickenRenderer(EntityRendererProvider.Context context) {
        super(context, new ChickenModel<>(context.bakeLayer(CHICKEN_LAYER)), 0.2F);
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull Chicken entity) {
        return CHICKEN_LOCATION;
    }

    protected float getBob(@NotNull Chicken livingBase, float partialTicks) {
        float f = Mth.lerp(partialTicks, livingBase.oFlap, livingBase.flap);
        float f1 = Mth.lerp(partialTicks, livingBase.oFlapSpeed, livingBase.flapSpeed);
        return (Mth.sin(f) + 1.0F) * f1;
    }
}
