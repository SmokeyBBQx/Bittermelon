package com.site21.bittermelon.common.systems.rage;

import com.site21.bittermelon.common.content.entities.mimicplayer.Mimic;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.ENRAGED;
import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.RAGE;

public class RageHandler {
    private static final int HEARTBEAT_THRESHOLD = 20;
    private static final int RAGE_DECAY_INTERVAL = 60;

    public static void tick(Level level, Player player) {
        if (level.isClientSide) return;
        long gameTime = level.getGameTime();
        int rage = player.getData(RAGE);

        if (rage > HEARTBEAT_THRESHOLD && gameTime % getHeartbeatDelay(rage) == 0) {
            float pitch = 1.0f + (rage - 20) / 100f;
            player.playNotifySound(BitterSounds.SLOW_BEAT.value(), SoundSource.AMBIENT, 0.25f, pitch);
        }

        if (gameTime % RAGE_DECAY_INTERVAL != 0) return;
        RageUtil.updateRage(player, -1);
    }

    public static void triggerRageEvent(LivingEntity entity, int amount) {
        if (amount < 100) return;

        if (entity instanceof ServerPlayer player) {
            if (!player.getData(ENRAGED)) {
                spawnMimic(player.level(), player);
                player.setData(ENRAGED, true);
            }
        }
    }

    private static void spawnMimic(Level level, ServerPlayer player) {
        Mimic mimic = new Mimic(level, player);
        mimic.setPos(player.getX(), player.getY(), player.getZ());
        level.addFreshEntity(mimic);
        player.setCamera(mimic);
        player.setInvulnerable(true);
        player.setInvisible(true);
    }

    private static int getHeartbeatDelay(int rage) {
        return 10 + (100 - rage) / 5;
    }
}
