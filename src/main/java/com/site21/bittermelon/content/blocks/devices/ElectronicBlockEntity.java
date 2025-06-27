package com.site21.bittermelon.content.blocks.devices;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class ElectronicBlockEntity extends BlockEntity implements ElectronicDevice {
    public ElectronicBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (level != null) {
            clearElectronicData(level);
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        syncToClient();
    }

    public void syncToClient() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return this.saveCustomOnly(registries);
    }
}
