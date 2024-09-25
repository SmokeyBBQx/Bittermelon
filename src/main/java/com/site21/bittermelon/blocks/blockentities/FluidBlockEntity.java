package com.site21.bittermelon.blocks.blockentities;

import com.site21.bittermelon.blocks.FluidBlock;
import com.site21.bittermelon.substance.Substance;
import com.site21.bittermelon.substance.SubstanceStack;
import com.site21.bittermelon.substance.reactions.Reaction;
import com.site21.bittermelon.substance.reactions.ReactionContainer;
import com.site21.bittermelon.substance.reactions.ReactionHandler;
import com.site21.bittermelon.util.ColorUtil;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.sound.midi.SysexMessage;
import java.util.*;
import java.util.stream.Collectors;

import static com.site21.bittermelon.init.BlockEntityInit.FLUID_BLOCK_ENTITY;
import static com.site21.bittermelon.init.BlockInit.FLUID;

public class FluidBlockEntity extends BlockEntity implements Tickable, ReactionContainer {
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

    @Override
    public void tick() {
        if (getTotalAmount() < 1) {
            removePuddleBlock();
            return;
        }

        handleSubstanceInteractions();

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
                return;
            }
        }

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
        return substances.stream()
                .map(stack -> stack.getSubstance().getHeatCapacity() * stack.getAmount())
                .reduce(0f, Float::sum);
    }

    public void setTemperature(float temperature) {
        for (SubstanceStack stack : substances) {
            stack.setTemperature(temperature / substances.size());
        }
    }

    public void modifyTemperature(float temperature) {
        setTemperature(Math.max(0, temperature + getTemperature()));
    }

    public void mixWith(List<SubstanceStack> substances) {
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

    public void checkIfOnPuddle(BlockPos pos, Level level, List<SubstanceStack> substances) {
        if (level.getBlockEntity(pos.below()) instanceof FluidBlockEntity blockEntity) {
            if (blockEntity.getTotalVolume() < MAX_CAPACITY) {
                blockEntity.mixWith(substances);
                substances.clear();
            }
        }
    }

    public boolean spread(float amount) {
        if (level == null) return false;

        List<BlockPos> validNeighbors = getValidNeighbors();
        if (validNeighbors.isEmpty()) return false;

        int spreadDirections = Math.min(validNeighbors.size(), 4);
        float spillAmount = amount / spreadDirections;

        for (BlockPos neighbor : validNeighbors) {
            spill(neighbor, spillAmount);
        }

        System.out.println("Spread returning true");
        return true;
    }

    private List<BlockPos> getValidNeighbors() {
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
                    if (getTotalVolume() < entity.getTotalVolume() && entity.getTotalVolume() < SPREAD_THRESHOLD) {
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

    private void spill(BlockPos blockPos, float amount) {
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
        float totalAmount = getTotalAmount();
        float totalTransferred = 0;

        for (SubstanceStack stack : substances) {
            float proportion = stack.getAmount() / totalAmount;
            float transferAmount = Math.min(amount * proportion, stack.getAmount());

            if (transferAmount > 0) {
                SubstanceStack stackToTransfer = stack.copy();
                stackToTransfer.setAmount(transferAmount);
                System.out.println("Stack to transfer amount is " + stackToTransfer.getAmount());
                substancesToTransfer.add(stackToTransfer);
                System.out.println("Original stack being removed with " + transferAmount + " with original stack amount being " + stack.getAmount());
                stack.modifyAmount(-transferAmount);
                totalTransferred += transferAmount;
            }
        }

        if (totalTransferred > 0) {
            if (targetPuddle == null) {
                level.setBlock(blockPos, FLUID.get().defaultBlockState(), 3);
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
    }

    private static void mergeSubstances(List<SubstanceStack> substances, SubstanceStack stack) {
        for (SubstanceStack substance : substances) {
            if (stack.canMergeWith(substance)) {
                substance.modifyAmount(stack.getAmount());
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
                if (entity.getTotalAmount() != getTotalAmount()) {
                    flowCandidates.add(entity);
                }
            }
        }

        if (flowCandidates.size() > 1) {
            List<SubstanceStack> totalSubstances = new ArrayList<>();
            for (FluidBlockEntity fluid : flowCandidates) {
                for (SubstanceStack stack : fluid.getSubstances()) {
                    mergeSubstances(totalSubstances, stack);
                }
            }

            totalSubstances.removeIf(stack -> {
                if (stack.getAmount() > 0) {
                    System.out.println("Running equalization for substance with " + stack.getAmount() + " amount, divided by " + flowCandidates.size());
                    stack.setAmount(stack.getAmount() / flowCandidates.size());
                    System.out.println("Setting amount with " + stack.getAmount());
                    return false;
                }
                return true;
            });


            for (FluidBlockEntity puddle : flowCandidates) {
                puddle.setSubstances(new ArrayList<>(totalSubstances));
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
        super.saveAdditional(tag, registries);
        CompoundTag contentsData = tag.getCompound(CONTENTS_KEY);
        deserializeData(contentsData, registries);
        active = tag.getBoolean(ACTIVE_KEY);
    }

    private CompoundTag serializeData(HolderLookup.Provider registries) {
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

    private void deserializeData(CompoundTag nbt, HolderLookup.Provider registries) {
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
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }
}
