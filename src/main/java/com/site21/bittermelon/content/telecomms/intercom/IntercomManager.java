package com.site21.bittermelon.content.telecomms.intercom;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.devices.implementations.intercom.IntercomBlockEntity;
import com.site21.bittermelon.content.syncsound.SyncSoundEvent;
import com.site21.bittermelon.content.telecomms.intercom.networking.AddIntercomToClient;
import com.site21.bittermelon.content.telecomms.intercom.networking.RemoveIntercomFromClient;
import com.site21.bittermelon.content.telecomms.intercom.networking.SyncIntercomList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class IntercomManager extends SavedData {
    private static IntercomManager clientInstance;
    private static final String DATA_NAME = "intercom";
    private final Map<BlockPos, String> intercomIDs = new HashMap<>();

    public static IntercomManager get(@NotNull Level level) {
        if (level.isClientSide()) {
            return getClient();
        } else {
            ServerLevel overworld = level.getServer().getLevel(Level.OVERWORLD);
            return overworld.getDataStorage().computeIfAbsent(
                    new SavedData.Factory<>(
                            IntercomManager::new,
                            IntercomManager::load,
                            DataFixTypes.LEVEL
                    ),
                    DATA_NAME
            );
        }
    }

    public static @NotNull IntercomManager get(@NotNull MinecraftServer server) {
        return server.getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(
                        IntercomManager::new,
                        IntercomManager::load,
                        DataFixTypes.LEVEL
                ),
                DATA_NAME
        );
    }

    @OnlyIn(Dist.CLIENT)
    private static IntercomManager getClient() {
        if (clientInstance == null) {
            clientInstance = new IntercomManager();
        }
        return clientInstance;
    }

    @OnlyIn(Dist.CLIENT)
    public static void clearClientData() {
        if (clientInstance != null) {
            clientInstance.intercomIDs.clear();
        }
    }

    public void transmitMessage(SyncSoundEvent event, String targetID, Level level) {
        for (Map.Entry<BlockPos, String> entry : intercomIDs.entrySet()) {
            if (Objects.equals(entry.getValue(), targetID)) {
                if (level.getBlockEntity(entry.getKey()) instanceof IntercomBlockEntity intercom) {
                    intercom.transmitMessage(event);
                }
            }
        }
    }

    public void addIntercom(BlockPos pos, String id) {
        if (id != null && !id.isEmpty()) {
            intercomIDs.put(pos, id);
            setDirty();
            PacketDistributor.sendToAllPlayers(new AddIntercomToClient(pos, id));
        }
    }

    public void removeIntercom(BlockPos pos) {
        if (intercomIDs.remove(pos) != null) {
            setDirty();
            PacketDistributor.sendToAllPlayers(new RemoveIntercomFromClient(pos));
        }
    }

    public String getIntercomId(BlockPos pos) {
        return intercomIDs.get(pos);
    }

    public Map<BlockPos, String> getIntercomIDs() {
        return intercomIDs;
    }

    @OnlyIn(Dist.CLIENT)
    public void addIntercomFromServer(BlockPos pos, String id) {
        intercomIDs.put(pos, id);
    }

    @OnlyIn(Dist.CLIENT)
    public void removeIntercomFromServer(BlockPos pos) {
        intercomIDs.remove(pos);
    }

    @OnlyIn(Dist.CLIENT)
    public void updateAllFromServer(Map<BlockPos, String> intercoms) {
        intercomIDs.clear();
        intercomIDs.putAll(intercoms);
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        ListTag positionsList = new ListTag();

        for (Map.Entry<BlockPos, String> entry : intercomIDs.entrySet()) {
            CompoundTag positionTag = new CompoundTag();
            positionTag.putLong("pos", entry.getKey().asLong());
            positionTag.putString("id", entry.getValue());
            positionsList.add(positionTag);
        }

        tag.put("positions", positionsList);
        return tag;
    }

    public static @NotNull IntercomManager load(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        IntercomManager manager = new IntercomManager();

        ListTag positionsList = tag.getList("positions", ListTag.TAG_COMPOUND);

        for (int i = 0; i < positionsList.size(); i++) {
            CompoundTag positionTag = positionsList.getCompound(i);
            long posLong = positionTag.getLong("pos");
            String id = positionTag.getString("id");

            if (!id.isEmpty()) {
                BlockPos pos = BlockPos.of(posLong);
                manager.intercomIDs.put(pos, id);
            }
        }

        return manager;
    }
}
