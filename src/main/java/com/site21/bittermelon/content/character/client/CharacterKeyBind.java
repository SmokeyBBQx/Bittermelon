package com.site21.bittermelon.content.character.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.character.networking.OpenCharacterScreenC2S;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import static com.site21.bittermelon.init.neoforge.BitterKeyBindings.CHARACTER_KEY;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class CharacterKeyBind {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (CHARACTER_KEY.get().consumeClick()) {
            openCharacterScreen();
        }
    }

    public static void openCharacterScreen() {
//        Minecraft.getInstance().setScreen(new RoleSelectionScreen(null, null));
        ClientPacketDistributor.sendToServer(new OpenCharacterScreenC2S(Minecraft.getInstance().player.getUUID()));

//        HitResult hitResult = mc.hitResult;
//        if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
//            EntityHitResult entityHit = (EntityHitResult) hitResult;
//            PacketDistributor.sendToServer(new OpenHealthScreenC2S(player.getUUID(), entityHit.getEntity().getUUID()));
//        } else {
//            PacketDistributor.sendToServer(new OpenHealthScreenC2S(player.getUUID(), UUID.randomUUID()));
//        }

    }
}
