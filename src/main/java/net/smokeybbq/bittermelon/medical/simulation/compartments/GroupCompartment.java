package net.smokeybbq.bittermelon.medical.simulation.compartments;

import net.smokeybbq.bittermelon.character.medical.MedicalStats;

import java.util.HashMap;
import java.util.Map;


public abstract class GroupCompartment extends Compartment {

    protected Map<String, Compartment> compartments = new HashMap<>();

    public GroupCompartment(String name, MedicalStats medicalStats) {
        super(name, medicalStats);
        initializeCompartments();
    }

    public abstract void initializeCompartments();

    @Override
    public Compartment getCompartment(String name) {
        return compartments.get(name);
    }

    public void addCompartment(Compartment compartment) {
        compartments.put(compartment.getName(), compartment);
    }

    public Map<String, Compartment> getCompartments() {
        return compartments;
    }

    @Override
    public void setBloodFlow(float bloodFlow) {
        this.bloodFlow = bloodFlow;
        updateBloodFlow();
    }

    public void updateBloodFlow() {
        for (Compartment compartment : compartments.values()) {
            if (compartment != null) {
                compartment.setBloodFlow(bloodFlow);
            } else {
                System.err.println("Null compartment found in " + getName());
            }
        }
    }

    public void increaseCompartmentHealth(String name, float health) {
        if (compartments.containsKey(name)) {
            compartments.get(name).addHealth(health);
        }
    }

    public void decreaseCompartmentHealth(String name, float health) {
        if (compartments.containsKey(name)) {
            compartments.get(name).removeHealth(health);
        }
    }
    protected float getAverageHealth() {
        if (compartments == null || compartments.isEmpty()) {
            return 0;
        }

        float totalHealth = 0;
        for (Compartment compartment : compartments.values()) {
            totalHealth += compartment.getHealth();
        }

        return totalHealth / compartments.size();
    }
}
