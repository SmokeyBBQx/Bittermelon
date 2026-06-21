package com.site21.bittermelon.common.content.blocks.stickynote;

import com.mojang.math.OctahedralGroup;
import com.site21.bittermelon.common.content.blocks.stickynote.networking.OpenStickyNoteScreen;
import com.site21.bittermelon.init.neoforge.BitterItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.site21.bittermelon.init.neoforge.BitterBlocks.STICKY_NOTE;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.MESSAGE;
import static com.site21.bittermelon.init.neoforge.BitterItems.PEN;
import static net.minecraft.sounds.SoundSource.BLOCKS;
import static net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock.canAttach;

public class StickyNoteBlock extends Block implements EntityBlock {
    public static final EnumProperty<AttachFace> FACE;
    public static final EnumProperty<Direction> FACING;
    public static final BooleanProperty TOP_LEFT;
    public static final BooleanProperty TOP_RIGHT;
    public static final BooleanProperty BOTTOM_LEFT;
    public static final BooleanProperty BOTTOM_RIGHT;
    private static final Map<@NotNull Position, @NotNull Map<AttachFace, Map<Direction, VoxelShape>>> NOTE_SHAPES;

    public StickyNoteBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACE, AttachFace.WALL)
                .setValue(FACING, Direction.NORTH)
                .setValue(TOP_LEFT, false)
                .setValue(TOP_RIGHT, false)
                .setValue(BOTTOM_LEFT, false)
                .setValue(BOTTOM_RIGHT, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACE, FACING, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new StickyNoteBlockEntity(blockPos, blockState);
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        AttachFace face = state.getValue(FACE);
        Direction facing = state.getValue(FACING);

        VoxelShape combinedShape = Shapes.empty();

        for (Position corner : Position.values()) {
            if (state.getValue(corner.getProperty())) {
                combinedShape = Shapes.or(combinedShape, NOTE_SHAPES.get(corner).get(face).get(facing));
            }
        }

        return combinedShape.isEmpty() ? NOTE_SHAPES.get(Position.TOP_LEFT).get(face).get(facing) : combinedShape;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        ItemStack stack = context.getItemInHand();
        BlockState state = defaultBlockState();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (!level.getBlockState(pos).is(STICKY_NOTE)) {
            state = placeBlock(context, state);
        } else {
            state = level.getBlockState(pos);
            level.playSound(null, pos, SoundEvents.BOOK_PUT, BLOCKS, 1.0f, 1.0f);
        }

        if (state == null) return null;
        Position position = getPosition(state, context.getClickLocation(), pos);
        state = updateNotePosition(position, state, true);
        if (level.getBlockEntity(pos) instanceof StickyNoteBlockEntity stickyNote) {
            stickyNote.setNote(position.ordinal(), stack.get(MESSAGE));
        }

        return state;
    }

    private @Nullable BlockState placeBlock(@NotNull BlockPlaceContext context, BlockState state) {
        for (Direction direction : context.getNearestLookingDirections()) {
            if (direction.getAxis() == Direction.Axis.Y) {
                state = defaultBlockState()
                        .setValue(FACE, direction == Direction.UP.getOpposite() ? AttachFace.CEILING : AttachFace.FLOOR)
                        .setValue(FACING, context.getHorizontalDirection());
            } else {
                state = defaultBlockState()
                        .setValue(FACE, AttachFace.WALL)
                        .setValue(FACING, direction);
            }
        }

        return state.canSurvive(context.getLevel(), context.getClickedPos()) ? state : null;
    }

    @Override
    protected boolean canBeReplaced(@NotNull BlockState state, @NotNull BlockPlaceContext useContext) {
        return useContext.getItemInHand().is(BitterItems.STICKY_NOTE);
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        return canAttach(level, pos, getConnectedDirection(state).getOpposite());
    }

    @Override
    protected void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
        if (!canSurvive(state, level, pos) || areAllPositionsEmpty(state)) {
            level.removeBlock(pos, false);
        }
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

    @Override
    protected @NotNull InteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.CONSUME;
        }

        Position position = getPosition(state, hitResult.getLocation(), pos);

        if (!hasNoteAtPosition(position, state)) return InteractionResult.FAIL;

        if (stack.is(PEN) && player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new OpenStickyNoteScreen(pos, position.ordinal()));
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (level.isClientSide()) return InteractionResult.CONSUME;

        Position position = getPosition(state, hitResult.getLocation(), pos);

        if (hasNoteAtPosition(position, state)) {
            if (player.isShiftKeyDown()) {
                return takeNote(level, pos, player, position, state);
            }

            if (level.getBlockEntity(pos) instanceof StickyNoteBlockEntity stickyNote) {
                String message = stickyNote.getNotes()[position.ordinal()];
                Component component = message != null && !message.isBlank() ?
                        Component.literal(message).withStyle() :
                        Component.literal("Blank Note").withStyle(ChatFormatting.ITALIC)
                                .withStyle(ChatFormatting.GRAY);
                player.sendSystemMessage(component);
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }

    public InteractionResult takeNote(@NotNull Level level, BlockPos pos, Player player, Position position, BlockState state) {
        ItemStack stack = STICKY_NOTE.toStack();
        if (level.getBlockEntity(pos) instanceof StickyNoteBlockEntity stickyNote) {
            stack.set(MESSAGE, stickyNote.getNotes()[position.ordinal()]);
            stickyNote.setNote(position.ordinal(), null);
        }

        player.addItem(stack);

        state = updateNotePosition(position, state, false);

        if (areAllPositionsEmpty(state)) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            return InteractionResult.SUCCESS;
        } else {
            level.setBlockAndUpdate(pos, state);
        }

        level.playSound(null, pos, SoundEvents.BOOK_PAGE_TURN, BLOCKS, 1.0f, 1.0f);

        return InteractionResult.SUCCESS;
    }

    @Contract(pure = true)
    public static @NotNull Position getPosition(@NotNull BlockState state, @NotNull Vec3 hitPos, @NotNull BlockPos blockPos) {
        Direction facing = state.getValue(FACING);
        AttachFace face = state.getValue(FACE);
        double relX = hitPos.x - blockPos.getX();
        double relY = hitPos.y - blockPos.getY();
        double relZ = hitPos.z - blockPos.getZ();
        double horizontal = switch (facing) {
            case SOUTH -> 1 - relX;
            case WEST -> 1 - relZ;
            case EAST -> relZ;
            default -> relX;
        };
        if (face == AttachFace.FLOOR || face == AttachFace.CEILING) {
            boolean floor = face == AttachFace.FLOOR;
            double vertical = switch (facing) {
                case SOUTH -> floor ? 1 - relZ : relZ;
                case WEST -> floor ? relX : 1 - relX;
                case EAST -> floor ? 1 - relX : relX;
                default -> floor ? relZ : 1 - relZ;
            };
            if (floor ? vertical < 0.5 : vertical > 0.5) {
                return horizontal < 0.5 ? Position.TOP_LEFT : Position.TOP_RIGHT;
            } else {
                return horizontal < 0.5 ? Position.BOTTOM_LEFT : Position.BOTTOM_RIGHT;
            }
        } else {
            if (relY > 0.5) {
                return horizontal > 0.5 ? Position.TOP_LEFT : Position.TOP_RIGHT;
            } else {
                return horizontal > 0.5 ? Position.BOTTOM_LEFT : Position.BOTTOM_RIGHT;
            }
        }
    }

    public boolean hasNoteAtPosition(@NotNull BlockState state, @NotNull Vec3 hitPos, @NotNull BlockPos blockPos) {
        return hasNoteAtPosition(getPosition(state, hitPos, blockPos), state);
    }

    public boolean hasNoteAtPosition(@NotNull Position position, @NotNull BlockState state) {
        return state.getValue(position.getProperty());
    }

    public @NotNull BlockState updateNotePosition(@NotNull Position position, @NotNull BlockState state, boolean value) {
        return state.setValue(position.getProperty(), value);
    }

    public boolean areAllPositionsEmpty(@NotNull BlockState state) {
        return !state.getValue(TOP_RIGHT) && !state.getValue(TOP_LEFT) && !state.getValue(BOTTOM_RIGHT) && !state.getValue(BOTTOM_LEFT);
    }

    public enum Position {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT;

        public BooleanProperty getProperty() {
            return switch (this) {
                case TOP_LEFT -> StickyNoteBlock.TOP_LEFT;
                case TOP_RIGHT -> StickyNoteBlock.TOP_RIGHT;
                case BOTTOM_LEFT -> StickyNoteBlock.BOTTOM_LEFT;
                case BOTTOM_RIGHT -> StickyNoteBlock.BOTTOM_RIGHT;
            };
        }
    }

    // Modification of Shapes#rotateAttachFace that works for this case, should work generally for similar custom blocks
    public static Map<AttachFace, Map<Direction, VoxelShape>> rotateAttachFace(VoxelShape shape) {
        return Map.of(
                AttachFace.WALL, Shapes.rotateHorizontal(shape),
                AttachFace.FLOOR, Shapes.rotateHorizontal(Shapes.rotate(shape, OctahedralGroup.ROT_180_EDGE_YZ_NEG)),
                AttachFace.CEILING, Shapes.rotateHorizontal(Shapes.rotate(shape, OctahedralGroup.ROT_90_REF_X_NEG))
        );
    }

    static {
        FACE = BlockStateProperties.ATTACH_FACE;
        FACING = HorizontalDirectionalBlock.FACING;
        TOP_LEFT = BooleanProperty.create("top_left");
        TOP_RIGHT = BooleanProperty.create("top_right");
        BOTTOM_LEFT = BooleanProperty.create("bottom_left");
        BOTTOM_RIGHT = BooleanProperty.create("bottom_right");
        NOTE_SHAPES = Map.of(
                Position.TOP_LEFT, rotateAttachFace(Block.box(8.0, 8.0, 15.9, 16.0, 16.0, 16.0)),
                Position.TOP_RIGHT, rotateAttachFace(Block.box(0.0, 8.0, 15.9, 8.0, 16.0, 16.0)),
                Position.BOTTOM_LEFT, rotateAttachFace(Block.box(8.0, 0.0, 15.9, 16.0, 8.0, 16.0)),
                Position.BOTTOM_RIGHT, rotateAttachFace(Block.box(0.0, 0.0, 15.9, 8.0, 8.0, 16.0))
        );
    }
}


