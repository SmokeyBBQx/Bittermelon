package com.site21.bittermelon.content.blocks.devices.implementations.speaker;

import com.site21.bittermelon.content.blocks.devices.IElectronic;
import com.site21.bittermelon.content.blocks.devices.connection.*;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpeakerBlockEntity extends BlockEntity implements IElectronic {
    private final Map<String, InputPort> inputPorts = new HashMap<>();
    private String address = "";

    public SpeakerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        address = generateAddress("SPE");
    }

    private void initializePorts() {
        InputPort BROADCAST = new InputPort("BROADCAST", this::broadcast, worldPosition);

        inputPorts.put(BROADCAST.id, BROADCAST);
    }

    private void broadcast(Signal signal) {

    }

    @Override
    public Map<String, InputPort> getInputPorts() {
        return inputPorts;
    }

    @Override
    public String getAddress() {
        return address;
    }
}
