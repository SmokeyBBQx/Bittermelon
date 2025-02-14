package com.site21.bittermelon.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.site21.bittermelon.blocks.blockentities.ContainmentPanelBlockEntity;
import com.site21.bittermelon.blocks.blockentities.FluidBlockEntity;
import com.site21.bittermelon.containment.client.ContainmentPanelScreen;
import com.site21.bittermelon.items.laserdesignator.LaserDesignatorItem;
import com.site21.bittermelon.networking.client.OpenATMScreen;
import com.site21.bittermelon.networking.client.OpenContainmentPanelScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.site21.bittermelon.init.BitterDataComponents.POSITION_1;
import static com.site21.bittermelon.init.BitterDataComponents.POSITION_2;
import static com.site21.bittermelon.init.BitterSounds.SCANNER_BEEP;

public class ContainmentPanelBlock extends Block implements EntityBlock {
    public static final BooleanProperty ON = BooleanProperty.create("on");
    public static final DirectionProperty FACING;
    private static final Map<Direction, VoxelShape> AABBS;

    public ContainmentPanelBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(ON, false)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        BlockState newState = state.setValue(ON, true);
        level.setBlock(pos, newState, 3);
        if (level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ContainmentPanelBlockEntity containmentPanel) {
                Minecraft.getInstance().setScreen(new ContainmentPanelScreen(containmentPanel, true));
            }
        }

        return InteractionResult.SUCCESS_NO_ITEM_USED;
    }

    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof ContainmentPanelBlockEntity blockEntity) {
            BlockPos pos1 = stack.get(POSITION_1.get());
            BlockPos pos2 = stack.get(POSITION_2.get());

            if (pos1 == null || pos2 == null) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

            BoundingBox boundingBox = BoundingBox.fromCorners(new Vec3i(pos1.getX(), pos1.getY(), pos1.getZ()), new Vec3i(pos2.getX(), pos2.getY(), pos2.getZ()));
            blockEntity.setBoundingBox(boundingBox);

            player.level().playSound(null, player.getOnPos(), SCANNER_BEEP.get(), SoundSource.PLAYERS, 0.5f, 0.8f);
        }

        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new ContainmentPanelBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : (level0, state0, blockEntityType0, blockEntity) -> ((ContainmentPanelBlockEntity) blockEntity).tick();
    }

    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return AABBS.get(state.getValue(FACING));
    }

    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockState blockstate = super.getStateForPlacement(context);
        BlockGetter blockgetter = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Direction[] adirection = context.getNearestLookingDirections();

        for (Direction direction : adirection) {
            if (direction.getAxis().isHorizontal()) {
                Direction direction1 = direction.getOpposite();
                assert blockstate != null;
                blockstate = blockstate.setValue(FACING, direction1);
                if (!blockgetter.getBlockState(blockpos.relative(direction)).canBeReplaced(context)) {
                    return blockstate;
                }
            }
        }

        return null;
    }

    protected @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    protected @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ON);
        builder.add(FACING);
    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
        AABBS = Maps.newEnumMap(ImmutableMap.of(
                Direction.NORTH, Block.box(4.0, 4.0, 12.0, 12.0, 12.0, 16.0),
                Direction.SOUTH, Block.box(4.0, 4.0, 0.0, 12.0, 12.0, 4.0),
                Direction.EAST, Block.box(0.0, 4.0, 4.0, 4.0, 12.0, 12.0),
                Direction.WEST, Block.box(12.0, 4.0, 4.0, 16.0, 12.0, 12.0)
        ));
    }
}
