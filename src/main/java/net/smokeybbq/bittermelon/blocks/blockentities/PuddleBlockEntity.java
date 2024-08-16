package net.smokeybbq.bittermelon.blocks.blockentities;

import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.smokeybbq.bittermelon.blocks.PuddleBlock;
import net.smokeybbq.bittermelon.init.BlockEntityInit;
import net.smokeybbq.bittermelon.substances.Substance;
import net.smokeybbq.bittermelon.util.ColorUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static net.smokeybbq.bittermelon.init.BlockInit.PUDDLE;

public class PuddleBlockEntity extends BlockEntity implements Tickable {
    private final int MAX_CAPACITY = 20;
    private final Map<Substance, Integer> substances = new HashMap<>();
    private int cachedColor = -1;
    private int spreadTimer = 0;
    private int mixTimer = 0;
    private int amountToSpread = 0;
    private static final int SPREAD_DELAY = 10;
    private static final int MIX_DELAY = 40;

    public PuddleBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.PUDDLE_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public Map<Substance, Integer> getContents() {
        return substances;
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

        int totalAmount = substances.values().stream().mapToInt(Integer::intValue).sum();
        Map<Substance, Integer> transferredSubstances = new HashMap<>();
        List<Substance> substancesToRemove = new ArrayList<>();

        for (Map.Entry<Substance, Integer> entry : substances.entrySet()) {
            Substance substance = entry.getKey();
            int availableAmount = entry.getValue();

            double proportion = (double) availableAmount / totalAmount;
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
        assert level != null;
        if (!level.isClientSide) {
            level.removeBlock(getBlockPos(), false);
        }
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


    private void setSubstances(Map<Substance, Integer> newSubstances) {
        substances.clear();
        substances.putAll(newSubstances);
        setChanged();
    }

    public Map<Substance, Integer> getSubstances() {
        return substances;
    }

    private List<PuddleBlockEntity> getNeighboringPuddles() {
        List<PuddleBlockEntity> neighbors = new ArrayList<>();
        if (level == null) return neighbors;

        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = worldPosition.relative(dir);
            BlockEntity blockEntity = level.getBlockEntity(neighborPos);
            if (blockEntity instanceof PuddleBlockEntity) {
                neighbors.add((PuddleBlockEntity) blockEntity);
            }
        }
        Collections.shuffle(neighbors);
        return neighbors;
    }


    private void spread(int amount) {
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
        Queue<BlockPos> queue = new LinkedList<>();
        Set<BlockPos> visited = new HashSet<>();
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        assert level != null;
        for (Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(level.random)) {
            queue.offer(worldPosition.relative(dir));
        }

        while (!queue.isEmpty() && validNeighbors.size() < 4) {
            BlockPos pos = queue.poll();
            if (!visited.add(pos)) continue;

            if (level == null || !level.isLoaded(pos)) continue;

            BlockState state = level.getBlockState(pos);
            if (canSpreadTo(pos)) {
                if (state.isAir()) {
                    validNeighbors.add(pos.immutable());
                } else if (level.getBlockEntity(pos) instanceof PuddleBlockEntity puddleBlockEntity) {
                    if (puddleBlockEntity.getTotalAmount() < MAX_CAPACITY) {
                        validNeighbors.add(pos.immutable());
                    }
                }
            }

            if (state.isAir() || state.getBlock() instanceof PuddleBlock || !state.isSolid()) {
                for (Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(level.random)) {
                    mutablePos.set(pos).move(dir);
                    if (!visited.contains(mutablePos)) {
                        queue.offer(mutablePos.immutable());
                    }
                }
            }

            if (validNeighbors.isEmpty()) {
                for (Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(level.random)) {
                    mutablePos.set(worldPosition).move(dir);
                    BlockEntity blockEntity = level.getBlockEntity(mutablePos);
                    if (blockEntity instanceof PuddleBlockEntity) {
                        validNeighbors.add(mutablePos.immutable());
                    }
                }
            }
        }


        Collections.shuffle(validNeighbors);
        return validNeighbors;
    }

    private boolean canSpreadTo(BlockPos pos) {
        if (level == null) return false;
        BlockState state = level.getBlockState(pos);
        return state.isAir() || state.getBlock() instanceof PuddleBlock;
    }

    private void spill(BlockPos blockPos, int amount) {
        if (level == null) return;

        BlockState targetState = level.getBlockState(blockPos);
        PuddleBlockEntity targetPuddle;

        if (targetState.isAir()) {
            // Don't place the block yet, wait until we know we have substance to transfer
            targetPuddle = null;
        } else if (level.getBlockEntity(blockPos) instanceof PuddleBlockEntity entity) {
            targetPuddle = entity;
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
        nbt.put("PuddleData", serializeData());
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        deserializeData(nbt.getCompound("PuddleData"));
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

    public void mixWith(CompoundTag nbt) {
        ListTag substancesList = nbt.getList("Substances", 10);
        for (int i = 0; i < substancesList.size(); i++) {
            CompoundTag substanceTag = substancesList.getCompound(i);
            Substance substance = Substance.fromNBT(substanceTag);
            int amount = substanceTag.getInt("Amount");
            updateSubstance(substance, amount);
        }
    }

    public void checkForOverflow() {
        int totalAmount = getTotalAmount();
        if (totalAmount > MAX_CAPACITY) {
            int excessAmount = totalAmount - MAX_CAPACITY;
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
            mixTimer++;

            if (mixTimer >= MIX_DELAY) {
                mixWithNeighbors();
                mixTimer = 0;
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
        }
    }

    private void updatePuddleLevel() {
        int totalAmount = getTotalAmount();
        int newLevel;
        if (totalAmount < 6) {
            newLevel = 0;
        } else if (totalAmount < 12) {
            newLevel = 1;
        } else {
            newLevel = 2;
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
