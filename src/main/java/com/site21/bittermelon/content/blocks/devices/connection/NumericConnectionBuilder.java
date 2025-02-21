package com.site21.bittermelon.content.blocks.devices.connection;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class NumericConnectionBuilder {
    @Contract("_, _, _ -> new")
    public static @NotNull Connection<Float, Boolean> greaterThan(OutputPort<Float> output, InputPort<Boolean> input, float threshold) {
        CompoundTag data = new CompoundTag();
        data.putFloat("threshold", threshold);
        return new Connection<>(output, input, value -> value > threshold, "greater_than", data);
    }

    @Contract("_, _, _ -> new")
    public static @NotNull Connection<Float, Boolean> lessThan(OutputPort<Float> output, InputPort<Boolean> input, float threshold) {
        CompoundTag data = new CompoundTag();
        data.putFloat("threshold", threshold);
        return new Connection<>(output, input, value -> value < threshold, "less_than", data);
    }

    @Contract("_, _, _ -> new")
    public static @NotNull Connection<Float, Float> scale(OutputPort<Float> output, InputPort<Float> input, float factor) {
        CompoundTag data = new CompoundTag();
        data.putFloat("factor", factor);
        return new Connection<>(output, input, value -> value * factor, "scale", data);
    }
}

