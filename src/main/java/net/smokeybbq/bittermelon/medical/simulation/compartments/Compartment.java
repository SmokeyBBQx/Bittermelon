package net.smokeybbq.bittermelon.medical.simulation.compartments;

import net.smokeybbq.bittermelon.medical.simulation.Inhibitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Compartment {
    protected String name;
    protected double volume;
    protected double concentration;
    protected Map<String, Double> concentrations = new HashMap<>();
    protected List<Inhibitor> inhibitors = new ArrayList<>();

    public Compartment(String name, double volume) {
        this.name = name;
        this.volume = volume;
    }

    public double getConcentration() {
        return concentration;
    }

    public double getDerivative() {
        return 0;
    }

    public void updateConcentration(String drugName, double derivative, double timeStep) {
        double updatedConcentration = concentrations.get(drugName) + derivative * timeStep;
        concentrations.put(drugName, updatedConcentration);
    }

    public void addConcentration(String drugName, double concentration) {
        double updatedConcentration = concentrations.get()
    }

    public void addInhibitor(Inhibitor inhibitor) {
        inhibitors.add(inhibitor);
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public String getName() {
        return name;
    }

    public void setRateConstant(double eliminationRateConstant) {
    }

}
