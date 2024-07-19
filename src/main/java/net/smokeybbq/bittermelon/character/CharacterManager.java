package net.smokeybbq.bittermelon.character;

import com.google.gson.Gson;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.model.CompositeModel;
import net.minecraftforge.fml.loading.FMLPaths;
import net.smokeybbq.bittermelon.util.DataManager;

public class CharacterManager extends DataManager<UUID, Character> {
    private static MinecraftServer minecraftServer;
    private static CharacterManager instance = null;
    private final Map<UUID, List<Character>> playerUUIDToCharacter = new ConcurrentHashMap<>();
    private static final Map<UUID, Character> activeCharacters = new HashMap<>();

    private CharacterManager() {
        super(FMLPaths.GAMEDIR.get().resolve("characters/").toString(), Character.class);
        mapPlayersToCharacters();
    }

    // Get the singleton instance of CharacterManager
    public static synchronized CharacterManager getInstance() {
        if (instance == null) {
            instance = new CharacterManager();
        }
        return instance;
    }

    // For adding data and saving it, use addCharacter instead for adding new characters
    @Override
    public void addData(UUID characterUUID, Character character) {
        saveData(character);
        dataMap.put(characterUUID, character);
    }

    public void mapPlayersToCharacters() {
        for (Character character : dataMap.values()) {
            playerUUIDToCharacter.computeIfAbsent(character.getPlayerUUID(), k -> new ArrayList<>()).add(character);
        }
    }

    public void addCharacter(Character character) {
        playerUUIDToCharacter.computeIfAbsent(character.getPlayerUUID(), k -> new ArrayList<>()).add(character);
        addData(character.getUUID(), character);
    }

    /**
     * Deletes the directory the character corresponds to and removes it from character maps
     * @param character Character to be removed
     */
    public void removeCharacter(Character character) {
        deleteData(character);
        List<Character> characterList = playerUUIDToCharacter.getOrDefault(character.getPlayerUUID(), null);
        if (characterList != null) {
            characterList.remove(character);
        }
        if (activeCharacters.containsValue(character)) {
            activeCharacters.remove(character.getPlayerUUID(), character);
        }
    }

    public static void setActiveCharacter(ServerPlayer player, Character character) {
        activeCharacters.put(player.getUUID(), character);
    }
    // gets the active character using UUID
    public static Character getActiveCharacter(UUID playerUUID) {
        return activeCharacters.getOrDefault(playerUUID, null);
    }

    public List<Character> getCharacters(UUID playerUUID) {
        return playerUUIDToCharacter.getOrDefault(playerUUID, new ArrayList<>());
    }

    public Character getCharacter(UUID characterUUID) {
        return getData(characterUUID);
    }

    public Map<UUID, Character> getCharacterMap() {
        return getDataMap();
    }

    public static Map<UUID, Character> getActiveCharacters() {
        return activeCharacters;
    }

    public void updateData(Character character) {
        saveData(character);
    }

    public static void setMinecraftServer(MinecraftServer server) {
        minecraftServer = server;
    }

    public static MinecraftServer getServer() {
        return minecraftServer;
    }

    @Override
    protected String getFileName(Character data) {
        return data.getUUID().toString();
    }

    @Override
    protected UUID getKey(Character data) {
        return data.getUUID();
    }

}
