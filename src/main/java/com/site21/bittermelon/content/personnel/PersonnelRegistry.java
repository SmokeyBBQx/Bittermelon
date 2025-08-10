package com.site21.bittermelon.content.personnel;

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
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class PersonnelRegistry extends SavedData {
    private static PersonnelRegistry clientInstance;
    private final Map<Integer, PersonnelEntry> personnelEntries = new HashMap<>();

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
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void updateEntryFromServer(int id, PersonnelEntry entry) {
        personnelEntries.put(id, entry);
    }

    public void updateEntry(int id, Consumer<PersonnelEntry> updater) {
        PersonnelEntry entry = personnelEntries.get(id);
        if (entry != null) {
            updater.accept(entry);
            this.setDirty();
        }
    }

    public PersonnelEntry getEntry(int id) {
        return personnelEntries.get(id);
    }

    public Map<Integer, PersonnelEntry> getPersonnelEntries() {
        return personnelEntries;
    }

    public static @NotNull PersonnelRegistry load(@NotNull CompoundTag tag, HolderLookup.Provider lookupProvider) {
        PersonnelRegistry data = new PersonnelRegistry();
        ListTag entryList = tag.getList("personnel_entries", ListTag.TAG_COMPOUND);
        entryList.forEach(entryTag -> {
            CompoundTag compound = (CompoundTag) entryTag;
            int id = compound.getInt("id");
            PersonnelEntry.CODEC.parse(NbtOps.INSTANCE, compound.get("entry"))
                    .result()
                    .ifPresent(entry -> data.personnelEntries.put(id, entry));
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
