package com.site21.bittermelon.content.blocks.devices.implementations.containmentalarm;

import com.site21.bittermelon.content.blocks.devices.IDeviceEntity;
import com.site21.bittermelon.content.blocks.devices.connection.Connection;
import com.site21.bittermelon.content.blocks.devices.connection.InputPort;
import com.site21.bittermelon.content.blocks.devices.connection.OutputPort;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class ContainmentAlarmBlockEntity extends BlockEntity implements IDeviceEntity {
    private final List<Connection<?, ?>> connections = new ArrayList<>();
    private final List<String> connectedDevices = new ArrayList<>();
    private boolean isActive = false;
    private boolean isAlerted = false;
    private boolean isEmergency = false;

    public final InputPort<Boolean> SET_ALERT = new InputPort<>("set_alert", this::setAlert);
    public final InputPort<Boolean> SET_EMERGENCY = new InputPort<>("set_emergency", this::setEmergency);

    public final OutputPort<Boolean> IS_ALERTED = new OutputPort<>("is_alerted", this::isAlerted);
    public final OutputPort<Boolean> IS_EMERGENCY = new OutputPort<>("is_emergency", this::isEmergency);

    public ContainmentAlarmBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }


    public void tick() {
        if (level == null || level.isClientSide) return;

        for (Connection<?, ?> connection : connections) {
            connection.update();
        }

        if (isActive != (isAlerted || isEmergency)) {
            isActive = isAlerted || isEmergency;
            setChanged();
        }
    }

    @Override
    public String getAddress() {
        return "";
    }

    public boolean isAlerted() {
        return isAlerted;
    }

    public boolean isEmergency() {
        return isEmergency;
    }

    public void setAlert(boolean value) {
        if (isAlerted != value) {
            isAlerted = value;
            setChanged();
        }
    }

    public void setEmergency(boolean value) {
        if (isEmergency != value) {
            isEmergency = value;
            setChanged();
        }
    }

    public void addConnection(Connection<?, ?> connection) {
        connections.add(connection);
        setChanged();
    }

    public void removeConnection(Connection<?, ?> connection) {
        connections.remove(connection);
        setChanged();
    }

    public void addConnectedDevice(String deviceAddress) {
        if (!connectedDevices.contains(deviceAddress)) {
            connectedDevices.add(deviceAddress);
            setChanged();
        }
    }



}
