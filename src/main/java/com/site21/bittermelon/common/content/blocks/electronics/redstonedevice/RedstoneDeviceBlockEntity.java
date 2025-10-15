package com.site21.bittermelon.common.content.blocks.electronics.redstonedevice;

import com.site21.bittermelon.common.systems.electronics.ElectronicBlockEntity;
import com.site21.bittermelon.common.systems.electronics.wiring.InputPort;
import com.site21.bittermelon.common.systems.electronics.wiring.OutputPort;
import com.site21.bittermelon.common.systems.electronics.wiring.Signal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.REDSTONE_DEVICE_BLOCK_ENTITY;

public class RedstoneDeviceBlockEntity extends ElectronicBlockEntity {
    private final Map<String, InputPort> inputPorts;
    private final Map<String, OutputPort> outputPorts;
    private int powerLevel = 0;

    public RedstoneDeviceBlockEntity(BlockPos pos, BlockState blockState) {
        super(REDSTONE_DEVICE_BLOCK_ENTITY.get(), pos, blockState);

        inputPorts = new LinkedHashMap<>(Map.of(
            "POWER", new InputPort("POWER", this::receivePower, worldPosition)
        ));

        outputPorts = new LinkedHashMap<>(Map.of(
            "POWERED", new OutputPort("POWERED", this::isPowered, worldPosition)
        ));
    }

    @Override
    public Map<String, InputPort> getInputPorts() {
        return inputPorts;
    }

    @Override
    public Map<String, OutputPort> getOutputPorts() {
        return outputPorts;
    }

    private void receivePower(@NotNull Signal signal) {
        BlockState blockState = getBlockState();

        if (signal.value() instanceof Number number) {
            if (powerLevel != number.intValue()) {
                powerLevel = number.intValue();
                setChanged();
                level.setBlock(worldPosition, blockState.setValue(RedstoneDeviceBlock.POWERED, powerLevel != 0), 3);
            }
        } else {
            int newPowerLevel = signal.asBoolean() ? 15 : 0;
            if (powerLevel != newPowerLevel) {
                powerLevel = newPowerLevel;
                setChanged();
                level.setBlock(worldPosition, blockState.setValue(RedstoneDeviceBlock.POWERED, powerLevel != 0), 3);
            }
        }
    }

    private boolean isPowered() {
        return getBlockState().getValue(RedstoneDeviceBlock.POWERED);
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        output.putInt("powerLevel", powerLevel);
        saveInputPorts(output);
        saveOutputPorts(output);
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        powerLevel = input.getIntOr("powerLevel", 0);
        loadInputPorts(input);
        loadOutputPorts(input);
    }
}
