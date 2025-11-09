package com.site21.bittermelon.common.systems.stumble.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.stumble.StumbleHandler;
import com.site21.bittermelon.common.systems.stumble.networking.AttemptToRise;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.UUID;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class RiseKeyHandler {
    private static int ticksHeld = 0;
    public static final int TICKS_REQUIRED = 25;
    private static boolean keyPressed = false;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        UUID uuid = player.getUUID();
        if (StumbleHandler.isStumbled(player)) {
            if (StumbleHandler.isStunned(player)) return;

            if (Minecraft.getInstance().options.keyJump.isDown()) {
                keyPressed = true;
                ticksHeld++;
                if (ticksHeld >= TICKS_REQUIRED) {
                    PacketDistributor.sendToServer(new AttemptToRise(uuid));
                    keyPressed = false;
                    ticksHeld = 0;
                }
            } else {
                ticksHeld = 0;
                keyPressed = false;
            }
        }
    }

    public static int getTicksHeld() {
        return ticksHeld;
    }

    public static boolean isKeyPressed() {
        return keyPressed;
    }
}