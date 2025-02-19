package com.site21.bittermelon.util;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class LocalMessageHelper {
    public static void sendLocalMessage(@NotNull Entity entity, int range, Component messageComponent) {
        List<ServerPlayer> serverPlayers = Objects.requireNonNull(entity.getServer()).getPlayerList().getPlayers();
        for (ServerPlayer serverPlayer : serverPlayers) {
            if (entity.distanceTo(serverPlayer) <= range) {
                serverPlayer.sendSystemMessage(messageComponent);
            }
        }
    }

    public static void sendLocalMessage(@NotNull Level level, @NotNull BlockPos pos, int range, Component messageComponent) {
        List<ServerPlayer> serverPlayers = Objects.requireNonNull(level.getServer()).getPlayerList().getPlayers();
        double rangeSq = range * range;
        for (ServerPlayer serverPlayer : serverPlayers) {
            if (pos.distToCenterSqr(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ()) <= rangeSq) {
                serverPlayer.sendSystemMessage(messageComponent);
            }
        }
    }
}
