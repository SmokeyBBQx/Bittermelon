package com.site21.bittermelon.events;

import net.minecraft.world.damagesource.DamageTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class DamageHandler {
    @SubscribeEvent
    public static void onLivingDamageEvent(LivingDamageEvent.Pre event) {
    }
}
