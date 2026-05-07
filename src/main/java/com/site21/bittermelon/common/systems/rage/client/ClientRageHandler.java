package com.site21.bittermelon.common.systems.rage.client;

import com.site21.bittermelon.init.neoforge.BitterSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.world.entity.player.Player;

import static com.site21.bittermelon.common.systems.rage.RageHandler.HEARTBEAT_THRESHOLD;
import static com.site21.bittermelon.common.systems.rage.RageHandler.getHeartbeatDelay;
import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.RAGE;

public class ClientRageHandler {
    public static void tick(Minecraft minecraft, Player player) {
        long gameTime = player.level().getGameTime();
        int rage = player.getData(RAGE);

        if (rage > HEARTBEAT_THRESHOLD && gameTime % getHeartbeatDelay(rage) == 0) {
            float pitch = 1.0f + (rage - 20) / 100f;
            minecraft.getSoundManager().play(SimpleSoundInstance.forLocalAmbience(
                    BitterSounds.SLOW_BEAT.value(), pitch, 0.25f));
        }
    }
}
