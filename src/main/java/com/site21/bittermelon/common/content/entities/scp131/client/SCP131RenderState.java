package com.site21.bittermelon.common.content.entities.scp131.client;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SCP131RenderState extends LivingEntityRenderState {
    public int variant;
}
