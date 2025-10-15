package com.site21.bittermelon.common.content.blocks.substance.fluid;

import com.google.common.collect.ImmutableMap;
import com.site21.bittermelon.common.systems.stumble.StumbleHandler;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.FLUID_BLOCK_ENTITY;

public class FluidBlock extends Block implements EntityBlock {
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 10);
    public static final BooleanProperty FLOATING = BooleanProperty.create("floating");

    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = ImmutableMap.of(
            Direction.NORTH, NORTH,
            Direction.EAST, EAST,
            Direction.SOUTH, SOUTH,
            Direction.WEST, WEST
    );

    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 0.1D, 16.0D);

    public FluidBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(LEVEL, 0)
                .setValue(FLOATING, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, LEVEL, FLOATING);
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull LevelReader level, @NotNull ScheduledTickAccess scheduledTickAccess, @NotNull BlockPos pos, @NotNull Direction direction, @NotNull BlockPos neighborPos, @NotNull BlockState neighborState, @NotNull RandomSource random) {
        boolean canConnect = this.canConnectTo(level, neighborPos);

        BooleanProperty property = PROPERTY_BY_DIRECTION.get(direction);
        if (property != null) {
            state = state.setValue(property, canConnect);
        }

        boolean isFloating = level.getBlockState(pos.below()).isAir();
        state = state.setValue(FLOATING, isFloating);

        return state;
    }

    private boolean canConnectTo(@NotNull LevelReader world, BlockPos facingPos) {
        BlockState facingState = world.getBlockState(facingPos);
        return facingState.isFaceSturdy(world, facingPos, Direction.UP) || facingState.getBlock() instanceof FluidBlock;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return FLUID_BLOCK_ENTITY.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : (level0, state0, blockEntityType0, blockEntity) -> ((FluidBlockEntity) blockEntity).tick();
    }

    @Override
    protected void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, orientation, movedByPiston);
        if (level.getBlockEntity(pos) instanceof FluidBlockEntity fluidEntity) {
            fluidEntity.setActive();
        }
    }

    @Override
    public boolean canBeReplaced(@NotNull BlockState state, @NotNull BlockPlaceContext context) {
        return true;
    }

    @Override
    protected void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity, @NotNull InsideBlockEffectApplier effectApplier) {
        if (level.isClientSide) return;

        if (level.getBlockEntity(pos) instanceof FluidBlockEntity fluidBlockEntity) {
            float slipperiness = fluidBlockEntity.getSlipperiness();

            if (entity instanceof LivingEntity livingEntity) {
                if (entity.getDeltaMovement().length() > 0) {
                    if (entity.getRandom().nextFloat() > (entity.isSprinting() ? 1 - slipperiness : 1 - slipperiness / 10)) {
                        StumbleHandler.stumble(livingEntity);
                        level.playSound(null, pos, BitterSounds.SLIP.value(), SoundSource.AMBIENT);
                    }
                }
            }
        }

        // TODO: Implement getting chemicals on skin
    }

    @Override
    public float getFriction(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos, @Nullable Entity entity) {
        // TODO: Friction don't work

        if (entity == null) return friction;

        if (entity.level().getBlockEntity(pos) instanceof FluidBlockEntity fluidBlockEntity) {
            return fluidBlockEntity.getSlipperiness();
        }

        return friction;
    }
}
