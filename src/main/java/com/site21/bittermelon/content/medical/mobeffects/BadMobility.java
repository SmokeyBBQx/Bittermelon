package com.site21.bittermelon.content.medical.mobeffects;

import com.site21.bittermelon.content.stumble.StumbleHandler;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class BadMobility extends MobEffect {
    public BadMobility() {
        super(MobEffectCategory.HARMFUL, 0);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 100 == 0;
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide) return true;

        float stumbleChance = entity.getRandom().nextFloat();

        if (stumbleChance > 1 - ((float) amplifier / (entity.isSprinting() ? 10 : 20))) {
            StumbleHandler.stumble(entity);
        }

        return true;
    }
}
