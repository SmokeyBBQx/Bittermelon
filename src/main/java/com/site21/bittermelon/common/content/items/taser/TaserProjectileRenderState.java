package com.site21.bittermelon.common.content.items.taser;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TaserProjectileRenderState extends EntityRenderState {
    Vec3 shooterPos;
    Vec3 projectilePos;
}
