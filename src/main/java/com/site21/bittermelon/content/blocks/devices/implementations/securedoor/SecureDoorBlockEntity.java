package com.site21.bittermelon.content.blocks.devices.implementations.securedoor;

import com.site21.bittermelon.content.blocks.devices.IElectronic;
import com.site21.bittermelon.content.blocks.devices.wiring.InputPort;
import com.site21.bittermelon.content.blocks.devices.wiring.OutputPort;
import com.site21.bittermelon.content.blocks.devices.wiring.Signal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SecureDoorBlockEntity extends BlockEntity implements IElectronic {
    private final Map<String, OutputPort> outputPorts;
    private final Map<String, InputPort> inputPorts;
    private boolean isLocked = true;

    public SecureDoorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);

        outputPorts = Map.of(
                "IS_LOCKED", new OutputPort("IS_LOCKED", this::isLocked, worldPosition)
        );

        inputPorts = Map.of(
                "TOGGLE_LOCK", new InputPort("TOGGLE_LOCK", this::toggleState, worldPosition),
                "SET_LOCK", new InputPort("SET_LOCK", this::setState, worldPosition)
        );
    }

    private void toggleState(@NotNull Signal signal) {
        if (signal.asBoolean()) {
            setLocked(!isLocked);
        }
    }

    private void setState(@NotNull Signal signal) {
        setLocked(signal.asBoolean());
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        if (locked != isLocked) {
            isLocked = locked;
            setChanged();
        }
    }

    @Override
    public Map<String, OutputPort> getOutputPorts() {
        return outputPorts;
    }

    @Override
    public Map<String, InputPort> getInputPorts() {
        return inputPorts;
    }

    @Override
    public String getAddress() {
        return "";
    }
}
