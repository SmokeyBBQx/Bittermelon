package com.site21.bittermelon.common.content.entities.base;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.schedule.Activity;

import java.util.function.Function;

public class NeedInstance {
    private float decayRate;
    private float value;
    private Function<Float, Double> priorityEvaluator;
    private Activity activity;

    public NeedInstance(float decayRate, float value, Function<Float, Double> priorityEvaluator, Activity activity) {
        this.decayRate = decayRate;
        this.value = value;
        this.priorityEvaluator = priorityEvaluator;
        this.activity = activity;
    }

    public NeedInstance(float decayRate, Function<Float, Double> priorityEvaluator, Activity activity) {
        this(decayRate, 100, priorityEvaluator, activity);
    }

    public float getValue() {
        return value;
    }

    public Function<Float, Double> getPriorityEvaluator() {
        return priorityEvaluator;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void modifyValue(float value) {
        this.value = Mth.clamp(this.value + value, 0, 100);
    }

    public void decay() {
        modifyValue(-decayRate);
    }

    public double evaluatePriority() {
        return priorityEvaluator.apply(value);
    }
}
