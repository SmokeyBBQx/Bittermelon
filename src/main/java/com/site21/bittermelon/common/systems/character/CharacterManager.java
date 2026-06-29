package com.site21.bittermelon.common.systems.character;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.character.skin.SkinOverrideSystem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.ACTIVE_CHARACTER;

public class CharacterManager extends SavedData {
    public static final SavedDataType<CharacterManager> TYPE;
    private static CharacterManager clientInstance;
    private final Map<UUID, Character> characters = new ConcurrentHashMap<>();

    public static CharacterManager get(@NotNull Level level) {
        if (level.isClientSide()) {
            return getClient();
        } else {
            ServerLevel overworld = level.getServer().getLevel(Level.OVERWORLD);
            return overworld.getDataStorage().computeIfAbsent(TYPE);
        }
    }

    public static @NotNull CharacterManager get(@NotNull MinecraftServer server) {
        return server.getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(TYPE);
    }

    
    private static CharacterManager getClient() {
        if (clientInstance == null) {
            clientInstance = new CharacterManager();
        }
        return clientInstance;
    }

    
    public static void clearClientData() {
        if (clientInstance != null) {
            clientInstance.characters.clear();
        }
    }

    /** Sets the active character for the given entity.
     *
     * @param entity        The entity to set the active character for.
     * @param characterUUID The UUID of the character to set as active.
     */
    public void setActiveCharacter(Entity entity, UUID characterUUID) {
        if (characters.containsKey(characterUUID)) {
            entity.setData(ACTIVE_CHARACTER.get(), characterUUID);
        }
    }

    /**
     * Gets the active character for the given entity.
     *
     * @param entity The entity to get the active character for.
     * @return The active character, or null if none is set or the character does not exist.
     */
    public @Nullable Character getActiveCharacter(@NotNull Entity entity) {
        UUID characterUUID = entity.getData(ACTIVE_CHARACTER.get());
        return characters.get(characterUUID);
    }

    /**
     * Gets the map of all characters.
     *
     * @return The map of all characters.
     */
    public Map<UUID, Character> getCharacters() {
        return characters;
    }

    /**
     * Gets a character by its UUID.
     *
     * @param uuid The UUID of the character.
     * @return The character, or null if not found.
     */
    public Character getCharacter(UUID uuid) {
        return characters.get(uuid);
    }

    /**
     * Adds a character to the manager.
     *
     * @param character The character to add.
     */
    public void addCharacter(Character character) {
        characters.put(character.getId(), character);
        setDirty();
    }

    /**
     * Removes a character from the manager.
     *
     * @param uuid The UUID of the character to remove.
     */
    public void removeCharacter(UUID uuid) {
        characters.remove(uuid);
        setDirty();
    }

    /**
     * Gets a list of characters associated with the given entity UUID.
     *
     * @param entityUUID The UUID of the entity.
     * @return A list of characters associated with the entity UUID.
     */
    public List<Character> getCharactersByEntityUUID(UUID entityUUID) {
        List<Character> characterList = new ArrayList<>();

        for (Character character : characters.values()) {
            if (character.getEntityUUID().equals(entityUUID)) {
                characterList.add(character);
            }
        }

        return characterList;
    }

    /**
     * Switches the active character for the given player.
     * Saves the previous character's data and loads the new character's data.
     *
     * @param player            The player to switch the character for.
     * @param previousCharacter The previous active character, or null if none.
     * @param switchedTo        The character to switch to.
     */
    public void switchCharacter(@NotNull Player player, @Nullable Character previousCharacter, @NotNull Character switchedTo) {
//        if (previousCharacter != null) {
//            CompoundTag playerData = player.saveWithoutId();
//            previousCharacter.savePlayerData(playerData, (ServerLevel) player.level());
//        }
//
//        CompoundTag newPlayerData = switchedTo.getPlayerData((ServerLevel) player.level());
//        if (newPlayerData != null) {
//            player.load(newPlayerData);
//            player.teleportTo(player.getX(), player.getY(), player.getZ());
//            player.getInventory().setChanged();
            setActiveCharacter(player, switchedTo.getId());
//        }

        switchedTo.getPlayerInfo().ifPresent(info ->
                SkinOverrideSystem.setSkinOverride(player.getUUID(), switchedTo.getId(), info.getSkinURL(), info.getModel()));
    }

    static {
        TYPE = new SavedDataType<>(
                Bittermelon.identifier("characters"),
                CharacterManager::new,
                RecordCodecBuilder.create(instance -> instance.group(
                        Character.CODEC.listOf().fieldOf("characters").forGetter(cm ->
                                new ArrayList<>(cm.characters.values()))
                ).apply(instance, (List<Character> chars) -> {
                    CharacterManager cm = new CharacterManager();
                    for (Character character : chars) {
                        cm.characters.put(character.getId(), character);
                    }
                    return cm;
                }))
        );
    }
}
