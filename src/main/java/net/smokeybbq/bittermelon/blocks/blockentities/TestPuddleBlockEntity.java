package net.smokeybbq.bittermelon.blocks.blockentities;

import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.smokeybbq.bittermelon.blocks.PuddleBlock;
import net.smokeybbq.bittermelon.init.BlockEntityInit;
import net.smokeybbq.bittermelon.systems.substances.Substance;
import net.smokeybbq.bittermelon.util.ColorUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static net.smokeybbq.bittermelon.init.BlockInit.PUDDLE;

public class TestPuddleBlockEntity extends BlockEntity implements Tickable {
    private final int SPREAD_THRESHOLD = 20;
    private final int MAX_CAPACITY = 40;
    private static final int SPREAD_DELAY = 10;
    private static final int FLOW_DELAY = 20;
    private static final int EQUALIZE_DELAY = 10;

    private final Map<Substance, Integer> substances = new HashMap<>();
    private int cachedColor = -1;
    private int spreadTimer = 0;
    private int amountToSpread = 0;
    private int flowTimer = 0;
    private int equalizeTimer = 0;
    private int flow;

    public TestPuddleBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.PUDDLE_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public void updateSubstance(Substance substance, int amount) {
        int currentAmount = substances.getOrDefault(substance, 0);
        int newAmount = Math.max(currentAmount + amount, 0);

        if (newAmount > 0) {
            substances.put(substance, newAmount);
        }

        updatePuddleLevel();
        setChanged();
    }

    public Map<Substance, Integer> transferSubstances(int amount) {
        if (substances.isEmpty()) {
            return new HashMap<>();
        }

        int totalAmount = getTotalAmount();
        Map<Substance, Integer> transferredSubstances = new HashMap<>();
        List<Substance> substancesToRemove = new ArrayList<>();

        for (Map.Entry<Substance, Integer> entry : substances.entrySet()) {
            Substance substance = entry.getKey();
            int availableAmount = entry.getValue();

            float proportion = (float) availableAmount / totalAmount;
            int transferAmount = (int) Math.ceil(amount * proportion);
            int actualAmount = Math.min(availableAmount, transferAmount);

            if (actualAmount > 0) {
                updateSubstance(substance, -actualAmount);
                transferredSubstances.put(substance, actualAmount);

                if (availableAmount <= actualAmount) {
                    substancesToRemove.add(substance);
                }
            }
        }

        substancesToRemove.forEach(substances::remove);

        if (substances.isEmpty()) {
            removePuddleBlock();
        }

        setChanged();

        return transferredSubstances;
    }

    private void removePuddleBlock() {
        if (level != null && !level.isClientSide) {
            level.removeBlock(getBlockPos(), false);
        }
    }

//    public void onPlaced() {
//        boolean canSpread = false;
//        for (Direction dir : Direction.Plane.HORIZONTAL) {
//            BlockPos downhillPos = worldPosition.relative(dir).below();
//            if (canSpreadTo(downhillPos)) {
//                canSpread = true;
//            }
//        }
//        if (canSpread) {
//            amountToSpread = getTotalAmount();
//        }
//    }

    public Map<Substance, Integer> getSubstances() {
        return substances;
    }

    public void spread(int amount) {
        if (level == null) return;

        List<BlockPos> validNeighbors = getValidNeighbors();
        if (validNeighbors.isEmpty()) return;

        int spreadDirections = Math.min(validNeighbors.size(), 4);
        int spillAmount = amount / spreadDirections;

        for (BlockPos neighbor : validNeighbors) {
            spill(neighbor, spillAmount);
        }
    }

    private List<BlockPos> getValidNeighbors() {
        List<BlockPos> validNeighbors = new ArrayList<>();
        if (level == null) return validNeighbors;

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        BlockState stateBelow = level.getBlockState(worldPosition.below());

//        // Rule 1. If block below is a liquid, pathfind through the liquid to find an open space.
//        if (stateBelow.getBlock() instanceof PuddleBlock) {
//
//        }
//
        // Rule 2. Flow below if no block is beneath
        if (stateBelow.canBeReplaced()) {
            validNeighbors.add(worldPosition.below());
            return validNeighbors;
        }

        Queue<BlockPos> queue = new LinkedList<>();
        Set<BlockPos> visited = new HashSet<>();

        for (Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(level.random)) {
            BlockPos pos = worldPosition.relative(dir);
            if (level.getBlockState(pos).canBeReplaced()) {
                queue.offer(pos.below());
            }
        }

        for (Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(level.random)) {
            queue.offer(worldPosition.relative(dir));
        }

        while (!queue.isEmpty() && validNeighbors.size() < 4) {
            BlockPos pos = queue.poll();

            // If it's not been visited and the level is loaded, continue the search
            if (!visited.add(pos) || !level.isLoaded(pos)) continue;

            BlockState state = level.getBlockState(pos);
            if (canSpreadTo(pos)) {
                if (level.getBlockEntity(pos) instanceof PuddleBlockEntity entity) {
                    if (entity.getTotalAmount() <= SPREAD_THRESHOLD) {
                        validNeighbors.add(pos);
                    }
                } else {
                    validNeighbors.add(pos);
                }
            }

            if (state.canBeReplaced() || state.getBlock() instanceof PuddleBlock) {
                for (Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(level.random)) {
                    mutablePos.set(pos).move(dir);

                    // If it's not already been visited, it will add it.
                    if (!visited.contains(mutablePos)) {
                        queue.offer(mutablePos.immutable());
                    }
                }
            }

            if (!validNeighbors.isEmpty()) {
                for (BlockPos neighbor : validNeighbors) {
                    if (worldPosition.getY() - neighbor.getY() > 0) {
                        return validNeighbors;
                    }
                }
            }

            if (validNeighbors.isEmpty()) {
                for (Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(level.random)) {
                    mutablePos.set(pos).move(dir);
                    BlockEntity entity = level.getBlockEntity(mutablePos);
                    if (entity instanceof PuddleBlockEntity blockEntity) {
                        if (blockEntity.getTotalAmount() != getTotalAmount()) {
                            validNeighbors.add(mutablePos.immutable());
                        }
                    }
                }
            }
        }

        if (validNeighbors.isEmpty() && !(level.getBlockState(worldPosition).getBlock() instanceof PuddleBlock)) {
            validNeighbors.add(worldPosition.above());
        }

        Collections.shuffle(validNeighbors);
        return validNeighbors;
    }

    private static Map<Substance, Integer> mergeSubstances(Map<Substance, Integer> substances, Substance newSubstance, Integer amount) {
        boolean alreadyExists = false;
        for (Substance substance : substances.keySet()) {
            if (substance.compare(newSubstance)) {
                substances.merge(substance, amount, Integer::sum);
                alreadyExists = true;
            }
        }
        if (!alreadyExists) {
            substances.put(newSubstance, amount);
        }
        return new HashMap<>(substances);
    }

    private void equalizeHorizontally() {
        List<PuddleBlockEntity> flowCandidates = new ArrayList<>();

        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = worldPosition.relative(dir);
            if (level != null && level.getBlockEntity(neighborPos) instanceof PuddleBlockEntity entity) {
                if (entity.getTotalAmount() != getTotalAmount()) {
                    flowCandidates.add(entity);
                }
            }
        }

        if (!flowCandidates.isEmpty()) {
            Map<Substance, Integer> totalSubstances = new HashMap<>();
            for (PuddleBlockEntity puddle : flowCandidates) {
                for (Map.Entry<Substance, Integer> entry : puddle.getSubstances().entrySet()) {
                    totalSubstances = mergeSubstances(totalSubstances, entry.getKey(), entry.getValue());
                }
            }

            Map<Substance, Integer> equalizedSubstances = new HashMap<>();
            for (Map.Entry<Substance, Integer> entry : totalSubstances.entrySet()) {
                int equalizedAmount = Math.round((float) entry.getValue() / flowCandidates.size());
                if (equalizedAmount > 0) {
                    equalizedSubstances.put(entry.getKey(), equalizedAmount);
                }
            }

            for (PuddleBlockEntity puddle : flowCandidates) {
                puddle.setSubstances(new HashMap<>(equalizedSubstances));
            }
        }
    }

    private boolean canFlowTo(BlockPos pos) {
        return level != null && level.getBlockEntity(pos) instanceof PuddleBlockEntity;
    }

    private boolean canSpreadTo(BlockPos pos) {
        if (level == null) return false;
        BlockState state = level.getBlockState(pos);

        if (level.getBlockState(pos.below()).isAir()) {
            return true;
        }

        if (level.getBlockState(pos.below()).canBeReplaced()) {
            return false;
        }

        if (level.getBlockEntity(pos) instanceof PuddleBlockEntity blockEntity) {
            if (blockEntity.getTotalAmount() < MAX_CAPACITY) {
                return true;
            }
        }

        if (state.canBeReplaced()) {
            return true;
        }

        return false;
    }

    private void spill(BlockPos blockPos, int amount) {
        if (level == null) return;

        BlockState targetState = level.getBlockState(blockPos);
        PuddleBlockEntity targetPuddle;

        if (level.getBlockEntity(blockPos) instanceof PuddleBlockEntity entity) {
            targetPuddle = entity;
        } else if (targetState.canBeReplaced()) {
            // Don't place the block yet, wait until we know we have substance to transfer
            if (level.getBlockEntity(blockPos.below()) instanceof PuddleBlockEntity entity) {
                targetPuddle = entity;
            } else {
                targetPuddle = null;
            }
        } else {
            return;
        }

        Map<Substance, Integer> substancesToTransfer = new HashMap<>();
        int totalTransferred = 0;

        for (Map.Entry<Substance, Integer> entry : substances.entrySet()) {
            Substance substance = entry.getKey();
            int totalSubstanceAmount = entry.getValue();
            float proportion = (float) totalSubstanceAmount / getTotalAmount();
            int transferAmount = Math.min((int) (amount * proportion), totalSubstanceAmount);

            if (transferAmount > 0) {
                substancesToTransfer.put(substance, transferAmount);
                totalTransferred += transferAmount;
            }
        }

        if (totalTransferred > 0) {
            if (targetPuddle == null) {
                level.setBlock(blockPos, PUDDLE.get().defaultBlockState(), 3);
                BlockEntity newBlockEntity = level.getBlockEntity(blockPos);
                if (!(newBlockEntity instanceof PuddleBlockEntity)) {
                    return;
                }
                targetPuddle = (PuddleBlockEntity) newBlockEntity;
            }

            // Transfer the substances
            for (Map.Entry<Substance, Integer> entry : substancesToTransfer.entrySet()) {
                targetPuddle.updateSubstance(entry.getKey(), entry.getValue());
                updateSubstance(entry.getKey(), -entry.getValue());
            }

            if (worldPosition.getY() - blockPos.getY() != 0) {
                this.removePuddleBlock();
            }

            targetPuddle.setChanged();
            setChanged();
        }

    }

    @OnlyIn(Dist.CLIENT)
    public int getColor() {
        if (cachedColor == -1) {
            if (substances.isEmpty()) {
                cachedColor = 0xFFAAD5DB; // Default color if no substances
            } else {
                Map<Integer, Integer> colors = new HashMap<>();
                for (Map.Entry<Substance, Integer> entry : substances.entrySet()) {
                    colors.put(entry.getKey().getColor(), entry.getValue());
                }
                cachedColor = ColorUtil.mixColors(colors);
            }
        }
        return cachedColor;
    }

    public int getTotalAmount() {
        return substances.values().stream().reduce(0, Integer::sum);
    }

    public String getContentsDescription() {
        if (substances.isEmpty()) {
            return "Empty";
        }

        return substances.entrySet().stream()
                .map(entry -> String.format("%s: %d", entry.getKey().getName(), entry.getValue()))
                .collect(Collectors.joining(", "));
    }

    /**
     * NBT/DATA
     */

    private CompoundTag serializeData() {
        CompoundTag nbt = new CompoundTag();
        ListTag substancesList = new ListTag();
        for (Map.Entry<Substance, Integer> entry : substances.entrySet()) {
            CompoundTag substanceTag = entry.getKey().serializeNBT();
            substanceTag.putInt("Amount", entry.getValue());
            substancesList.add(substanceTag);
        }
        nbt.put("Substances", substancesList);
        return nbt;
    }

    private void deserializeData(CompoundTag nbt) {
        substances.clear();
        ListTag substancesList = nbt.getList("Substances", 10);
        for (int i = 0; i < substancesList.size(); i++) {
            CompoundTag substanceTag = substancesList.getCompound(i);
            Substance substance = Substance.fromNBT(substanceTag);
            int amount = substanceTag.getInt("Amount");
            substances.put(substance, amount);
        }
        setChanged();
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("Flow", flow);
        nbt.put("PuddleData", serializeData());
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        CompoundTag puddleData = nbt.getCompound("PuddleData");
        deserializeData(puddleData);
        flow = puddleData.getInt("Flow");
    }

    /**
     * NETWORKING
     */
    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.put("PuddleData", serializeData());
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        deserializeData(tag.getCompound("PuddleData"));
        if (level != null && level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(Objects.requireNonNull(pkt.getTag()));
    }

    private void syncToClient() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        invalidateColor();
        syncToClient();
    }

    private void invalidateColor() {
        cachedColor = -1;
        requestModelDataUpdate();
    }

    public void mixWith(Map<Substance, Integer> substances) {
        for (Map.Entry<Substance, Integer> entry : substances.entrySet()) {
            updateSubstance(entry.getKey(), entry.getValue());
        }
    }

    public void setSubstances(Map<Substance, Integer> newSubstances) {
        substances.clear();
        substances.putAll(newSubstances);
        updatePuddleLevel();
        setChanged();
    }

//    @Override
//    public void onLoad() {
//        super.onLoad();
//        if (level instanceof ServerLevel serverLevel) {
//            FluidManager.getInstance().registerPuddle(serverLevel, worldPosition);
//        }
//    }
//
//    @Override
//    public void setRemoved() {
//        super.setRemoved();
//        if (level instanceof ServerLevel serverLevel) {
//           FluidManager.getInstance().unregisterPuddle(serverLevel, worldPosition);
//        }
//    }

    public void checkForOverflow() {
        int totalAmount = getTotalAmount();
        if (totalAmount > SPREAD_THRESHOLD) {
            int excessAmount = totalAmount - SPREAD_THRESHOLD;
            amountToSpread += excessAmount;
        }
    }

    @Override
    public void tick() {
        if (substances.isEmpty()) {
            removePuddleBlock();
            return;
        }

        if (level != null && !level.isClientSide) {
            equalizeTimer++;

            if (equalizeTimer >= EQUALIZE_DELAY) {
                equalizeHorizontally();
                equalizeTimer = 0;
            }

            if (amountToSpread > 0) {
                spreadTimer++;
                if (spreadTimer >= SPREAD_DELAY) {
                    spread(amountToSpread);
                    amountToSpread = 0;
                    spreadTimer = 0;
                }
            } else {
                spreadTimer = 0;
                checkForOverflow();
            }
            checkIfOnPuddle(worldPosition, level, substances);
        }
    }

    public void checkIfOnPuddle(BlockPos pos, Level level, Map<Substance, Integer> substances) {
//        ModLogger.debug("Checking if block below is a puddle");

        if (level.getBlockEntity(pos.below()) instanceof PuddleBlockEntity blockEntity) {
            if (blockEntity.getTotalAmount() < MAX_CAPACITY) {
                transferSubstances(getTotalAmount());
                blockEntity.mixWith(substances);
            }
        } else if (level.getBlockState(pos.below()).canBeReplaced()) {
            amountToSpread = getTotalAmount();
        }
    }

    private void updatePuddleLevel() {
        if (level != null && level.getBlockState(worldPosition).getBlock() instanceof PuddleBlock) {
            int totalAmount = getTotalAmount();
            int newLevel;
            if (totalAmount < 6) {
                newLevel = 0;
            } else if (totalAmount <= 12) {
                newLevel = 1;
            } else if (totalAmount <= 22) {
                newLevel = 2;
            } else if (totalAmount <= 24) {
                newLevel = 3;
            } else if (totalAmount <= 26) {
                newLevel = 4;
            } else if (totalAmount <= 28) {
                newLevel = 5;
            } else if (totalAmount <= 30) {
                newLevel = 6;
            } else if (totalAmount <= 32) {
                newLevel = 7;
            } else if (totalAmount <= 34) {
                newLevel = 8;
            } else if (totalAmount <= 36) {
                newLevel = 9;
            } else {
                newLevel = 10;
            }

            if (level != null) {
                BlockState currentState = level.getBlockState(worldPosition);
                if (currentState.getValue(PuddleBlock.LEVEL) != newLevel) {
                    level.setBlock(worldPosition, currentState.setValue(PuddleBlock.LEVEL, newLevel), 3);
                    level.sendBlockUpdated(worldPosition, currentState, currentState.setValue(PuddleBlock.LEVEL, newLevel), 3);
                }
            }
        }
    }

    public void onPlaced() {
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos downhillPos = worldPosition.relative(dir).below();
            if (canSpreadTo(downhillPos)) {
//                if (level != null && level.getBlockState(downhillPos.above()).canBeReplaced()) {
                flow += 2;
            } else {
                flow--;
//                }
            }
        }

        if (flow > 0) {
            amountToSpread = getTotalAmount();
        }
    }

    public boolean moveTo(BlockPos newPos) {
//        if (level == null || level.isClientSide) {
//            return false;
//        }
//
//        if (!canSpreadTo(newPos)) {
//            return false;
//        }
//
//        Map<Substance, Integer> currentSubstances = new HashMap<>(substances);
//        level.removeBlock(worldPosition, false);
//        level.setBlock(newPos, BlockInit.PUDDLE.get().defaultBlockState(), 3);
//
//        BlockEntity newEntity = level.getBlockEntity(newPos);
//        if (!(newEntity instanceof PuddleBlockEntity newPuddleEntity)) {
//            ModLogger.error("Failed to create new PuddleBlockEntity at " + newPos);
//            return false;
//        }
//
//        for (Map.Entry<Substance, Integer> entry : currentSubstances.entrySet()) {
//            newPuddleEntity.updateSubstance(entry.getKey(), entry.getValue());
//        }
//
//        newPuddleEntity.flow = flow;
//
//        newPuddleEntity.updatePuddleLevel();
//        newPuddleEntity.setChanged();
//        level.neighborChanged(newPos, BlockInit.PUDDLE.get(), newPos);
//
        return true;
    }


    private void mixWithNeighbors() {
        // Issues:
        // Mixes through walls
        // If substance is picked up and placed again, it causes it to bug out
//
//        List<PuddleBlockEntity> validNeighbors = new ArrayList<>();
//        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
//        int radius = 1;
//
//        for (int x = -radius; x <= radius; x++) {
//            for (int z = -radius; z <= radius; z++) {
//                // Skip the center block (the puddle itself)
//                if (x == 0 && z == 0) continue;
//
//                // Optionally, use this to make it circular instead of square
//                if (x * x + z * z > radius * radius) continue;
//
//                mutablePos.set(worldPosition).move(x, 0, z);
//
//                if (level == null || !level.isLoaded(mutablePos)) {
//                    continue;
//                }
//
//                if (!canSpreadTo(mutablePos)) {
//                    continue;
//                }
//
//                BlockEntity blockEntity = level.getBlockEntity(mutablePos);
//                if (blockEntity instanceof PuddleBlockEntity puddleBlockEntity) {
//                    validNeighbors.add(puddleBlockEntity);
//                }
//            }
//        }
//
//        if (validNeighbors.isEmpty()) {
//            return;
//        }
//
//        Map<Substance, Integer> totalSubstances = new HashMap<>(substances);
//
//        for (PuddleBlockEntity neighbor : validNeighbors) {
//            for (Map.Entry<Substance, Integer> entry : neighbor.getSubstances().entrySet()) {
//                totalSubstances.merge(entry.getKey(), entry.getValue(), Integer::sum);
//            }
//        }
//
//        Map<Substance, Integer> distributedSubstances = new HashMap<>(totalSubstances);
//        for (Map.Entry<Substance, Integer> entry : totalSubstances.entrySet()) {
//            int amount = Math.round((float) entry.getValue() / (validNeighbors.size() + 1));
//            if (amount > 0) {
////                ModLogger.debug(amount + " Distributed amount for: " + entry.getKey().getName() + " from original amount: " + entry.getValue() + " for this many puddles: " + validNeighbors.size());
//                distributedSubstances.put(entry.getKey(), amount);
//            }
//        }
//
//        for (PuddleBlockEntity neighbor : validNeighbors) {
//            neighbor.setSubstances(distributedSubstances);
//        }
//
//        setSubstances(distributedSubstances);
    }
}
