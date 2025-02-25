package com.site21.bittermelon.content.blocks.devices;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.devices.connection.Connection;
import com.site21.bittermelon.content.blocks.devices.connection.InputPort;
import com.site21.bittermelon.content.blocks.devices.connection.OutputPort;
import com.site21.bittermelon.content.blocks.devices.connection.WireConnection;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
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

    List<WireConnection> getConnections();

    default void saveConnections(CompoundTag tag) {
        ListTag connectionsList = new ListTag();
        for (WireConnection connection : getConnections()) {
            connectionsList.add(connection.save());
        }
        tag.put("connections", connectionsList);
    }

    default void loadConnections(@NotNull CompoundTag tag, Level level) {
        getConnections().clear();

        ListTag connectionsList = tag.getList("connections", CompoundTag.TAG_COMPOUND);
        for (int i = 0; i < connectionsList.size(); i++) {
            CompoundTag connectionTag = connectionsList.getCompound(i);
            WireConnection connection = WireConnection.load(connectionTag, level);
            if (connection != null) {
                getConnections().add(connection);
            }
        }
    }
}
