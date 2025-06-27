package com.site21.bittermelon.content.blocks.devices.implementations.thermometer;

import com.site21.bittermelon.content.atmosphere.AtmosHandler;
import com.site21.bittermelon.content.atmosphere.AtmosInstance;
import com.site21.bittermelon.content.blocks.devices.ElectronicBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.THERMOMETER_BLOCK_ENTITY;

public class ThermometerBlockEntity extends ElectronicBlockEntity {
    private float temperature = 0;

    public ThermometerBlockEntity(BlockPos pos, BlockState blockState) {
        super(THERMOMETER_BLOCK_ENTITY.get(), pos, blockState);
    }

    public void tick() {
        if (level == null) return;
        AtmosInstance atmosInstance = AtmosHandler.getAtmosInstanceAt(level, worldPosition);
        if (atmosInstance == null) {
            if (temperature != 22) {
                temperature = 22;
                setChanged();
            }
            return;
        }
        float temperature = atmosInstance.getTemperature();

        if (temperature != this.temperature) {
            this.temperature = temperature;
            setChanged();
        }
    }

    public float getTemperature() {
        return temperature - 273.15f;
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

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
