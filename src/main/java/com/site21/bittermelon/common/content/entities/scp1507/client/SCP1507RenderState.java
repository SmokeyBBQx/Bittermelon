package com.site21.bittermelon.common.content.entities.scp1507.client;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SCP1507RenderState extends LivingEntityRenderState {
    public float attackTime;
    public boolean onGround;
}
