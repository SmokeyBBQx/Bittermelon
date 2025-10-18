package com.site21.bittermelon.common.content.blocks.electronics.slidingdoor;

import com.site21.bittermelon.common.content.blocks.DoorHelper;
import com.site21.bittermelon.common.content.blocks.properties.Placement;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class SlidingDoorBlock extends Block implements EntityBlock {
    public static final EnumProperty<Direction> FACING;
    public static final BooleanProperty OPEN;
    public static final BooleanProperty VISIBLE;
    public static final EnumProperty<DoorHingeSide> HINGE;
    public static final EnumProperty<DoubleBlockHalf> HALF;

    private static final Map<Direction, Map<Placement, VoxelShape>> SHAPES;
    protected static final VoxelShape NORTH_SOUTH_AABB;
    protected static final VoxelShape EAST_WEST_AABB;

    public SlidingDoorBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPEN, false)
                .setValue(VISIBLE, true)
                .setValue(HINGE, DoorHingeSide.LEFT)
                .setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN, VISIBLE, HINGE, HALF);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new SlidingDoorBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return (level0, pos, state0, blockEntity) -> {
            if (level.isClientSide) {
                SlidingDoorBlockEntity.clientTick(level0, pos, state0, (SlidingDoorBlockEntity) blockEntity);
            } else {
                SlidingDoorBlockEntity.serverTick(level0, pos, state0, (SlidingDoorBlockEntity) blockEntity);
            }
        };
    }

    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        if (!state.getValue(OPEN)) {
            return switch (state.getValue(FACING)) {
                case NORTH, SOUTH -> NORTH_SOUTH_AABB;
                case EAST, WEST -> EAST_WEST_AABB;
                default -> throw new IllegalStateException("Unexpected value: " + state.getValue(FACING));
            };
        } else {
            Direction facing = state.getValue(FACING);
            Placement placement = state.getValue(HINGE) == DoorHingeSide.LEFT ? Placement.LEFT : Placement.RIGHT;
            return SHAPES.get(facing).get(placement);
        }
    }

    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        if (pos.getY() >= level.getMaxY() - 1) return null;

        BlockState upperBlockState = level.getBlockState(pos.above());
        if (!upperBlockState.canBeReplaced(context)) return null;

        return defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(HINGE, getHinge(context))
                .setValue(HALF, DoubleBlockHalf.LOWER);
    }

    // Adapted from net.minecraft.world.level.block.DoorBlock.getHinge
    private DoorHingeSide getHinge(@NotNull BlockPlaceContext context) {
        BlockGetter level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getHorizontalDirection();

        BlockPos upperPos = pos.above();
        Direction leftDir = facing.getCounterClockWise();
        Direction rightDir = facing.getClockWise();

        BlockPos leftPos = pos.relative(leftDir);
        BlockPos upperLeftPos = upperPos.relative(leftDir);
        BlockPos rightPos = pos.relative(rightDir);
        BlockPos upperRightPos = upperPos.relative(rightDir);

        BlockState leftState = level.getBlockState(leftPos);
        BlockState upperLeftState = level.getBlockState(upperLeftPos);
        BlockState rightState = level.getBlockState(rightPos);
        BlockState upperRightState = level.getBlockState(upperRightPos);

        int collisionBias = 0;
        if (leftState.isCollisionShapeFullBlock(level, leftPos)) collisionBias--;
        if (upperLeftState.isCollisionShapeFullBlock(level, upperLeftPos)) collisionBias--;
        if (rightState.isCollisionShapeFullBlock(level, rightPos)) collisionBias++;
        if (upperRightState.isCollisionShapeFullBlock(level, upperRightPos)) collisionBias++;

        boolean leftDoor = leftState.getBlock() instanceof DoorBlock &&
                leftState.getValue(HALF) == DoubleBlockHalf.LOWER;
        boolean rightDoor = rightState.getBlock() instanceof DoorBlock &&
                rightState.getValue(HALF) == DoubleBlockHalf.LOWER;

        if (leftDoor && !rightDoor && collisionBias > 0) {
            return DoorHingeSide.RIGHT;
        }

        if (rightDoor && !leftDoor && collisionBias < 0) {
            return DoorHingeSide.LEFT;
        }

        Vec3 clickPos = context.getClickLocation();
        double relativeX = clickPos.x - pos.getX();
        double relativeZ = clickPos.z - pos.getZ();

        int stepX = facing.getStepX();
        int stepZ = facing.getStepZ();

        boolean useRightHinge = (stepX < 0 && relativeZ < 0.5) ||
                (stepX > 0 && relativeZ > 0.5) ||
                (stepZ < 0 && relativeX > 0.5) ||
                (stepZ > 0 && relativeX < 0.5);

        return useRightHinge ? DoorHingeSide.RIGHT : DoorHingeSide.LEFT;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (DoorHelper.handleKnocking(level, player)) return InteractionResult.SUCCESS;

        if (level.getBlockEntity(pos) instanceof SlidingDoorBlockEntity blockEntity) {
            if (!blockEntity.isOn() && !state.getValue(OPEN)) {
                level.gameEvent(player, GameEvent.BLOCK_OPEN, pos);
                setOpen(level, pos, true);
                return InteractionResult.SUCCESS;
            } else if (blockEntity.isOn()) {
                player.displayClientMessage(Component.literal("The door's motors prevent you from opening it by hand.")
                                .withStyle(ChatFormatting.ITALIC)
                                .withStyle(ChatFormatting.GRAY),
                        true);
                return InteractionResult.PASS;
            }
        }

        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    public void setOpen(@NotNull Level level, BlockPos pos, boolean open) {
        playSound(null, level, pos, open);

        BlockState currentState = level.getBlockState(pos);
        BlockPos otherHalfPos = currentState.getValue(HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos.above();
        BlockState otherHalfState = level.getBlockState(otherHalfPos);

        currentState = currentState.setValue(OPEN, open).setValue(VISIBLE, false);
        otherHalfState = otherHalfState.setValue(OPEN, open).setValue(VISIBLE, false);

        level.setBlock(pos, currentState, 3);
        level.setBlock(otherHalfPos, otherHalfState, 3);
    }

    private void playSound(@Nullable Entity source, @NotNull Level level, BlockPos pos, boolean isOpening) {
        level.playSound(source, pos, BitterSounds.SLIDING_DOOR_CLOSE.value(), SoundSource.BLOCKS, 1.0f, 1.1f);
    }

    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, LivingEntity placer, @NotNull ItemStack stack) {
        level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = level.getBlockState(blockpos);
        return state.getValue(HALF) == DoubleBlockHalf.LOWER ? blockstate.isFaceSturdy(level, blockpos, Direction.UP) : blockstate.is(this);
    }

    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        if (state.getValue(VISIBLE)) {
            return RenderShape.MODEL;
        } else {
            return RenderShape.INVISIBLE;
        }
    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
        OPEN = BlockStateProperties.OPEN;
        VISIBLE = BooleanProperty.create("visible");
        HINGE = BlockStateProperties.DOOR_HINGE;
        HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

        NORTH_SOUTH_AABB = Block.box(0.0, 0.0, 6.5, 16.0, 16.0, 9.5);
        EAST_WEST_AABB = Block.box(6.5, 0.0, 0.0, 9.5, 16.0, 16.0);

        double offset = 14.0;
        SHAPES = Map.of(
                Direction.NORTH, Map.of(
                        Placement.LEFT, Block.box(0.0, 0.0, 6.5, 16.0 - offset, 16.0, 9.5),
                        Placement.RIGHT, Block.box(offset, 0.0, 6.5, 16.0, 16.0, 9.5)
                ),
                Direction.SOUTH, Map.of(
                        Placement.LEFT, Block.box(offset, 0.0, 6.5, 16.0, 16.0, 9.5),
                        Placement.RIGHT, Block.box(0.0, 0.0, 6.5, 16.0 - offset, 16.0, 9.5)
                ),
                Direction.EAST, Map.of(
                        Placement.LEFT, Block.box(6.5, 0.0, 0.0, 9.5, 16.0, 16.0 - offset),
                        Placement.RIGHT, Block.box(6.5, 0.0, offset, 9.5, 16.0, 16.0)
                ),
                Direction.WEST, Map.of(
                        Placement.LEFT, Block.box(6.5, 0.0, offset, 9.5, 16.0, 16.0),
                        Placement.RIGHT, Block.box(6.5, 0.0, 0.0, 9.5, 16.0, 16.0 - offset)
                )
        );
    }
}
