package com.site21.bittermelon.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.site21.bittermelon.blocks.blockentities.ContainmentPanelBlockEntity;
import com.site21.bittermelon.blocks.blockentities.ThermometerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ThermometerBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING;
    private static final Map<Direction, VoxelShape> AABBS;

    public ThermometerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new ThermometerBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : (level0, state0, blockEntityType0, blockEntity) -> ((ThermometerBlockEntity) blockEntity).tick();
    }

    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return AABBS.get(state.getValue(FACING));
    }

    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockState blockstate = super.getStateForPlacement(context);
        BlockGetter blockgetter = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Direction[] adirection = context.getNearestLookingDirections();

        for(Direction direction : adirection) {
            if (direction.getAxis().isHorizontal()) {
                Direction direction1 = direction.getOpposite();
                assert blockstate != null;
                blockstate = blockstate.setValue(FACING, direction1);
                if (!blockgetter.getBlockState(blockpos.relative(direction)).canBeReplaced(context)) {
                    return blockstate;
                }
            }
        }

        return null;
    }

    protected @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    protected @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
        AABBS = Maps.newEnumMap(ImmutableMap.of(
                Direction.NORTH, Block.box(4.0, 4.0, 12.0, 12.0, 12.0, 16.0),
                Direction.SOUTH, Block.box(4.0, 4.0, 0.0, 12.0, 12.0, 4.0),
                Direction.EAST, Block.box(0.0, 4.0, 4.0, 4.0, 12.0, 12.0),
                Direction.WEST, Block.box(12.0, 4.0, 4.0, 16.0, 12.0, 12.0)
        ));
    }
}
