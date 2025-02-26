package com.site21.bittermelon.content.blocks.devices.implementations.panicbutton;

import com.site21.bittermelon.content.blocks.devices.IElectronic;
import com.site21.bittermelon.content.blocks.devices.wiring.OutputPort;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class PanicButtonBlockEntity extends BlockEntity implements IElectronic {
    private boolean isOn = false;

    public PanicButtonBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        if (on != isOn) {
            isOn = on;
            setChanged();
        }
    }

    @Override
    public Map<String, OutputPort> getOutputPorts() {
        return Map.of(
                "ON", new OutputPort("ON", this::isOn, worldPosition)
        );
    }

    @Override
    public String getAddress() {
        return "";
    }
}
