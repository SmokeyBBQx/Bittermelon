package com.site21.bittermelon.content.blocks.powergrid.distributionboard;

import com.site21.bittermelon.content.blocks.devices.ElectronicBlockEntity;
import com.site21.bittermelon.content.blocks.devices.wiring.InputPort;
import com.site21.bittermelon.content.blocks.devices.wiring.OutputPort;
import com.site21.bittermelon.content.blocks.devices.wiring.Signal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.DISTRIBUTION_BOARD_BLOCK_ENTITY;

public class DistributionBoardBlockEntity extends ElectronicBlockEntity {
    private final Map<String, OutputPort> outputPorts;
    private final Map<String, InputPort> inputPorts;
    private boolean mainSwitch = true;
    private final Map<String, Boolean> breakers;
    private float draw;
    private float supply = 300;
    private int maxDraw = 1000;
    private final Map<String, Float> loads;
    private int updateTimer = 0;
    private final int UPDATE_THRESHOLD = 20;

    public DistributionBoardBlockEntity(BlockPos pos, BlockState blockState) {
        super(DISTRIBUTION_BOARD_BLOCK_ENTITY.get(), pos, blockState);

        outputPorts = Map.of(
                "WAY_1", new OutputPort("WAY_1", this::supplyPower, worldPosition),
                "WAY_2", new OutputPort("WAY_2", this::supplyPower, worldPosition),
                "WAY_3", new OutputPort("WAY_3", this::supplyPower, worldPosition),
                "WAY_4", new OutputPort("WAY_4", this::supplyPower, worldPosition),
                "WAY_5", new OutputPort("WAY_5", this::supplyPower, worldPosition),
                "WAY_6", new OutputPort("WAY_6", this::supplyPower, worldPosition),
                "WAY_7", new OutputPort("WAY_7", this::supplyPower, worldPosition),
                "WAY_8", new OutputPort("WAY_8", this::supplyPower, worldPosition)
        );

        inputPorts = Map.of(
                "SUPPLY", new InputPort("SUPPLY", this::receivePower, worldPosition)
        );

        breakers = new HashMap<>();
        loads = new HashMap<>();
    }

    public void tick() {
        updateTimer++;

        if (updateTimer >= UPDATE_THRESHOLD) {
            updateTimer = 0;
//            for (String load : loads.keySet()) {
//                outputPorts.get(load).emit();
//            }
            for (OutputPort outputPort : outputPorts.values()) {
                outputPort.update();
            }
        }
    }

    @Contract(mutates = "this")
    private void receivePower(@NotNull Signal signal) {
        supply = (float) signal.value();
    }

    private boolean supplyPower() {
        return true;
    }

    public float drawPower(String port, float requestedDraw) {
        if (!mainSwitch) return 0;
        if (!breakers.getOrDefault(port, true)) return 0;

        loads.put(port, requestedDraw);

        float totalDemand = loads.values().stream().reduce(0f, Float::sum);

        if (totalDemand > maxDraw) {
            setMainSwitch(false);
            loads.clear();
            return 0;
        }

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
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        saveInputPorts(tag);
        saveOutputPorts(tag);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        loadInputPorts(tag, level);
        loadOutputPorts(tag, level);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
