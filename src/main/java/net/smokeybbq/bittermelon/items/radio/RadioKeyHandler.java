package net.smokeybbq.bittermelon.items.radio;

import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.smokeybbq.bittermelon.init.ModKeyBindings;
import net.smokeybbq.bittermelon.items.radio.networking.RadioKeyC2SPacket;
import net.smokeybbq.bittermelon.networking.PacketHandler;
import net.smokeybbq.bittermelon.util.ModLogger;

public class RadioKeyHandler {
    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        if (ModKeyBindings.RADIO_CHAT.consumeClick()) {
            ModLogger.debug("Radio key pressed");
            PacketHandler.INSTANCE.sendToServer(new RadioKeyC2SPacket());
        }
    }
}
