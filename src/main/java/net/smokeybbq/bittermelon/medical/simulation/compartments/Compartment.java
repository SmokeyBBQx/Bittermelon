package net.smokeybbq.bittermelon.medical.simulation.compartments;

import net.smokeybbq.bittermelon.character.medical.MedicalStats;
import net.smokeybbq.bittermelon.medical.substance.Substance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Compartment {
    protected String name;
    protected float immunePrivilege = 100;
    protected float concentration;
    protected float health = 100;
    protected float bloodFlow;
    protected float inflammation = 0.1F;
    protected float tissueOverBloodPartitionCoefficient;
    protected MedicalStats medicalStats;
    protected Map<Substance, Float> concentrations = new HashMap<>();

    public Compartment(String name, MedicalStats medicalStats) {
        this.name = name;
        this.medicalStats = medicalStats;
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

    public void removeConcentration(Substance substance, float concentration) {
        float updatedConcentration = concentrations.getOrDefault(substance, 0.0F) - concentration;
        concentrations.put(substance, Math.max(updatedConcentration, 0));
    }

    public void clearConcentrationMapping(Substance substance) {
        concentrations.remove(substance);
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

    public void increaseBloodFlow(float bloodFlow) {
        this.bloodFlow += bloodFlow;
    }

    public void decreaseBloodFlow(float bloodFlow) {
        this.bloodFlow -= bloodFlow;
    }

    public void setBloodFlow(float bloodFlow) {
        this.bloodFlow = bloodFlow;
    }

    public float getInflammation() {
        return inflammation;
    }

    public void removeHealth(float health) {
        this.health -= health;
        this.health = Math.max(this.health, 0);
    }

    public void increaseInflammation(float value) {
        inflammation += value;
        inflammation = Math.min(inflammation, 1);
        bloodFlow += value;
        bloodFlow = Math.min(bloodFlow, 1);
    }

    public void decreaseInflammation(float value) {
        inflammation -= value;
        inflammation = Math.max(inflammation, 0.1F);
        bloodFlow -= value;
        bloodFlow = Math.max(bloodFlow, 0.1F);
    }

    public MedicalStats getMedicalStats() {
        return medicalStats;
    }

    public void setImmunePrivilege(float value) {
        immunePrivilege = value;
    }

    public void decreaseImmunePrivilege(float value) {
        immunePrivilege -= value;
    }

    public void increaseImmunePrivilege(float value) {
        immunePrivilege += value;
    }

    public float getImmunePrivilege() {
        return immunePrivilege;
    }

    public Compartment getCompartment(String name) {
        return this;
    }

    public Compartment getMainOrgan() {
        return this;
    }
}
