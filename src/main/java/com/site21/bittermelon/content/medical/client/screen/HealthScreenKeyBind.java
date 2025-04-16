package com.site21.bittermelon.content.medical.client.screen;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.medical.client.screen.networking.OpenHealthScreenC2S;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.UUID;

import static com.site21.bittermelon.init.neoforge.BitterKeyBindings.HEALTH_SCREEN_KEY;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class HealthScreenKeyBind {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (HEALTH_SCREEN_KEY.get().consumeClick()) {
            openHealthScreen();
        }
    }

    public static void openHealthScreen() {
        Minecraft mc = Minecraft.getInstance();
        Player player = Minecraft.getInstance().player;

        if (player != null) {
            HitResult hitResult = mc.hitResult;
            if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
                EntityHitResult entityHit = (EntityHitResult) hitResult;
                PacketDistributor.sendToServer(new OpenHealthScreenC2S(player.getUUID(), entityHit.getEntity().getUUID()));
            } else {
                PacketDistributor.sendToServer(new OpenHealthScreenC2S(player.getUUID(), UUID.randomUUID()));
            }
        }
    }
}
