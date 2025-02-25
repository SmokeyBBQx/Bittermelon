package com.site21.bittermelon.content.blocks.devices.connection;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.devices.IElectronic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PLC implements IElectronic {
    private final List<WireConnection> connections = new ArrayList<>();
    private final BlockPos worldPosition;
    private final Level level;

    public PLC(BlockPos worldPosition, Level level) {
        this.worldPosition = worldPosition;
        this.level = level;
    }

    @Override
    public String getAddress() {
        return "";
    }

    @Override
    public Map<String, OutputPort> getOutputPorts() {
        return Map.of(

        );
    }

    @Override
    public Map<String, InputPort> getInputPorts() {
        return Map.of(
                "input_1", new InputPort("input_1", this::handleInput, worldPosition),
                "input_2", new InputPort("input_2", this::handleInput, worldPosition),
                "input_3", new InputPort("input_3", this::handleInput, worldPosition),
                "input_4", new InputPort("input_4", this::handleInput, worldPosition),
                "input_5", new InputPort("input_5", this::handleInput, worldPosition),
                "input_6", new InputPort("input_6", this::handleInput, worldPosition)
        );
    }

    private void handleInput(Signal signal) {

    }

    @Override
    public List<WireConnection> getConnections() {
        return connections;
    }
}

