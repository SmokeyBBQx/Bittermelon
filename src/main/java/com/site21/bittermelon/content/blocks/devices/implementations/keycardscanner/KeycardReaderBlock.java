package com.site21.bittermelon.content.blocks.devices.implementations.keycardscanner;

import com.site21.bittermelon.content.blocks.properties.BitterStateProperties;
import com.site21.bittermelon.content.blocks.properties.Placement;
import com.site21.bittermelon.content.personnel.registry.PersonnelEntry;
import com.site21.bittermelon.content.personnel.registry.PersonnelRegistry;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.site21.bittermelon.init.neoforge.BitterItems.KEYCARD;

public class KeycardReaderBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
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
        Direction direction = context.getHorizontalDirection().getOpposite();
        Vec3 hitVec = context.getClickLocation().subtract(context.getClickedPos().getX(), context.getClickedPos().getY(), context.getClickedPos().getZ());
        Placement placement = hitVec.x < 0.5 ? Placement.RIGHT : Placement.LEFT;
        return defaultBlockState()
                .setValue(FACING, direction)
                .setValue(PLACEMENT, placement);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Direction direction = state.getValue(FACING);
        Placement placement = state.getValue(PLACEMENT);
        return SHAPES.get(direction).get(placement);
    }

    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof KeycardReaderBlockEntity reader) {
            if (stack.is(KEYCARD.get())) {
                int id = stack.getOrDefault(BitterDataComponents.ID_NUMBER, 0);
                if (id == 0) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

                scan(id, level, pos, reader);
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public void scan(int id, Level level, BlockPos pos, KeycardReaderBlockEntity reader) {
        if (level == null) return;
        if (!reader.isOn()) return;

        PersonnelEntry entry = PersonnelRegistry.get(level).getEntry(id);
        if (entry == null) return;

        Map<String, Boolean> requiredPrivileges = reader.getPrivileges();
        Map<String, Boolean> privileges = entry.getPrivileges();
        if (reader.hasPermission(privileges, requiredPrivileges)) {
            reader.triggerAccessGranted();
            level.playSound(null, pos, SoundEvents.NOTE_BLOCK_BELL.value(), SoundSource.BLOCKS);
        }
    }

    static {
        SHAPES = Map.of(
                Direction.NORTH, Map.of(
                        Placement.LEFT, Block.box(9, 0, 15, 15, 8, 16),
                        Placement.RIGHT, Block.box(1, 0, 15, 7, 8, 16)
                ),
                Direction.EAST, Map.of(
                        Placement.LEFT, Block.box(0, 0, 9, 1, 8, 15),
                        Placement.RIGHT, Block.box(0, 0, 1, 1, 8, 7)
                ),
                Direction.SOUTH, Map.of(
                        Placement.LEFT, Block.box(1, 0, 0, 7, 8, 1),
                        Placement.RIGHT, Block.box(9, 0, 0, 15, 8, 1)
                ),
                Direction.WEST, Map.of(
                        Placement.LEFT, Block.box(15, 0, 1, 16, 8, 7),
                        Placement.RIGHT, Block.box(15, 0, 9, 16, 8, 15)
                )
        );
    }
}
