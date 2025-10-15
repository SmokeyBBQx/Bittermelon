package com.site21.bittermelon.content.blocks.electronics.panicbutton;

import com.site21.bittermelon.systems.electronics.ElectronicDevice;
import com.site21.bittermelon.systems.electronics.ElectronicBlockEntity;
import com.site21.bittermelon.systems.electronics.wiring.OutputPort;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class PanicButtonBlockEntity extends ElectronicBlockEntity implements ElectronicDevice {
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
}
