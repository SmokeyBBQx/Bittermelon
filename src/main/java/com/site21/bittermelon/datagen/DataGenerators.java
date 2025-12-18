package com.site21.bittermelon.datagen;

import com.site21.bittermelon.Bittermelon;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        event.createProvider(BitterModelProvider::new);
        event.createProvider(BitterLanguageProvider::new);
    }
}