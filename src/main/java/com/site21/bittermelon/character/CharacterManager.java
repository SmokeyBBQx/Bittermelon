package com.site21.bittermelon.character;

import com.site21.bittermelon.util.DataManager;
import net.neoforged.fml.loading.FMLPaths;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CharacterManager extends DataManager<UUID, Character> {
    private static CharacterManager instance = null;
    private final Map<UUID, List<Character>> UUIDToCharacter = new ConcurrentHashMap<>();
    private static final Map<UUID, Character> activeCharacters = new HashMap<>();


    protected CharacterManager() {
        super(FMLPaths.GAMEDIR.get().resolve("characters/").toString(), Character.class);
        mapEntitiesToCharacters();
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

    private void mapEntitiesToCharacters() {
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
        List<Character> characterList = UUIDToCharacter.get(character.getEntityUUID());
        if (characterList != null) {
            characterList.remove(character);
        }
        if (activeCharacters.containsValue(character)) {
            activeCharacters.remove(character.getEntityUUID(), character);
        }
    }

    public void setActiveCharacter(UUID entityUUID, Character character) {
        activeCharacters.put(entityUUID, character);
    }

    public void setActiveCharacter(UUID entityUUID, UUID characterUUID) {
        activeCharacters.put(entityUUID, getCharacter(characterUUID));
    }

    @Nullable
    public Character getActiveCharacter(UUID entityUUID) {
        return activeCharacters.get(entityUUID);
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

    public void updateData(UUID characterUUID) {
        saveData(getCharacter(characterUUID));
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
