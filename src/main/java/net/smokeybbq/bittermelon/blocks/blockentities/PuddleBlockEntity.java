package net.smokeybbq.bittermelon.blocks.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.smokeybbq.bittermelon.init.BlockEntityInit;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class PuddleBlockEntity extends BlockEntity {
    private int color;
    public PuddleBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.PUDDLE_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
    }

    public int getColor() {
        return color;
    }
}
