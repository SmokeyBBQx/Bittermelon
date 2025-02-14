package com.site21.bittermelon.blocks.blockentities;

import com.site21.bittermelon.atmosphere.AtmosInstance;
import com.site21.bittermelon.atmosphere.AtmosUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.BitterBlockEntities.THERMOMETER_BLOCK_ENTITY;

public class ThermometerBlockEntity extends BlockEntity {
    private float temperature = 0;

    public ThermometerBlockEntity(BlockPos pos, BlockState blockState) {
        super(THERMOMETER_BLOCK_ENTITY.get(), pos, blockState);
    }

    public void tick() {
        if (level == null) return;
        AtmosInstance atmosInstance = AtmosUtils.getAtmosInstanceAt(level, worldPosition);
        if (atmosInstance == null) {
            if (temperature != 22) {
                temperature = 22;
                setChanged();
            }
            return;
        }
        float temperature = atmosInstance.getTemperature();

        if (temperature != this.temperature) {
            this.temperature = atmosInstance.getTemperature();
            setChanged();
        }
    }

    public float getTemperature() {
        return temperature;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putFloat("temperature", temperature);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        temperature = tag.getFloat("temperature");
    }


    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return this.saveCustomOnly(registries);
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
}
