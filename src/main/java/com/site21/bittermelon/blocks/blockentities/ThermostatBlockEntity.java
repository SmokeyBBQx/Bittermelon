package com.site21.bittermelon.blocks.blockentities;

import com.site21.bittermelon.atmosphere.AtmosHandler;
import com.site21.bittermelon.atmosphere.AtmosInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ThermostatBlockEntity extends BlockEntity {
    private float temperature = 0;
    private float targetTemperature = 22;

    public ThermostatBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public void tick() {
        if (level == null) return;
        AtmosInstance atmosInstance = AtmosHandler.getAtmosInstanceAt(level, worldPosition);
        if (atmosInstance == null) {
            if (temperature != 22) {
                temperature = 22;
                setChanged();
            }
            return;
        }
        float temperature = atmosInstance.getTemperature();

        if (temperature != this.temperature) {
            this.temperature = atmosInstance.getTemperature();
            setChanged();
        }
    }
}
