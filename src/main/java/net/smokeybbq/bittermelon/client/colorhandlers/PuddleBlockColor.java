package net.smokeybbq.bittermelon.client.colorhandlers;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.smokeybbq.bittermelon.blocks.blockentities.PuddleBlockEntity;
import org.jetbrains.annotations.Nullable;

public class PuddleBlockColor implements BlockColor {
    @Override
    public int getColor(BlockState pState, @Nullable BlockAndTintGetter pLevel, @Nullable BlockPos pPos, int pTintIndex) {

        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity == null) {
            blockEntity = pLevel.getBlockEntity(pPos.below());
            if (blockEntity == null) {
                return -1;
            }
        }
        if (blockEntity instanceof PuddleBlockEntity puddleBlockEntity) {
            return puddleBlockEntity.getColor();
        } else {
            return -1;
        }
    }
}
