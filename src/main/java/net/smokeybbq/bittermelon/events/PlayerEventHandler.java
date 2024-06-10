package net.smokeybbq.bittermelon.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.GameShuttingDownEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;


public class PlayerEventHandler {

    @SubscribeEvent
    /**
     * Saves character data when the server saves player data
     * Does not work on game shutdown or hard crash
     * TODO: Test if it works on server shutdown
      */
    public void onPlayerExit(PlayerEvent.SaveToFile event) {
        Player player = event.getEntity();
        Character activeCharacter = CharacterManager.getActiveCharacter(player.getUUID());
        if (activeCharacter != null) {
            CompoundTag playerData = player.saveWithoutId(new CompoundTag());
            activeCharacter.savePlayerData(playerData);
        }
    }
}
