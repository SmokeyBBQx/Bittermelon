package com.site21.bittermelon.common.content.blocks.lights;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
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

public class CageLampBlock extends Block implements SimpleWaterloggedBlock {
    public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final Map<Direction, VoxelShape> SHAPES;

    public CageLampBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACE, AttachFace.WALL)
                .setValue(FACING, Direction.NORTH)
                .setValue(LIT, false)
                .setValue(WATERLOGGED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACE, FACING, LIT, WATERLOGGED);
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        AttachFace face = state.getValue(FACE);

        if (face == AttachFace.WALL) {
            return SHAPES.get(state.getValue(FACING));
        } else if (face == AttachFace.FLOOR) {
            return SHAPES.get(Direction.UP);
        } else {
            return SHAPES.get(Direction.DOWN);
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        AttachFace face = switch (context.getClickedFace()) {
            case DOWN -> AttachFace.CEILING;
            case UP -> AttachFace.FLOOR;
            default -> AttachFace.WALL;
        };

        return defaultBlockState()
                .setValue(FACE, face)
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    protected @NotNull FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (!state.getValue(LIT)) return;

        if (random.nextInt(5) == 0) {
            level.addParticle(
                    ParticleTypes.MYCELIUM,
                    pos.getX() + random.nextDouble(),
                    pos.getY() + 0.5,
                    pos.getZ() + random.nextDouble(),
                    0.0,
                    0.0,
                    0.0
            );
        }
    }

    static {
        SHAPES = Maps.newEnumMap(ImmutableMap.of(
                Direction.NORTH, Block.box(4.0F, 4.0F, 8.0F, 12.0F, 12.0F, 16.0F),
                Direction.SOUTH, Block.box(4.0F, 4.0F, 0.0F, 12.0F, 12.0F, 8.0F),
                Direction.EAST, Block.box(0.0F, 4.0F, 4.0F, 8.0F, 12.0F, 12.0F),
                Direction.WEST, Block.box(8.0F, 4.0F, 4.0F, 16.0F, 12.0F, 12.0F),
                Direction.UP, Block.box(4.0F, 0.0F, 4.0F, 12.0F, 8.0F, 12.0F),
                Direction.DOWN, Block.box(4.0F, 8.0F, 4.0F, 12.0F, 16.0F, 12.0F)
        ));
    }
}
