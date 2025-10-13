package com.site21.bittermelon.content.blocks.powergrid.distributionboard;

import com.site21.bittermelon.content.blocks.devices.ElectronicBlockEntity;
import com.site21.bittermelon.content.blocks.devices.wiring.InputPort;
import com.site21.bittermelon.content.blocks.devices.wiring.OutputPort;
import com.site21.bittermelon.content.blocks.devices.wiring.Signal;
import com.site21.bittermelon.content.blocks.powergrid.PowerCell;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.DISTRIBUTION_BOARD_BLOCK_ENTITY;

public class DistributionBoardBlockEntity extends ElectronicBlockEntity implements PowerCell {
    private final Map<String, OutputPort> outputPorts;
    private final Map<String, InputPort> inputPorts;
    private boolean mainSwitch = true;
    private final Map<String, Boolean> breakers;
    private float draw;
    private float supply = 300;
    private int maxDraw = 1000;
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
        if (totalDemand > maxDraw) {
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
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putBoolean("mainSwitch", mainSwitch);

        CompoundTag breakersTag = new CompoundTag();
        for (Map.Entry<String, Boolean> entry : breakers.entrySet()) {
            breakersTag.putBoolean(entry.getKey(), entry.getValue());
        }
        tag.put("breakers", breakersTag);

        saveInputPorts(tag);
        saveOutputPorts(tag);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);

        mainSwitch = tag.getBoolean("mainSwitch");

        if (tag.contains("breakers")) {
            CompoundTag breakersTag = tag.getCompound("breakers");
            breakers.clear();
            for (String key : breakersTag.getAllKeys()) {
                breakers.put(key, breakersTag.getBoolean(key));
            }
        }

        loadInputPorts(tag);
        loadOutputPorts(tag);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
