package net.smokeybbq.bittermelon.character;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.loading.FMLPaths;
import net.smokeybbq.bittermelon.util.DataManager;

public class CharacterManager extends DataManager<UUID, Character> {
    private static MinecraftServer minecraftServer;
    private static CharacterManager instance = null;
    private final Map<UUID, List<Character>> UUIDToCharacter = new ConcurrentHashMap<>();
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
            UUID entityUUID = character.getEntityUUID();
            if (entityUUID != null) {
                UUIDToCharacter.computeIfAbsent(entityUUID, k -> new ArrayList<>()).add(character);
            } else {
                System.err.println("Warning: Character found with null entityUUID: " + character.getUUID());
            }
        }
    }

    public void addCharacter(Character character) {
        UUIDToCharacter.computeIfAbsent(character.getEntityUUID(), k -> new ArrayList<>()).add(character);
        addData(character.getUUID(), character);
    }

    /**
     * Deletes the directory the character corresponds to and removes it from character maps
     * @param character Character to be removed
     */
    public void removeCharacter(Character character) {
        deleteData(character.getUUID());
        List<Character> characterList = UUIDToCharacter.getOrDefault(character.getEntityUUID(), null);
        if (characterList != null) {
            characterList.remove(character);
        }
        if (activeCharacters.containsValue(character)) {
            activeCharacters.remove(character.getEntityUUID(), character);
        }
    }

    public static void setActiveCharacter(UUID entityUUID, Character character) {
        activeCharacters.put(entityUUID, character);
    }
    // gets the active character using UUID
    public static Character getActiveCharacter(UUID entityUUID) {
        return activeCharacters.getOrDefault(entityUUID, null);
    }

    public List<Character> getCharacters(UUID entityUUID) {
        return UUIDToCharacter.getOrDefault(entityUUID, new ArrayList<>());
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
