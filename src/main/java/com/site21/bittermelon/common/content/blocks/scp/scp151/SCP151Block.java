package com.site21.bittermelon.common.content.blocks.scp.scp151;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class SCP151Block extends Block implements SimpleWaterloggedBlock, EntityBlock {
    public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final Map<Direction, VoxelShape> AABBS;

    public SCP151Block(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(FACE, AttachFace.WALL)
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACE, FACING, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        if (stateForPlacement == null) return null;

        AttachFace face = switch (context.getClickedFace().getOpposite()) {
            case UP -> AttachFace.CEILING;
            case DOWN -> AttachFace.FLOOR;
            default -> AttachFace.WALL;
        };

        return stateForPlacement
                .setValue(FACE, face)
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new SCP151BlockEntity(blockPos, blockState);
    }

    @javax.annotation.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : (level0, state0, blockEntityType0, blockEntity) -> ((SCP151BlockEntity) blockEntity).tick();
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Direction facing = state.getValue(FACING);
        AttachFace face = state.getValue(FACE);

        if (face == AttachFace.FLOOR) {
            return AABBS.get(Direction.DOWN);
        } else if (face == AttachFace.CEILING) {
            return AABBS.get(Direction.UP);
        } else {
            return AABBS.get(facing);
        }
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ?
                Fluids.WATER.getSource(false) :
                super.getFluidState(state);
    }

    static {
        AABBS = Map.of(
                Direction.NORTH, Block.box(0.0F, 0.0F, 15.0F, 16.0F, 16.0F, 16.0F),
                Direction.SOUTH, Block.box(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 1.0F),
                Direction.EAST, Block.box(0.0F, 0.0F, 0.0F, 1.0F, 16.0F, 16.0F),
                Direction.WEST, Block.box(15.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F),
                Direction.UP, Block.box(0.0F, 15.0F, 0.0F, 16.0F, 16.0F, 16.0F),
                Direction.DOWN, Block.box(0.0F, 0.0F, 0.0F, 16.0F, 1.0F, 16.0F)
        );
    }
}
