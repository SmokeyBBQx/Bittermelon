package com.site21.bittermelon.content.blocks.devices.implementations.keycardscanner;

import com.site21.bittermelon.content.blocks.devices.ElectronicBlockEntity;
import com.site21.bittermelon.content.blocks.devices.wiring.InputPort;
import com.site21.bittermelon.content.blocks.devices.wiring.OutputPort;
import com.site21.bittermelon.content.blocks.devices.wiring.Signal;
import com.site21.bittermelon.content.personnel.privilege.PrivilegeOwner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.KEYCARD_READER_BLOCK_ENTITY;

public class KeycardReaderBlockEntity extends ElectronicBlockEntity implements PrivilegeOwner {
    private final Map<String, Boolean> privileges;

    private final Map<String, OutputPort> outputPorts;
    private final Map<String, InputPort> inputPorts;

    public KeycardReaderBlockEntity(BlockPos pos, BlockState blockState) {
        super(KEYCARD_READER_BLOCK_ENTITY.get(), pos, blockState);
        privileges = new HashMap<>();

        outputPorts = new LinkedHashMap<>(Map.of(
                "ACCESS_GRANTED", new OutputPort("ACCESS_GRANTED", null, worldPosition)
        ));

        inputPorts = new LinkedHashMap<>(Map.of(
                "POWER_SUPPLY", new InputPort("POWER_SUPPLY", this::receivePower, worldPosition)
        ));
    }

    private void receivePower(Signal signal) {
        drawPower(draw);
    }

    public float getIdleDraw() {
        return 5;
    }

    @Override
    public Map<String, Boolean> getPrivileges() {
        return privileges;
    }

    @Override
    public String getName() {
        return "";
    }

    public void triggerAccessGranted() {
        drawPower(20);
        if (!isOn()) return;

        InputPort connectedPort = findOutputPort("ACCESS_GRANTED").getConnectedPort(level);
        if (connectedPort != null) {
            connectedPort.receive(new Signal(true));
        }

        sleep();
    }

    @Override
    public Map<String, InputPort> getInputPorts() {
        return inputPorts;
    }

    @Override
    public Map<String, OutputPort> getOutputPorts() {
        return outputPorts;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);

        saveInputPorts(tag);
        saveOutputPorts(tag);
        serializePrivileges(tag);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);

        loadInputPorts(tag);
        loadOutputPorts(tag);
        deserializePrivileges(tag);
    }
}
