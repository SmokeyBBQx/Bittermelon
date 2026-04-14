package com.site21.bittermelon.common.content.entities.scp718;

import com.site21.bittermelon.common.systems.stress.StressUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class EyeballGrowthEffect extends MobEffect {
    public EyeballGrowthEffect() {
        super(MobEffectCategory.HARMFUL, 0);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 100 == 0;
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        if (entity instanceof Player player) {
            StressUtil.updateStress(player, 2 * amplifier);
        }

        return true;
    }
}
