package com.site21.bittermelon.client.visualeffects.screenshake;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;

import java.util.Random;
import java.util.UUID;

import static com.site21.bittermelon.client.visualeffects.screenshake.ScreenshakeHandler.instances;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class ScreenshakeRenderer {
    private static final Random random = new Random();

    @SubscribeEvent
    public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        UUID playerUUID = player.getUUID();
        ScreenshakeHandler.ScreenshakeData shakeData = instances.get(playerUUID);

        if (shakeData != null && shakeData.duration > 0) {
            float intensity = shakeData.getCurrentIntensity();
            float offsetYaw = (random.nextFloat() - 0.5F) * intensity;
            float offsetPitch = (random.nextFloat() - 0.5F) * intensity;
            float offsetRoll = (random.nextFloat() - 0.5F) * intensity;

            event.setYaw(event.getYaw() + offsetYaw);
            event.setPitch(event.getPitch() + offsetPitch);
            event.setRoll(event.getRoll() + offsetRoll);

            shakeData.duration--;
            if (shakeData.duration <= 0) {
                instances.remove(playerUUID);
            }
        }
    }
}
