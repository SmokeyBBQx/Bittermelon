package com.site21.bittermelon.common.content.blocks.wallwriting;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.site21.bittermelon.common.content.items.writingutensils.WallWriter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static net.minecraft.world.item.Items.SPONGE;
import static net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock.canAttach;

public class WallWritingBlock extends Block implements EntityBlock, SimpleWaterloggedBlock {
    public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final Map<Direction, VoxelShape> AABBS;

    public WallWritingBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(FACE, AttachFace.WALL)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING, FACE, WATERLOGGED);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new WallWritingBlockEntity(blockPos, blockState);
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        AttachFace face = state.getValue(FACE);
        if (face == AttachFace.FLOOR) {
            return AABBS.get(Direction.UP);
        } else if (face == AttachFace.CEILING) {
            return AABBS.get(Direction.DOWN);
        } else {
            return AABBS.get(state.getValue(FACING));
        }
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        return canAttach(level, pos, getConnectedDirection(state).getOpposite());
    }

    protected static Direction getConnectedDirection(@NotNull BlockState state) {
        switch (state.getValue(FACE)) {
            case CEILING -> {
                return Direction.DOWN;
            }
            case FLOOR -> {
                return Direction.UP;
            }
            default -> {
                return state.getValue(FACING);
            }
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        for (Direction direction : context.getNearestLookingDirections()) {
            BlockState state;
            if (direction.getAxis() == Direction.Axis.Y) {
                state = defaultBlockState()
                        .setValue(FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR)
                        .setValue(FACING, context.getHorizontalDirection());
            } else {
                state = defaultBlockState()
                        .setValue(FACE, AttachFace.WALL)
                        .setValue(FACING, direction.getOpposite());
            }

            state.setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);

            if (state.canSurvive(context.getLevel(), context.getClickedPos())) {
                return state;
            }
        }

        return null;
    }

    @Override
    protected @NotNull InteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (stack.is(SPONGE)) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.SPONGE_HIT, SoundSource.BLOCKS, 1.0f, level.getRandom().nextFloat() * 0.1f + 0.9f);
            return InteractionResult.SUCCESS;
        }

        if (level.isClientSide) return InteractionResult.PASS;

        if (level.getBlockEntity(pos) instanceof WallWritingBlockEntity wallWriting && stack.getItem() instanceof WallWriter wallWriter) {
            if (wallWriter.tryApplyToWall(level, wallWriting, player, stack)) {
                level.sendBlockUpdated(pos, state, state, UPDATE_CLIENTS);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            }
        }

        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected @NotNull FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    static {
        AABBS = Maps.newEnumMap(ImmutableMap.<Direction, VoxelShape>builder()
                .put(Direction.NORTH, Block.box(0.0F, 4.5F, 15.9F, 16.0F, 12.5F, 16.0F))
                .put(Direction.SOUTH, Block.box(0.0F, 4.5F, 0.0F, 16.0F, 12.5F, 0.1F))
                .put(Direction.EAST, Block.box(0.0F, 4.5F, 0.0F, 0.1F, 12.5F, 16.0F))
                .put(Direction.WEST, Block.box(15.9F, 4.5F, 0.0F, 16.0F, 12.5F, 16.0F))
                .put(Direction.UP, Block.box(0.0F, 0.0F, 0.0F, 16.0F, 0.1F, 16.0F))
                .put(Direction.DOWN, Block.box(0.0F, 15.9F, 0.0F, 16.0F, 16.0F, 16.0F))
                .build());
    }
}
