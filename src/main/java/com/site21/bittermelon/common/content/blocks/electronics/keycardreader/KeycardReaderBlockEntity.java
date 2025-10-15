package com.site21.bittermelon.common.content.blocks.electronics.keycardreader;

import com.site21.bittermelon.common.systems.electronics.ElectronicBlockEntity;
import com.site21.bittermelon.common.systems.electronics.wiring.InputPort;
import com.site21.bittermelon.common.systems.electronics.wiring.OutputPort;
import com.site21.bittermelon.common.systems.electronics.wiring.Signal;
import com.site21.bittermelon.common.systems.personnel.privilege.PrivilegeOwner;
import com.site21.bittermelon.common.systems.personnel.registry.PersonnelEntry;
import com.site21.bittermelon.common.systems.personnel.registry.PersonnelRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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

    public void scan(int id) {
        drawPower(20);
        if (!isOn()) return;

        PersonnelEntry entry = PersonnelRegistry.get(level).getEntry(id);
        if (entry == null) return;

        Map<String, Boolean> requiredPrivileges = getPrivileges();
        Map<String, Boolean> privileges = entry.getPrivileges();
        if (hasPermission(privileges, requiredPrivileges)) {
            triggerAccessGranted();
            level.playSound(null, worldPosition, SoundEvents.NOTE_BLOCK_BELL.value(), SoundSource.BLOCKS);
        }

        sleep();
    }

    public void triggerAccessGranted() {
        InputPort connectedPort = findOutputPort("ACCESS_GRANTED").getConnectedPort(level);
        if (connectedPort != null) {
            connectedPort.receive(new Signal(true));
        }
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
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        serializePrivileges(output);
        saveInputPorts(output);
        saveOutputPorts(output);
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        deserializePrivileges(input);
        loadInputPorts(input);
        loadOutputPorts(input);
    }
}
