package com.site21.bittermelon.client.visualeffects.screenshake;

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

public class ScreenshakeHandler {
    public static final Map<UUID, ScreenshakeData> instances = new HashMap<>();

    public static class ScreenshakeData {
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

    private static float easeOut(float t) {
        return t * (2 - t);
    }
}
