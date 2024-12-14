package com.site21.bittermelon.miscellaneous;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

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
        if (!StumbleHandler.containsUUID(uuid)) return;
        if (StumbleHandler.isStunned(uuid)) return;

        if (Minecraft.getInstance().options.keyJump.isDown()) {
            keyPressed = true;
            ticksHeld++;
//                    PacketDistributor.sendToAllPlayers(new S2CSetForcedPose(uuid, Pose.SITTING));
            // TODO: Fix this animation
            if (ticksHeld >= TICKS_REQUIRED) {
                StumbleHandler.attemptToRise(uuid);
                keyPressed = false;
                ticksHeld = 0;
            }
        } else {
            ticksHeld = 0;
            keyPressed = false;
        }
    }

    public static int getTicksHeld() {
        return ticksHeld;
    }

    public static boolean isKeyPressed() {
        return keyPressed;
    }
}
