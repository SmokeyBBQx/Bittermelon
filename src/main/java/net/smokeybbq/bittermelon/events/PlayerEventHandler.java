package net.smokeybbq.bittermelon.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;
import net.smokeybbq.bittermelon.chat.Channel;
import net.smokeybbq.bittermelon.chat.ChannelManager;


public class PlayerEventHandler {

    /**
     * Saves character data when the player logs out
     * Does not work on game shutdown or hard crash
     * TODO: Test if it works on server shutdown
    */
    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        ServerPlayer player = CharacterManager.keyToServerPlayer(event.getEntity().getUUID());
        if (player == null) { // hopefully never does anything
            return;
        }

        Character activeCharacter = CharacterManager.getActiveCharacter(player.getUUID());
        if (activeCharacter != null) {
            // enforces activeCharacterUUID in persistentData
            CompoundTag persistentData = player.getPersistentData();
            if (!persistentData.contains("bittermelon:activeCharacterUUID")) {
                persistentData.putUUID("bittermelon:activeCharacterUUID", activeCharacter.getUUID());
            } else if (!persistentData.getUUID("bittermelon:activeCharacterUUID").equals(activeCharacter.getUUID())) {
                persistentData.remove("bittermelon:activeCharacterUUID");
                persistentData.putUUID("bittermelon:activeCharacterUUID", activeCharacter.getUUID());
            }
            CompoundTag playerData = player.saveWithoutId(new CompoundTag());
            activeCharacter.savePlayerData(playerData);
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = CharacterManager.keyToServerPlayer(event.getEntity().getUUID());
        if (player == null) { // hopefully never does anything
            return;
        }

        CompoundTag persistentData = player.getPersistentData();
        if (persistentData.contains("bittermelon:activeCharacterUUID")) {
            Character character = CharacterManager.getInstance().getCharacter(persistentData.getUUID("bittermelon:activeCharacterUUID"));
            if (character == null) { // should only happen when the character has been deleted
                player.sendSystemMessage(Component.literal("Something went wrong with your character"));
                persistentData.remove("bittermelon:activeCharacterUUID");
                return;
            }
            if (!CharacterManager.getActiveCharacters().containsValue(character)) { // prevent unnecessary sets
                CharacterManager.setActiveCharacter(player, character);
            }

            if (persistentData.contains("bittermelon:activeChannel")) {
                Channel channel = ChannelManager.getInstance().getChannel(persistentData.getString("bittermelon:activeChannel"));
                if (channel == null) {
                    player.sendSystemMessage(Component.literal("Something went wrong with your channel")); // should only happen when the channel has been deleted
                    persistentData.remove("bittermelon:activeChannel");
                    return;
                }
                if (ChannelManager.getPlayerActiveChannel(character) != channel) { // prevent unnecessary sets
                    ChannelManager.setPlayerActiveChannel(character, channel);
                }
            }
        }
    }

    // just in case; only works for one playerData.dat at a time
    private static void clearPersistentData(ServerPlayer player) {
        for (String key : player.getPersistentData().getAllKeys()) {
            player.getPersistentData().remove(key);
        }
    }
}
