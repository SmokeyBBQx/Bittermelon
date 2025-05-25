package com.site21.bittermelon.content.blocks.devices.implementations.slidingdoor;

import com.site21.bittermelon.init.neoforge.BitterSounds;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.site21.bittermelon.init.neoforge.BitterBlocks.LARGE_SLIDING_DOOR;

public class LargeSlidingDoorBlock extends Block implements EntityBlock {
    public static final EnumProperty<State> STATE = EnumProperty.create("state", State.class);
    public static final BooleanProperty Z_AXIS = BooleanProperty.create("z_axis");
    public static final BooleanProperty MASTER = BooleanProperty.create("master");
    private static final VoxelShape X_SHAPE = Block.box(0, 0, 6, 16, 16, 10);
    private static final VoxelShape Z_SHAPE = Block.box(6, 0, 0, 10, 16, 16);

    public LargeSlidingDoorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(STATE, State.CLOSED)
                        .setValue(Z_AXIS, false)
                        .setValue(MASTER, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(STATE, Z_AXIS, MASTER);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        if (!state.getValue(MASTER)) return null;
        return new LargeSlidingDoorBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        if (!state.getValue(MASTER)) return null;
        return level.isClientSide() ?
                (level0, state0, blockEntityType0, blockEntity) -> ((LargeSlidingDoorBlockEntity) blockEntity).clientTick()
                : (level0, state0, blockEntityType0, blockEntity) -> ((LargeSlidingDoorBlockEntity) blockEntity).tick();
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (state == null) return null;

        // Determine whether it should be placed in the z-axis
        boolean zAxis = context.getHorizontalDirection().getAxis() == Direction.Axis.X;

        // Directions the dummy door blocks should be placed in.
        Direction[] directions = zAxis ?
                new Direction[]{Direction.NORTH, Direction.SOUTH} :
                new Direction[]{Direction.EAST, Direction.WEST};

        // The actual master position (2 blocks above placement)
        BlockPos masterPos = pos.above(2);

        // 3x3 positions from master position.
        BlockPos[] dummyBlockPositions = {
                pos.above(),
                pos.relative(directions[0]),
                pos.relative(directions[0]).above(),
                pos.relative(directions[0]).above(2),
                pos.relative(directions[1]),
                pos.relative(directions[1]).above(),
                pos.relative(directions[1]).above(2)
        };

        // Check if there are any blocks in the way.
        for (BlockPos placePos : dummyBlockPositions) {
            if (!level.getBlockState(placePos).canBeReplaced()) {
                return null;
            }
        }

        for (BlockPos placePos : dummyBlockPositions) {
            level.setBlock(placePos, LARGE_SLIDING_DOOR.get().defaultBlockState().setValue(Z_AXIS, zAxis), 3);
        }

        level.setBlock(masterPos, LARGE_SLIDING_DOOR.get().defaultBlockState().setValue(Z_AXIS, zAxis).setValue(MASTER, true), 3);

        return state.setValue(Z_AXIS, zAxis);
    }

    @Override
    protected void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean movedByPiston) {
        // TODO: Half-baked break logic (only works for the master block)

        super.onRemove(state, level, pos, newState, movedByPiston);

        if (level.isClientSide) return;
        if (state.getBlock() == newState.getBlock()) return;
        if (!state.getValue(MASTER)) return;

        boolean zAxis = state.getValue(Z_AXIS);
        Direction[] directions = zAxis ?
                new Direction[]{Direction.NORTH, Direction.SOUTH} :
                new Direction[]{Direction.EAST, Direction.WEST};

        BlockPos[] dummyBlockPositions = {
                pos.below(),
                pos.below(2),
                pos.relative(directions[0]),
                pos.relative(directions[0]).below(),
                pos.relative(directions[0]).below(2),
                pos.relative(directions[1]),
                pos.relative(directions[1]).below(),
                pos.relative(directions[1]).below(2)
        };

        for (BlockPos dummyPos : dummyBlockPositions) {
            removeDummyBlock(level, dummyPos);
        }
    }

    private void removeDummyBlock(@NotNull Level level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos);
        if (blockState.is(LARGE_SLIDING_DOOR.get())) {
            level.removeBlock(pos, false);
        }
    }

    public boolean canClose(Level level, BlockPos pos) {
        if (level == null) return true;

        BlockPos middle1 = pos.below();
        BlockPos middle2 = pos.below(2);

        BlockState state1 = level.getBlockState(middle1);
        BlockState state2 = level.getBlockState(middle2);

        boolean blocksCanClose = (state1.is(LARGE_SLIDING_DOOR.get()) || state1.canBeReplaced()) &&
                (state2.is(LARGE_SLIDING_DOOR.get()) || state2.canBeReplaced());

        if (!blocksCanClose) return false;

        AABB twoHighBox = new AABB(middle2.getX(), middle2.getY(), middle2.getZ(),
                middle2.getX() + 1, middle2.getY() + 2, middle2.getZ() + 1);

        return level.getEntities(null, twoHighBox).isEmpty();
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide) {
            if (!state.getValue(MASTER)) {
                BlockPos masterPos = findMasterBlock(level, pos);
                if (masterPos != null) {
                    BlockState masterState = level.getBlockState(masterPos);
                    return masterState.useWithoutItem(level, player, new BlockHitResult(hitResult.getLocation(), hitResult.getDirection(), masterPos, hitResult.isInside()));
                }
                return InteractionResult.FAIL;
            }

            if (state.getValue(STATE) == State.OPEN && canClose(level, pos)) {
                // Closing logic

                level.setBlock(pos, state.setValue(STATE, State.CLOSING), 3);
                level.playSound(null, pos, BitterSounds.LARGE_SLIDING_DOOR_CLOSE.get(), SoundSource.BLOCKS);
            } else if (state.getValue(STATE) == State.CLOSED) {
                // Opening logic

                level.setBlock(pos, state.setValue(STATE, State.OPENING), 3);

                // Remove blocks under master block
                removeDummyBlock(level, pos.below());
                removeDummyBlock(level, pos.below(2));

                level.playSound(null, pos, BitterSounds.LARGE_SLIDING_DOOR_OPEN.get(), SoundSource.BLOCKS);
            } else if (!canClose(level, pos)) {
                // Stuck logic

                level.setBlock(pos, state.setValue(STATE, State.STUCK), 3);
                level.playSound(null, pos, BitterSounds.LARGE_SLIDING_DOOR_OPEN.get(), SoundSource.BLOCKS);
            }
        }
        return InteractionResult.SUCCESS;
    }

    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level.isClientSide) return ItemInteractionResult.FAIL;

        // FORCE OPEN LOGIC
        if (!stack.is(Items.IRON_AXE)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (!state.getValue(MASTER)) {
            pos = findMasterBlock(level, pos);
            if (pos == null) return ItemInteractionResult.FAIL;
            state = level.getBlockState(pos);
        }
        if (level.getBlockState(pos).getValue(STATE) == State.OPEN) return ItemInteractionResult.FAIL;
        if (level.getBlockEntity(pos) instanceof LargeSlidingDoorBlockEntity blockEntity) {
            // Update gap size.
            blockEntity.setDoorProgress(blockEntity.getDoorProgress() + 0.05f);
            // Placeholder sound
            level.playSound(null, pos, SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 0.3f, 0.1f);

            // Create opening if the gap is equal to a block.
            if (blockEntity.getDoorProgress() >= 0.40f) {
                removeDummyBlock(level, pos.below());
                removeDummyBlock(level, pos.below(2));
            }

            // Handle the door being fully opened.
            if (blockEntity.getDoorProgress() >= 1) {
                level.setBlock(pos, state.setValue(STATE, State.OPEN), 3);
            }
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Nullable
    private BlockPos findMasterBlock(@NotNull BlockGetter level, BlockPos pos) {
        BlockState currentState = level.getBlockState(pos);
        if (!currentState.is(LARGE_SLIDING_DOOR.get())) return null;

        boolean zAxis = currentState.getValue(Z_AXIS);
        Direction[] directions = zAxis ?
                new Direction[]{Direction.NORTH, Direction.SOUTH} :
                new Direction[]{Direction.EAST, Direction.WEST};

        List<BlockPos> possibleMasterPositions = new ArrayList<>();

        // Check in each direction in the determined axis for each y-level.
        for (int yOffset = 0; yOffset <= 2; yOffset++) {
            BlockPos checkPos = pos.above(yOffset);
            possibleMasterPositions.add(checkPos);

            for (Direction dir : directions) {
                possibleMasterPositions.add(checkPos.relative(dir));
            }
        }

        for (BlockPos masterPos : possibleMasterPositions) {
            BlockState state = level.getBlockState(masterPos);
            if (state.is(LARGE_SLIDING_DOOR.get()) && state.getValue(MASTER)) {
                return masterPos;
            }
        }

        return null;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return state.getValue(Z_AXIS) ? Z_SHAPE : X_SHAPE;
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        if (state.getValue(MASTER)) {
            return state.getValue(STATE) == State.OPEN || state.getValue(STATE) == State.OPENING ? Shapes.empty() : state.getShape(level, pos);
        } else {
            BlockPos masterPos = findMasterBlock(level, pos);
            if (masterPos == null) return Shapes.empty();
            return level.getBlockState(masterPos).getCollisionShape(level, masterPos, context);
        }
    }

    public enum State implements StringRepresentable {
        OPEN("open"),
        OPENING("opening"),
        CLOSED("closed"),
        CLOSING("closing"),
        STUCK("stuck");

        private final String name;

        State(String name) {
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

        public static final StreamCodec<ByteBuf, State> STATE_STREAM_CODEC =
                ByteBufCodecs.VAR_INT.map(
                        ordinal -> State.values()[ordinal],
                        Enum::ordinal
                );
    }
}
