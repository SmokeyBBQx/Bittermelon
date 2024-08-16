package net.smokeybbq.bittermelon.blocks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.smokeybbq.bittermelon.blocks.blockentities.PuddleBlockEntity;
import net.smokeybbq.bittermelon.entities.PuddleFallingBlockEntity;
import net.smokeybbq.bittermelon.init.BlockEntityInit;
import net.smokeybbq.bittermelon.items.substancecontainers.SubstanceContainerItem;
import net.smokeybbq.bittermelon.substances.Substance;
import net.smokeybbq.bittermelon.util.ModLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Random;

import static net.minecraft.world.level.block.PipeBlock.PROPERTY_BY_DIRECTION;
import static net.smokeybbq.bittermelon.substances.FlavorUtil.getFlavorMessageComponent;


public class PuddleBlock extends FallingBlock implements EntityBlock {
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 2);

    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = ImmutableMap.of(
            Direction.NORTH, NORTH,
            Direction.EAST, EAST,
            Direction.SOUTH, SOUTH,
            Direction.WEST, WEST
    );
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 0.1D, 16.0D);

    public PuddleBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(LEVEL, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, LEVEL);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        int currentLevel = stateIn.getValue(LEVEL);

        boolean canConnect = this.canConnectTo(worldIn, currentPos, facingPos);

        stateIn = stateIn.setValue(PROPERTY_BY_DIRECTION.get(facing), canConnect);

        return stateIn;
    }

    private boolean canConnectTo(LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        BlockState facingState = world.getBlockState(facingPos);
        if (!(facingState.getBlock() instanceof PuddleBlock)) {
            return false;
        }
        int facingLevel = facingState.getValue(LEVEL);
        return true;
    }

//    @Override
//    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
//        if (!level.isClientSide()) {
//            if (player.isShiftKeyDown() && !(player.getMainHandItem().getItem() instanceof SubstanceContainerItem)) {
//                if (level.getBlockEntity(blockPos) instanceof PuddleBlockEntity puddleBlockEntity) {
//                    Map<Substance, Integer> substances = puddleBlockEntity.getSubstances();
//                    int totalAmount = puddleBlockEntity.getTotalAmount();
//
//                    player.sendSystemMessage(getFlavorMessageComponent(substances, totalAmount));
//
//                    int totalSubstances = substances.size();
//                    if (totalSubstances == 0) return InteractionResult.PASS;
//
//                    substances.forEach((substance, amount) -> {
//                        float proportion = (float) amount / totalAmount;
//                        int consumeAmount = (int) Math.ceil(5 * proportion);
//                        int actualAmount = Math.min(amount, consumeAmount);
//
//                        puddleBlockEntity.updateSubstance(substance, -actualAmount);
//                        substance.getEffects(player, actualAmount);
//                    });
//                }
//
//                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
//                return InteractionResult.CONSUME_PARTIAL;
//
//            }
//        }
//        return InteractionResult.PASS;
//    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return BlockEntityInit.PUDDLE_BLOCK_ENTITY.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : (level0, state0, blockEntityType0, blockEntity) -> ((PuddleBlockEntity) blockEntity).tick();
    }

//    @Override
//    public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
//        if (entity instanceof ServerPlayer player && !level.isClientSide) {
//            Vec3 movement = player.getDeltaMovement();
//
//            float MOVEMENT_THRESHOLD = 0.01F;
//
//            System.out.println(Math.abs(movement.x));
//            System.out.println(Math.abs(movement.z));
//
//            if (Math.abs(movement.x) > MOVEMENT_THRESHOLD || Math.abs(movement.z) > MOVEMENT_THRESHOLD) {
//                if (level.getRandom().nextFloat() < 0.02f) {
//                    ServerPlayer serverPlayer = (ServerPlayer) player;
//                    Stumble stumble = new Stumble(serverPlayer);
//                }
//            }
//        }
//    }

    @Override
    public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (entity instanceof Player player && !level.isClientSide) {
            Random random = new Random();
            Vec3 movement = player.getDeltaMovement();
            float MOVEMENT_THRESHOLD = 0.01F;

            if (Math.abs(movement.x) > MOVEMENT_THRESHOLD || Math.abs(movement.z) > MOVEMENT_THRESHOLD) {
                if (random.nextFloat() < 0.02f) {
                    ServerPlayer serverPlayer = (ServerPlayer) player;
//                    Stumble stumble = new Stumble(serverPlayer);
                }
            }
        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (isFree(pLevel.getBlockState(pPos.below())) && pPos.getY() >= pLevel.getMinBuildHeight()) {
            if (pLevel.getBlockEntity(pPos) instanceof PuddleBlockEntity blockEntity) {
                CompoundTag nbt = blockEntity.serializeNBT();

                if (nbt == null) {
                    ModLogger.warn("Falling block has null nbt");
                }

                PuddleFallingBlockEntity fallingBlockEntity = PuddleFallingBlockEntity.fallPuddle(pLevel, pPos, pState);
                fallingBlockEntity.dropItem = false;
                fallingBlockEntity.blockData = nbt;

                this.falling(fallingBlockEntity);
            }
        }
    }


    @Override
    public void onLand(Level level, BlockPos pos, BlockState state, BlockState replaceableState, FallingBlockEntity fallingBlock) {
        super.onLand(level, pos, state, replaceableState, fallingBlock);
        ModLogger.info("Puddle landed at: " + pos);

        if (fallingBlock.blockData != null && level.getBlockEntity(pos) instanceof PuddleBlockEntity blockEntity) {
            blockEntity.load(fallingBlock.blockData);
        }
    }

    @Override
    public void onBrokenAfterFall(Level level, @NotNull BlockPos pos, @NotNull FallingBlockEntity fallingBlock) {
        ModLogger.info("Puddle broken at: " + pos);
        if (level.getBlockEntity(pos.below()) instanceof PuddleBlockEntity targetEntity) {
            assert fallingBlock.blockData != null;
            targetEntity.mixWith(fallingBlock.blockData);
        }
    }
}
