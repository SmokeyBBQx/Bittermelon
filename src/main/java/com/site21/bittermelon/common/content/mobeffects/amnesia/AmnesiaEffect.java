package com.site21.bittermelon.common.content.mobeffects.amnesia;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class AmnesiaEffect extends MobEffect {
    public AmnesiaEffect() {
        super(MobEffectCategory.HARMFUL, 0xA000000);
    }

    @Override
    public void onEffectStarted(LivingEntity mob, int amplifier) {

    }
}
