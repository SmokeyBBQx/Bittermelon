package com.site21.bittermelon.content.blocks.substance.fluid;

import com.site21.bittermelon.content.substance.SubstanceStack;
import com.site21.bittermelon.content.substance.reactions.ReactionContainer;
import com.site21.bittermelon.content.substance.reactions.ReactionHandler;
import com.site21.bittermelon.util.ColorUtil;
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.FLUID_BLOCK_ENTITY;
import static com.site21.bittermelon.init.neoforge.BitterBlocks.FLUID;

public class FluidBlockEntity extends BlockEntity implements ReactionContainer {
    // Constants
    private static final int SPREAD_THRESHOLD = 16;
    private static final int MAX_CAPACITY = 40;
    private static final int OVERFLOW_DELAY = 10;
    private static final int SPREAD_DELAY = 10;
    private static final String ACTIVE_KEY = "Active";
    private static final String CONTENTS_KEY = "Contents";

    // Instance variables
    private final List<SubstanceStack> substances = new ArrayList<>();
    private int cachedColor = -1;
    private int overflowTimer = 0;
    private int spreadTimer = 0;
    private float amountToSpread = 0;
    private int updateDelay = 0;
    private boolean active = true;

    public FluidBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public FluidBlockEntity(BlockPos pos, BlockState blockState) {
        super(FLUID_BLOCK_ENTITY.get(), pos, blockState);
    }

    public void tick() {
        if (getTotalAmount() <= 0) {
            removePuddleBlock();
            return;
        }

//        handleSubstanceInteractions();

        if (!active) {
            updateDelay = 20;
        } else {
            updateDelay = 0;
        }

        if (level != null && !level.isClientSide) {
            handleSpread();
            handleOverflow();
            checkIfOnPuddle(worldPosition, level, substances);
        }
    }

    private void handleSpread() {
        if (++spreadTimer >= SPREAD_DELAY + updateDelay) {
            spreadTimer = 0;
            if (!substances.isEmpty()) {
                equalizeSubstances();
                equalizeTemperature();
                applyGravity();
//                handleSubstanceInteractions();
                updatePuddleLevel();
            }
        }
    }

    private void handleOverflow() {
        if (amountToSpread > 0 && level != null) {
            if (!level.getBlockState(worldPosition.below()).isAir()) {
                if (++overflowTimer >= OVERFLOW_DELAY && active) {
                    if (spread(amountToSpread)) {
                        amountToSpread = 0;
                        overflowTimer = 0;
                    } else {
                        active = false;
                    }
                }
            }
        } else {
            overflowTimer = 0;
            checkForOverflow();
        }
    }

    /**
     * ---------Substance Handling---------
     */

    public void updateSubstance(SubstanceStack substance) {
        for (SubstanceStack stack : substances) {
            if (stack.canMergeWith(substance)) {
                stack.modifyAmount(substance.getAmount());
                updatePuddleLevel();
                setChanged();
                return;
            }
        }

        System.out.println("Adding " + substance.getSubstance().getName() + " with volume " + substance.getVolume() + " and amount " + substance.getAmount() + " to " + worldPosition);

        substances.add(substance);

        updatePuddleLevel();
        setChanged();
    }

    public List<SubstanceStack> transferSubstances(float amount) {
        if (substances.isEmpty()) {
            return Collections.emptyList();
        }

        float totalAmount = getTotalAmount();
        List<SubstanceStack> transferredSubstances = new ArrayList<>();
        Iterator<SubstanceStack> iterator = substances.iterator();

        while (iterator.hasNext()) {
            SubstanceStack stack = iterator.next();
            float availableAmount = stack.getAmount();

            float transferAmount = (amount * availableAmount) / totalAmount;
            float actualAmount = Math.min(availableAmount, transferAmount);

            if (actualAmount > 0) {
                stack.modifyAmount(-actualAmount);
                SubstanceStack stackToTransfer = stack.copy();
                stackToTransfer.setAmount(actualAmount);
                transferredSubstances.add(stackToTransfer);

                if (availableAmount <= actualAmount) {
                    iterator.remove();
                }
            }
        }

        if (substances.isEmpty()) {
            removePuddleBlock();
        }

        setChanged();
        return transferredSubstances;
    }

    public List<SubstanceStack> transferSubstancesVolume(float amount) {
        if (substances.isEmpty()) {
            return Collections.emptyList();
        }

        float totalVolume = getTotalVolume();
        List<SubstanceStack> transferredSubstances = new ArrayList<>();
        Iterator<SubstanceStack> iterator = substances.iterator();

        while (iterator.hasNext()) {
            SubstanceStack stack = iterator.next();
            float availableVolume = stack.getVolume();

            float transferAmount = (amount * availableVolume) / totalVolume;
            float actualAmount = Math.min(availableVolume, transferAmount);

            if (actualAmount > 0) {
                stack.modifyVolume(-actualAmount);
                SubstanceStack stackToTransfer = stack.copy();
                stackToTransfer.setVolume(actualAmount);
                transferredSubstances.add(stackToTransfer);

                if (availableVolume <= actualAmount) {
                    iterator.remove();
                }
            }
        }

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

    public List<SubstanceStack> getSubstances() {
        return substances;
    }

    public float getTotalAmount() {
        return substances.stream()
                .map(SubstanceStack::getAmount)
                .reduce(0f, Float::sum);
    }

    public float getTotalVolume() {
        return substances.stream()
                .map(SubstanceStack::getVolume)
                .reduce(0f, Float::sum);
    }

    public float getTemperature() {
        return substances.stream()
                .map(SubstanceStack::getTemperature)
                .reduce(0f, Float::sum);

        // TODO: Cache temperature and only update when necessary
    }

    public float getHeatCapacity() {
//        return substances.stream()
//                .map(stack -> stack.getSubstance().getHeatCapacity() * stack.getAmount())
//                .reduce(0f, Float::sum);
        return 0f;
    }

    public float getSlipperiness() {
        // TODO Cache?

        return substances.isEmpty() ? 0f :
                substances.stream()
                        .map(stack -> stack.getSubstance().getSlipperiness())
                        .reduce(0f, Float::sum) / substances.size();
    }

    public void setTemperature(float temperature) {
        for (SubstanceStack stack : substances) {
            stack.setTemperature(temperature / substances.size());
        }
    }

    public void modifyTemperature(float temperature) {
        setTemperature(Math.max(0, temperature + getTemperature()));
    }

    public void mixWith(@NotNull List<SubstanceStack> substances) {
        substances.forEach(this::updateSubstance);
    }

    public void setSubstances(List<SubstanceStack> newSubstances) {
        substances.clear();
        substances.addAll(newSubstances);
        updatePuddleLevel();
        setChanged();
    }

    /**
     * ---------Substance Interactions---------
     */

    private void handleSubstanceInteractions() {
        ReactionHandler.getInstance().handleReactions(substances, this);
        substances.removeIf(stack -> stack.getAmount() <= 0);
    }



    /**
     * ---------Fluid Behavior---------
     */

    private void applyGravity() {
        if (level == null) return;

        BlockPos belowPos = worldPosition.below();
        BlockState belowState = level.getBlockState(belowPos);

        if (level.getBlockEntity(belowPos) instanceof FluidBlockEntity) {
            return;
        }

        if (belowState.canBeReplaced()) {
            level.setBlock(belowPos, FLUID.get().defaultBlockState(), 3);

            if (level.getBlockEntity(belowPos) instanceof FluidBlockEntity targetPuddle) {
                targetPuddle.setSubstances(substances);
                substances.clear();
                removePuddleBlock();
            }
        }
    }

    public void checkForOverflow() {
        float totalAmount = getTotalVolume();
        if (totalAmount > SPREAD_THRESHOLD) {
            float excessAmount = totalAmount - SPREAD_THRESHOLD;
            amountToSpread += excessAmount;
        }
    }

    public void checkIfOnPuddle(@NotNull BlockPos pos, @NotNull Level level, List<SubstanceStack> substances) {
        if (level.getBlockEntity(pos.below()) instanceof FluidBlockEntity blockEntity) {
            if (blockEntity.getTotalVolume() < MAX_CAPACITY) {
                blockEntity.mixWith(substances);
                substances.clear();
            }
        }
    }

    public boolean spread(float amount) {
        if (level == null) return false;

        Set<BlockPos> validNeighbors = getValidNeighbors();
        if (validNeighbors.isEmpty()) return false;

        int spreadDirections = Math.min(validNeighbors.size(), 4);
        float spillAmount = amount / spreadDirections;

        for (BlockPos neighbor : validNeighbors) {
            spill(neighbor, spillAmount);
        }

        return true;
    }

    private Set<BlockPos> getValidNeighbors() {
        level.getProfiler().push("fluid-bfs");

        if (level == null) return Collections.emptySet();

        LongSet validNeighbors = new LongOpenHashSet(4);
        LongArrayFIFOQueue queue = new LongArrayFIFOQueue();
        LongSet visited = new LongOpenHashSet();

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();

        int worldY = worldPosition.getY();

        BlockState currentState = level.getBlockState(worldPosition);
        boolean isDisplaced = !(currentState.getBlock() instanceof FluidBlock) || getTotalVolume() > MAX_CAPACITY;

        for (Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(level.random)) {
            mutablePos.set(worldPosition).move(dir);
            if (level.getBlockState(mutablePos).canBeReplaced()) {
                mutablePos.move(Direction.DOWN);
                queue.enqueue(mutablePos.asLong());
            }
        }

        for (Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(level.random)) {
            mutablePos.set(worldPosition).move(dir);
            queue.enqueue(mutablePos.asLong());
        }

        while (!queue.isEmpty() && validNeighbors.size() < 4) {
            Long packedPos = queue.dequeueLong();
            if (!visited.add(packedPos)) continue;

            mutablePos.set(packedPos);

            if (canSpreadTo(mutablePos)) {
                BlockEntity blockEntity = level.getBlockEntity(mutablePos);
                if (blockEntity instanceof FluidBlockEntity entity) {
                    if (entity.getTotalVolume() <= SPREAD_THRESHOLD) {
                        validNeighbors.add(packedPos);
                    }
                } else {
                    validNeighbors.add(packedPos);
                }
            }

            // TODO: Fix issue with four neighbors being found in a straight enclosed line

            BlockState state = level.getBlockState(mutablePos);
            if (state.canBeReplaced() || state.getBlock() instanceof FluidBlock) {
                for (Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(level.random)) {
                    checkPos.set(mutablePos).move(dir);
                    long neighborPacked = checkPos.asLong();

                    if (!visited.contains(neighborPacked)) {
                        queue.enqueue(neighborPacked);
                    }
                }
            }

            if (!validNeighbors.isEmpty()) {
                for (long neighborPacked : validNeighbors) {
                    if (worldY - BlockPos.getY(neighborPacked) > 0) {
                        return longSetToBlockPos(validNeighbors);
                    }
                }
            }
        }

        if (isDisplaced && validNeighbors.isEmpty()) {
            for (Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(level.random)) {
                mutablePos.set(worldPosition).move(dir).move(Direction.UP);
                BlockEntity blockEntity = level.getBlockEntity(mutablePos);

                if (blockEntity instanceof FluidBlockEntity entity &&
                        getTotalVolume() < entity.getTotalVolume()) {
                    validNeighbors.add(mutablePos.asLong());
                }
            }
        }

        level.getProfiler().pop();
        return longSetToBlockPos(validNeighbors);
    }

    private @NotNull Set<BlockPos> longSetToBlockPos(@NotNull LongSet longs) {
        if (longs.isEmpty()) return Collections.emptySet();

        List<BlockPos> positions = new ArrayList<>(longs.size());
        for (long packed : longs) {
            positions.add(BlockPos.of(packed));
        }
        Collections.shuffle(positions);
        return new HashSet<>(positions);
    }

    private List<BlockPos> getValidNeighborsOld() {
        List<BlockPos> validNeighbors = new ArrayList<>();
        if (level == null) return validNeighbors;

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        Queue<BlockPos> queue = new LinkedList<>();
        Set<BlockPos> visited = new HashSet<>();

        BlockState currentState = level.getBlockState(worldPosition);
        boolean isDisplaced = !(currentState.getBlock() instanceof FluidBlock) || getTotalVolume() > MAX_CAPACITY;

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
                if (level.getBlockEntity(pos) instanceof FluidBlockEntity entity) {
                    if (entity.getTotalVolume() <= SPREAD_THRESHOLD) {
                        validNeighbors.add(pos);
                    }
                } else {
                    validNeighbors.add(pos);
                }
            }

            // TODO: Fix issue with four neighbors being found in a straight enclosed line

            if (state.canBeReplaced() || state.getBlock() instanceof FluidBlock) {
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
                if (level.getBlockEntity(pos) instanceof FluidBlockEntity entity) {
                    if (getTotalVolume() < entity.getTotalVolume()) {
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
            if (level.getBlockEntity(belowPos) instanceof FluidBlockEntity blockEntity) {
                return blockEntity.getTotalVolume() > MAX_CAPACITY;
            }
            return false;
        }

        if (level.getBlockEntity(pos) instanceof FluidBlockEntity blockEntity) {
            return blockEntity.getTotalVolume() < MAX_CAPACITY;
        }

        return state.canBeReplaced();
    }

    private void spill(BlockPos blockPos, float volumeToTransfer) {
        if (level == null) return;

        BlockState targetState = level.getBlockState(blockPos);
        FluidBlockEntity targetPuddle;

        if (level.getBlockEntity(blockPos) instanceof FluidBlockEntity entity) {
            targetPuddle = entity;
        } else if (targetState.canBeReplaced()) {
            // Don't place the block yet, wait until we know we have substance to transfer
            if (level.getBlockEntity(blockPos.below()) instanceof FluidBlockEntity entity) {
                targetPuddle = entity;
            } else {
                targetPuddle = null;
            }
        } else {
            return;
        }

        List<SubstanceStack> substancesToTransfer = new ArrayList<>();
        float totalVolume = getTotalVolume();
        float totalTransferred = 0;

        for (SubstanceStack stack : substances) {
            float proportion = stack.getVolume() / totalVolume;
            float transferVolume = Math.min(volumeToTransfer * proportion, stack.getVolume());

            if (transferVolume > 0) {
                SubstanceStack stackToTransfer = stack.copy();
                stackToTransfer.setVolume(transferVolume);
                substancesToTransfer.add(stackToTransfer);
                stack.modifyVolume(-transferVolume);
                totalTransferred += transferVolume;
            }
        }

        if (totalTransferred > 0) {
            if (targetPuddle == null) {
                level.setBlock(blockPos, FLUID.get().defaultBlockState(), 3);
                level.playSound(null, worldPosition, SoundEvents.GENERIC_SPLASH, SoundSource.AMBIENT, 0.1f, 1.2f);
                BlockEntity newBlockEntity = level.getBlockEntity(blockPos);
                if (!(newBlockEntity instanceof FluidBlockEntity)) {
                    return;
                }
                targetPuddle = (FluidBlockEntity) newBlockEntity;
            }

            // Transfer the substances
            for (SubstanceStack stack : substancesToTransfer) {
                targetPuddle.updateSubstance(stack);
            }

            targetPuddle.setChanged();
            setChanged();
        }
        // TODO: Issue because we're using amounts and not volumes?
    }

    private static void mergeSubstances(@NotNull List<SubstanceStack> substances, SubstanceStack stack) {
        for (SubstanceStack substance : substances) {
            if (stack.canMergeWith(substance)) {
                substance.modifyVolume(stack.getVolume());
                return;
            }
        }

        substances.add(stack);
    }

    private void equalizeSubstances() {
        List<FluidBlockEntity> flowCandidates = new ArrayList<>(5);
        flowCandidates.add(this);

        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = worldPosition.relative(dir);
            if (level != null && level.getBlockEntity(neighborPos) instanceof FluidBlockEntity entity) {
                if (entity.getTotalVolume() != getTotalVolume()) {
                    flowCandidates.add(entity);
                }
            }
        }

        if (flowCandidates.size() > 1) {
            List<SubstanceStack> totalSubstances = new ArrayList<>();

            for (FluidBlockEntity fluid : flowCandidates) {
                for (SubstanceStack stack : fluid.getSubstances()) {
                    SubstanceStack stackCopy = stack.copy();
                    mergeSubstances(totalSubstances, stackCopy);
                }
            }

            totalSubstances.removeIf(stack -> {
                if (stack.getVolume() > 0) {
                    stack.setVolume(stack.getVolume() / flowCandidates.size());
                    return false;
                }
                return true;
            });

            for (FluidBlockEntity puddle : flowCandidates) {
                List<SubstanceStack> puddleSubstances = new ArrayList<>();
                for (SubstanceStack stack : totalSubstances) {
                    puddleSubstances.add(stack.copy());
                }
                puddle.setSubstances(puddleSubstances);
            }
        }
    }

    private void equalizeTemperature() {
        List<FluidBlockEntity> flowCandidates = new ArrayList<>(5);
        flowCandidates.add(this);

        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = worldPosition.relative(dir);
            if (level != null && level.getBlockEntity(neighborPos) instanceof FluidBlockEntity entity) {
                if (entity.getTemperature() != getTemperature()) {
                    flowCandidates.add(entity);
                }
            }
        }

        if (flowCandidates.size() > 1) {
            float totalTemperature = 0;

            for (FluidBlockEntity fluid : flowCandidates) {
                totalTemperature += fluid.getTemperature();
            }

            float distributedTemperature = totalTemperature / flowCandidates.size();

            for (FluidBlockEntity fluid : flowCandidates) {
                fluid.setTemperature(distributedTemperature);
            }
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setActive() {
        setActive(true);
    }

    /**
     * ------- Visuals -------
     */

    public int getColor() {
        if (cachedColor == -1) {
            if (substances.isEmpty()) {
                cachedColor = 0xFFAAD5DB; // Default color if no substances
            } else {
                Map<Integer, Float> colors = new HashMap<>();
                for (SubstanceStack stack : substances) {
                    colors.put(stack.getSubstance().getColor(), stack.getAmount());
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

        return substances.stream()
                .map(entry -> String.format("%s: %f", entry.getSubstance().getName(), entry.getAmount()))
                .collect(Collectors.joining(", "));
    }

    private void updatePuddleLevel() {
        if (level != null && level.getBlockState(worldPosition).getBlock() instanceof FluidBlock) {
            int newLevel = getNewLevel();

            if (level != null) {
                BlockState currentState = level.getBlockState(worldPosition);
                if (currentState.getValue(FluidBlock.LEVEL) != newLevel) {
                    level.setBlock(worldPosition, currentState.setValue(FluidBlock.LEVEL, newLevel), 3);
                    level.sendBlockUpdated(worldPosition, currentState, currentState.setValue(FluidBlock.LEVEL, newLevel), 3);
                }
            }
        }
    }

    private int getNewLevel() {
        float totalAmount = getTotalVolume();
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

    @Override
    public void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(CONTENTS_KEY, serializeData(registries));
        tag.putBoolean(ACTIVE_KEY, active);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        CompoundTag contentsData = tag.getCompound(CONTENTS_KEY);
        deserializeData(contentsData, registries);
        active = tag.getBoolean(ACTIVE_KEY);
    }

    private @NotNull CompoundTag serializeData(HolderLookup.Provider registries) {
        CompoundTag nbt = new CompoundTag();
        ListTag substancesList = new ListTag();
        for (SubstanceStack stack : substances) {
            if (stack.getAmount() > 0) {
                substancesList.add(stack.save(registries));
            }
        }
        nbt.put(CONTENTS_KEY, substancesList);
        return nbt;
    }

    private void deserializeData(@NotNull CompoundTag nbt, HolderLookup.Provider registries) {
        substances.clear();
        ListTag substancesList = nbt.getList(CONTENTS_KEY, 10);
        for (int i = 0; i < substancesList.size(); i++) {
            CompoundTag substanceTag = substancesList.getCompound(i);
            SubstanceStack substance = SubstanceStack.parseOptional(registries, substanceTag);
            substances.add(substance);
        }
        setChanged();
    }

    /**
     * ------- Networking -------
     */

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        CompoundTag tag = new CompoundTag();
        CompoundTag contents = serializeData(registries);
        tag.put(CONTENTS_KEY, contents);
//        tag.putBoolean(ACTIVE_KEY, active);
        return tag;
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

    private void syncToClient() {
        if (level != null && level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }
}
