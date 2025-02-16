package com.site21.bittermelon.content.atmosphere;

import com.site21.bittermelon.content.atmosphere.data.AtmosBlockData;
import com.site21.bittermelon.content.atmosphere.data.AtmosLevelData;
import com.site21.bittermelon.content.atmosphere.networking.AtmosChunkUpdate;
import com.site21.bittermelon.content.substance.SubstanceStack;
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

import java.util.*;

import static com.site21.bittermelon.init.BitterAttachmentTypes.ATMOSPHERE;

public final class AtmosHandler {
    private static final Direction[] DIRECTIONS = Direction.values();

    public static @Nullable AtmosInstance getAtmosInstanceAt(@NotNull Level level, BlockPos pos) {
        LevelChunk chunk = level.getChunkAt(pos);
        AtmosBlockData data = chunk.getData(ATMOSPHERE.get());

        return data.getAtmosInstance(level, pos);
    }

    public static void updateAtmosphereAt(@NotNull Level level, @NotNull BlockPos pos) {
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

    public static void removeAtmosphere(Level level, @NotNull AtmosInstance instance) {
        AtmosLevelData.get(level).removeAtmosInstance(instance.getUuid());
    }

    public static void releaseGas(Level level, BlockPos pos, @NotNull SubstanceStack gas) {
        AtmosInstance instance = getAtmosInstanceAt(level, pos);
        if (instance != null) {
            Set<BlockPos> floodFill = FloodFill.run(level, pos, 1000);
            gas.setAmount(gas.getAmount() / floodFill.size());

            System.out.println(gas.getAmount());

            instance.updateGas(gas);
            updateAtmosphere(level, instance, floodFill);
        } else {
            addAtmosphere(level, pos, gas.getTemperature(), List.of(gas));
        }
    }

    public static void releaseGas(Level level, BlockPos pos, @NotNull List<SubstanceStack> gases) {
        for (SubstanceStack stack : gases) {
            releaseGas(level, pos, stack);
        }
    }

    public static void addAtmosphere(@NotNull Level level, BlockPos startPos, float temperature, List<SubstanceStack> gasses) {
        AtmosInstance newInstance = new AtmosInstance(temperature, gasses);
        updateAtmosphere(level, startPos, newInstance);
        AtmosLevelData.get(level).addAtmosInstance(newInstance);
    }

    public static void updateAtmosphere(@NotNull Level level, BlockPos startPos, AtmosInstance atmosInstance) {
        Set<BlockPos> floodFill = FloodFill.run(level, startPos, 1000);
        updateAtmosphere(level, atmosInstance, floodFill);
    }

    public static void updateAtmosphere(@NotNull Level level, @NotNull AtmosInstance atmosInstance, @NotNull Set<BlockPos> floodFill) {
        Set<UUID> mergedInstances = new HashSet<>();

        LongSet newBlocks = new LongOpenHashSet();
        for (BlockPos pos : floodFill) {
            newBlocks.add(pos.asLong());
        }

        LongSet disconnectedBlocks = new LongOpenHashSet(atmosInstance.getBlocks());
        disconnectedBlocks.removeAll(newBlocks);

        if (!disconnectedBlocks.isEmpty()) {
            AtmosInstance newInstance = new AtmosInstance(
                    atmosInstance.getTemperature(),
                    atmosInstance.getGases().stream()
                            .map(gas -> {
                                SubstanceStack copy = gas.copy();
                                copy.setAmount(gas.getAmount() * disconnectedBlocks.size() / atmosInstance.getBlocks().size());
                                return copy;
                            })
                            .toList()
            );

            // Update the original instance's gas amounts
            float remainingRatio = (float) newBlocks.size() / atmosInstance.getBlocks().size();
            for (SubstanceStack gas : atmosInstance.getGases()) {
                gas.setAmount(gas.getAmount() * remainingRatio);
            }

            // Process disconnected blocks
            for (long packedPos : disconnectedBlocks) {
                BlockPos pos = BlockPos.of(packedPos);
                LevelChunk chunk = level.getChunkAt(pos);
                AtmosBlockData data = chunk.getData(ATMOSPHERE.get());
                data.addAtmosBlock(pos, newInstance.getUuid());
                newInstance.addBlock(packedPos);
            }

            AtmosLevelData.get(level).addAtmosInstance(newInstance);
        }

        atmosInstance.getBlocks().clear();

        for (BlockPos pos : floodFill) {
            LevelChunk chunk = level.getChunkAt(pos);
            AtmosBlockData data = chunk.getData(ATMOSPHERE.get());

            AtmosInstance existing = getAtmosInstanceAt(level, pos);
            if (existing != null && !mergedInstances.contains(existing.getUuid()) && existing != atmosInstance) {
                atmosInstance.merge(existing);
                mergedInstances.add(existing.getUuid());
                removeAtmosphere(level, existing);
            }
            data.addAtmosBlock(pos, atmosInstance.getUuid());
            atmosInstance.addBlock(pos.asLong());
        }
    }
}
