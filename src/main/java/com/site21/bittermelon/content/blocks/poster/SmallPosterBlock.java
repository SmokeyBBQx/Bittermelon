package com.site21.bittermelon.content.blocks.poster;

import com.site21.bittermelon.content.blocks.properties.BitterStateProperties;
import com.site21.bittermelon.content.blocks.properties.Placement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SmallPosterBlock extends Block implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Placement> PLACEMENT = BitterStateProperties.PLACEMENT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final Map<Direction, Map<Placement, VoxelShape>> SHAPES;

    public SmallPosterBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(PLACEMENT, Placement.LEFT)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING, PLACEMENT, WATERLOGGED);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Direction direction = state.getValue(FACING);
        Placement placement = state.getValue(PLACEMENT);
        return SHAPES.get(direction).get(placement);
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        if (stateForPlacement == null) return null;
        Direction horizontalDirection = context.getHorizontalDirection();
        Placement placement = getPlacement(context, horizontalDirection);

        return stateForPlacement
                .setValue(FACING, horizontalDirection)
                .setValue(PLACEMENT, placement)
                .setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == net.minecraft.world.level.material.Fluids.WATER);
    }

    private static @NotNull Placement getPlacement(@NotNull BlockPlaceContext context, @NotNull Direction horizontalDirection) {
        Placement placement;
        Vec3 clickLocation = context.getClickLocation();
        BlockPos clickedPos = context.getClickedPos();

        double x = clickLocation.x - clickedPos.getX();
        double z = clickLocation.z - clickedPos.getZ();

        placement = switch (horizontalDirection) {
            case SOUTH -> x > 0.5 ? Placement.LEFT : Placement.RIGHT;
            case NORTH -> x < 0.5 ? Placement.LEFT : Placement.RIGHT;
            case WEST -> z > 0.5 ? Placement.LEFT : Placement.RIGHT;
            case EAST -> z < 0.5 ? Placement.LEFT : Placement.RIGHT;
            default -> Placement.RIGHT;
        };
        return placement;
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    static {
        SHAPES = Map.of(
                Direction.NORTH, Map.of(
                        Placement.LEFT, Block.box(1.5, 0.5, 0, 9.5, 11.5, 1),
                        Placement.RIGHT, Block.box(6.5, 0.5, 0, 14.5, 11.5, 1)
                ),
                Direction.EAST, Map.of(
                        Placement.LEFT, Block.box(15, 0.5, 1.5, 16, 11.5, 9.5),
                        Placement.RIGHT, Block.box(15, 0.5, 6.5, 16, 11.5, 14.5)
                ),
                Direction.SOUTH, Map.of(
                        Placement.LEFT, Block.box(6.5, 0.5, 15, 14.5, 11.5, 16),
                        Placement.RIGHT, Block.box(1.5, 0.5, 15, 9.5, 11.5, 16)
                ),
                Direction.WEST, Map.of(
                        Placement.LEFT, Block.box(0, 0.5, 6.5, 1, 11.5, 14.5),
                        Placement.RIGHT, Block.box(0, 0.5, 1.5, 1, 11.5, 9.5)
                )
        );
    }
}
