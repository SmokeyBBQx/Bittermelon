package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class BitterGameRules {
    public static GameRules.Key<GameRules.BooleanValue> ENTITIES_MAKE_FLOORS_DIRTY_RULE;

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        ENTITIES_MAKE_FLOORS_DIRTY_RULE = GameRules.register(
                "entitiesMakeFloorsDirty",
                GameRules.Category.MISC,
                GameRules.BooleanValue.create(false));
    }
}
