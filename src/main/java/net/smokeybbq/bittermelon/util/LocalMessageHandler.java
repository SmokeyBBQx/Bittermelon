package net.smokeybbq.bittermelon.util;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import java.util.List;

public class LocalMessageHandler {

    LocalMessageHandler() {}

    public static void sendLocalMessage(ServerPlayer player, int range, Component messageComponent) {
        List<ServerPlayer> serverPlayers = player.server.getPlayerList().getPlayers();
        for (ServerPlayer serverPlayer : serverPlayers) {
            if (compareDistance(player, serverPlayer) <= range) {
                serverPlayer.sendSystemMessage(messageComponent);
            }
        }
    }

    public static double compareDistance(ServerPlayer player1, ServerPlayer player2) {
        double x = Math.abs(player1.getX() - player2.getX());
        double y = Math.abs(player1.getY() - player2.getY());
        double z = Math.abs(player1.getZ() - player2.getZ());
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }
}
