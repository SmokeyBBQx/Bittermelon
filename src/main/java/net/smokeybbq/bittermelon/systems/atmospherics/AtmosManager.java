package net.smokeybbq.bittermelon.systems.atmospherics;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.smokeybbq.bittermelon.systems.substances.Substance;
import net.smokeybbq.bittermelon.util.ModLogger;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AtmosManager extends SavedData {
    private static AtmosManager instance;
    private final Map<ChunkPos, AtmosChunk> chunks = new ConcurrentHashMap<>();
    private final PriorityBlockingQueue<BlockUpdate> updateQueue = new PriorityBlockingQueue<>();
    private final ExecutorService executorService;
    private final Level level;
    private static final int UPDATE_INTERVAL = 20;
    private static final int FLOOD_LIMIT = 100;
    private final AtomicInteger tickCounter = new AtomicInteger(0);

    public AtmosManager(Level level) {
        this.level = level;
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        instance = this;
    }

    public static synchronized AtmosManager getInstance(Level level) {
        if (instance == null) {
            instance = new AtmosManager(level);
        }
        return instance;
    }

    public static AtmosManager getInstance() {
        return instance;
    }

    public static void init(Level level) {
        if (instance == null) {
            instance = new AtmosManager(level);
        }
    }

    public void tick() {
        if (tickCounter.incrementAndGet() >= UPDATE_INTERVAL) {
            tickCounter.set(0);
            update();
        }
    }

    public void update() {
        long currentTime = System.currentTimeMillis();
        int availableThreads = Runtime.getRuntime().availableProcessors();

        // Process updates in parallel
        for (int i = 0; i < availableThreads; i++) {
            executorService.submit(() -> processUpdates(currentTime));
        }
    }

    private void processUpdates(long currentTime) {
        BlockUpdate update;
        while ((update = updateQueue.poll()) != null && update.updateTime <= currentTime) {
            updateBlock(update.pos);
        }
    }

    private void updateBlock(BlockPos pos) {
        ModLogger.debug("Updating block!");

        AtmosCell cell = getCellFromBlockPos(pos);
        if (cell == null) {
            ModLogger.error("Cell is null!");
        }
        if (cell != null && isActive(pos)) {
            ModLogger.debug("Cell is not null and it is " + isActive(pos));

            updateAtmospherics(cell, pos);
            scheduleNextUpdate(pos);
        }
    }

    private void updateAtmospherics(AtmosCell cell, BlockPos pos) {
        ModLogger.debug("Updating atmos cell!");

        equalizeGasses(floodFill(pos, FLOOD_LIMIT));
    }

    private void equalizeTemperature(List<AtmosCell> cells, AtmosCell mainCell) {
        double totalTemperature = 0;
        double totalAmount = 0;

        for (AtmosCell cell : cells) {
            if (cell.getTemperature() != mainCell.getTemperature()) {
                totalTemperature += cell.getTemperature() * cell.getTotalAmount();
                totalAmount += cell.getTotalAmount();
            }
        }

        float newTemperature = totalAmount > 0 ? (float) (totalTemperature / totalAmount) : 0;
        for (AtmosCell cell : cells) {
            cell.setTemperature(newTemperature);
        }
    }

    private List<BlockPos> floodFill(BlockPos startPos, int floodLimit) {
        ModLogger.debug("Running floodFill!!");

        List<BlockPos> positions = new ArrayList<>();
        Queue<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        if (isValidNeighbor(startPos)) {
            queue.offer(startPos);
            visited.add(startPos);
            positions.add(startPos);
        }

        while (!queue.isEmpty() && positions.size() < floodLimit) {
            BlockPos currentPos = queue.poll();

            for (Direction dir : Direction.values()) {
                mutablePos.set(currentPos).move(dir);
                BlockPos neighborPos = mutablePos.immutable();

                if (!visited.contains(neighborPos) && isValidNeighbor(neighborPos)) {
                    visited.add(neighborPos);
                    positions.add(neighborPos);
                    queue.offer(neighborPos);

                    if (positions.size() >= floodLimit) break;
                }
            }
        }
        return positions;
    }

    private List<BlockPos> parallelFloodFill(BlockPos startPos, int floodLimit) {
        ModLogger.debug("Running flood fill!");

        List<BlockPos> positions = Collections.synchronizedList(new ArrayList<>(floodLimit));
        Set<BlockPos> visited = Collections.synchronizedSet(new HashSet<>(floodLimit));
        Queue<BlockPos> queue = new ConcurrentLinkedQueue<>();

        queue.offer(startPos);
        visited.add(startPos);

        List<Future<?>> futures = new ArrayList<>();

        while (!queue.isEmpty() && positions.size() < floodLimit) {
            int batchSize = Math.min(queue.size(), Runtime.getRuntime().availableProcessors());
            for (int i = 0; i < batchSize; i++) {
                BlockPos currentPos = queue.poll();
                if (currentPos == null) break;

                futures.add(executorService.submit(() -> processPosition(currentPos, queue, visited, positions, floodLimit)));
            }

            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    ModLogger.error("Error in parallel flood fill: " + e.getMessage());
                }
            }
            futures.clear();
        }

        return new ArrayList<>(positions);
    }

    private void processPosition(BlockPos currentPos, Queue<BlockPos> queue, Set<BlockPos> visited, List<BlockPos> positions, int floodLimit) {
        if (isValidNeighbor(currentPos) && getCellFromBlockPos(currentPos) != null) {
            positions.add(currentPos);

            for (Direction dir : Direction.values()) {
                BlockPos neighborPos = currentPos.relative(dir);
                if (visited.add(neighborPos) && positions.size() < floodLimit) {
                    queue.offer(neighborPos);
                }
            }
        }
    }

    private void equalizeGassesParallel(List<BlockPos> positions) {
        ModLogger.debug("Running equalization with: " + positions.size());

        if (positions.isEmpty()) {
            ModLogger.debug("Equalization gasses empty!");
            return;
        }

        Map<Substance, Float> totalGasses = Collections.synchronizedMap(new HashMap<>());
        List<AtmosCell> cellsToEqualize = Collections.synchronizedList(new ArrayList<>());

        positions.parallelStream().forEach(pos -> {
            AtmosCell cell = getCellFromBlockPos(pos);
            cellsToEqualize.add(cell);
            cell.getGasses().forEach((substance, amount) -> {
                synchronized (totalGasses) {
                    mergeSubstances(totalGasses, substance, amount);
                }
            });
        });


        if (!cellsToEqualize.isEmpty()) {
            Map<Substance, Float> equalizedGasses = new ConcurrentHashMap<>();
            int cellCount = cellsToEqualize.size();

            totalGasses.forEach((substance, totalAmount) ->
                    equalizedGasses.put(substance, (totalAmount / cellCount)));

            cellsToEqualize.parallelStream().forEach(cell ->
                    cell.setGasses(new HashMap<>(equalizedGasses)));
        }
    }

    private void equalizeGasses(List<BlockPos> positions) {
        if (positions.isEmpty()) {
            return;
        }

        ModLogger.debug("Running equalization with: " + positions.size());
        Map<Substance, Float> totalGasses = new HashMap<>();
        List<AtmosCell> cellsToEqualize = new ArrayList<>();

        for (BlockPos pos : positions) {
            AtmosCell cell = getCellFromBlockPos(pos);
            if (cell == null) {
                ModLogger.error("Equalization cell is null for position: " + pos);
                continue;
            }
            if (!isActive(pos)) {
                getChunkFromCell(cell).setActive(pos.getX(), pos.getY(), pos.getZ(), true);
            }

            cellsToEqualize.add(cell);
            for (Map.Entry<Substance, Float> entry : cell.getGasses().entrySet()) {
                mergeSubstances(totalGasses, entry.getKey(), entry.getValue());
            }
        }

        if (!cellsToEqualize.isEmpty()) {
            Map<Substance, Float> equalizedGasses = new HashMap<>();
            for (Map.Entry<Substance, Float> entry : totalGasses.entrySet()) {
                equalizedGasses.put(entry.getKey(), entry.getValue() / cellsToEqualize.size());
            }

            for (AtmosCell cellToEqualize : cellsToEqualize) {
                ModLogger.debug("Equalizing with " + cellsToEqualize.size());
                cellToEqualize.setGasses(new HashMap<>(equalizedGasses));
            }
        }
    }

    private static void mergeSubstances(Map<Substance, Float> substances, Substance newSubstance, Float amount) {
        boolean alreadyExists = false;
        for (Substance substance : substances.keySet()) {
            if (substance.compare(newSubstance)) {
                substances.merge(substance, amount, Float::sum);
                alreadyExists = true;
            }
        }
        if (!alreadyExists) {
            substances.put(newSubstance, amount);
        }
    }

    public AtmosCell getCellFromBlockPos(BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);
        AtmosChunk chunk = chunks.get(chunkPos);
        if (chunk != null) {
            return chunk.getCell(pos.getX() & 15, pos.getY(), pos.getZ() & 15);
        }
        ModLogger.error("Chunk is null!");
        return null;
    }

    public AtmosChunk getChunkFromCell(AtmosCell cell) {
        for (Map.Entry<ChunkPos, AtmosChunk> entry : chunks.entrySet()) {
            AtmosChunk chunk = entry.getValue();
            if (chunk.containsCell(cell)) {
                return chunk;
            }
        }
        return null;
    }


    private List<AtmosCell> getNeighbors(BlockPos pos) {
        List<AtmosCell> neighbors = new ArrayList<>(6);
        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = pos.relative(dir);
            if (isValidNeighbor(neighborPos)) {
                AtmosCell neighborCell = getCellFromBlockPos(neighborPos);
                if (neighborCell != null) {
                    AtmosChunk neighborChunk = getChunkFromCell(neighborCell);
                    BlockPos neighborCellPos = neighborChunk.getCellPosition(neighborCell);
                    neighbors.add(neighborCell);
                }
            }
        }
        return neighbors;
    }

    private boolean isValidNeighbor(BlockPos pos) {
        if (!level.isLoaded(pos)) {
            return false;
        }

        if (getCellFromBlockPos(pos) == null) {
            return false;
        }

        BlockState state = level.getBlockState(pos);
        return state.canBeReplaced() || state.getBlock() instanceof SimpleWaterloggedBlock;
    }

    private boolean isActive(BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);
        AtmosChunk chunk = chunks.get(chunkPos);
        return chunk != null && chunk.isActive(pos.getX() & 15, pos.getY(), pos.getZ() & 15);
    }

    private void scheduleNextUpdate(BlockPos pos) {


        updateQueue.offer(new BlockUpdate(pos, System.currentTimeMillis() + 1000, 1));
    }

    private void getPriorityRate() {

    }

    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        instance = null;
    }

    public void onChunkLoad(ChunkPos pos) {
        chunks.computeIfAbsent(pos, k -> new AtmosChunk());
        setDirty();
    }

    public void onChunkUnload(ChunkPos pos) {
        chunks.remove(pos);
        setDirty();
    }


    public void onBlockChange(BlockPos pos, boolean isAir) {
        ChunkPos chunkPos = new ChunkPos(pos);
        AtmosChunk chunk = chunks.get(chunkPos);
        if (chunk != null) {
            try {
                chunk.setActive(pos.getX() & 15, pos.getY(), pos.getZ() & 15, isAir);
                updateBlock(pos);
                ModLogger.debug("Atmos cell placed!");

                setDirty();
            } catch (Exception e) {
                ModLogger.error("Error in onBlockChange: " + e.getMessage());
            }
        } else {
            ModLogger.warn("Chunk not found for position: " + pos);
        }
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag) {
        CompoundTag chunksTag = new CompoundTag();
        chunks.forEach((pos, chunk) -> {
            chunksTag.put(pos.toString(), chunk.save(new CompoundTag()));
        });
        tag.put("chunks", chunksTag);
        return tag;
    }

    public Set<BlockPos> getActiveCells() {
        Set<BlockPos> activeCells = new HashSet<>();
        for (Map.Entry<ChunkPos, AtmosChunk> entry : chunks.entrySet()) {
            ChunkPos chunkPos = entry.getKey();
            AtmosChunk chunk = entry.getValue();
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 256; y++) {
                    for (int z = 0; z < 16; z++) {
                        if (chunk.isActive(x, y, z)) {
                            activeCells.add(new BlockPos(chunkPos.getMinBlockX() + x, y, chunkPos.getMinBlockZ() + z));
                        }
                    }
                }
            }
        }
        return activeCells;
    }
//
//    public static AtmosManager load(CompoundTag tag) {
//        AtmosManager manager = new AtmosManager();
//        CompoundTag chunksTag = tag.getCompound("chunks");
//        chunksTag.getAllKeys().forEach(key -> {
//            ChunkPos pos = ChunkPos.fromString(key).orElseThrow();
//            AtmosChunk chunk = AtmosChunk.load(chunksTag.getCompound(key));
//            manager.chunks.put(pos, chunk);
//        });
//        return manager;
//    }
}
