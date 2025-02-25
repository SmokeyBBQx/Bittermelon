package com.site21.bittermelon.content.blocks.devices.implementations.environmentsensor;

import com.site21.bittermelon.content.atmosphere.AtmosHandler;
import com.site21.bittermelon.content.atmosphere.AtmosInstance;
import com.site21.bittermelon.content.blocks.devices.IElectronic;
import com.site21.bittermelon.content.blocks.devices.connection.*;
import com.site21.bittermelon.content.substance.SubstanceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.ENVIRONMENT_SENSOR_BLOCK_ENTITY;

public class EnvironmentSensorBlockEntity extends BlockEntity implements IElectronic {
    private static final float FALLBACK_TEMPERATURE = 22;
    private static final float FALLBACK_PRESSURE = 101.325f;
    private final Map<String, OutputPort> outputPorts = new HashMap<>();
    private String address;
    private float temperature = 0; // Kelvin
    private float pressure = 0; // kPa
    private final Set<SubstanceStack> gases = new HashSet<>();
    private final List<WireConnection> connections = new ArrayList<>();

    public EnvironmentSensorBlockEntity(BlockPos pos, BlockState blockState) {
        super(ENVIRONMENT_SENSOR_BLOCK_ENTITY.get(), pos, blockState);
        address = generateAddress("ENV");
        initializePorts();
    }

    private void initializePorts() {
        OutputPort TEMPERATURE = new OutputPort("temperature", this::getTemperature, worldPosition);
        OutputPort PRESSURE = new OutputPort("pressure", this::getPressure, worldPosition);

        outputPorts.put(TEMPERATURE.id(), TEMPERATURE);
        outputPorts.put(PRESSURE.id(), PRESSURE);
    }

    public void tick() {
        if (level == null) return;
        AtmosInstance atmosInstance = AtmosHandler.getAtmosInstanceAt(level, worldPosition.above());
        // TODO: Remove above once shape is fixed
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

        for (WireConnection connection : connections) {
            connection.update();
        }
    }

    @Override
    public Map<String, OutputPort> getOutputPorts() {
        return outputPorts;
    }

    @Override
    public Map<String, InputPort> getInputPorts() {
        return Map.of();
    }

    public String getAddress() {
        return address;
    }

    @Override
    public List<WireConnection> getConnections() {
        return connections;
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

        saveConnections(tag);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        address = tag.getString("address");
        temperature = tag.getFloat("temperature");
        pressure = tag.getFloat("pressure");

        loadConnections(tag, level);
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
