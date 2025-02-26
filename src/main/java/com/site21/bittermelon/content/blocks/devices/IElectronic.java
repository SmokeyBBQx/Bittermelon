package com.site21.bittermelon.content.blocks.devices;

import com.site21.bittermelon.content.blocks.devices.connection.InputPort;
import com.site21.bittermelon.content.blocks.devices.connection.OutputPort;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface IElectronic {
    default Map<String, OutputPort> getOutputPorts() {
        return Map.of();
    }

    default Map<String, InputPort> getInputPorts() {
        return Map.of();
    }

    default OutputPort findOutputPort(String id) {
        return getOutputPorts().get(id);
    }

    default InputPort findInputPort(String id) {
        return getInputPorts().get(id);
    }

    default void connectToOutputPort(String outputPortID, InputPort inputPort) {
        OutputPort outputPort = findOutputPort(outputPortID);
        if (outputPort != null) {
            outputPort.connectedPort = inputPort;
        }
    }

    default void connectToInputPort(String inputPortID, OutputPort outputPort) {
        InputPort inputPort = findInputPort(inputPortID);
        if (inputPort != null) {
            inputPort.connectedPort = outputPort;
        }
    }

    default @NotNull String generateAddress(String prefix) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder id = new StringBuilder();

        id.append(prefix).append("-");

        for (int i = 0; i < 4; i++) {
            int index = (int) (Math.random() * characters.length());
            id.append(characters.charAt(index));
        }

        return id.toString();
    }

    String getAddress();

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
                if (level.getBlockEntity(connectedPos) instanceof IElectronic electronic) {
                    outputPort = electronic.findOutputPort(connectedID);
                }

                port.connectedPort = outputPort;
            }
        }
    }

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
                if (level.getBlockEntity(connectedPos) instanceof IElectronic electronic) {
                    inputPort = electronic.findInputPort(connectedID);
                }

                port.connectedPort = inputPort;
            }
        }
    }
}
