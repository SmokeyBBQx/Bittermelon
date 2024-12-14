package com.site21.bittermelon.events;

import com.site21.bittermelon.character.CharacterManager;
import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.damagesource.DamageTypes.MOB_ATTACK;

public class DamageHandler {

    @SubscribeEvent
    public static void OnIncomingDamage(@NotNull LivingIncomingDamageEvent event) {
        if (CharacterManager.getInstance().getActiveCharacter(event.getEntity().getUUID()) != null) {
            DamageSource source = event.getSource();

            if (source.is(MOB_ATTACK)) { return; }

        }
    }
}
