package net.smokeybbq.bittermelon.medical.simulation.compartments;

import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.simulation.Inhibitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Compartment {
    protected String name;
    protected double volume;
    protected double concentration;
    protected double health = 100;
    protected double bloodFlow;
    protected Map<Substance, Double> concentrations = new HashMap<>();
    protected List<Inhibitor> inhibitors = new ArrayList<>();

    public Compartment(String name, double volume) {
        this.name = name;
        this.volume = volume;
    }

    public double getConcentration(Substance substance) {
        return concentrations.getOrDefault(substance, 0.0);
    }

    public Map<Substance, Double> getConcentrations() {
        return concentrations;
    }

    public double getDerivative() {
        return 0;
    }

    public void updateConcentration(Substance substance, double derivative, double timeStep) {
        double updatedConcentration = concentrations.getOrDefault(substance, 0.0) + derivative * timeStep;

        concentrations.put(substance, updatedConcentration);
    }

    public void addConcentration(Substance substance, double concentration) {
        double updatedConcentration = concentrations.getOrDefault(substance, 0.0) + concentration;
        concentrations.put(substance, updatedConcentration);
    }

    public void clearConcentrationMapping(Substance substance) {
        concentrations.remove(substance);
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

    public double getHealth() {
        return health;
    }

    public double getBloodFlow() {
        return bloodFlow;
    }

    public void addHealth(double health) {
        this.health += health;
    }

    public void addBloodFlow(double bloodFlow) {
        this.bloodFlow += bloodFlow;
    }

    public void setBloodFlow(double bloodFlow) {
        this.bloodFlow = bloodFlow;
    }

    public void removeHealth(double health) {
        this.health -= health;
    }

    public void removeBloodFlow(double bloodFlow) {
        this.bloodFlow -= bloodFlow;
    }

}
