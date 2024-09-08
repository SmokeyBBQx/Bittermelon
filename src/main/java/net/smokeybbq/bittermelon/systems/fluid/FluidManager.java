package net.smokeybbq.bittermelon.systems.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.smokeybbq.bittermelon.blocks.blockentities.PuddleBlockEntity;
import net.smokeybbq.bittermelon.systems.substances.Substance;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FluidManager {
    private static final int EQUALIZE_INTERVAL = 20; // Ticks between equalization
    private static final FluidManager INSTANCE = new FluidManager();

    private final Map<ServerLevel, Map<ChunkPos, Set<BlockPos>>> puddlePositions = new ConcurrentHashMap<>();
    private int tickCounter = 0;

    private FluidManager() {}

    public static FluidManager getInstance() {
        return INSTANCE;
    }

    public void tick(ServerLevel level) {
        tickCounter++;
        if (tickCounter >= EQUALIZE_INTERVAL) {
            equalizePuddles(level);
            tickCounter = 0;
        }
    }

    public void registerPuddle(ServerLevel level, BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);
        puddlePositions.computeIfAbsent(level, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(chunkPos, k -> ConcurrentHashMap.newKeySet())
                .add(pos);
    }

    public void unregisterPuddle(ServerLevel level, BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);
        Map<ChunkPos, Set<BlockPos>> levelPuddles = puddlePositions.get(level);
        if (levelPuddles != null) {
            Set<BlockPos> positions = levelPuddles.get(chunkPos);
            if (positions != null) {
                positions.remove(pos);
                if (positions.isEmpty()) {
                    levelPuddles.remove(chunkPos);
                }
            }
            if (levelPuddles.isEmpty()) {
                puddlePositions.remove(level);
            }
        }
    }

    private void equalizePuddles(ServerLevel level) {
        Map<ChunkPos, Set<BlockPos>> levelPuddles = puddlePositions.get(level);
        if (levelPuddles == null) return;

        for (Map.Entry<ChunkPos, Set<BlockPos>> entry : levelPuddles.entrySet()) {
            LevelChunk chunk = level.getChunk(entry.getKey().x, entry.getKey().z);
            Set<BlockPos> positions = entry.getValue();

            Map<BlockPos, PuddleBlockEntity> puddleEntities = new HashMap<>();
            for (BlockPos pos : positions) {
                if (chunk.getBlockEntity(pos) instanceof PuddleBlockEntity puddle) {
                    puddleEntities.put(pos, puddle);
                }
            }

            equalizeGroup(puddleEntities);
        }
    }

    private void equalizeGroup(Map<BlockPos, PuddleBlockEntity> puddleEntities) {
        if (puddleEntities.size() <= 1) return;

        Map<Substance, Integer> totalSubstances = new HashMap<>();
        for (PuddleBlockEntity puddle : puddleEntities.values()) {
            for (Map.Entry<Substance, Integer> entry : puddle.getSubstances().entrySet()) {
                totalSubstances.merge(entry.getKey(), entry.getValue(), Integer::sum);
            }
        }

        Map<Substance, Integer> equalizedSubstances = new HashMap<>();
        for (Map.Entry<Substance, Integer> entry : totalSubstances.entrySet()) {
            int equalizedAmount = entry.getValue() / puddleEntities.size();
            if (equalizedAmount > 0) {
                equalizedSubstances.put(entry.getKey(), equalizedAmount);
            }
        }

        for (PuddleBlockEntity puddle : puddleEntities.values()) {
            puddle.setSubstances(new HashMap<>(equalizedSubstances));
        }
    }
}
