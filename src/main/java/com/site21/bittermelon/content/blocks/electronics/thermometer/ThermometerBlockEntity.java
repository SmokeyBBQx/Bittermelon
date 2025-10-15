package com.site21.bittermelon.content.blocks.electronics.thermometer;

import com.site21.bittermelon.systems.atmosphere.AtmosHandler;
import com.site21.bittermelon.systems.atmosphere.AtmosInstance;
import com.site21.bittermelon.systems.electronics.ElectronicBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.THERMOMETER_BLOCK_ENTITY;

public class ThermometerBlockEntity extends ElectronicBlockEntity {
    private float temperature = 0;

    public ThermometerBlockEntity(BlockPos pos, BlockState blockState) {
        super(THERMOMETER_BLOCK_ENTITY.get(), pos, blockState);
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
            this.temperature = temperature;
            setChanged();
        }
    }

    public float getTemperature() {
        return temperature - 273.15f;
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        output.putFloat("temperature", temperature);
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        temperature = input.getFloatOr("temperature", 0);
    }
}
