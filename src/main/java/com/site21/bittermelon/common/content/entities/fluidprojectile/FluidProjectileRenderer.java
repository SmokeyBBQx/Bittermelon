package com.site21.bittermelon.common.content.entities.fluidprojectile;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class FluidProjectileRenderer extends EntityRenderer<FluidProjectile, FluidProjectileRenderState> {
    public FluidProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public FluidProjectileRenderState createRenderState() {
        return new FluidProjectileRenderState();
    }
}
