package com.site21.bittermelon.common.systems.rage;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.RAGE;

public class RageUtil {
    public static void updateRage(LivingEntity entity, int delta) {
        int newRage = entity.getData(RAGE) + delta;
        setRage(entity, newRage);
    }

    public static void setRage(LivingEntity entity, int value) {
        int newRage = Mth.clamp(value, 0, 100);
        RageHandler.triggerRageEvent(entity, newRage);

        entity.setData(RAGE, newRage);
    }
}
