package com.site21.bittermelon.content.character;

import com.site21.bittermelon.content.character.networking.SyncCharacters;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.ACTIVE_CHARACTER;

public class CharacterManager extends SavedData {
    private static CharacterManager clientInstance;
    private final Map<UUID, Character> characters = new HashMap<>();
    private static final String DATA_NAME = "character_registry";

    public static CharacterManager get(@NotNull Level level) {
        if (level.isClientSide()) {
            return getClient();
        } else {
            ServerLevel overworld = level.getServer().getLevel(Level.OVERWORLD);
            return overworld.getDataStorage().computeIfAbsent(
                    new SavedData.Factory<>(
                            CharacterManager::new,
                            CharacterManager::load,
                            DataFixTypes.LEVEL
                    ),
                    DATA_NAME
            );
        }
    }

    public static @NotNull CharacterManager get(@NotNull MinecraftServer server) {
        return server.getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(
                        CharacterManager::new,
                        CharacterManager::load,
                        DataFixTypes.LEVEL
                ),
                DATA_NAME
        );
    }

    @OnlyIn(Dist.CLIENT)
    private static CharacterManager getClient() {
        if (clientInstance == null) {
            clientInstance = new CharacterManager();
        }
        return clientInstance;
    }

    @OnlyIn(Dist.CLIENT)
    public static void clearClientData() {
        if (clientInstance != null) {
            clientInstance.characters.clear();
        }
    }

    public void setActiveCharacter(Entity entity, UUID characterUUID) {
        if (characters.containsKey(characterUUID)) {
            entity.setData(ACTIVE_CHARACTER.get(), characterUUID);
        }
    }

    public @Nullable Character getActiveCharacter(@NotNull Entity entity) {
        UUID characterUUID = entity.getData(ACTIVE_CHARACTER.get());

        return characters.get(characterUUID);
    }

    public Map<UUID, Character> getCharacters() {
        return characters;
    }

    public Character getCharacter(UUID uuid) {
        return characters.get(uuid);
    }

    public void addCharacter(Character character) {
        characters.put(character.getUUID(), character);
        setDirty();
    }

    public void removeCharacter(UUID uuid) {
        characters.remove(uuid);
        setDirty();
    }

    public void updateCharacter(UUID uuid, Consumer<Character> updater) {
        Character character = characters.get(uuid);
        if (character != null) {
            updater.accept(character);
            setDirty();
        }
    }

    public List<Character> getCharactersByEntityUUID(UUID entityUUID) {
        List<Character> characterList = new ArrayList<>();

        for (Character character : characters.values()) {
            if (character.getEntityUUID().equals(entityUUID)) {
                characterList.add(character);
            }
        }

        return characterList;
    }

    @OnlyIn(Dist.CLIENT)
    public void updateCharacterFromServer(Character character) {
        characters.put(character.getUUID(), character);
    }

    public static @NotNull CharacterManager load(@NotNull CompoundTag tag, HolderLookup.Provider lookupProvider) {
        CharacterManager manager = new CharacterManager();

        ListTag characterList = tag.getList("characters", ListTag.TAG_COMPOUND);
        characterList.forEach(characterTag -> {
            Character.CODEC.parse(NbtOps.INSTANCE, characterTag)
                    .result()
                    .ifPresent(character -> manager.characters.put(character.getUUID(), character));
        });

        return manager;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        ListTag characterList = new ListTag();
        characters.values().forEach(character -> {
            Character.CODEC.encodeStart(NbtOps.INSTANCE, character)
                    .result()
                    .ifPresent(characterList::add);
        });
        tag.put("characters", characterList);

        return tag;
    }

    private void syncToClient() {
        PacketDistributor.sendToAllPlayers(new SyncCharacters(characters));

        // TODO: Add syncing for individual characters
    }

    @Override
    public void setDirty() {
        super.setDirty();
        syncToClient();
    }
}
