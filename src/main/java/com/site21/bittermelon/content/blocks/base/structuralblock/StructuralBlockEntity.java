package com.site21.bittermelon.content.blocks.base.structuralblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.BitterBlockEntities.STRUCTURAL_BLOCK_ENTITY;

public class StructuralBlockEntity extends BlockEntity {
    private float breakProgress = 0.0f;

    public StructuralBlockEntity(BlockPos pos, BlockState state) {
        super(STRUCTURAL_BLOCK_ENTITY.get(), pos, state);
    }

    public void setBreakProgress(float progress) {
        this.breakProgress = Math.min(1, progress);
        setChanged();
    }

    public float getBreakProgress() {
        return breakProgress;
    }

    public int getBreakStage() {
        return Math.min(9, (int)(getBreakProgress() * 10));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putFloat("breakProgress", breakProgress);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        breakProgress = tag.getFloat("breakProgress");
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        syncToClient();
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("breakProgress", breakProgress);
        return tag;
    }

    private void syncToClient() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }
}