package com.site21.bittermelon.common.systems.fluid;

import com.site21.bittermelon.common.content.blocks.properties.BitterStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.site21.bittermelon.init.neoforge.BitterFluids.SUBSTANCE_FLUID;

public class SubstanceFluidBlock extends Block implements LiquidBlockContainer, EntityBlock {
    public static final IntegerProperty LEVEL = BitterStateProperties.LEVEL;
    private final List<FluidState> stateCache;
    private final SubstanceFluid fluid;

    public SubstanceFluidBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(LEVEL, 20));
        this.stateCache = new ArrayList<>();
        this.fluid = SUBSTANCE_FLUID.get();

        for (int i = 1; i < 20; i++) {
            stateCache.add(fluid.defaultFluidState().setValue(LEVEL, i));
        }

        stateCache.add(fluid.defaultFluidState());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SubstanceFluidBlockEntity(pos, state);
    }

    @Override
    protected @NotNull FluidState getFluidState(@NotNull BlockState state) {
        return stateCache.get(state.getValue(LEVEL) - 1);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull LevelReader level, @NotNull ScheduledTickAccess scheduledTickAccess, @NotNull BlockPos pos, @NotNull Direction direction, @NotNull BlockPos neighborPos, @NotNull BlockState neighborState, @NotNull RandomSource random) {
        return super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        level.scheduleTick(pos, state.getFluidState().getType(), fluid.getTickDelay(level));
    }

    @Override
    protected void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
        level.scheduleTick(pos, state.getFluidState().getType(), fluid.getTickDelay(level));
    }

    @Override
    public boolean canPlaceLiquid(@Nullable LivingEntity owner, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Fluid fluid) {
        return true;
    }

    @Override
    public boolean placeLiquid(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull FluidState fluidState) {
//        level.setBlock(pos, state.setValue(LEVEL, Math.clamp(state.getValue(LEVEL) + fluidState.getValue(LEVEL), 1, 16)), 3);
        level.setBlock(pos, state.setValue(LEVEL, fluidState.getAmount()), 3);
        return true;
    }
}
