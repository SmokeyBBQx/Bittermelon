package com.site21.bittermelon.common.systems.rage;

import com.site21.bittermelon.common.content.entities.mimicplayer.Mimic;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.ENRAGED;

public class RageHandler {
    public static final int HEARTBEAT_THRESHOLD = 20;
    private static final int RAGE_DECAY_INTERVAL = 60;

    public static void tick(Level level, Player player) {
        if (level.isClientSide) return;
        long gameTime = level.getGameTime();

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
        player.setGameMode(GameType.SPECTATOR);
        player.setCamera(mimic);
    }

    public static int getHeartbeatDelay(int rage) {
        return 10 + (100 - rage) / 5;
    }
}
