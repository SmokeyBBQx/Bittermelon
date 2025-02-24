package com.site21.bittermelon.content.blocks.devices.connection;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.devices.IElectronic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PLC {
    private final List<Connection<?, ?>> connections = new ArrayList<>();
    public final OutputPort<?>[] inputs;
    public final InputPort<?>[] outputs;

    public PLC(int inputAmount, int outputAmount) {
        inputs = new OutputPort[inputAmount];
        outputs = new InputPort[outputAmount];
    }

    public void update() {
        for (Connection<?, ?> connection : connections) {
            connection.update();
        }
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        ListTag connectionsList = new ListTag();
        for (Connection<?, ?> connection : connections) {
            connectionsList.add(connection.save());
        }
        tag.put("connections", connectionsList);

        ListTag inputsList = new ListTag();
        for (OutputPort<?> port : inputs) {
            CompoundTag portTag = new CompoundTag();
            portTag.putLong("pos", port.pos().asLong());
            portTag.putString("id", port.id());
            inputsList.add(portTag);
        }
        tag.put("inputs", inputsList);

        ListTag outputsList = new ListTag();
        for (InputPort<?> port : outputs) {
            CompoundTag portTag = new CompoundTag();
            portTag.putLong("pos", port.pos().asLong());
            portTag.putString("id", port.id());
            outputsList.add(portTag);
        }
        tag.put("outputs", outputsList);

        return tag;
    }

    public void load(@NotNull CompoundTag tag, @NotNull Level level) {
        connections.clear();
        Arrays.fill(inputs, null);
        Arrays.fill(outputs, null);

        ListTag connectionsList = tag.getList("connections", CompoundTag.TAG_COMPOUND);
        for (int i = 0; i < connectionsList.size(); i++) {
            CompoundTag connectionTag = connectionsList.getCompound(i);
            try {
                Connection<?, ?> connection = Connection.load(connectionTag, level);
                connections.add(connection);
            } catch (IllegalArgumentException e) {
                Bittermelon.LOGGER.error("Failed to load connection: {}", e.getMessage());
            }
        }

        ListTag inputsList = tag.getList("inputs", CompoundTag.TAG_COMPOUND);
        for (int i = 0; i < Math.min(inputsList.size(), inputs.length); i++) {
            CompoundTag portTag = inputsList.getCompound(i);
            BlockPos pos = BlockPos.of(portTag.getLong("pos"));
            String id = portTag.getString("id");

            if (level.getBlockEntity(pos) instanceof IElectronic device) {
                OutputPort<?> port = device.findOutputPort(id);
                if (port != null) {
                    inputs[i] = port;
                }
            }
        }

        ListTag outputsList = tag.getList("outputs", CompoundTag.TAG_COMPOUND);
        for (int i = 0; i < Math.min(outputsList.size(), outputs.length); i++) {
            CompoundTag portTag = outputsList.getCompound(i);
            BlockPos pos = BlockPos.of(portTag.getLong("pos"));
            String id = portTag.getString("id");

            if (level.getBlockEntity(pos) instanceof IElectronic device) {
                InputPort<?> port = device.findInputPort(id);
                if (port != null) {
                    outputs[i] = port;
                }
            }
        }
    }
}
