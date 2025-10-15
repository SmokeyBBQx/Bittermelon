package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class BitterScreens {
    @SubscribeEvent
    public static void register(@NotNull RegisterMenuScreensEvent event) {
    }
}
