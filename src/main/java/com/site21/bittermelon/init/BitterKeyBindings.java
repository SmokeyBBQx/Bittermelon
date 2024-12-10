package com.site21.bittermelon.init;

import com.mojang.blaze3d.platform.InputConstants;
import com.site21.bittermelon.Bittermelon;
import net.neoforged.jarjar.nio.util.Lazy;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BitterKeyBindings {

    public static final Lazy<KeyMapping> HEALTH_SCREEN_KEY = Lazy.of(() ->
            new KeyMapping(
                    "key.bittermelon.health_screen",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_H,
                    "key.category.bittermelon"
            )
    );

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        event.register(HEALTH_SCREEN_KEY.get());
    }
}
