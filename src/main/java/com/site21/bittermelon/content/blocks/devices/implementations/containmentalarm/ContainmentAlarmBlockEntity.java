package com.site21.bittermelon.content.blocks.devices.implementations.containmentalarm;

import com.site21.bittermelon.content.blocks.devices.IDeviceEntity;
import com.site21.bittermelon.content.blocks.devices.connection.Connection;
import com.site21.bittermelon.content.blocks.devices.connection.InputPort;
import com.site21.bittermelon.content.blocks.devices.connection.OutputPort;
import com.site21.bittermelon.content.blocks.devices.connection.hub.IDeviceHub;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.CONTAINMENT_ALARM_BLOCK_ENTITY;

public class ContainmentAlarmBlockEntity extends BlockEntity implements IDeviceEntity, IDeviceHub {
    private final List<Connection<?, ?>> connections = new ArrayList<>();
    private final Map<String, OutputPort<?>> outputPorts = new HashMap<>();
    private final Map<String, InputPort<?>> inputPorts = new HashMap<>();
    private final List<BlockPos> linkedDevices = new ArrayList<>();
    private boolean isActive = false;
    private boolean isAlerted = false;
    private boolean isEmergency = false;

    public ContainmentAlarmBlockEntity(BlockPos pos, BlockState blockState) {
        super(CONTAINMENT_ALARM_BLOCK_ENTITY.get(), pos, blockState);
        initializePorts();
    }

    private void initializePorts() {
        InputPort<Boolean> SET_ALERT = new InputPort<>("set_alert", this::setAlert, worldPosition);
        InputPort<Boolean> SET_EMERGENCY = new InputPort<>("set_emergency", this::setEmergency, worldPosition);

        inputPorts.put(SET_ALERT.id(), SET_ALERT);
        inputPorts.put(SET_EMERGENCY.id(), SET_EMERGENCY);

        OutputPort<Boolean> IS_ALERTED = new OutputPort<>("is_alerted", this::isAlerted, worldPosition);
        OutputPort<Boolean> IS_EMERGENCY = new OutputPort<>("is_emergency", this::isEmergency, worldPosition);

        outputPorts.put(IS_ALERTED.id(), IS_ALERTED);
        outputPorts.put(IS_EMERGENCY.id(), IS_EMERGENCY);
    }

    public void tick() {
        if (level == null || level.isClientSide) return;

        for (Connection<?, ?> connection : connections) {
            connection.update();
        }
    }

    @Override
    public Map<String, OutputPort<?>> getOutputPorts() {
        return outputPorts;
    }

    @Override
    public Map<String, InputPort<?>> getInputPorts() {
        return inputPorts;
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

    public void addLinkedDevice(BlockPos pos) {
        if (!linkedDevices.contains(pos)) {
            linkedDevices.add(pos);
            setChanged();
        }
    }

    public void removeLinkedDevice(BlockPos pos) {
        if (linkedDevices.remove(pos)) {
            setChanged();
        }
    }

    public List<BlockPos> getLinkedDevices() {
        return Collections.unmodifiableList(linkedDevices);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("isActive", isActive);
        tag.putBoolean("isAlerted", isAlerted);
        tag.putBoolean("isEmergency", isEmergency);

        ListTag connectionsList = new ListTag();
        for (Connection<?, ?> connection : connections) {
            connectionsList.add(connection.save());
        }
        tag.put("connections", connectionsList);

        ListTag linkedDevicesTag = new ListTag();
        for (BlockPos pos : linkedDevices) {
            CompoundTag posTag = new CompoundTag();
            posTag.putLong("pos", pos.asLong());
            linkedDevicesTag.add(posTag);
        }
        tag.put("linkedDevices", linkedDevicesTag);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        isActive = tag.getBoolean("isActive");
        isAlerted = tag.getBoolean("isAlerted");
        isEmergency = tag.getBoolean("isEmergency");

        ListTag connectionsList = tag.getList("connections", ListTag.TAG_COMPOUND);
        if (level == null) return;
        connections.clear();

        for (int i = 0; i < connectionsList.size(); i++) {
            CompoundTag connectionTag = connectionsList.getCompound(i);
            connections.add(Connection.load(connectionTag, level));
        }

        ListTag linkedDevicesTag = tag.getList("linkedDevices", ListTag.TAG_COMPOUND);
        linkedDevices.clear();

        for (int i = 0; i < linkedDevicesTag.size(); i++) {
            CompoundTag posTag = linkedDevicesTag.getCompound(i);
            BlockPos pos = BlockPos.of(posTag.getLong("pos"));
            linkedDevices.add(pos);
        }
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
