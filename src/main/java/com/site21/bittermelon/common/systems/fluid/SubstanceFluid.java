package com.site21.bittermelon.common.systems.fluid;

import com.google.common.collect.Maps;
import com.site21.bittermelon.common.content.blocks.properties.BitterStateProperties;
import com.site21.bittermelon.common.systems.substance.Substance;
import com.site21.bittermelon.common.systems.substance.SubstanceStack;
import com.site21.bittermelon.init.neoforge.BitterFluidTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.site21.bittermelon.init.neoforge.BitterBlocks.SUBSTANCE_FLUID_BLOCK;

public class SubstanceFluid extends Fluid {
    public static final IntegerProperty LEVEL = BitterStateProperties.LEVEL;
    private static final float SPREAD_THRESHOLD = 2.0f;
    private static final float OVERFLOW_THRESHOLD = 15.5f;

    private final Map<FluidState, VoxelShape> shapes = Maps.newIdentityHashMap();

    private final Supplier<? extends BucketItem> bucket;

    public SubstanceFluid(Supplier<? extends BucketItem> bucket) {
        this.bucket = bucket;
        registerDefaultState(getStateDefinition().any()
                .setValue(LEVEL, 1));
    }

    @Override
    protected void createFluidStateDefinition(StateDefinition.@NotNull Builder<Fluid, FluidState> builder) {
        builder.add(LEVEL);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState blockState, @NotNull FluidState fluidState) {
        if (level.getBlockEntity(pos) instanceof SubstanceFluidBlockEntity fluidBE) {
            if (fluidBE.getVolume() <= 0) {
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                return;
            }

            spreadDownwards(level, pos, fluidBE);
            spread(level, pos, fluidBE);
            equalizeSubstances(level, pos, fluidBE);
        }
    }

    private void spreadDownwards(@NotNull Level level, @NotNull BlockPos pos, @NotNull SubstanceFluidBlockEntity fluidBE) {
        // Check if the fluid below is the same type and not full
        // If not, spread downwards

        FluidState belowState = level.getFluidState(pos.below());
        if (!belowState.is(this) || belowState.getAmount() >= 15) return;

        if (level.getBlockEntity(pos.below()) instanceof SubstanceFluidBlockEntity downBE) {
            downBE.transferSubstances(fluidBE.getSubstances());
        }

        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
    }

    private void spread(@NotNull Level level, BlockPos pos, @NotNull SubstanceFluidBlockEntity fluidBE) {
        float substanceVolume = fluidBE.getVolume();
        if (substanceVolume <= SPREAD_THRESHOLD) return;

        List<BlockPos> spreadPositions = getSpreadPositions(level, pos, fluidBE);
        if (spreadPositions.isEmpty()) return;

        // Calculate volume to spread to each position
        float volumePerSpread = substanceVolume / (spreadPositions.size() + 1);
        List<SubstanceStack> substancesToSpread = getSubstancesForSpread(fluidBE, volumePerSpread);

        // Spread to each position
        for (BlockPos spreadPos : spreadPositions) {
            spreadTo(level, spreadPos, substancesToSpread);
        }

        // Remove spread substances from original block entity
        for (SubstanceStack spreadStack : substancesToSpread) {
            float totalRemoved = spreadStack.getAmount() * spreadPositions.size();
            fluidBE.removeSubstance(spreadStack, totalRemoved);
        }
    }

    private @NotNull List<BlockPos> getSpreadPositions(@NotNull Level level, @NotNull BlockPos pos, SubstanceFluidBlockEntity fluidBE) {
        List<BlockPos> neighbors = new ArrayList<>();

        // Try to spread downwards first if the fluid below is not the same type
        if (!level.getFluidState(pos.below()).is(this)) {
            neighbors = findNeighbors(level, pos.below(), fluidBE);
        }

        // If no downward spread positions, try horizontally
        if (neighbors.isEmpty()) {
            neighbors = findNeighbors(level, pos, fluidBE);

            // If still no neighbors and volume is high enough, try upwards
            if (neighbors.isEmpty()) {
                if (fluidBE.getVolume() >= OVERFLOW_THRESHOLD) {
                    spreadUpwards(level, pos, fluidBE);
                    return List.of();
                }
            }
        }

        return neighbors;
    }

    private void spreadUpwards(@NotNull Level level, @NotNull BlockPos pos, @NotNull SubstanceFluidBlockEntity fluidBE) {
        float spreadVolume = fluidBE.getVolume() - 15f;
        if (spreadVolume <= 0f) return;

        BlockPos abovePos = pos.above();

        // Check if we can spread upwards
        if (!canSpreadTo(level, abovePos, fluidBE)) return;
        List<SubstanceStack> substancesToSpread = getSubstancesForSpread(fluidBE, spreadVolume);
        spreadTo(level, abovePos, substancesToSpread);

        for (SubstanceStack spreadStack : substancesToSpread) {
            fluidBE.removeSubstance(spreadStack, spreadStack.getAmount());
        }
    }


    private @NotNull List<SubstanceStack> getSubstancesForSpread(@NotNull SubstanceFluidBlockEntity fluidBE, float volume) {
        List<SubstanceStack> originalSubstances = fluidBE.getSubstances();
        List<SubstanceStack> spreadSubstances = new ArrayList<>();

        for (SubstanceStack stack : originalSubstances) {
            float proportion = stack.getVolume() / fluidBE.getVolume();
            float transferVolume = Math.min(volume * proportion, stack.getVolume());

            if (transferVolume > 0) {
                SubstanceStack spreadStack = stack.copy();
                spreadStack.setVolume(transferVolume);
                spreadSubstances.add(spreadStack);
            }
        }

        return spreadSubstances;
    }

    private @NotNull @Unmodifiable List<BlockPos> findNeighbors(Level level, BlockPos pos, SubstanceFluidBlockEntity fluidBE) {
        List<BlockPos> spreadPositions = new ArrayList<>();

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (canSpreadTo(level, pos.relative(direction), fluidBE)) {
                spreadPositions.add(pos.relative(direction));
            }
        }

        Collections.shuffle(spreadPositions);

        return spreadPositions;
    }

    private boolean canSpreadTo(@NotNull Level level, @NotNull BlockPos pos, SubstanceFluidBlockEntity fluidBE) {
        FluidState neighborFluidState = level.getFluidState(pos);
        if (!neighborFluidState.isEmpty() && !neighborFluidState.is(this)) return false;
        if (level.getBlockEntity(pos) instanceof SubstanceFluidBlockEntity neighborBE) {
            if (neighborBE.getVolume() > fluidBE.getVolume()) {
                return false;
            }
        }

        BlockState blockState = level.getBlockState(pos);
        Block block = blockState.getBlock();
        if (block instanceof LiquidBlockContainer liquidBlockContainer) {
            return liquidBlockContainer.canPlaceLiquid(null, level, pos, blockState, this);
        }

        return blockState.canBeReplaced();
    }

    private void equalizeSubstances(@NotNull Level level, BlockPos worldPosition, SubstanceFluidBlockEntity fluidBE) {
        // TODO: Doesn't handle data components yet

        List<SubstanceFluidBlockEntity> fluids = new ArrayList<>(5);
        fluids.add(fluidBE);

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = worldPosition.relative(direction);
            if (level.getBlockEntity(neighborPos) instanceof SubstanceFluidBlockEntity neighborBE) {
                fluids.add(neighborBE);
            }
        }

        if (fluids.size() < 2) return;

        Map<Substance, Float> totalsByType = new HashMap<>();
        for (SubstanceFluidBlockEntity be : fluids) {
            for (SubstanceStack stack : be.getSubstances()) {
                Substance key = stack.getSubstance();
                totalsByType.merge(key, stack.getAmount(), Float::sum);
            }
        }

        List<SubstanceStack> equalizedStacks = new ArrayList<>();
        for (var entry : totalsByType.entrySet()) {
            float equalizedAmount = entry.getValue() / fluids.size();
            SubstanceStack template = entry.getKey().toStack();
            if (template != null) {
                SubstanceStack newStack = template.copy();
                newStack.setAmount(equalizedAmount);
                equalizedStacks.add(newStack);
            }
        }

        // Apply to all fluid blocks
        for (SubstanceFluidBlockEntity be : fluids) {
            be.setSubstances(equalizedStacks.stream()
                    .map(SubstanceStack::copy)
                    .collect(Collectors.toList()));
        }
    }

    private void spreadTo(@NotNull LevelAccessor level, BlockPos pos, List<SubstanceStack> substances) {
        if (!level.getFluidState(pos).is(this)) {
            FluidState newState = defaultFluidState().setValue(LEVEL, 1);
            level.setBlock(pos, createLegacyBlock(newState), 3);
        }

        if (level.getBlockEntity(pos) instanceof SubstanceFluidBlockEntity spreadBE) {
            spreadBE.transferSubstances(substances);
        }
    }

    @Override
    public @NotNull Item getBucket() {
        return bucket.get();
    }

    @Override
    protected boolean canBeReplacedWith(@NotNull FluidState state, @NotNull BlockGetter level, @NotNull BlockPos pos,
                                        @NotNull Fluid fluid, @NotNull Direction direction) {
        return true;
    }

    @Override
    protected @NotNull Vec3 getFlow(@NotNull BlockGetter blockReader, @NotNull BlockPos pos, @NotNull FluidState fluidState) {
        return Vec3.ZERO;
    }

    @Override
    public int getTickDelay(@NotNull LevelReader level) {
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 0;
    }

    @Override
    public float getHeight(@NotNull FluidState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return state.getType().isSame(level.getFluidState(pos.above()).getType()) ? 1f : getOwnHeight(state);
    }

    @Override
    public float getOwnHeight(@NotNull FluidState state) {
        return 14 / 16f * state.getAmount() / 16;
    }

    @Override
    protected @NotNull BlockState createLegacyBlock(@NotNull FluidState state) {
        return SUBSTANCE_FLUID_BLOCK.get().defaultBlockState().setValue(BitterStateProperties.LEVEL,
                state.getValue(LEVEL));
    }

    @Override
    public boolean isSource(@NotNull FluidState state) {
        return true;
    }

    @Override
    public int getAmount(@NotNull FluidState state) {
        return state.getValue(LEVEL);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull FluidState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return shapes.computeIfAbsent(state, (fluidState) -> Shapes.box(0.0, 0.0, 0.0, 1.0,
                fluidState.getHeight(level, pos), 1.0));
    }

    @Override
    public @NotNull FluidType getFluidType() {
        return BitterFluidTypes.SUBSTANCE_FLUID_TYPE.get();
    }
}
