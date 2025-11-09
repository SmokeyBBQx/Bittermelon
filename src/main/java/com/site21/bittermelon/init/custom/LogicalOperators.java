package com.site21.bittermelon.init.custom;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.electronics.wiring.Signal;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.LOGICAL_OPERATORS_REGISTRY_KEY;

public class LogicalOperators {
    public static final DeferredRegister<Function<Float, Function<Signal, Signal>>> LOGICAL_OPERATORS = DeferredRegister.create(LOGICAL_OPERATORS_REGISTRY_KEY, Bittermelon.MOD_ID);

    public static final Supplier<Function<Float, Function<Signal, Signal>>> BUFFER = LOGICAL_OPERATORS.register("buffer",
            () -> value -> signal -> signal);

    public static final Supplier<Function<Float, Function<Signal, Signal>>> NOT = LOGICAL_OPERATORS.register("not",
            () -> value -> signal -> new Signal(!signal.asBoolean()));

    public static final Supplier<Function<Float, Function<Signal, Signal>>> EQUALS = LOGICAL_OPERATORS.register("equals", () ->
            value -> signal -> {
                if (signal.value() instanceof Number number) {
                    return new Signal(number.floatValue() == value);
                }
                return new Signal(false);
            }
    );

    public static final Supplier<Function<Float, Function<Signal, Signal>>> GREATER_THAN = LOGICAL_OPERATORS.register("greater_than",
            () -> value -> signal -> {
                if (signal.value() instanceof Number number) {
                    return new Signal(number.floatValue() > value);
                }
                return new Signal(false);
            });

    public static final Supplier<Function<Float, Function<Signal, Signal>>> LESS_THAN = LOGICAL_OPERATORS.register("less_than",
            () -> value -> signal -> {
                if (signal.value() instanceof Number number) {
                    return new Signal(number.floatValue() < value);
                }
                return new Signal(false);
            });

    public static final Supplier<Function<Float, Function<Signal, Signal>>> GREATER_EQUAL = LOGICAL_OPERATORS.register("greater_equal",
            () -> value -> signal -> {
                if (signal.value() instanceof Number number) {
                    return new Signal(number.floatValue() >= value);
                }
                return new Signal(false);
            });

    public static final Supplier<Function<Float, Function<Signal, Signal>>> LESS_EQUAL = LOGICAL_OPERATORS.register("less_equal",
            () -> value -> signal -> {
                if (signal.value() instanceof Number number) {
                    return new Signal(number.floatValue() <= value);
                }
                return new Signal(false);
            });

    public static final Supplier<Function<Float, Function<Signal, Signal>>> ADD = LOGICAL_OPERATORS.register("add",
            () -> value -> signal -> {
                if (signal.value() instanceof Number number) {
                    return new Signal(number.floatValue() + value);
                }
                return signal;
            });

    public static final Supplier<Function<Float, Function<Signal, Signal>>> SUBTRACT = LOGICAL_OPERATORS.register("subtract",
            () -> value -> signal -> {
                if (signal.value() instanceof Number number) {
                    return new Signal(number.floatValue() - value);
                }
                return signal;
            });

    public static final Supplier<Function<Float, Function<Signal, Signal>>> MULTIPLY = LOGICAL_OPERATORS.register("multiply",
            () -> value -> signal -> {
                if (signal.value() instanceof Number number) {
                    return new Signal(number.floatValue() * value);
                }
                return signal;
            });

    public static final Supplier<Function<Float, Function<Signal, Signal>>> DIVIDE = LOGICAL_OPERATORS.register("divide",
            () -> value -> signal -> {
                if (signal.value() instanceof Number number) {
                    if (Math.abs(value) < 0.00001f) {
                        return new Signal(0.0f);
                    }
                    return new Signal(number.floatValue() / value);
                }
                return signal;
            });

    public static final Supplier<Function<Float, Function<Signal, Signal>>> INCREMENT = LOGICAL_OPERATORS.register("increment",
            () -> value -> signal -> {
                if (signal.value() instanceof Number number) {
                    return new Signal(number.floatValue() + 1.0f);
                }
                return signal;
            });

    public static final Supplier<Function<Float, Function<Signal, Signal>>> DECREMENT = LOGICAL_OPERATORS.register("decrement",
            () -> value -> signal -> {
                if (signal.value() instanceof Number number) {
                    return new Signal(number.floatValue() - 1.0f);
                }
                return signal;
            });

    public static final Supplier<Function<Float, Function<Signal, Signal>>> DOUBLE = LOGICAL_OPERATORS.register("double",
            () -> value -> signal -> {
                if (signal.value() instanceof Number number) {
                    return new Signal(number.floatValue() * 2.0f);
                }
                return signal;
            });

    public static final Supplier<Function<Float, Function<Signal, Signal>>> HALVE = LOGICAL_OPERATORS.register("halve",
            () -> value -> signal -> {
                if (signal.value() instanceof Number number) {
                    return new Signal(number.floatValue() / 2.0f);
                }
                return signal;
            });

    public static final Supplier<Function<Float, Function<Signal, Signal>>> SQUARE = LOGICAL_OPERATORS.register("square",
            () -> value -> signal -> {
                if (signal.value() instanceof Number number) {
                    float n = number.floatValue();
                    return new Signal(n * n);
                }
                return signal;
            });

    public static final Supplier<Function<Float, Function<Signal, Signal>>> SQUARE_ROOT = LOGICAL_OPERATORS.register("square_root",
            () -> value -> signal -> {
                if (signal.value() instanceof Number number) {
                    float n = number.floatValue();
                    if (n >= 0) {
                        return new Signal((float) Math.sqrt(n));
                    } else {
                        return new Signal(0.0f);
                    }
                }
                return signal;
            });

    public static final Supplier<Function<Float, Function<Signal, Signal>>> MODULO = LOGICAL_OPERATORS.register("modulo",
            () -> value -> signal -> {
                if (signal.value() instanceof Number number) {
                    if (Math.abs(value) < 0.00001f) {
                        return new Signal(0.0f);
                    }
                    return new Signal(number.floatValue() % value);
                }
                return signal;
            });

    public static final Supplier<Function<Float, Function<Signal, Signal>>> CONSTANT = LOGICAL_OPERATORS.register("constant",
            () -> value -> signal -> new Signal(value));

    public static final Supplier<Function<Float, Function<Signal, Signal>>> CONSTANT_TRUE = LOGICAL_OPERATORS.register("constant_true",
            () -> value -> signal -> new Signal(true));

    public static final Supplier<Function<Float, Function<Signal, Signal>>> CONSTANT_FALSE = LOGICAL_OPERATORS.register("constant_false",
            () -> value -> signal -> new Signal(false));
}
