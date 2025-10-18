package com.site21.bittermelon.util;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class LocalMessageHelper {
    /**
     * Sends a local message to all players within a certain range of an entity.
     *
     * @param entity           The entity to measure distance from.
     * @param range            The range within which players will receive the message.
     * @param messageComponent The message to send.
     */
    public static void sendLocalMessage(@NotNull Entity entity, int range, Component messageComponent) {
        List<ServerPlayer> serverPlayers = Objects.requireNonNull(entity.getServer()).getPlayerList().getPlayers();
        for (ServerPlayer serverPlayer : serverPlayers) {
            if (entity.distanceTo(serverPlayer) <= range) {
                serverPlayer.sendSystemMessage(messageComponent);
            }
        }
    }

    /**
     * Sends a local message to all players within a certain range of a block position.
     *
     * @param level            The level where the block position is located.
     * @param pos              The block position to measure distance from.
     * @param range            The range within which players will receive the message.
     * @param messageComponent The message to send.
     */
    public static void sendLocalMessage(@NotNull Level level, @NotNull BlockPos pos, int range, Component messageComponent) {
        List<ServerPlayer> serverPlayers = Objects.requireNonNull(level.getServer()).getPlayerList().getPlayers();
        double rangeSq = range * range;
        for (ServerPlayer serverPlayer : serverPlayers) {
            if (pos.distToCenterSqr(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ()) <= rangeSq) {
                serverPlayer.sendSystemMessage(messageComponent);
            }
        }
    }

    /**
     * Sends an emote message from a character associated with an entity to all players within a certain range.
     *
     * @param level   The level where the entity is located.
     * @param entity  The entity whose character will send the emote message.
     * @param range   The range within which players will receive the message.
     * @param message The emote message to send.
     */
    public static void sendEmoteMessage(@NotNull Level level, Entity entity, int range, String message) {
        if (level.isClientSide) return;

        Character character = CharacterManager.get(level).getActiveCharacter(entity);
        if (character != null) {
            sendLocalMessage(level, entity.getOnPos(), range, Component.literal(character.getName() + " " + message).withColor(character.getEmoteColor()));
        }
    }
}
