package com.site21.bittermelon.content.blocks.scp.scp151;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterMobEffects.DROWNING;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class LivingBreatheEventHandler {
    @SubscribeEvent
    public static void onLivingBreathe(@NotNull LivingBreatheEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (player.hasEffect(DROWNING)) {
            if (player.getEffect(DROWNING).getAmplifier() > 9) {
                event.setCanBreathe(false);
            }
            event.setRefillAirAmount(0);
        }
    }
}
