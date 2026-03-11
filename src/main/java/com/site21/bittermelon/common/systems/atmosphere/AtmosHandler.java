package com.site21.bittermelon.common.systems.atmosphere;

import com.site21.bittermelon.common.systems.atmosphere.data.AtmosBlockData;
import com.site21.bittermelon.common.systems.atmosphere.data.AtmosLevelData;
import com.site21.bittermelon.common.systems.atmosphere.networking.AtmosChunkUpdate;
import com.site21.bittermelon.common.systems.substance.SubstanceStack;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.ATMOSPHERE;

public final class AtmosHandler {
    private static final Direction[] DIRECTIONS = Direction.values();

    /** Private constructor to prevent instantiation. */
    public static @Nullable AtmosInstance getAtmosInstanceAt(@NotNull Level level, BlockPos pos) {
        LevelChunk chunk = level.getChunkAt(pos);
        AtmosBlockData data = chunk.getData(ATMOSPHERE.get());

        return data.getAtmosInstance(level, pos);
    }

    /**
     * Updates the atmosphere at the given position and its adjacent blocks.
     * @param level The level in which to update the atmosphere.
     * @param pos The position at which to update the atmosphere.
     */
    public static void updateAtmosphereAt(@NotNull Level level, @NotNull BlockPos pos) {
        if (level.isClientSide) return;

        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());
        Set<AtmosInstance> updatedInstances = new HashSet<>();

        for (Direction direction : DIRECTIONS) {
            mutableBlockPos.move(direction);

            if (getAtmosInstanceAt(level, mutableBlockPos) instanceof AtmosInstance atmosInstance
                    && !updatedInstances.contains(atmosInstance)) {
                updateAtmosphere(level, mutableBlockPos.immutable(), atmosInstance);
                updatedInstances.add(atmosInstance);
            }
        }

        if (level instanceof ServerLevel serverLevel) {
            ChunkPos chunkPos = new ChunkPos(pos);
            PacketDistributor.sendToPlayersTrackingChunk(serverLevel, chunkPos, new AtmosChunkUpdate(chunkPos, level.getChunk(chunkPos.x, chunkPos.z).getData(ATMOSPHERE)));
        }
    }

    /**
     * Removes the given atmosphere instance from the level.
     * @param level The level from which to remove the atmosphere instance.
     * @param instance The atmosphere instance to remove.
     */
    public static void removeAtmosphere(@NotNull Level level, @NotNull AtmosInstance instance) {
        AtmosLevelData.get(level).removeAtmosInstance(instance.getUUID());
    }

    /**
     * Releases the given gas at the specified position in the level.
     * If an atmosphere instance already exists at that position, it updates the gas in that instance.
     * Otherwise, it creates a new atmosphere instance with the given gas.
     * @param level The level in which to release the gas.
     * @param pos The position at which to release the gas.
     * @param gas The gas to release.
     */
    public static void releaseGas(@NotNull Level level, BlockPos pos, @NotNull SubstanceStack gas) {
        if (level.isClientSide) return;

        AtmosInstance instance = getAtmosInstanceAt(level, pos);
        if (instance != null) {
            gas.setAmount(gas.getAmount());
            instance.updateGas(gas, level);
        } else {
            addAtmosphere(level, pos, gas.getTemperature(), List.of(gas));
        }

        if (level instanceof ServerLevel serverLevel) {
            ChunkPos chunkPos = new ChunkPos(pos);
            PacketDistributor.sendToPlayersTrackingChunk(serverLevel, chunkPos, new AtmosChunkUpdate(chunkPos, level.getChunk(chunkPos.x, chunkPos.z).getData(ATMOSPHERE)));
        }
    }

    /**
     * Releases multiple gases at the specified position in the level.
     * @param level The level in which to release the gases.
     * @param pos The position at which to release the gases.
     * @param gases The list of gases to release.
     */
    public static void releaseGas(Level level, BlockPos pos, @NotNull List<SubstanceStack> gases) {
        for (SubstanceStack stack : gases) {
            releaseGas(level, pos, stack);
        }
    }

    /**
     * Adds a new atmosphere instance at the specified position in the level.
     * @param level The level in which to add the atmosphere instance.
     * @param startPos The starting position for the atmosphere instance.
     * @param temperature The temperature of the atmosphere instance.
     * @param gasses The list of gases in the atmosphere instance.
     */
    public static void addAtmosphere(@NotNull Level level, BlockPos startPos, float temperature, List<SubstanceStack> gasses) {
        if (level.isClientSide) return;

        AtmosInstance newInstance = new AtmosInstance(temperature, gasses);
        updateAtmosphere(level, startPos, newInstance);
        AtmosLevelData.get(level).addAtmosInstance(newInstance);
    }

    /**
     * Updates the atmosphere starting from the specified position using a flood fill algorithm.
     * @param level The level in which to update the atmosphere.
     * @param startPos The starting position for the flood fill.
     * @param atmosInstance The atmosphere instance to update.
     */
    public static void updateAtmosphere(@NotNull Level level, BlockPos startPos, AtmosInstance atmosInstance) {
        Set<BlockPos> floodFill = FloodFill.run(level, startPos, 1000);
        updateAtmosphere(level, atmosInstance, floodFill);
    }

    /**
     * Updates the atmosphere instance with the given set of blocks.
     * Merges with existing instances if necessary and redistributes gases.
     * @param level The level in which to update the atmosphere.
     * @param atmosInstance The atmosphere instance to update.
     * @param floodFill The set of blocks to include in the atmosphere instance.
     */
    public static void updateAtmosphere(@NotNull Level level, @NotNull AtmosInstance atmosInstance, @NotNull Set<BlockPos> floodFill) {
        if (level.isClientSide) return;
        Set<UUID> mergedInstances = new HashSet<>();

        // Convert floodFill BlockPos to LongSet for easier comparison
        LongSet newBlocks = new LongOpenHashSet();
        for (BlockPos pos : floodFill) {
            newBlocks.add(pos.asLong());
        }

        // Identify disconnected blocks that are no longer part of the flood fill
        LongSet disconnectedBlocks = new LongOpenHashSet(atmosInstance.getBlocks());
        disconnectedBlocks.removeAll(newBlocks);

        // If there are disconnected blocks, create a new AtmosInstance for them
        if (!disconnectedBlocks.isEmpty()) {
            AtmosInstance newInstance = new AtmosInstance(
                    atmosInstance.getTemperature(),
                    // Distribute gases proportionally based on the number of blocks
                    atmosInstance.getGases().stream()
                            .map(gas -> {
                                SubstanceStack copy = gas.copy();
                                copy.setAmount(gas.getAmount() * disconnectedBlocks.size() / atmosInstance.getBlocks().size());
                                return copy;
                            })
                            .toList()
            );

            // Reduce gases in the original instance proportionally
            int remainingRatio = newBlocks.size() / atmosInstance.getBlocks().size();
            for (SubstanceStack gas : atmosInstance.getGases()) {
                gas.setAmount(gas.getAmount() * remainingRatio);
            }

            // Assign disconnected blocks to the new instance
            for (long packedPos : disconnectedBlocks) {
                BlockPos pos = BlockPos.of(packedPos);
                LevelChunk chunk = level.getChunkAt(pos);
                AtmosBlockData data = chunk.getData(ATMOSPHERE.get());
                data.addAtmosBlock(pos, newInstance.getUUID());
                newInstance.addBlock(packedPos, level);
            }

            AtmosLevelData.get(level).addAtmosInstance(newInstance);
        }

        // Clear current blocks and reassign based on flood fill
        atmosInstance.getBlocks().clear();

        for (BlockPos pos : floodFill) {
            LevelChunk chunk = level.getChunkAt(pos);
            AtmosBlockData data = chunk.getData(ATMOSPHERE.get());

            // Merge with existing instance if present
            AtmosInstance existing = getAtmosInstanceAt(level, pos);
            if (existing != null && !mergedInstances.contains(existing.getUUID()) && existing != atmosInstance) {
                atmosInstance.merge(existing, level);
                mergedInstances.add(existing.getUUID());
                removeAtmosphere(level, existing);
            }
            data.addAtmosBlock(pos, atmosInstance.getUUID());
            atmosInstance.addBlock(pos.asLong(), level);
        }

        // Sync the updated instance
        AtmosLevelData.get(level).syncInstance(atmosInstance);
    }
}
