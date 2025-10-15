package com.site21.bittermelon.common.content.entities.chicken.client;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChickenRenderState extends LivingEntityRenderState {
    public float flap;
    public float flapSpeed;
}
