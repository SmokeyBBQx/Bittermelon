package com.site21.bittermelon.common.content.entities.scp1507.client;

import com.site21.bittermelon.client.event.HealthStages;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class SCP1507RenderState extends LivingEntityRenderState {
    public float attackTime;
    public float awakenTime;
    public boolean onGround;
    public boolean leftLegAttached = true;
    public boolean rightLegAttached = true;
    public HealthStages.Level crackiness = HealthStages.Level.NONE;

}
