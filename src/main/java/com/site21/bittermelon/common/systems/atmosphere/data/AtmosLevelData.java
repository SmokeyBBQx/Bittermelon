package com.site21.bittermelon.common.systems.atmosphere.data;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.common.systems.atmosphere.AtmosInstance;
import com.site21.bittermelon.common.systems.atmosphere.networking.CreateAtmosInstance;
import com.site21.bittermelon.common.systems.atmosphere.networking.RemoveAtmosInstance;
import com.site21.bittermelon.common.systems.atmosphere.networking.SyncAtmosInstance;
import com.site21.bittermelon.common.systems.atmosphere.networking.SyncAtmosInstances;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AtmosLevelData extends SavedData {
    public static final SavedDataType<AtmosLevelData> TYPE;
    private static AtmosLevelData clientInstance;
    private final Map<UUID, AtmosInstance> atmosInstances = new ConcurrentHashMap<>();

    public static @NotNull AtmosLevelData get(@NotNull Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.getDataStorage().computeIfAbsent(TYPE);
        }
        return getClient();
    }

    
    private static AtmosLevelData getClient() {
        if (clientInstance == null) {
            clientInstance = new AtmosLevelData();
        }
        return clientInstance;
    }

    
    public static void clearClientData() {
        if (clientInstance != null) {
            clientInstance.atmosInstances.clear();
        }
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

    static {
        TYPE = new SavedDataType<>(
                "atmosphere",
                AtmosLevelData::new,
                RecordCodecBuilder.create(instance -> instance.group(
                                AtmosInstance.CODEC.listOf().fieldOf("instances")
                                        .forGetter(data -> new ArrayList<>(data.atmosInstances.values())))
                        .apply(instance, instances -> {
                            AtmosLevelData data = new AtmosLevelData();
                            for (AtmosInstance atmosInstance : instances) {
                                data.atmosInstances.put(atmosInstance.getUUID(), atmosInstance);
                            }
                            return data;
                        })));
    }
}
