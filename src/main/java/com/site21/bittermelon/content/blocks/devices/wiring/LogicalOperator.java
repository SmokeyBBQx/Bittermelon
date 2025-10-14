package com.site21.bittermelon.content.blocks.devices.wiring;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.LOGICAL_OPERATORS_REGISTRY;

public final class LogicalOperator {
    public final String functionID;
    public final Function<Signal, Signal> function;
    public final Float configValue;

    public LogicalOperator(String functionID, Function<Signal, Signal> function, float configValue) {
        this.functionID = functionID;
        this.function = function;
        this.configValue = configValue;
    }

    public Signal apply(Signal signal) {
        return function.apply(signal);
    }
//
//    public @NotNull CompoundTag save() {
//        CompoundTag tag = new CompoundTag();
//        tag.putString("functionID", functionID);
//        tag.putFloat("configValue", configValue);
//        return tag;
//    }
//
//    public static @NotNull LogicalOperator load(@NotNull CompoundTag tag) {
//        String id = tag.getString("functionID");
//        float config = tag.getFloat("configValue");
//
//        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, id);
//        Optional<Function<Float, Function<Signal, Signal>>> optionalLogicFunctionFunction = LOGICAL_OPERATORS_REGISTRY.getOptional(resourceLocation);
//
//        if (optionalLogicFunctionFunction.isPresent()) {
//            Function<Float, Function<Signal, Signal>> logicFunctionFunction = optionalLogicFunctionFunction.get();
//            Function<Signal, Signal> function = logicFunctionFunction.apply(config);
//            return new LogicalOperator(id, function, config);
//        } else {
//            Bittermelon.LOGGER.warn("Unknown logical operator ID: {}. Falling back to BUFFER operator.", id);
//            return new LogicalOperator("buffer", signal -> signal, config);
//        }
//    }
}
