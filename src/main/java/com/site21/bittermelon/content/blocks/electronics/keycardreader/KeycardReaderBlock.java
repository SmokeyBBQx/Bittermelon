package com.site21.bittermelon.content.blocks.electronics.keycardreader;

import com.site21.bittermelon.content.blocks.properties.BitterStateProperties;
import com.site21.bittermelon.content.blocks.properties.Placement;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.site21.bittermelon.content.blocks.SmallPosterBlock.getPlacement;
import static com.site21.bittermelon.init.neoforge.BitterItems.KEYCARD;

public class KeycardReaderBlock extends Block implements EntityBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Placement> PLACEMENT = BitterStateProperties.PLACEMENT;
    private static final Map<Direction, Map<Placement, VoxelShape>> SHAPES;

    public KeycardReaderBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(PLACEMENT, Placement.LEFT));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING, PLACEMENT);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new KeycardReaderBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection();
        Placement placement = getPlacement(context, direction);

        return defaultBlockState()
                .setValue(FACING, direction.getOpposite())
                .setValue(PLACEMENT, placement);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Direction direction = state.getValue(FACING);
        Placement placement = state.getValue(PLACEMENT);
        return SHAPES.get(direction).get(placement);
    }

    @Override
    protected @NotNull InteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof KeycardReaderBlockEntity reader) {
            if (stack.is(KEYCARD.get())) {
                int id = stack.getOrDefault(BitterDataComponents.ID_NUMBER, 0);
                if (id == 0) return InteractionResult.TRY_WITH_EMPTY_HAND;

                reader.scan(id);

                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    static {
        SHAPES = Map.of(
                Direction.NORTH, Map.of(
                        Placement.LEFT, Block.box(10, 3, 15.5, 14, 8, 16),
                        Placement.RIGHT, Block.box(2, 3, 15.5, 6, 8, 16)
                ),
                Direction.EAST, Map.of(
                        Placement.LEFT, Block.box(0, 3, 10, 0.5, 8, 14),
                        Placement.RIGHT, Block.box(0, 3, 2, 0.5, 8, 6)
                ),
                Direction.SOUTH, Map.of(
                        Placement.LEFT, Block.box(2, 3, 0, 6, 8, 0.5),
                        Placement.RIGHT, Block.box(10, 3, 0, 14, 8, 0.5)
                ),
                Direction.WEST, Map.of(
                        Placement.LEFT, Block.box(15.5, 3, 2, 16, 8, 6),
                        Placement.RIGHT, Block.box(15.5, 3, 10, 16, 8, 14)
                )
        );
    }
}
