package com.site21.bittermelon.common.systems.fluid;

import com.google.common.collect.Maps;
import com.site21.bittermelon.common.content.blocks.properties.BitterStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.site21.bittermelon.init.neoforge.BitterBlocks.SIMPLE_FLUID_BLOCK;
import static com.site21.bittermelon.init.neoforge.BitterFluidTypes.SIMPLE_FLUID_TYPE;


public class SimpleFluid extends Fluid {
    public static final IntegerProperty LEVEL = BitterStateProperties.LEVEL;

    private final Map<FluidState, VoxelShape> shapes = Maps.newIdentityHashMap();
    private final Supplier<? extends BucketItem> bucket;

    public SimpleFluid(Supplier<? extends BucketItem> bucket) {
        this.bucket = bucket;
        registerDefaultState(getStateDefinition().any().setValue(LEVEL, 20));
    }

    @Override
    protected void createFluidStateDefinition(StateDefinition.@NotNull Builder<Fluid, FluidState> builder) {
        builder.add(LEVEL);
    }

    @Override
    public @NotNull Item getBucket() {
        return bucket.get();
    }

    @Override
    public @NotNull FluidType getFluidType() {
        return SIMPLE_FLUID_TYPE.get();
    }

    @Override
    protected @NotNull BlockState createLegacyBlock(@NotNull FluidState state) {
        return SIMPLE_FLUID_BLOCK.get().defaultBlockState().setValue(LEVEL, state.getValue(LEVEL));
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
    public float getHeight(@NotNull FluidState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return state.getType().isSame(level.getFluidState(pos.above()).getType()) ? 1f : getOwnHeight(state);
    }

    @Override
    public float getOwnHeight(@NotNull FluidState state) {
        return (float) state.getValue(LEVEL) / 21.0F;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull FluidState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return shapes.computeIfAbsent(state, (fluidState) -> Shapes.box(0.0, 0.0, 0.0, 1.0,
                fluidState.getHeight(level, pos), 1.0));
    }

    @Override
    protected boolean canBeReplacedWith(@NotNull FluidState state, @NotNull BlockGetter level, @NotNull BlockPos pos,
                                        @NotNull Fluid fluid, @NotNull Direction direction) {
        return direction == Direction.DOWN && !isSame(fluid);
    }

    @Override
    protected @NotNull Vec3 getFlow(@NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull FluidState state) {
        double flowX = 0.0;
        double flowZ = 0.0;
        BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();

        // Calculate flow based on height differences with neighboring fluid blocks
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            neighborPos.setWithOffset(pos, direction);
            FluidState neighborState = level.getFluidState(neighborPos);

            if (isSame(neighborState.getType())) {
                float neighborHeight = neighborState.getOwnHeight();
                float heightDrop = 0.0f;
                if (neighborHeight < state.getOwnHeight()) {
                    heightDrop = state.getOwnHeight() - neighborHeight;
                }

                flowX += (double) direction.getStepX() * heightDrop;
                flowZ += (double) direction.getStepZ() * heightDrop;
            }
        }

        // If the flow is very small, return zero
        if (flowX * flowX + flowZ * flowZ < 0.01) {
            return Vec3.ZERO;
        }

        Vec3 flowDirection = new Vec3(flowX, 0.0, flowZ);

        return flowDirection.normalize();
    }

    @Override
    public int getTickDelay(@NotNull LevelReader level) {
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F;
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState blockState, @NotNull FluidState fluidState) {
        if (spreadDownwards(level, pos, fluidState)) {
            return;
        }

        if (!spreadHorizontally(level, pos, fluidState)) {
            if (fluidState.getValue(LEVEL) > 20) {
                spreadUpwards(level, pos, fluidState);
            }
        }
        level.scheduleTick(pos, this, getTickDelay(level));
    }

    private boolean spreadDownwards(LevelAccessor level, BlockPos pos, FluidState state) {
        BlockPos belowPos = pos.below();
        if (canSpreadTo(level, belowPos)) {
            level.setBlock(belowPos, createLegacyBlock(state.setValue(LEVEL, state.getValue(LEVEL))), Block.UPDATE_ALL);
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            return true;
        }
        return false;
    }

    private boolean spreadHorizontally(Level level, BlockPos pos, FluidState state) {
        List<BlockPos> spreadPositions = new ArrayList<>();

        getDownwardSpreadPositions(level, pos, spreadPositions);
        boolean downwardSpread = !spreadPositions.isEmpty();
        if (!downwardSpread) {
            getSpreadPositions(level, pos, spreadPositions);
            if (spreadPositions.isEmpty()) return false;
        }

        int spreadCount = spreadPositions.size();
        int distributedLevel = downwardSpread
                ? (state.getValue(LEVEL) - 1) / spreadCount
                : state.getValue(LEVEL) / (spreadCount + 1);

        if (distributedLevel <= 0) return false;

        for (BlockPos spreadPos : spreadPositions) {
            if (level.getFluidState(spreadPos) instanceof FluidState existing && existing.is(this)) {
                int newLevel = Mth.clamp(existing.getValue(LEVEL) + distributedLevel, 1, 20);
                level.setBlock(spreadPos, createLegacyBlock(state.setValue(LEVEL, newLevel)), Block.UPDATE_ALL);
            } else {
                level.setBlock(spreadPos, createLegacyBlock(state.setValue(LEVEL, distributedLevel)), Block.UPDATE_ALL);
            }
        }

        level.setBlock(pos, createLegacyBlock(state.setValue(LEVEL,
                state.getValue(LEVEL) - distributedLevel * spreadCount)), Block.UPDATE_ALL);

        equalizeFluid(level, pos, state);
        return true;
    }

    private void getDownwardSpreadPositions(Level level, BlockPos pos, List<BlockPos> positions) {
        BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            neighborPos.setWithOffset(pos, direction);
            if (level.getBlockState(neighborPos).canBeReplaced() && canSpreadTo(level, neighborPos.below())) {
                positions.add(neighborPos);
            }
        }
    }

    private void getSpreadPositions(Level level, BlockPos pos, List<BlockPos> positions) {
        BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            neighborPos.setWithOffset(pos, direction);
            if (canSpreadTo(level, neighborPos)) {
                positions.add(neighborPos);
            }
        }
    }

    private void spreadUpwards(LevelAccessor level, BlockPos pos, FluidState state) {

    }

    private boolean canSpreadTo(LevelAccessor level, BlockPos pos) {
        FluidState fluidState = level.getFluidState(pos);
        if (fluidState.isEmpty()) {
            BlockState blockState = level.getBlockState(pos);
            return blockState.canBeReplaced(this);
        }
        if (fluidState.is(this)) {
            return fluidState.getValue(LEVEL) < 20;
        }
        return false;
    }

    private void equalizeFluid(Level level, BlockPos pos, FluidState state) {
        List<BlockPos> connectedPositions = new ArrayList<>();
        connectedPositions.add(pos);
        BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();

        for (Direction dir : Direction.Plane.HORIZONTAL) {
            neighborPos.setWithOffset(pos, dir);
            FluidState neighborState = level.getFluidState(neighborPos);
            boolean aboveThreshold = Math.abs(state.getAmount() - neighborState.getAmount()) > 1;

            if (neighborState.is(this) && aboveThreshold) {
                connectedPositions.add(neighborPos);
            }
        }

        if (connectedPositions.size() <= 1) return;

        int totalLevel = 0;
        for (BlockPos cellPos : connectedPositions) {
            FluidState cellState = level.getFluidState(cellPos);
            if (!cellState.is(this)) return;
            totalLevel += cellState.getValue(LEVEL);
        }

        int count = connectedPositions.size();
        int baseLevel = totalLevel / count;
        int remainderLevel = totalLevel % count;

        for (int i = 0; i < count; i++) {
            int redistributedLevel = Mth.clamp(baseLevel + (i < remainderLevel ? 1 : 0), 1, 20);
            level.setBlock(
                    connectedPositions.get(i),
                    createLegacyBlock(defaultFluidState().setValue(LEVEL, redistributedLevel)),
                    Block.UPDATE_ALL
            );
        }
    }
}
