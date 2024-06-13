package net.smokeybbq.bittermelon.util;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;
import net.smokeybbq.bittermelon.chat.Channel;
import net.smokeybbq.bittermelon.chat.ChannelManager;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CommandUtil {
    private static MinecraftServer server = CharacterManager.getServer();

    // just in case; only works for the active playerData
    public static void clearPersistentData(ServerPlayer player) {
        for (String key : player.getPersistentData().getAllKeys()) {
            player.getPersistentData().remove(key);
        }
    }

    /**
     * Ensures that activeCharacterUUID exists and corresponds to the active character.
     * Removes activeCharacterUUID if there is no active character.
     * @param player Target player
     * @return 0 if there is no active character, 1 if the data is validated
     */
    public static int validateStoredCharacterUUID(ServerPlayer player) {
        CompoundTag persistentData = player.getPersistentData();
        Character activeCharacter = CharacterManager.getActiveCharacter(player.getUUID());

        if (activeCharacter == null) {
            if (persistentData.contains("bittermelon:activeCharacterUUID")) {
                persistentData.remove("bittermelon:activeCharacterUUID");
            }
            return 0;
        }

        if (!persistentData.contains("bittermelon:activeCharacterUUID")) {
            persistentData.putUUID("bittermelon:activeCharacterUUID", activeCharacter.getUUID());
        } else if (!persistentData.getUUID("bittermelon:activeCharacterUUID").equals(activeCharacter.getUUID())) {
            persistentData.remove("bittermelon:activeCharacterUUID");
            persistentData.putUUID("bittermelon:activeCharacterUUID", activeCharacter.getUUID());
        }
        return 1;
    }

    /**
     * Retrieves the channel from the player's persistentData.
     * If the data returns a null channel for any reason, the persistentData it was pulled from is deleted.
     * @param player Target player
     * @return Channel that corresponds to the active playerData
     */
    @Nullable
    public static Channel getActiveChannelFromData(ServerPlayer player) {
        CompoundTag persistentData = player.getPersistentData();
        Channel channel = null;
        if (persistentData.contains("bittermelon:activeChannel")) {
            channel = ChannelManager.getInstance().getChannel(player.getPersistentData().getString("bittermelon:activeChannel")); // the tag should always be
            if (channel == null) {
                persistentData.remove("bittermelon:activeChannel");
            }
        }
        return channel;
    }

    @Nullable
    public static Character getActiveCharacterFromData(ServerPlayer player) {
        CompoundTag persistentData = player.getPersistentData();
        Character character = null;
        if (persistentData.contains("bittermelon:activeCharacterUUID")) {
            character = CharacterManager.getInstance().getCharacter(persistentData.getUUID("bittermelon:activeCharacterUUID")); // the tag should always be
            if (character == null) {
                persistentData.remove("bittermelon:activeCharacterUUID");
            }
        }
        return character;
    }

    // TODO: code review
    public static Channel getChannelIgnoreCase(String name) {
        List<Channel> channels = ChannelManager.getInstance().getChannels().values().stream().toList();

        Optional<Channel> selectedChannel = channels.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst();
        if (!selectedChannel.isPresent()) {
            return null;
        }
        return selectedChannel.get();
    }


    /**
     * Ignores case
     * @param player Player whose characters are being search
     * @param characterName Non-case sensitive name to search for
     * @return Character that corresponds to characterName under the player from context
     */
    @Nullable
    public static Character getCharacterIgnoreCase(ServerPlayer player, String characterName) {
        List<Character> characters = CharacterManager.getInstance().getCharacters(player.getUUID());

        Optional<Character> selectedCharacter = characters.stream()
                .filter(c -> c.getName().equalsIgnoreCase(characterName))
                .findFirst();

        if (!selectedCharacter.isPresent()) {
            return null;
        }
        return selectedCharacter.get();
    }

    /**
     *
     * @param playerUUID UUID of the target player
     * @return ServerPlayer corresponding to playerUUID on the player list
     */
    @Nullable
    public static ServerPlayer keyToServerPlayer(UUID playerUUID) {
        return server.getPlayerList().getPlayer(playerUUID);
    }

    /**
     * Adds the channel to the player's persistentData
     * @param channel Channel to be added
     * @param player Target player
     */
    public static void writeChannelToPlayerData(Channel channel, ServerPlayer player) {
        CompoundTag persistentData = player.getPersistentData();
        if (persistentData.contains("bittermelon:activeChannel")) {
            persistentData.remove("bittermelon:activeChannel");
        }
        persistentData.putString("bittermelon:activeChannel", channel.getName());
    }

    @Nullable
    public static ServerLevel getActiveLevel(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        if (data.contains("bittermelon:activeWorld")) {
            for (ServerLevel level : server.getAllLevels()) {
                if (level.dimension().toString().equals(data.getString("bittermelon:activeWorld"))) {
                    return level;
                }
            }
            data.remove("bittermelon:activeWorld");
        }
        return null;
    }

    public static void setActiveLevel(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        if (data.contains("bittermelon:activeWorld")) {
            data.remove("bittermelon:activeWorld");
        }
        data.putString("bittermelon:activeWorld", player.serverLevel().dimension().toString());
    }
}
