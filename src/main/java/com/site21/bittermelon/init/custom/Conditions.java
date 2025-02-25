package com.site21.bittermelon.init.custom;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.devices.connection.Signal;
import com.site21.bittermelon.content.substance.Substance;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.CONDITIONS_REGISTRY_KEY;
import static com.site21.bittermelon.init.neoforge.BitterRegistries.SUBSTANCE_REGISTRY_KEY;

public class Conditions {
    public static final DeferredRegister<Function<Signal, Signal>> CONDITIONS = DeferredRegister.create(CONDITIONS_REGISTRY_KEY, Bittermelon.MOD_ID);

//    public static final Supplier<Function<Signal, Signal>> NOT = CONDITIONS.register("not",
//            () -> value -> !value);
//
//    public static final Supplier<Function<Signal[], Signal>> AND = CONDITIONS.register("and",
//            () -> values -> values[0] && values[1]);
//
//    public static final Supplier<Function<Boolean[], Boolean>> OR = CONDITIONS.register("or",
//            () -> values -> values[0] || values[1]);
//
//    public static final Supplier<Function<Boolean[], Boolean>> XOR = CONDITIONS.register("xor",
//            () -> values -> values[0] ^ values[1]);
//
//    public static final Supplier<Function<Number[], Boolean>> GREATER_THAN = CONDITIONS.register("greater_than",
//            () -> values -> values[0].doubleValue() > values[1].doubleValue());
//
//    public static final Supplier<Function<Number[], Boolean>> LESS_THAN = CONDITIONS.register("less_than",
//            () -> values -> values[0].doubleValue() < values[1].doubleValue());
//
//    public static final Supplier<Function<Number[], Boolean>> EQUALS = CONDITIONS.register("equals",
//            () -> values -> values[0].doubleValue() == values[1].doubleValue());
}
