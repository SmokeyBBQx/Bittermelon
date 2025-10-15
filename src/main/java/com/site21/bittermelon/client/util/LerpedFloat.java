package com.site21.bittermelon.client.util;

import net.minecraft.util.Mth;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class LerpedFloat {
    private final Interpolator interpolator;
    private EasingFunction easingFunction;
    private float value;
    private float previousValue;
    private float target;
    private float easeSpeed = 0.01f;

    public LerpedFloat(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull LerpedFloat linear() {
        return new LerpedFloat((p, c, t) -> (float) Mth.lerp(p, c, t));
    }

    public LerpedFloat startWithValue(float startValue) {
        value = startValue;
        previousValue = startValue;
        target = startValue;
        return this;
    }

    public LerpedFloat animate(float value, float speed, EasingFunction easingFunction) {
        target = value;
        easeSpeed = speed;
        this.easingFunction = easingFunction;
        return this;
    }

    public void tick() {
        previousValue = value;

        if (value == target) {
            value = target;
            return;
        }

        value = easingFunction.ease(value, easeSpeed, target);
    }

    public float getLerped(float partialTicks) {
        return interpolator.interpolate(
                partialTicks,
                previousValue,
                value
        );
    }

    public boolean finished() {
        return previousValue == value && value == target;
    }

    @FunctionalInterface
    public interface Interpolator {
        float interpolate(double progress, double current, double target);
    }

    @FunctionalInterface
    public interface EasingFunction {
        EasingFunction LINEAR = (c, s, t) -> (float) (c + Mth.clamp(t - c, -s, s));

        float ease(double current, double speed, double target);
    }
}
