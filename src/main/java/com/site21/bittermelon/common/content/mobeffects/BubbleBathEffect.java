package com.site21.bittermelon.common.content.mobeffects;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BubbleBathEffect extends MobEffect {
    public BubbleBathEffect() {
        super(MobEffectCategory.HARMFUL, 0);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 2 == 0;
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        float particleAmount = 5 + amplifier;

        for (int i = 0; i < (int) particleAmount; i++) {
            level.sendParticles(
                    ParticleTypes.SNEEZE,
                    entity.getX(),
                    entity.getY() + 1.0,
                    entity.getZ(),
                    1,
                    0.3, 0.2, 0.3,
                    0.05
            );
        }

        return true;
    }
}
