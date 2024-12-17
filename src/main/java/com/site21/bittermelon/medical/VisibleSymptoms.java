package com.site21.bittermelon.medical;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.character.CharacterManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class VisibleSymptoms {

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.@NotNull Post event) {
    }
}
