package net.smokeybbq.bittermelon.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.smokeybbq.bittermelon.blocks.blockentities.PuddleBlockEntity;
import net.smokeybbq.bittermelon.entities.PuddleFallingBlockEntity;
import net.smokeybbq.bittermelon.init.BlockEntityInit;
import net.smokeybbq.bittermelon.miscellaneous.Stumble;
import net.smokeybbq.bittermelon.util.ModLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;


public class PuddleBlock extends FallingBlock implements EntityBlock {
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 0.1D, 16.0D);

    public PuddleBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return BlockEntityInit.PUDDLE_BLOCK_ENTITY.get().create(pPos, pState);
    }

//    @Nullable
//    @Override
//    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
//        return level.isClientSide() ? null : (level0, state0, blockEntityType0, blockEntity) -> ((PuddleBlockEntity) blockEntity).tick();
//    }

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
                    Stumble stumble = new Stumble(serverPlayer);
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
