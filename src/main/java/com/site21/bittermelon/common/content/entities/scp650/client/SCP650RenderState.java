package com.site21.bittermelon.common.content.entities.scp650.client;

import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SCP650RenderState extends LivingEntityRenderState {
    public AnimationDefinition pose;
}
