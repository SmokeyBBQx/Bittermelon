package com.site21.bittermelon.common.systems.atmosphere.data;

import com.site21.bittermelon.common.systems.atmosphere.AtmosInstance;
import com.site21.bittermelon.common.systems.atmosphere.networking.CreateAtmosInstance;
import com.site21.bittermelon.common.systems.atmosphere.networking.RemoveAtmosInstance;
import com.site21.bittermelon.common.systems.atmosphere.networking.SyncAtmosInstance;
import com.site21.bittermelon.common.systems.atmosphere.networking.SyncAtmosInstances;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AtmosLevelData extends SavedData {
    private static AtmosLevelData clientInstance;
    private final Map<UUID, AtmosInstance> atmosInstances = new ConcurrentHashMap<>();
    private static final String DATA_NAME = "atmosphere_instances";

    public static @NotNull AtmosLevelData get(@NotNull Level level) {
        if (!level.isClientSide()) {
            if (level instanceof ServerLevel serverLevel) {
                return serverLevel.getDataStorage().computeIfAbsent(
                        new Factory<>(
                                AtmosLevelData::new,
                                AtmosLevelData::load
                        ),
                        DATA_NAME
                );
            }
        }
        return getClient();
    }

    @OnlyIn(Dist.CLIENT)
    private static AtmosLevelData getClient() {
        if (clientInstance == null) {
            clientInstance = new AtmosLevelData();
        }
        return clientInstance;
    }

    @OnlyIn(Dist.CLIENT)
    public static void clearClientData() {
        if (clientInstance != null) {
            clientInstance.atmosInstances.clear();
        }
    }

    private static @NotNull AtmosLevelData load(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        AtmosLevelData data = new AtmosLevelData();
        CompoundTag instances = tag.getCompound("instances");
        for (String key : instances.getAllKeys()) {
            AtmosInstance.CODEC.parse(NbtOps.INSTANCE, instances.get(key))
                    .result()
                    .ifPresent(instance -> data.atmosInstances.put(instance.getUUID(), instance));
        }
        return data;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        CompoundTag instances = new CompoundTag();
        atmosInstances.forEach((uuid, instance) -> {
            AtmosInstance.CODEC.encodeStart(NbtOps.INSTANCE, instance)
                    .result()
                    .ifPresent(nbt -> instances.put(uuid.toString(), nbt));
        });
        compoundTag.put("instances", instances);
        return compoundTag;
    }

    public Map<UUID, AtmosInstance> getAtmosInstances() {
        return atmosInstances;
    }

    public AtmosInstance getAtmosInstance(UUID uuid) {
        return atmosInstances.get(uuid);
    }

    public void addAtmosInstance(AtmosInstance instance) {
        atmosInstances.put(instance.getUUID(), instance);
        PacketDistributor.sendToAllPlayers(new CreateAtmosInstance(instance));
        setDirty();
    }

    public void removeAtmosInstance(UUID uuid) {
        atmosInstances.remove(uuid);
        syncInstanceRemoval(uuid);
        setDirty();
    }

    public void syncToClient() {
        PacketDistributor.sendToAllPlayers(new SyncAtmosInstances(atmosInstances));
    }

    public void syncInstance(AtmosInstance instance) {
        PacketDistributor.sendToAllPlayers(new SyncAtmosInstance(instance));
    }

    public void syncInstanceRemoval(UUID uuid) {
        PacketDistributor.sendToAllPlayers(new RemoveAtmosInstance(uuid));
    }
}
