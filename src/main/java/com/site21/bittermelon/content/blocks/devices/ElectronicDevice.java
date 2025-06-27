package com.site21.bittermelon.content.blocks.devices;

import com.site21.bittermelon.content.blocks.devices.wiring.InputPort;
import com.site21.bittermelon.content.blocks.devices.wiring.OutputPort;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Interface for blocks that can function as electronic devices with I/O for wiring.
 * Provides implementation for port updating and serialization.
 * Implementing classes should override getInputPorts() and/or getOutputPorts() to define their ports.
 * If not extending from ElectronicBlockEntity, clearElectronicData() should be added to
 * the block entity's removal method (typically, setRemoved())
 */
public interface ElectronicDevice {

    /**
     * Returns a map of all output ports this device has.
     * Default implementation returns an empty map.
     *
     * @return map of port ID to OutputPort objects
     */
    default Map<String, OutputPort> getOutputPorts() {
        return Map.of();
    }

    /**
     * Returns a map of all input ports this device has.
     * Default implementation returns an empty map.
     *
     * @return map of port ID to InputPort objects
     */
    default Map<String, InputPort> getInputPorts() {
        return Map.of();
    }

    /**
     * Finds an output port by its ID.
     *
     * @param id the port identifier
     * @return the OutputPort with the given ID, or null if not found
     */
    default OutputPort findOutputPort(String id) {
        return getOutputPorts().get(id);
    }

    /**
     * Finds an input port by its ID.
     *
     * @param id the port identifier
     * @return the InputPort with the given ID, or null if not found
     */
    default InputPort findInputPort(String id) {
        return getInputPorts().get(id);
    }

    /**
     * Connects an input port to one of this device's output ports.
     * Creates a unidirectional connection from output to input.
     *
     * @param outputPortID the ID of this device's output port
     * @param inputPort the input port to connect to
     */
    default void connectToOutputPort(String outputPortID, InputPort inputPort) {
        OutputPort outputPort = findOutputPort(outputPortID);
        if (outputPort != null) {
            outputPort.connectedPort = inputPort;
        }
    }

    /**
     * Connects an output port to one of this device's input ports.
     * Creates a unidirectional connection from output to input.
     *
     * @param inputPortID the ID of this device's input port
     * @param outputPort the output port to connect to
     */
    default void connectToInputPort(String inputPortID, OutputPort outputPort) {
        InputPort inputPort = findInputPort(inputPortID);
        if (inputPort != null) {
            inputPort.connectedPort = outputPort;
        }
    }

    /**
     * Serializes all input port data to NBT.
     * Saves port IDs and their connection information (position and connected port ID).
     *
     * @param tag the CompoundTag to write data to
     */
    default void saveInputPorts(CompoundTag tag) {
        ListTag portsListTag = new ListTag();

        for (InputPort port : getInputPorts().values()) {
            CompoundTag portTag = new CompoundTag();

            portTag.putString("id", port.id);
            if (port.connectedPort != null) {
                portTag.putLong("connectedPos", port.connectedPort.pos.asLong());
                portTag.putString("connectedID", port.connectedPort.id);
            }

            portsListTag.add(portTag);
        }

        tag.put("inputPorts", portsListTag);
    }

    /**
     * Deserializes input port data from NBT and restores connections.
     * Looks up connected devices by position and reconnects ports by ID.
     *
     * @param tag the CompoundTag to read data from
     * @param level the world level for looking up block entities
     */
    default void loadInputPorts(@NotNull CompoundTag tag, Level level) {
        ListTag portsListTag = tag.getList("inputPorts", Tag.TAG_COMPOUND);

        for (int i = 0; i < portsListTag.size(); i++) {
            CompoundTag portTag = portsListTag.getCompound(i);
            String portId = portTag.getString("id");
            InputPort port = findInputPort(portId);

            if (portTag.contains("connectedPos") && portTag.contains("connectedID")) {
                BlockPos connectedPos = BlockPos.of(portTag.getLong("connectedPos"));
                String connectedID = portTag.getString("connectedID");

                OutputPort outputPort = null;
                if (level.getBlockEntity(connectedPos) instanceof ElectronicDevice electronic) {
                    outputPort = electronic.findOutputPort(connectedID);
                }

                port.connectedPort = outputPort;
            }
        }
    }

    /**
     * Serializes all output port data to NBT.
     * Saves port IDs and their connection information (position and connected port ID).
     *
     * @param tag the CompoundTag to write data to
     */
    default void saveOutputPorts(CompoundTag tag) {
        ListTag portsListTag = new ListTag();

        for (OutputPort port : getOutputPorts().values()) {
            CompoundTag portTag = new CompoundTag();

            portTag.putString("id", port.id);

            if (port.connectedPort != null) {
                portTag.putLong("connectedPos", port.connectedPort.pos.asLong());
                portTag.putString("connectedID", port.connectedPort.id);
            }

            portsListTag.add(portTag);
        }

        tag.put("outputPorts", portsListTag);
    }

    /**
     * Deserializes output port data from NBT and restores connections.
     * Looks up connected devices by position and reconnects ports by ID.
     *
     * @param tag the CompoundTag to read data from
     * @param level the world level for looking up block entities
     */
    default void loadOutputPorts(@NotNull CompoundTag tag, Level level) {
        ListTag portsListTag = tag.getList("outputPorts", Tag.TAG_COMPOUND);

        for (int i = 0; i < portsListTag.size(); i++) {
            CompoundTag portTag = portsListTag.getCompound(i);
            String portId = portTag.getString("id");
            OutputPort port = findOutputPort(portId);

            if (portTag.contains("connectedPos") && portTag.contains("connectedID")) {
                BlockPos connectedPos = BlockPos.of(portTag.getLong("connectedPos"));
                String connectedID = portTag.getString("connectedID");

                InputPort inputPort = null;
                if (level.getBlockEntity(connectedPos) instanceof ElectronicDevice electronic) {
                    inputPort = electronic.findInputPort(connectedID);
                }

                port.connectedPort = inputPort;
            }
        }
    }

    /**
     * Clears all electronic connections when this device is removed from the world.
     * Disconnects all connected ports on other devices and marks them as changed for saving.
     *
     * @param level the world level for looking up block entities
     */
    default void clearElectronicData(@NotNull Level level) {
        if (level.isClientSide) return;
        Set<BlockEntity> updatedBlockEntities = new HashSet<>();

        // Disconnect all devices connected to our input ports
        for (InputPort port : getInputPorts().values()) {
            OutputPort connectedPort = port.connectedPort;
            if (connectedPort != null) {
                connectedPort.connectedPort = null;

                BlockEntity connectedPortBlockEntity = level.getBlockEntity(connectedPort.pos);
                if (connectedPortBlockEntity != null) {
                    updatedBlockEntities.add(connectedPortBlockEntity);
                }
            }
        }

        // Disconnect all devices connected to our output ports
        for (OutputPort port : getOutputPorts().values()) {
            InputPort connectedPort = port.connectedPort;
            if (connectedPort != null) {
                connectedPort.connectedPort = null;

                BlockEntity connectedPortBlockEntity = level.getBlockEntity(connectedPort.pos);
                if (connectedPortBlockEntity != null) {
                    updatedBlockEntities.add(connectedPortBlockEntity);
                }
            }
        }

        // Mark all affected block entities as changed for saving
        for (BlockEntity blockEntity : updatedBlockEntities) {
            blockEntity.setChanged();
        }
    }
}
