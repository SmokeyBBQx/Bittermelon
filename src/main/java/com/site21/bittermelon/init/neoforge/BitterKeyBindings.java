package com.site21.bittermelon.init.neoforge;

import com.mojang.blaze3d.platform.InputConstants;
import com.site21.bittermelon.Bittermelon;
import net.neoforged.jarjar.nio.util.Lazy;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BitterKeyBindings {

    public static final Lazy<KeyMapping> HEALTH_SCREEN_KEY = Lazy.of(() ->
            new KeyMapping(
                    "Health Screen",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_H,
                    "key.category.bittermelon"
            )
    );

    public static final Lazy<KeyMapping> THROW_ITEM_KEY = Lazy.of(() ->
            new KeyMapping(
                    "Throw Item",
                    KeyConflictContext.IN_GAME,
                    KeyModifier.ALT,
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_Q,
                    "key.category.bittermelon"
            )
    );

    public static final Lazy<KeyMapping> CHARACTER_KEY = Lazy.of(() ->
            new KeyMapping(
                    "Character",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_C,
                    "key.category.bittermelon"
            )
    );

    @SubscribeEvent
    public static void register(@NotNull RegisterKeyMappingsEvent event) {
        event.register(HEALTH_SCREEN_KEY.get());
        event.register(THROW_ITEM_KEY.get());
        event.register(CHARACTER_KEY.get());
    }
}
