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
import net.smokeybbq.bittermelon.util.CommandUtil;


public class PlayerEventHandler {

    /**
     * Saves character data when the player logs out.
     * Probably does not work on hard server crash.
     * Works on client crash.
     * TODO: Test if it works on server shutdown
     * @param event
     */
    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        ServerPlayer player = CommandUtil.keyToServerPlayer(event.getEntity().getUUID());
        if (player == null) { // hopefully never does anything
            return; // add exception later?
        }

        CommandUtil.validateStoredCharacterUUID(player);
        Character character = CharacterManager.getInstance().getActiveCharacter(player.getUUID());
        if (character != null) {
            CompoundTag playerData = player.saveWithoutId(new CompoundTag());
            character.savePlayerData(playerData);
        }
    }

    /**
     * Makes sure the player's active character and channel, if they exist, are recorded in the managers.
     * This mainly ensures that such data persists after a server reload and that invalid data is removed.
     * @param event
     */
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = CommandUtil.keyToServerPlayer(event.getEntity().getUUID());
        // just in case
        if (player == null) {
            return;
        }

        Character character = CommandUtil.getActiveCharacterFromData(player);
        if (character != null) {
            // prevents setting if the character is already active
            if (!CharacterManager.getActiveCharacters().containsValue(character)) {
                CharacterManager.setActiveCharacter(player, character);
            }

            Channel channel = CommandUtil.getActiveChannelFromData(player);
            if (channel != null) {
                if (ChannelManager.getCharacterActiveChannel(character) != channel) { // prevent unnecessary sets
                    ChannelManager.setCharacterActiveChannel(character, channel);
                }
            }
        }
    }

}
