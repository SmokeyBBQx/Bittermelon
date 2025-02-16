package com.site21.bittermelon.blocks.blockentities;

import com.site21.bittermelon.atmosphere.AtmosHandler;
import com.site21.bittermelon.atmosphere.AtmosInstance;
import com.site21.bittermelon.substance.Substance;
import com.site21.bittermelon.substance.SubstanceStack;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class EnvironmentSensorBlockEntity extends BlockEntity implements IDevice {
    private static final float FALLBACK_TEMPERATURE = 22;
    private static final float FALLBACK_PRESSURE = 101.325f;
    private String address;
    private float temperature = 0; // Kelvin
    private float pressure = 0; // kPa
    private final Set<SubstanceStack> gases = new HashSet<>();

    public EnvironmentSensorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        address = generateAddress("ENV");
    }

    public void tick() {
        if (level == null) return;
        AtmosInstance atmosInstance = AtmosHandler.getAtmosInstanceAt(level, worldPosition);
        if (atmosInstance == null) {
            if (temperature != FALLBACK_TEMPERATURE || pressure != FALLBACK_PRESSURE) {
                temperature = FALLBACK_TEMPERATURE;
                pressure = FALLBACK_PRESSURE;
                setChanged();
            }
            return;
        }
        float temperature = atmosInstance.getTemperature();
        float pressure = atmosInstance.getPressure();
        boolean changed = false;

        if (temperature != this.temperature) {
            this.temperature = temperature;
            changed = true;
        }

        if (pressure != this.pressure) {
            this.pressure = pressure;
            changed = true;
        }

        if (!gases.containsAll(atmosInstance.getGases())) {
            gases.addAll(atmosInstance.getGases());
            changed = true;
        }

        if (changed) setChanged();
    }

    public String getAddress() {
        return address;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getPressure() {
        return pressure;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putString("address", address);
        tag.putFloat("temperature", temperature);
        tag.putFloat("pressure", pressure);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        address = tag.getString("address");
        temperature = tag.getFloat("temperature");
        pressure = tag.getFloat("pressure");
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
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
