package com.site21.bittermelon.content.blocks.devices.connection;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class Connection<T, U> {
    private final OutputPort<T> outputPort;
    private final InputPort<U> inputPort;
    private final Function<T, U> condition;
    private final String conditionId;
    private final CompoundTag conditionData;

    public Connection(OutputPort<T> outputPort, InputPort<U> inputPort,
                      Function<T, U> condition, String conditionId, CompoundTag conditionData) {
        this.outputPort = outputPort;
        this.inputPort = inputPort;
        this.condition = condition;
        this.conditionId = conditionId;
        this.conditionData = conditionData;
    }

    public void update() {
        T value = outputPort.output().get();
        inputPort.action().accept(condition.apply(value));
    }

    public String getConditionId() {
        return conditionId;
    }

    public void saveConditionData(@NotNull CompoundTag tag) {
        tag.merge(conditionData);
    }
}
