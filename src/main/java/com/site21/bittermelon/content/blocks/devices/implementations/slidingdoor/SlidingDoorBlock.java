package com.site21.bittermelon.content.blocks.devices.implementations.slidingdoor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SlidingDoorBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING;
    public static final BooleanProperty OPEN;
    public static final BooleanProperty VISIBLE;
    public static final EnumProperty<DoorHingeSide> HINGE;
    public static final EnumProperty<DoubleBlockHalf> HALF;

    protected static final VoxelShape NORTH_SOUTH_AABB;
    protected static final VoxelShape EAST_WEST_AABB;

    protected static final VoxelShape NORTH_RIGHT_OPEN;
    protected static final VoxelShape NORTH_LEFT_OPEN;
    protected static final VoxelShape SOUTH_RIGHT_OPEN;
    protected static final VoxelShape SOUTH_LEFT_OPEN;
    protected static final VoxelShape EAST_RIGHT_OPEN;
    protected static final VoxelShape EAST_LEFT_OPEN;
    protected static final VoxelShape WEST_RIGHT_OPEN;
    protected static final VoxelShape WEST_LEFT_OPEN;

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
        Direction direction = state.getValue(FACING);
        boolean isOpen = state.getValue(OPEN);
        DoorHingeSide hinge = state.getValue(HINGE);

        if (!isOpen) {
            if (direction == Direction.NORTH || direction == Direction.SOUTH) {
                return NORTH_SOUTH_AABB;
            } else {
                return EAST_WEST_AABB;
            }
        }

        if (direction == Direction.NORTH) {
            return hinge == DoorHingeSide.RIGHT ? NORTH_RIGHT_OPEN : NORTH_LEFT_OPEN;
        } else if (direction == Direction.SOUTH) {
            return hinge == DoorHingeSide.RIGHT ? SOUTH_RIGHT_OPEN : SOUTH_LEFT_OPEN;
        } else if (direction == Direction.EAST) {
            return hinge == DoorHingeSide.RIGHT ? EAST_RIGHT_OPEN : EAST_LEFT_OPEN;
        } else {
            return hinge == DoorHingeSide.RIGHT ? WEST_RIGHT_OPEN : WEST_LEFT_OPEN;
        }
    }

    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        if (pos.getY() >= level.getMaxBuildHeight() - 1) return null;

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

    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        playSound(player, level, pos, state.getValue(OPEN));
        level.gameEvent(player, isOpen(state) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);

        BlockPos otherHalf;
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            otherHalf = pos.below();
        } else {
            otherHalf = pos.above();
        }

        toggleOpen(level, pos);
        toggleOpen(level, otherHalf);

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public boolean isOpen(@NotNull BlockState state) {
        return state.getValue(OPEN);
    }

    public void toggleOpen(@NotNull Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);

        state = state.cycle(OPEN);
        state = state.setValue(VISIBLE, false);
        level.setBlock(pos, state, 10);
    }

    private void playSound(@Nullable Entity source, @NotNull Level level, BlockPos pos, boolean isOpening) {
        level.playSound(source, pos, isOpening ? BlockSetType.IRON.doorOpen() : BlockSetType.IRON.doorClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, LivingEntity placer, @NotNull ItemStack stack) {
        level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
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
        NORTH_RIGHT_OPEN = Block.box(offset, 0.0, 6.5, 16.0, 16.0, 9.5);
        NORTH_LEFT_OPEN = Block.box(0.0, 0.0, 6.5, 16.0 - offset, 16.0, 9.5);
        SOUTH_RIGHT_OPEN = Block.box(0.0, 0.0, 6.5, 16.0 - offset, 16.0, 9.5);
        SOUTH_LEFT_OPEN = Block.box(offset, 0.0, 6.5, 16.0, 16.0, 9.5);
        EAST_RIGHT_OPEN = Block.box(6.5, 0.0, offset, 9.5, 16.0, 16.0);
        EAST_LEFT_OPEN = Block.box(6.5, 0.0, 0.0, 9.5, 16.0, 16.0 - offset);
        WEST_RIGHT_OPEN = Block.box(6.5, 0.0, 0.0, 9.5, 16.0, 16.0 - offset);
        WEST_LEFT_OPEN = Block.box(6.5, 0.0, offset, 9.5, 16.0, 16.0);
    }
}
