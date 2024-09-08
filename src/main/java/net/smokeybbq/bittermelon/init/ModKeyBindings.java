package net.smokeybbq.bittermelon.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
    public static final KeyMapping THROW_ITEM = new KeyMapping(
            "key.bittermelon.throw_item",
            KeyConflictContext.IN_GAME,
            KeyModifier.ALT,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Q,
            KeyMapping.CATEGORY_MISC
    );

    public static final KeyMapping RADIO_CHAT = new KeyMapping(
            "key.bittermelon.radio_chat",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Y,
            KeyMapping.CATEGORY_MISC
    );

    public static void register(RegisterKeyMappingsEvent event) {
        event.register(THROW_ITEM);
        event.register(RADIO_CHAT);
    }
}
