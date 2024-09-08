package net.smokeybbq.bittermelon.systems.atmospherics;

import net.smokeybbq.bittermelon.systems.substances.Substance;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class AtmosCell {
    private Map<Substance, Float> gasses;
    private float temperature;
    private float activityLevel;
    private final static float R = 8.31F;


    public AtmosCell() {
        gasses = new HashMap<>();
    }

    public float getActivityLevel() {
        return activityLevel;
    }

    public float getTemperature() {
        return temperature;
    }

    public Map<Substance, Float> getGasses() {
        return gasses;
    }

    public void setGasses(Map<Substance, Float> gasses) {
        this.gasses = gasses;
    }
    public void addGas(Substance substance, float amount) {
        gasses.merge(substance, amount, Float::sum);
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getTotalAmount() {
        return gasses.values().stream().reduce(0f, Float::sum);
    }

    public float getPressure() {
        float totalPressure = 0;

        for (float n : gasses.values()) {
            totalPressure += (n * R) * temperature;
        }

        return totalPressure;
    }

    public String getContentsDescription() {
        if (gasses.isEmpty()) {
            return "Empty";
        }

        return gasses.entrySet().stream()
                .map(entry -> String.format("%s: %f", entry.getKey().getName(), entry.getValue()))
                .collect(Collectors.joining(", "));
    }
}
