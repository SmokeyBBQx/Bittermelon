package com.site21.bittermelon.atmosphere.data;

import com.mojang.serialization.Codec;
import com.site21.bittermelon.atmosphere.AtmosInstance;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AtmosLevelData extends SavedData {
    Map<UUID, AtmosInstance> atmosInstances = new HashMap<>();
    public static final Codec<Map<UUID, AtmosInstance>> INSTANCES_CODEC = Codec.unboundedMap(
            UUIDUtil.CODEC,
            AtmosInstance.CODEC
    );
    private static final String DATA_NAME = "atmosphere";

    @Contract("null -> fail")
    public static @NotNull AtmosLevelData get(Level level) {
        if (!(level instanceof ServerLevel serverLevel)) {
            throw new RuntimeException("Attempted to get AtmosLevelData from client side!");
        }
        return serverLevel.getDataStorage().computeIfAbsent(
                new Factory<>(
                        AtmosLevelData::new,
                        AtmosLevelData::load
                ),
                DATA_NAME
        );
    }

    private static @NotNull AtmosLevelData load(@NotNull CompoundTag tag, HolderLookup.Provider provider) {
        AtmosLevelData data = new AtmosLevelData();
        INSTANCES_CODEC.parse(NbtOps.INSTANCE, tag.get("Instances"))
                .result()
                .ifPresent(instances -> data.atmosInstances = instances);
        return data;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        INSTANCES_CODEC.encodeStart(NbtOps.INSTANCE, atmosInstances)
                .result()
                .ifPresent(tag -> compoundTag.put("Instances", tag));
        return compoundTag;
    }

    public Map<UUID, AtmosInstance> getAtmosInstances() {
        return atmosInstances;
    }

    public AtmosInstance getAtmosInstance(UUID uuid) {
        return atmosInstances.get(uuid);
    }

    public void addAtmosInstance(AtmosInstance instance) {
        atmosInstances.put(instance.getUuid(), instance);
        setDirty();
    }

    public void removeAtmosInstance(UUID uuid) {
        atmosInstances.remove(uuid);
        setDirty();
    }
}
