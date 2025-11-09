package com.site21.bittermelon.common.systems.germs;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GermRegistry extends SavedData {
    private final Map<UUID, Germ> germs = new HashMap<>();
    private static final String DATA_NAME = "germ_registry";

    public GermRegistry() {}

    @Contract("null -> fail")
    public static @NotNull GermRegistry get(Level level) {
        if (!(level instanceof ServerLevel serverLevel)) {
            throw new RuntimeException("Attempted to get GermRegistry from client side!");
        }
        return serverLevel.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(
                        GermRegistry::new,
                        GermRegistry::load,
                        DataFixTypes.LEVEL
                ),
                DATA_NAME
        );
    }

    public UUID registerGerm(Germ germ) {
        UUID id = UUID.randomUUID();
        germs.put(id, germ);
        setDirty();
        return id;
    }

    public Germ getGerm(UUID id) {
        return germs.get(id);
    }

    public void removeGerm(UUID id) {
        germs.remove(id);
        setDirty();
    }

    public static @NotNull GermRegistry load(@NotNull CompoundTag tag, HolderLookup.Provider lookupProvider) {
        GermRegistry data = new GermRegistry();
        ListTag germList = tag.getList("germs", ListTag.TAG_COMPOUND);
        germList.forEach(germTag -> {
            CompoundTag compound = (CompoundTag) germTag;
            UUID id = compound.getUUID("id");
            Germ.CODEC.parse(NbtOps.INSTANCE, compound.get("germ"))
                    .result()
                    .ifPresent(germ -> data.germs.put(id, germ));
        });
        return data;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookupProvider) {
        ListTag germList = new ListTag();

        germs.forEach((id, germ) -> {
            CompoundTag germTag = new CompoundTag();
            germTag.putUUID("id", id);
            Germ.CODEC.encodeStart(NbtOps.INSTANCE, germ)
                    .result()
                    .ifPresent(germNBT -> germTag.put("germ", germNBT));
            germList.add(germTag);
        });
        tag.put("germs", germList);
        return tag;
    }
}
