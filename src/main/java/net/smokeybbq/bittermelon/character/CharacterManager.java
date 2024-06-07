package net.smokeybbq.bittermelon.character;

import com.google.gson.Gson;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
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

    }


    public static synchronized CharacterManager getInstance() {
        if (instance == null) {
            instance = new CharacterManager();
        }
        return instance;
    }

    @Override
    public void addData(UUID characterUUID, Character character) {
        saveData(character);
        dataMap.put(characterUUID, character);
        playerUUIDToCharacter.computeIfAbsent(character.getPlayerUUID(), k -> new ArrayList<>()).add(character);
    }

    public void addCharacter(UUID playerUUID, Character character) {
        playerUUIDToCharacter.computeIfAbsent(playerUUID, k -> new ArrayList<>()).add(character);
        addData(character.getUUID(), character);
    }

    public static void setActiveCharacter(ServerPlayer player, Character character) {
        activeCharacters.put(player.getUUID(), character);
    }
    public static Character getActiveCharacter(ServerPlayer player) {
        return activeCharacters.getOrDefault(player.getUUID(), null);
    }

    public List<Character> getCharacters(UUID playerUUID) {
        return playerUUIDToCharacter.getOrDefault(playerUUID, new ArrayList<>());
    }

    public Character getCharacter(UUID characterUUID) {
        return getData(characterUUID);
    }

    public static void setMinecraftServer(MinecraftServer server) {
        minecraftServer = server;
    }

    public static MinecraftServer getServer() {
        return minecraftServer;
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

    @Override
    protected UUID getFileName(Character data) {
        return data.getUUID();
    }

    @Override
    protected UUID getKey(Character data) {
        return null;
    }
}
