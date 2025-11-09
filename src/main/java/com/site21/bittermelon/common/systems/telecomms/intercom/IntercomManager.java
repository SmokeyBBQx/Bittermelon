package com.site21.bittermelon.common.systems.telecomms.intercom;

import com.site21.bittermelon.common.content.blocks.electronics.intercom.IntercomBlockEntity;
import com.site21.bittermelon.common.systems.syncsound.SyncSoundEvent;
import com.site21.bittermelon.common.systems.telecomms.intercom.networking.AddIntercomToClient;
import com.site21.bittermelon.common.systems.telecomms.intercom.networking.RemoveIntercomFromClient;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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

    /**
     * Transmits a message to all intercoms with the specified target ID.
     * @param event The sound event to transmit.
     * @param targetID The target intercom ID.
     * @param level The level in which to find the intercoms.
     */
    public void transmitMessage(SyncSoundEvent event, String targetID, Level level) {
        for (Map.Entry<BlockPos, String> entry : intercomIDs.entrySet()) {
            if (Objects.equals(entry.getValue(), targetID)) {
                if (level.getBlockEntity(entry.getKey()) instanceof IntercomBlockEntity intercom) {
                    intercom.transmitMessage(event);
                }
            }
        }
    }

    /**
     * Adds an intercom to the manager and syncs it to all clients.
     * @param pos The position of the intercom.
     * @param id The unique ID of the intercom.
     */
    public void addIntercom(BlockPos pos, String id) {
        if (id != null && !id.isEmpty()) {
            intercomIDs.put(pos, id);
            setDirty();
            PacketDistributor.sendToAllPlayers(new AddIntercomToClient(pos, id));
        }
    }

    /**
     * Removes an intercom from the manager and syncs the removal to all clients.
     * @param pos The position of the intercom to remove.
     */
    public void removeIntercom(BlockPos pos) {
        if (intercomIDs.remove(pos) != null) {
            setDirty();
            PacketDistributor.sendToAllPlayers(new RemoveIntercomFromClient(pos));
        }
    }

    /**
     * Gets the intercom ID at the given position.
     * @param pos The position to check for an intercom.
     * @return The intercom ID at the given position, or null if none exists.
     */
    public String getIntercomId(BlockPos pos) {
        return intercomIDs.get(pos);
    }

    /**
     * Gets a map of all intercom positions and their corresponding IDs.
     * @return A map of all intercom positions and their IDs.
     */
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
