package com.site21.bittermelon.content.blocks.scp.scp151;

import com.site21.bittermelon.content.blocks.powergrid.distributionboard.DistributionBoardBlock;
import com.site21.bittermelon.content.blocks.powergrid.distributionboard.DistributionBoardBlockEntity;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SCP151Block extends Block implements SimpleWaterloggedBlock, EntityBlock {
    public static final EnumProperty<SCP151Block.Type> TYPE = EnumProperty.create("type", SCP151Block.Type.class);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape SHAPE_EAST;
    protected static final VoxelShape SHAPE_WEST;
    protected static final VoxelShape SHAPE_SOUTH;
    protected static final VoxelShape SHAPE_NORTH;
    protected static final VoxelShape SHAPE_DOWN;
    protected static final VoxelShape SHAPE_UP;

    public SCP151Block(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(TYPE, SCP151Block.Type.SIDE)
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(TYPE, FACING, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        if (stateForPlacement == null) return null;

        SCP151Block.Type type = switch (context.getClickedFace().getOpposite()) {
            case UP -> SCP151Block.Type.TOP;
            case DOWN -> SCP151Block.Type.BOTTOM;
            default -> SCP151Block.Type.SIDE;
        };

        return stateForPlacement
                .setValue(TYPE, type)
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
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
        SCP151Block.Type type = state.getValue(TYPE);

        if (type == SCP151Block.Type.SIDE) {
            return switch (facing) {
                case SOUTH -> SHAPE_SOUTH;
                case WEST -> SHAPE_WEST;
                case EAST -> SHAPE_EAST;
                default -> SHAPE_NORTH;
            };
        } else {
            return type == SCP151Block.Type.TOP ? SHAPE_UP : SHAPE_DOWN;
        }
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ?
                Fluids.WATER.getSource(false) :
                super.getFluidState(state);
    }

    static {
        SHAPE_EAST = Block.box(0.0F, 0.0F, 0.0F, 1.0F, 16.0F, 16.0F);
        SHAPE_WEST = Block.box(15.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F);
        SHAPE_SOUTH = Block.box(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 1.0F);
        SHAPE_NORTH = Block.box(0.0F, 0.0F, 15.0F, 16.0F, 16.0F, 16.0F);
        SHAPE_DOWN = Block.box(0.0F, 0.0F, 0.0F, 16.0F, 1.0F, 16.0F);
        SHAPE_UP = Block.box(0.0F, 15.0F, 0.0F, 16.0F, 16.0F, 16.0F);
    }

    public enum Type implements StringRepresentable {
        TOP("top"),
        BOTTOM("bottom"),
        SIDE("side");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
