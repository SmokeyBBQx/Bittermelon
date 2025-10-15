package com.site21.bittermelon.common.content.blocks.powergrid.battery;

import com.site21.bittermelon.common.systems.electronics.ElectronicBlockEntity;
import com.site21.bittermelon.common.systems.electronics.wiring.InputPort;
import com.site21.bittermelon.common.systems.electronics.wiring.OutputPort;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class BatteryBlockEntity extends ElectronicBlockEntity {
    private final Map<String, OutputPort> outputPorts;
    private final Map<String, InputPort> inputPorts;

    public BatteryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);

        outputPorts = Map.of(

        );

        inputPorts = Map.of(
        );
    }


}
