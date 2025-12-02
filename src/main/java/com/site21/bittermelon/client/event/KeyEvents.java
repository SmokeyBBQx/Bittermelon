package com.site21.bittermelon.client.event;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.character.networking.OpenCharacterScreenC2S;
import com.site21.bittermelon.common.systems.medical.client.screen.HealthScreenV2;
import com.site21.bittermelon.common.systems.throwing.ThrowItemPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import static com.site21.bittermelon.init.neoforge.BitterKeyBindings.*;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class KeyEvents {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (THROW_ITEM_KEY.get().consumeClick()) {
            Minecraft.getInstance().player.swing(InteractionHand.MAIN_HAND);
            ClientPacketDistributor.sendToServer(new ThrowItemPacket(Minecraft.getInstance().player.getUUID()));
        } else if (HEALTH_SCREEN_KEY.get().consumeClick()) {
            HealthScreenV2.openHealthScreen();
        } else if (CHARACTER_KEY.get().consumeClick()) {
            ClientPacketDistributor.sendToServer(new OpenCharacterScreenC2S(Minecraft.getInstance().player.getUUID()));
        }
    }
}
