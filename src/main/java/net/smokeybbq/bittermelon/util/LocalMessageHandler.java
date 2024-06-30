package net.smokeybbq.bittermelon.util;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Objects;

public class LocalMessageHandler {

    LocalMessageHandler() {}

    public static void sendLocalMessage(Entity entity, int range, Component messageComponent) {
        List<ServerPlayer> serverPlayers = Objects.requireNonNull(entity.getServer()).getPlayerList().getPlayers();
        for (ServerPlayer serverPlayer : serverPlayers) {
            if (compareDistance(entity, serverPlayer) <= range) {
                serverPlayer.sendSystemMessage(messageComponent);
            }
        }
    }

    public static double compareDistance(Entity entity1, Entity entity2) {
        double x = Math.abs(entity1.getX() - entity2.getX());
        double y = Math.abs(entity1.getY() - entity2.getY());
        double z = Math.abs(entity1.getZ() - entity2.getZ());
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }
}
