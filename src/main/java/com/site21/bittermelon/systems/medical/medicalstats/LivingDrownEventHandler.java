package com.site21.bittermelon.systems.medical.medicalstats;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.systems.character.CharacterManager;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDrownEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class LivingDrownEventHandler {
    @SubscribeEvent
    public static void onLivingDrown(@NotNull LivingDrownEvent event) {
        LivingEntity entity = event.getEntity();

        if (CharacterManager.get(entity.level()).getActiveCharacter(entity) != null) {
            event.setCanceled(true);
        }
    }
}
