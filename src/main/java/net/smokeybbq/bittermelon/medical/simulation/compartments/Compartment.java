package net.smokeybbq.bittermelon.medical.simulation.compartments;

import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.simulation.Inhibitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Compartment {
    protected String name;
    protected float volume;
    protected float concentration;
    protected float health = 100;
    protected float bloodFlow;
    protected float tissueOverBloodPartitionCoefficient;

    protected Map<Substance, Float> concentrations = new HashMap<>();
    protected List<Inhibitor> inhibitors = new ArrayList<>();

    public Compartment(String name, float volume) {
        this.name = name;
        this.volume = volume;
    }

    public float getConcentration(Substance substance) {
        return concentrations.getOrDefault(substance, 0.0F);
    }

    public Map<Substance, Float> getConcentrations() {
        return concentrations;
    }

    public float getDerivative() {
        return 0;
    }

    public void updateConcentration(Substance substance, float derivative, float timeStep) {
        float updatedConcentration = concentrations.getOrDefault(substance, 0.0F) + derivative * timeStep;

        concentrations.put(substance, updatedConcentration);
    }

    public void addConcentration(Substance substance, float concentration) {
        float updatedConcentration = concentrations.getOrDefault(substance, 0.0F) + concentration;
        concentrations.put(substance, updatedConcentration);
    }

    public void clearConcentrationMapping(Substance substance) {
        concentrations.remove(substance);
    }

    public void addInhibitor(Inhibitor inhibitor) {
        inhibitors.add(inhibitor);
    }

    public Float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public String getName() {
        return name;
    }

    public void setRateConstant(float eliminationRateConstant) {
    }

    public float getHealth() {
        return health;
    }

    public float getBloodFlow() {
        return bloodFlow;
    }

    public void addHealth(float health) {
        this.health += health;
    }

    public void addBloodFlow(float bloodFlow) {
        this.bloodFlow += bloodFlow;
    }

    public void setBloodFlow(float bloodFlow) {
        this.bloodFlow = bloodFlow;
    }

    public void removeHealth(float health) {
        this.health -= health;
        this.health = Math.max(this.health, 0);
    }

    public void removeBloodFlow(float bloodFlow) {
        this.bloodFlow -= bloodFlow;
    }

}
