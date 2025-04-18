package com.site21.bittermelon.content.stumble.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.stumble.StumbleHandler;
import com.site21.bittermelon.content.stumble.networking.AttemptToRise;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.UUID;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.STUMBLE_TICKS;

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
            player.setData(STUMBLE_TICKS, player.getData(STUMBLE_TICKS) - 1);
            if (StumbleHandler.isStunned(player)) return;

            if (Minecraft.getInstance().options.keyJump.isDown()) {
                keyPressed = true;
                ticksHeld++;
//                    PacketDistributor.sendToAllPlayers(new SetForcedPose(uuid, Pose.SITTING));
                // TODO: Fix this animation
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
