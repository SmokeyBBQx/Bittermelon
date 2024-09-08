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
import net.smokeybbq.bittermelon.blocks.PuddleBlock;
import net.smokeybbq.bittermelon.init.BlockEntityInit;
import net.smokeybbq.bittermelon.systems.substances.Substance;
import net.smokeybbq.bittermelon.util.ColorUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static net.smokeybbq.bittermelon.init.BlockInit.PUDDLE;

public class PuddleBlockEntity extends BlockEntity implements Tickable {
    // Constants
    private static final int SPREAD_THRESHOLD = 16;
    private static final int MAX_CAPACITY = 40;
    private static final int SPREAD_DELAY = 10;
    private static final int EQUALIZE_DELAY = 10;
    private static final int GRAVITY_CHECK_DELAY = 5;

    // Instance variables
    private final Map<Substance, Integer> substances = new HashMap<>();
    private int cachedColor = -1;
    private int spreadTimer = 0;
    private int amountToSpread = 0;
    private int equalizeTimer = 0;
    private int gravityCheckTimer = 0;

    public PuddleBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.PUDDLE_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void tick() {
        if (getTotalAmount() < 1) {
            removePuddleBlock();
            return;
        }

        if (level != null && !level.isClientSide) {
            handleEqualization();
            handleGravity();
            handleSpread();
            checkIfOnPuddle(worldPosition, level, substances);
        }
    }

    private void handleEqualization() {
        if (++equalizeTimer >= EQUALIZE_DELAY) {
            equalizeHorizontally();
            equalizeTimer = 0;
        }
    }

    private void handleGravity() {
        if (++gravityCheckTimer >= GRAVITY_CHECK_DELAY) {
            applyGravity();
            gravityCheckTimer = 0;
        }
    }

    private void handleSpread() {
        if (amountToSpread > 0 && level != null) {
            if (!level.getBlockState(worldPosition.below()).isAir()) {
                if (++spreadTimer >= SPREAD_DELAY) {
                    spread(amountToSpread);
                    amountToSpread = 0;
                    spreadTimer = 0;
                }
            }
        } else {
            spreadTimer = 0;
            checkForOverflow();
        }
    }

    /**
     * ---------Substance Handling---------
     */

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

    public Map<Substance, Integer> getSubstances() {
        return substances;
    }

    public int getTotalAmount() {
        return substances.values().stream().reduce(0, Integer::sum);
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

    /**
     * ---------Fluid Behavior---------
     */

    private void applyGravity() {
        if (level == null) return;

        BlockPos belowPos = worldPosition.below();
        BlockState belowState = level.getBlockState(belowPos);

        if (level.getBlockEntity(belowPos) instanceof PuddleBlockEntity) {
            return;
        }

        if (belowState.canBeReplaced()) {
            level.setBlock(belowPos, PUDDLE.get().defaultBlockState(), 3);

            if (level.getBlockEntity(belowPos) instanceof PuddleBlockEntity targetPuddle) {
                targetPuddle.setSubstances(substances);
                substances.clear();
                removePuddleBlock();
            }
        }
    }

    public void checkForOverflow() {
        int totalAmount = getTotalAmount();
        if (totalAmount > SPREAD_THRESHOLD) {
            int excessAmount = totalAmount - SPREAD_THRESHOLD;
            amountToSpread += excessAmount;
        }
    }

    public void checkIfOnPuddle(BlockPos pos, Level level, Map<Substance, Integer> substances) {
        if (level.getBlockEntity(pos.below()) instanceof PuddleBlockEntity blockEntity) {
            if (blockEntity.getTotalAmount() < MAX_CAPACITY) {
                transferSubstances(getTotalAmount());
                blockEntity.mixWith(substances);
            }
        }
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
        Queue<BlockPos> queue = new LinkedList<>();
        Set<BlockPos> visited = new HashSet<>();

        BlockState currentState = level.getBlockState(worldPosition);
        boolean isDisplaced = !(currentState.getBlock() instanceof PuddleBlock) || getTotalAmount() > MAX_CAPACITY;

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

            // TODO: Fix issue with four neighbors being found in a straight enclosed line

            if (state.canBeReplaced() || state.getBlock() instanceof PuddleBlock) {
                for (Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(level.random)) {
                    mutablePos.set(pos).move(dir);

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
        }

        if (isDisplaced && validNeighbors.isEmpty()) {
            for (Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(level.random)) {
                BlockPos pos = worldPosition.relative(dir).above();
                if (level.getBlockEntity(pos) instanceof PuddleBlockEntity entity) {
                    if (getTotalAmount() < entity.getTotalAmount()) {
                        validNeighbors.add(worldPosition.relative(dir).above());
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
        BlockPos belowPos = pos.below();

        if (level.getBlockState(belowPos).isAir()) {
            return level.getBlockState(belowPos.below()).isAir();
        }

        if (level.getBlockState(pos.below()).canBeReplaced()) {
            if (level.getBlockEntity(belowPos) instanceof PuddleBlockEntity blockEntity) {
                return blockEntity.getTotalAmount() > MAX_CAPACITY;
            }
            return false;
        }

        if (level.getBlockEntity(pos) instanceof PuddleBlockEntity blockEntity) {
            return blockEntity.getTotalAmount() < MAX_CAPACITY;
        }

        return state.canBeReplaced();
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

            targetPuddle.setChanged();
            setChanged();
        }
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
        flowCandidates.add(this);

        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = worldPosition.relative(dir);
            if (level != null && level.getBlockEntity(neighborPos) instanceof PuddleBlockEntity entity) {
                if (entity.getTotalAmount() != getTotalAmount()) {
                    flowCandidates.add(entity);
                }
            }
        }

        if (flowCandidates.size() > 1) {
            Map<Substance, Integer> totalSubstances = new HashMap<>();
            for (PuddleBlockEntity puddle : flowCandidates) {
                for (Map.Entry<Substance, Integer> entry : puddle.getSubstances().entrySet()) {
                    totalSubstances = mergeSubstances(totalSubstances, entry.getKey(), entry.getValue());
                }
            }

            Map<Substance, Integer> equalizedSubstances = totalSubstances.entrySet().stream()
                    .filter(entry -> entry.getValue() > 0)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> Math.round((float) entry.getValue() / flowCandidates.size())
                    ));

            for (PuddleBlockEntity puddle : flowCandidates) {
                puddle.setSubstances(new HashMap<>(equalizedSubstances));
            }
        }
    }

    /**
     * ------- Visuals -------
     */
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

    public String getContentsDescription() {
        if (substances.isEmpty()) {
            return "Empty";
        }

        return substances.entrySet().stream()
                .map(entry -> String.format("%s: %d", entry.getKey().getName(), entry.getValue()))
                .collect(Collectors.joining(", "));
    }

    private void updatePuddleLevel() {
        if (level != null && level.getBlockState(worldPosition).getBlock() instanceof PuddleBlock) {
            int newLevel = getNewLevel();

            if (level != null) {
                BlockState currentState = level.getBlockState(worldPosition);
                if (currentState.getValue(PuddleBlock.LEVEL) != newLevel) {
                    level.setBlock(worldPosition, currentState.setValue(PuddleBlock.LEVEL, newLevel), 3);
                    level.sendBlockUpdated(worldPosition, currentState, currentState.setValue(PuddleBlock.LEVEL, newLevel), 3);
                }
            }
        }
    }

    private int getNewLevel() {
        int totalAmount = getTotalAmount();
        if (totalAmount < 6) return 0;
        else if (totalAmount <= 11) return 1;
        else if (totalAmount <= 19) return 2;
        else if (totalAmount <= 24) return 3;
        else if (totalAmount <= 26) return 4;
        else if (totalAmount <= 28) return 5;
        else if (totalAmount <= 30) return 6;
        else if (totalAmount <= 32) return 7;
        else if (totalAmount <= 34) return 8;
        else if (totalAmount <= 36) return 9;
        else return 10;
    }

    /**
     * ------- NBT/Data -------
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
        CompoundTag puddleData = nbt.getCompound("PuddleData");
        deserializeData(puddleData);
    }

    /**
     * ------- Networking -------
     */
    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        CompoundTag puddleData = serializeData();
        tag.put("PuddleData", puddleData);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        CompoundTag puddleData = tag.getCompound("PuddleData");
        deserializeData(puddleData);
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
}
