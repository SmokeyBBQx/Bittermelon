package com.site21.bittermelon.content.blocks.powergrid.distributionboard;

import com.site21.bittermelon.content.blocks.devices.ElectronicBlockEntity;
import com.site21.bittermelon.content.blocks.devices.wiring.InputPort;
import com.site21.bittermelon.content.blocks.devices.wiring.OutputPort;
import com.site21.bittermelon.content.blocks.devices.wiring.Signal;
import com.site21.bittermelon.content.blocks.powergrid.PowerCell;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.DISTRIBUTION_BOARD_BLOCK_ENTITY;

public class DistributionBoardBlockEntity extends ElectronicBlockEntity implements PowerCell {
    private static final int MAX_DRAW = 1000;

    private final Map<String, OutputPort> outputPorts;
    private final Map<String, InputPort> inputPorts;
    private boolean mainSwitch = true;
    private final Map<String, Boolean> breakers;
    private float draw;
    private float supply = 300;
    private final Map<String, Float> loads;

    public DistributionBoardBlockEntity(BlockPos pos, BlockState blockState) {
        super(DISTRIBUTION_BOARD_BLOCK_ENTITY.get(), pos, blockState);
        breakers = new LinkedHashMap<>();
        loads = new HashMap<>();

        outputPorts = new LinkedHashMap<>();
        for (int i = 1; i <= 8; i++) {
            String key = "WAY_" + i;
            outputPorts.put(key, new OutputPort(key, this::supplyPower, worldPosition));
            breakers.put(key, false);
        }

        inputPorts = Map.of(
                "SUPPLY", new InputPort("SUPPLY", this::receivePower, worldPosition)
        );

    }

    public void tick() {
    }

    @Contract(mutates = "this")
    private void receivePower(@NotNull Signal signal) {
        supply = (float) signal.value();
    }

    private boolean supplyPower() {
        return true;
    }

    public float drawPower(String port, float requestedDraw) {
        // If main switch is off or breaker is off, no power is drawn
        if (!mainSwitch) return 0;
        if (!breakers.getOrDefault(port, false)) return 0;

        loads.put(port, requestedDraw);

        float totalDemand = loads.values().stream().reduce(0f, Float::sum);

        // Check for overload
        if (totalDemand > MAX_DRAW) {
            setMainSwitch(false);
            loads.clear();
            return 0;
        }

        // Distribute power proportionally if demand exceeds supply
        if (totalDemand > supply) {
            float actualDraw = (supply / totalDemand) * requestedDraw;
            loads.put(port, actualDraw);
            return actualDraw;
        }

        return requestedDraw;
    }

    public void setMainSwitch(boolean value) {
        if (mainSwitch != value) {
            mainSwitch = value;
            setChanged();
        }
    }

    public void toggleMainSwitch() {
        mainSwitch = !mainSwitch;
        setChanged();
    }

    public void toggleBreaker(String breakerName) {
        if (breakers.computeIfPresent(breakerName, (k, v) -> !v) != null) {
            setChanged();
        }
    }

    public boolean isMainSwitchOn() {
        return mainSwitch;
    }

    public boolean isBreakerOn(String breakerName) {
        return breakers.getOrDefault(breakerName, false);
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
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        output.putBoolean("mainSwitch", mainSwitch);

        ValueOutput.TypedOutputList<CompoundTag> breakersList = output.list("breakers", CompoundTag.CODEC);
        for (Map.Entry<String, Boolean> entry : breakers.entrySet()) {
            CompoundTag breakerTag = new CompoundTag();
            breakerTag.putString("name", entry.getKey());
            breakerTag.putBoolean("state", entry.getValue());
            breakersList.add(breakerTag);
        }

        saveInputPorts(output);
        saveOutputPorts(output);

    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        mainSwitch = input.getBooleanOr("mainSwitch", true);
        input.list("breakers", CompoundTag.CODEC).ifPresent(breakersList -> {
            breakers.clear();
            for (CompoundTag breakerTag : breakersList) {
                String name = breakerTag.getStringOr("name", "UNKNOWN");
                boolean state = breakerTag.getBooleanOr("state", false);
                breakers.put(name, state);
            }
        });

        loadInputPorts(input);
        loadOutputPorts(input);
    }
}
