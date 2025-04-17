package com.site21.bittermelon.content.throwing;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static com.site21.bittermelon.init.neoforge.BitterKeyBindings.THROW_ITEM_KEY;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class ThrowKeyBind {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (THROW_ITEM_KEY.get().consumeClick()) {
            PacketDistributor.sendToServer(new ThrowItem(Minecraft.getInstance().player.getUUID()));
        }
    }
}
