package com.site21.bittermelon.common.systems.fluid;

import com.google.common.collect.Maps;
import com.site21.bittermelon.common.content.blocks.properties.BitterStateProperties;
import com.site21.bittermelon.common.systems.blockdamage.BlockDamageUtil;
import com.site21.bittermelon.common.systems.substance.Substance;
import com.site21.bittermelon.common.systems.substance.SubstanceStack;
import com.site21.bittermelon.init.neoforge.BitterFluidTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.Profiler;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.site21.bittermelon.init.neoforge.BitterBlocks.SUBSTANCE_FLUID_BLOCK;

public class SubstanceFluid extends Fluid {
    public static final IntegerProperty LEVEL = BitterStateProperties.LEVEL;
    public static final int FULL_BLOCK_VOLUME = 1000;
    private static final int SPREAD_THRESHOLD = 100;
    private static final int PRESSURE_THRESHOLD = 100;
    private static final float DOWNWARDS_SPREAD_RATIO = 0.85f;

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
        Profiler.get().push("SubstanceFluidTick");

        if (level.getBlockEntity(pos) instanceof SubstanceFluidBlockEntity fluidBE) {
            if (fluidBE.getVolume() <= 0) {
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                return;
            }

            if (spreadDownwards(level, pos, fluidBE)) return;

            int volume = fluidBE.getVolume();
            if (volume > SPREAD_THRESHOLD) {
                if (!spreadHorizontally(level, pos, fluidBE, volume)) {
                    if (volume > FULL_BLOCK_VOLUME) {
                        spreadUpwards(level, pos, fluidBE);
                    }
                }
            }
        }

        Profiler.get().pop();
    }

    @Override
    protected void randomTick(ServerLevel level, BlockPos pos, FluidState state, RandomSource random) {
        if (level.getBlockEntity(pos) instanceof SubstanceFluidBlockEntity fluidBE) {
            exertPressure(fluidBE, level);
        }
    }

    @Override
    protected boolean isRandomlyTicking() {
        return true;
    }

    @Override
    protected void animateTick(Level level, BlockPos pos, FluidState state, RandomSource random) {
        super.animateTick(level, pos, state, random);
    }

    private boolean spreadDownwards(@NotNull Level level, @NotNull BlockPos pos, @NotNull SubstanceFluidBlockEntity fluidBE) {
        // Check if the fluid below is the same type and not full
        // If not, spread downwards

        List<SubstanceStack> substances = fluidBE.getSubstances();

        if (substances.isEmpty()) return false;
        if (!canSpreadTo(level, pos.below(), fluidBE)) return false;

        spreadTo(level, pos.below(), substances);
        fluidBE.setSubstances(new ArrayList<>());
        return true;
    }

    private boolean spreadHorizontally(@NotNull Level level, BlockPos pos, @NotNull SubstanceFluidBlockEntity fluidBE, int volume) {
        Profiler.get().push("spreadHorizontally");

        List<BlockPos> spreadPositions = new ArrayList<>(4);
        getDownwardSpreadPositions(level, pos, fluidBE, spreadPositions);
        boolean downwardSpread = !spreadPositions.isEmpty();

        if (!downwardSpread) {
            getSpreadPositions(level, pos, fluidBE, spreadPositions);
            if (spreadPositions.isEmpty()) {
                Profiler.get().pop();
                return false;
            }
        }

        int spreadCount = spreadPositions.size();
        List<SubstanceStack> substancesToSpread = getSubstancesForSpread(fluidBE, spreadCount, downwardSpread);

        if (substancesToSpread.isEmpty()) {
            Profiler.get().pop();
            return false;
        }

        for (BlockPos spreadPos : spreadPositions) {
            spreadTo(level, spreadPos, substancesToSpread);
        }

        for (SubstanceStack spreadStack : substancesToSpread) {
            int totalRemoved = spreadStack.getAmount() * spreadCount;
            fluidBE.removeSubstance(spreadStack, totalRemoved);
        }

        equalizeSubstances(level, pos, fluidBE);

        Profiler.get().pop();
        return true;
    }

    private List<SubstanceStack> getSubstancesForSpread(SubstanceFluidBlockEntity fluidBE, int spreadCount, boolean downwardSpread) {
        int sourceVolume = fluidBE.getVolume();
        int spreadVolume = downwardSpread ?
                Math.round(sourceVolume * DOWNWARDS_SPREAD_RATIO) / spreadCount :
                sourceVolume / (spreadCount + 1);

        return spreadSubstancesByVolume(
                fluidBE.getSubstances(),
                spreadVolume,
                sourceVolume
        );
    }

    private void getDownwardSpreadPositions(Level level, BlockPos pos, SubstanceFluidBlockEntity fluidBE,
                                                      List<BlockPos> positions) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = pos.relative(direction);
            if (level.getBlockState(neighborPos).canBeReplaced() && canSpreadTo(level, neighborPos.below(), fluidBE)) {
                positions.add(neighborPos);
            }
        }
    }

    private void getSpreadPositions(Level level, BlockPos pos, SubstanceFluidBlockEntity fluidBE,
                                              List<BlockPos> positions) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = pos.relative(direction);
            if (canSpreadTo(level, neighborPos, fluidBE)) {
                positions.add(neighborPos);
            }
        }
    }

    private void spreadUpwards(@NotNull Level level, @NotNull BlockPos pos, @NotNull SubstanceFluidBlockEntity fluidBE) {
        int spreadVolume = fluidBE.getVolume() - FULL_BLOCK_VOLUME;
        if (spreadVolume <= 0) return;

        BlockPos abovePos = pos.above();

        if (level.getFluidState(abovePos).is(this) || level.getBlockState(abovePos).canBeReplaced()) {
            List<SubstanceStack> substancesToSpread = spreadSubstancesByVolume(fluidBE.getSubstances(), spreadVolume,
                    fluidBE.getVolume());
            if (substancesToSpread.isEmpty()) return;

            spreadTo(level, abovePos, substancesToSpread);

            for (SubstanceStack spreadStack : substancesToSpread) {
                fluidBE.removeSubstance(spreadStack, spreadStack.getAmount());
            }
        }
    }

//    private @NotNull List<SubstanceStack> getSubstancesForSpread(@NotNull SubstanceFluidBlockEntity fluidBE, int volume) {
//        List<SubstanceStack> originalSubstances = fluidBE.getSubstances();
//        List<SubstanceStack> spreadSubstances = new ArrayList<>();
//
//        for (SubstanceStack stack : originalSubstances) {
//            float proportion = (float) stack.getVolume() / fluidBE.getVolume();
//            int transferVolume = Math.min((int)(volume * proportion), stack.getVolume());
//
//            if (transferVolume > 0) {
//                SubstanceStack spreadStack = stack.copy();
//                spreadStack.setVolume(transferVolume);
//                spreadSubstances.add(spreadStack);
//            }
//        }
//
//        return spreadSubstances;
//    }

    private List<SubstanceStack> spreadSubstances(List<SubstanceStack> substances, int spreadCount) {
        List<SubstanceStack> spreadStacks = new ArrayList<>();

        for (SubstanceStack stack : substances) {
            int amount = stack.getAmount() / spreadCount;

            if (amount > 0) {
                SubstanceStack spreadStack = stack.copy();
                spreadStack.setAmount(amount);
                spreadStacks.add(spreadStack);
            }
        }

        return spreadStacks;
    }

    private List<SubstanceStack> spreadSubstancesByVolume(List<SubstanceStack> substances, int transferVolume, int totalVolume) {
        List<SubstanceStack> spreadStacks = new ArrayList<>();
        float ratio = (float) transferVolume / totalVolume;

        for (SubstanceStack stack : substances) {
            int amount = (int) (stack.getAmount() * ratio);

            if (amount > 0) {
                SubstanceStack spreadStack = stack.copy();
                spreadStack.setAmount(amount);
                spreadStacks.add(spreadStack);
            }
        }

        return spreadStacks;
    }

    private boolean canSpreadTo(@NotNull Level level, @NotNull BlockPos pos, SubstanceFluidBlockEntity fluidBE) {
        // Check if the neighbor fluid state is empty or same type
        FluidState neighborFluidState = level.getFluidState(pos);
        if (!neighborFluidState.isEmpty() && !neighborFluidState.is(this)) return false;

        if (level.getBlockEntity(pos) instanceof SubstanceFluidBlockEntity neighborBE) {
            return neighborBE.getVolume() < FULL_BLOCK_VOLUME;
        }

        // Check if the block can be replaced by this fluid
        BlockState blockState = level.getBlockState(pos);
        if (blockState.getBlock() instanceof LiquidBlockContainer liquidBlockContainer) {
            return liquidBlockContainer.canPlaceLiquid(null, level, pos, blockState, this);
        }

        return blockState.canBeReplaced();
    }


    private void equalizeSubstances(@NotNull Level level, BlockPos worldPosition, SubstanceFluidBlockEntity fluidBE) {
        Profiler.get().push("equalizeSubstances");

        List<SubstanceFluidBlockEntity> fluids = new ArrayList<>(5);
        fluids.add(fluidBE);

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = worldPosition.relative(direction);
            if (level.getBlockEntity(neighborPos) instanceof SubstanceFluidBlockEntity neighborBE) {
                fluids.add(neighborBE);
            }
        }

        if (fluids.size() < 2) {
            Profiler.get().pop();
            return;
        }

        int totalVolume = 0;
        for (SubstanceFluidBlockEntity be : fluids) {
            totalVolume += be.getVolume();
        }

        int fluidCount = fluids.size();
        int averageVolume = totalVolume / fluidCount;

        boolean needsEqualization = false;
        for (SubstanceFluidBlockEntity be : fluids) {
            if (be.getVolume() != averageVolume) {
                needsEqualization = true;
                break;
            }
        }

        if (!needsEqualization) {
            Profiler.get().pop();
            return;
        }

        Map<Substance, Integer> totalsByType = new HashMap<>();
        for (SubstanceFluidBlockEntity be : fluids) {
            for (SubstanceStack stack : be.getSubstances()) {
                totalsByType.merge(stack.getSubstance(), stack.getAmount(), Integer::sum);
            }
        }

        for (int i = 0; i < fluidCount; i++) {
            SubstanceFluidBlockEntity be = fluids.get(i);
            List<SubstanceStack> newStacks = new ArrayList<>(totalsByType.size());

            for (var entry : totalsByType.entrySet()) {
                int totalAmount = entry.getValue();
                int baseAmount = totalAmount / fluidCount;
                int remainder = totalAmount % fluidCount;
                int amount = baseAmount + (i < remainder ? 1 : 0);

                if (amount > 0) {
                    SubstanceStack newStack = entry.getKey().toStack();
                    if (newStack != null) {
                        newStack.setAmount(amount);
                        newStacks.add(newStack);
                    }
                }
            }

            be.setSubstances(newStacks);
        }

        Profiler.get().pop();
    }

    private void spreadTo(@NotNull LevelAccessor level, BlockPos pos, List<SubstanceStack> substances) {
        if (!level.getFluidState(pos).is(this)) {
            FluidState newState = defaultFluidState().setValue(LEVEL, 1);
            level.setBlock(pos, createLegacyBlock(newState), Block.UPDATE_CLIENTS);
            playFlowSound(level, pos, level.getRandom());
        }

        if (level.getBlockEntity(pos) instanceof SubstanceFluidBlockEntity spreadBE) {
            spreadBE.transferSubstances(substances);
        }
    }

    private void exertPressure(SubstanceFluidBlockEntity fluidBE, LevelAccessor level) {
        if (fluidBE.getPressure() >= PRESSURE_THRESHOLD) {
            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = fluidBE.getBlockPos().relative(direction);
                if (level.getBlockState(neighborPos).canBeReplaced()) continue;

                BlockDamageUtil.addDamage(level, neighborPos, fluidBE.getPressure() / 100);
            }
        }
    }

    private void playFlowSound(LevelAccessor level, BlockPos pos, RandomSource random) {
        level.playSound(
                null,
                pos,
                SoundEvents.WATER_AMBIENT,
                SoundSource.AMBIENT,
                random.nextFloat() * 0.25F + 0.75F,
                random.nextFloat() + 0.5F
        );
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
        return state.getAmount() / 20.0f;
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
