package com.site21.bittermelon.client.effects;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class ScreenshakeHandler {
    private static final Random random = new Random();
    private static final Map<UUID, ScreenshakeData> instances = new HashMap<>();

    private static class ScreenshakeData {
        public int duration;
        private final int totalDuration;
        private final float intensity;

        ScreenshakeData(int duration, float intensity) {
            this.duration = duration;
            this.totalDuration = duration;
            this.intensity = intensity;
        }

        float getCurrentIntensity() {
            float progress = (float) duration / totalDuration;
            return intensity * easeOut(progress);
        }
    }

    public static void startScreenshake(@NotNull Player player, int duration, float intensity) {
        // TODO: Accessibility settings (reduce intensity)

        instances.put(player.getUUID(), new ScreenshakeData(duration, intensity));
    }

    @SubscribeEvent
    public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        UUID playerUUID = player.getUUID();
        ScreenshakeData shakeData = instances.get(playerUUID);

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

    private static float easeOut(float t) {
        return t * (2 - t);
    }
}
