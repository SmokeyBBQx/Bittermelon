package com.site21.bittermelon.content.personnel.registry;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.personnel.registry.networking.AddPersonnelEntry;
import com.site21.bittermelon.content.personnel.privilege.PrivilegeManager;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class PersonnelRegistry extends SavedData {
    private static PersonnelRegistry clientInstance;
    private final Map<Integer, PersonnelEntry> personnelEntries = new HashMap<>();
    private final Map<UUID, Integer> characterToEntry = new HashMap<>();

    public static PersonnelRegistry get(@NotNull Level level) {
        if (level.isClientSide()) {
            return getClient();
        } else {
            ServerLevel overworld = Objects.requireNonNull(level.getServer()).getLevel(Level.OVERWORLD);
            assert overworld != null;
            return overworld.getDataStorage().computeIfAbsent(
                    new SavedData.Factory<>(
                            PersonnelRegistry::new,
                            PersonnelRegistry::load,
                            DataFixTypes.LEVEL
                    ),
                    "personnel_registry"
            );
        }
    }

    // Alternative static getter that doesn't require a level
    public static @NotNull PersonnelRegistry get(@NotNull MinecraftServer server) {
        return Objects.requireNonNull(server.getLevel(Level.OVERWORLD)).getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(
                        PersonnelRegistry::new,
                        PersonnelRegistry::load,
                        DataFixTypes.LEVEL
                ),
                "personnel_registry"
        );
    }

    @OnlyIn(Dist.CLIENT)
    private static PersonnelRegistry getClient() {
        if (clientInstance == null) {
            clientInstance = new PersonnelRegistry();
        }
        return clientInstance;
    }

    @OnlyIn(Dist.CLIENT)
    public static void clearClientData() {
        if (clientInstance != null) {
            clientInstance.personnelEntries.clear();
            clientInstance.characterToEntry.clear();
        }
    }

    /**
     * Gets the PersonnelEntry associated with the given ID.
     * @param id The ID of the PersonnelEntry to retrieve.
     * @return The PersonnelEntry with the specified ID, or null if none exists.
     */
    public PersonnelEntry getEntry(int id) {
        return personnelEntries.get(id);
    }

    /**
     * Gets the PersonnelEntry associated with the given Character.
     * @param character The Character whose PersonnelEntry is to be retrieved.
     * @return The PersonnelEntry associated with the Character, or null if none exists.
     */
    public PersonnelEntry getEntry(@NotNull Character character) {
        Integer entryId = characterToEntry.get(character.getUUID());
        if (entryId == null) {
            return null;
        }
        return getEntry(entryId);
    }

    /**
     * Gets the PersonnelEntry ID associated with the given Character.
     * @return The PersonnelEntry ID, or null if none exists.
     */
    public Map<Integer, PersonnelEntry> getPersonnelEntries() {
        return personnelEntries;
    }

    /**
     * Adds a new PersonnelEntry to the registry and notifies all clients.
     * @param entry The PersonnelEntry to add.
     */
    public void addEntry(PersonnelEntry entry) {
        personnelEntries.put(entry.getId(), entry);
        characterToEntry.put(entry.getCharacterUUID(), entry.getId());
        setDirty();
        PacketDistributor.sendToAllPlayers(new AddPersonnelEntry(entry));
    }

    /**
     * Removes a PersonnelEntry from the registry by its ID and notifies all clients.
     * @param id The ID of the PersonnelEntry to remove.
     */
    public void removeEntry(int id) {
        characterToEntry.remove(getEntry(id).getCharacterUUID());
        personnelEntries.remove(id);
        setDirty();
    }

    public boolean containsId(int id) {
        return personnelEntries.containsKey(id);
    }

    @OnlyIn(Dist.CLIENT)
    public void addEntryFromServer(PersonnelEntry entry) {
        personnelEntries.put(entry.getId(), entry);
    }

    public static @NotNull PersonnelRegistry load(@NotNull CompoundTag tag, HolderLookup.Provider lookupProvider) {
        PersonnelRegistry data = new PersonnelRegistry();
        ListTag entryList = tag.getList("personnel_entries", ListTag.TAG_COMPOUND);
        entryList.forEach(entryTag -> {
            CompoundTag compound = (CompoundTag) entryTag;
            int id = compound.getInt("id");
            PersonnelEntry.CODEC.parse(NbtOps.INSTANCE, compound.get("entry"))
                    .result()
                    .ifPresent(entry -> {
                        data.personnelEntries.put(id, entry);
                        data.characterToEntry.put(entry.getCharacterUUID(), id);
                    });
        });
        return data;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        ListTag entryList = new ListTag();

        personnelEntries.forEach((id, entry) -> {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putInt("id", id);
            PersonnelEntry.CODEC.encodeStart(NbtOps.INSTANCE, entry)
                    .result()
                    .ifPresent(entryNBT -> entryTag.put("entry", entryNBT));
            entryList.add(entryTag);
        });
        tag.put("personnel_entries", entryList);
        return tag;
    }
}
