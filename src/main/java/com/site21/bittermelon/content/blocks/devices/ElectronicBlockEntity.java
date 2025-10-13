package com.site21.bittermelon.content.blocks.devices;

import com.site21.bittermelon.init.neoforge.BitterSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class ElectronicBlockEntity extends BlockEntity implements ElectronicDevice {
    protected float supply = 0;
    protected float draw = 0;

    public ElectronicBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public float getIdleDraw() {
        return 0;
    }

    public void setSupply(float supply) {
        if (this.supply != supply) {
            this.supply = supply;
            setChanged();
        }
    }

    public void setDraw(float draw) {
        if (this.draw != draw) {
            this.draw = draw;
            setChanged();
        }
    }

    public void drawPower(float draw) {
        ElectronicDevice.super.drawPower(level, draw);
    }

    public boolean isOn() {
        if (supply >= draw) return true;
        if (supply <= 0 || level == null) return false;
        float random = level.getRandom().nextFloat();

        if (random < (supply / draw)) {
            return true;
        } else {
            level.playSound(null, worldPosition, BitterSounds.SPARKS.get(), SoundSource.BLOCKS, 1, 1);
            return false;
        }
    }

    protected void sleep() {
        drawPower(getIdleDraw());
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
