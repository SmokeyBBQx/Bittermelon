package com.site21.bittermelon.content.blocks.base.structuralblock;


import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StructuralBlock extends Block implements EntityBlock {
    public StructuralBlock(Properties properties) {
        super(properties);
    }


//    @Override
//    public float getDestroyProgress(@NotNull BlockState state, @NotNull Player player, @NotNull BlockGetter level, @NotNull BlockPos pos) {
//        float progress = state.getValue(BREAK_PROGRESS) / 100.0f;
//
//        if (level instanceof Level realLevel && !realLevel.isClientSide()) {
//            int newProgress = Math.min(100, state.getValue(BREAK_PROGRESS) + 1);
//            int damage = (int) Math.round((newProgress / 100.0) * 10);
//
//            BlockState newState = state
//                    .setValue(BREAK_PROGRESS, newProgress)
//                    .setValue(DAMAGE, damage);
//
//            realLevel.setBlock(pos, newState, 3);

    /// /            realLevel.destroyBlockProgress(1, pos, state.getValue(DAMAGE));
//            player.getItemInHand(InteractionHand.MAIN_HAND).hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
//        }
//
//        if (progress < 1.0f) {
//            return (Math.round(progress * 10) / 10.0f) / 50.0f;
//        } else {
//            return 1.0f;
//        }
//    }

    @Override
    public float getDestroyProgress(@NotNull BlockState state, @NotNull Player player, @NotNull BlockGetter level, @NotNull BlockPos pos) {
//        if (level instanceof Level realLevel && !realLevel.isClientSide()) {
//            if (level.getBlockEntity(pos) instanceof StructuralBlockEntity blockEntity) {
//                float progress = blockEntity.getBreakProgress();
//                float newProgress = Math.min(1.0f, progress + 0.01f * player.getDigSpeed(state, pos));
//                player.getItemInHand(InteractionHand.MAIN_HAND).hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
//                blockEntity.setBreakProgress(newProgress);
//                if (newProgress >= 1.0f) {
//                    realLevel.destroyBlock(pos, true);
//                    return 1.0f;
//                }
//            } else {
//                realLevel.setBlockEntity(new StructuralBlockEntity(pos, state));
//            }
//        }
        return 0;
    }

    @Override
    public void destroy(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state) {
        super.destroy(level, pos, state);
        if (level instanceof Level realLevel) {
            realLevel.removeBlockEntity(pos);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        // Supposed to be null and created only when needed, but renderer won't work without this for some reason
        return new StructuralBlockEntity(blockPos, blockState);
    }
}
