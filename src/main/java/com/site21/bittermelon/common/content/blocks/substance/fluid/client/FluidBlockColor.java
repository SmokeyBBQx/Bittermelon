package com.site21.bittermelon.common.content.blocks.substance.fluid.client;

import com.site21.bittermelon.common.content.blocks.substance.fluid.FluidBlockEntity;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidBlockColor implements BlockColor {
    @Override
    public int getColor(@NotNull BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex) {
        if (level == null || pos == null) {
            return 0;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity == null) {
            blockEntity = level.getBlockEntity(pos.below());
            if (blockEntity == null) {
                return -1;
            }
        }
        if (blockEntity instanceof FluidBlockEntity fluidBlockEntity) {
            return fluidBlockEntity.getColor();
        } else {
            return -1;
        }
    }
}
