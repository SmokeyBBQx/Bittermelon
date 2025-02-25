package com.site21.bittermelon.content.blocks.devices.implementations.panicbutton;

import com.site21.bittermelon.content.blocks.devices.IElectronic;
import com.site21.bittermelon.content.blocks.devices.connection.Connection;
import com.site21.bittermelon.content.blocks.devices.connection.InputPort;
import com.site21.bittermelon.content.blocks.devices.connection.OutputPort;
import com.site21.bittermelon.content.blocks.devices.connection.WireConnection;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanicButtonBlockEntity extends BlockEntity implements IElectronic {
    private final Map<String, OutputPort> outputPorts = new HashMap<>();
    private boolean isOn = false;

    public PanicButtonBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    private void initializePorts() {
        OutputPort ON = new OutputPort("ON", this::isOn, worldPosition);

        outputPorts.put(ON.id(), ON);
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        if (on != isOn) {
            isOn = on;
            setChanged();
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

    @Override
    public String getAddress() {
        return "";
    }

    @Override
    public List<WireConnection> getConnections() {
        return List.of();
    }
}
