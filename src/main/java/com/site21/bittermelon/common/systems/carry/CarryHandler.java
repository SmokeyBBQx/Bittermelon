package com.site21.bittermelon.common.systems.carry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PlayerRideable;
import org.jetbrains.annotations.NotNull;

public class CarryHandler {
    public static void tickCarrying(@NotNull LivingEntity entity) {
        if (entity.isVehicle() && !(entity instanceof PlayerRideable)) {
            float totalVolume = 0;

            for (Entity passenger : entity.getPassengers()) {
                EntityDimensions dimensions = passenger.getDimensions(passenger.getPose());
                totalVolume += dimensions.width() * dimensions.height();
            }

            if (totalVolume < 1.0f) return;

            entity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 2,
                    Math.min((int)(totalVolume / 2), 6), true, false, true));
        }
    }
}
