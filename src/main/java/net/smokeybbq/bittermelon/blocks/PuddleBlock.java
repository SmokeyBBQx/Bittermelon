package net.smokeybbq.bittermelon.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.smokeybbq.bittermelon.blocks.blockentities.PuddleBlockEntity;
import net.smokeybbq.bittermelon.init.BlockEntityInit;
import net.smokeybbq.bittermelon.miscellaneous.Stumble;
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
}
