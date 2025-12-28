package com.site21.bittermelon.common.content.mobeffects;

import com.site21.bittermelon.common.systems.stress.StressUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class EuphoriaEffect extends MobEffect {
    public EuphoriaEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFD700);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 40 == 0;
    }

    @Override
    public boolean applyEffectTick(@NotNull ServerLevel level, @NotNull LivingEntity entity, int amplifier) {
        float stressRelief = amplifier * 0.05f;
        StressUtil.updateStressRelief(entity, stressRelief);
        if (entity instanceof Player player) {
            StressUtil.updateStress(player, (int) (-stressRelief / 10));
        }
        return true;
    }
}
