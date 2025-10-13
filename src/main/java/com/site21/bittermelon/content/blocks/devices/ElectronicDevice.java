package com.site21.bittermelon.content.blocks.devices;

import com.site21.bittermelon.content.blocks.devices.wiring.InputPort;
import com.site21.bittermelon.content.blocks.devices.wiring.OutputPort;
import com.site21.bittermelon.content.blocks.powergrid.PowerCell;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.LinkedHashMap;
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
        return new LinkedHashMap<>();
    }

    /**
     * Returns a map of all input ports this device has.
     * Default implementation returns an empty map.
     *
     * @return map of port ID to InputPort objects
     */
    default Map<String, InputPort> getInputPorts() {
        return new LinkedHashMap<>();
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
            outputPort.connectTo(inputPort);
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
            inputPort.connectTo(outputPort);
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
            if (port.connectedPos != null && port.connectedPortId != null) {
                portTag.putLong("connectedPos", port.connectedPos.asLong());
                portTag.putString("connectedID", port.connectedPortId);
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
     */
    default void loadInputPorts(@NotNull CompoundTag tag) {
        ListTag portsListTag = tag.getList("inputPorts", Tag.TAG_COMPOUND);
        for (int i = 0; i < portsListTag.size(); i++) {
            CompoundTag portTag = portsListTag.getCompound(i);
            String portId = portTag.getString("id");
            InputPort port = findInputPort(portId);

            if (port != null && portTag.contains("connectedPos")) {
                port.connectedPos = BlockPos.of(portTag.getLong("connectedPos"));
                port.connectedPortId = portTag.getString("connectedID");
                port.invalidateCache();
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
            if (port.connectedPos != null && port.connectedPortId != null) {
                portTag.putLong("connectedPos", port.connectedPos.asLong());
                portTag.putString("connectedID", port.connectedPortId);
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
     */
    default void loadOutputPorts(@NotNull CompoundTag tag) {
        ListTag portsListTag = tag.getList("outputPorts", Tag.TAG_COMPOUND);
        for (int i = 0; i < portsListTag.size(); i++) {
            CompoundTag portTag = portsListTag.getCompound(i);
            String portId = portTag.getString("id");
            OutputPort port = findOutputPort(portId);

            if (port != null && portTag.contains("connectedPos")) {
                port.connectedPos = BlockPos.of(portTag.getLong("connectedPos"));
                port.connectedPortId = portTag.getString("connectedID");
                port.invalidateCache();
            }
        }
    }

    /**
     * Clears all electronic connections when this device is removed from the world.
     * Disconnects all connected ports on other devices and marks them as changed for saving.
     *
     * @param level the world level for looking up block entities
     */
    default void clearElectronicData(Level level) {
        if (level == null || level.isClientSide) return;

        Set<BlockEntity> updatedBlockEntities = new HashSet<>();

        // Disconnect all devices connected to our input ports
        for (InputPort port : getInputPorts().values()) {
            if (port.connectedPos != null) {
                BlockEntity connectedBE = level.getBlockEntity(port.connectedPos);
                if (connectedBE instanceof ElectronicDevice electronic) {
                    OutputPort connectedPort = electronic.findOutputPort(port.connectedPortId);
                    if (connectedPort != null) {
                        connectedPort.disconnect();
                        updatedBlockEntities.add(connectedBE);
                    }
                }
                port.disconnect();
            }
        }

        // Disconnect all devices connected to our output ports
        for (OutputPort port : getOutputPorts().values()) {
            if (port.connectedPos != null) {
                BlockEntity connectedBE = level.getBlockEntity(port.connectedPos);
                if (connectedBE instanceof ElectronicDevice electronic) {
                    InputPort connectedPort = electronic.findInputPort(port.connectedPortId);
                    if (connectedPort != null) {
                        connectedPort.disconnect();
                        updatedBlockEntities.add(connectedBE);
                    }
                }
                port.disconnect();
            }
        }

        // Mark all affected block entities as changed for saving
        for (BlockEntity blockEntity : updatedBlockEntities) {
            blockEntity.setChanged();
        }
    }

    default void drawPower(Level level, float draw) {
        InputPort powerSupplyPort = getInputPorts().get("POWER_SUPPLY");
        if (powerSupplyPort == null) return;

        OutputPort connectedPort = powerSupplyPort.getConnectedPort(level);
        if (connectedPort == null) return;

        if (level.getBlockEntity(connectedPort.pos) instanceof PowerCell powerCell) {
            setSupply(powerCell.drawPower(connectedPort.id, draw));
            setDraw(draw);
        }
    }

    void setSupply(float supply);

    void setDraw(float draw);
}
