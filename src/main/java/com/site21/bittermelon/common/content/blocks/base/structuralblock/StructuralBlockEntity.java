package com.site21.bittermelon.common.content.blocks.base.structuralblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.STRUCTURAL_BLOCK_ENTITY;

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
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        output.putFloat("breakProgress", breakProgress);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        breakProgress = input.getFloatOr("breakProgress", 0.0f);
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