package net.smokeybbq.bittermelon.medical.simulation.compartments;

import net.smokeybbq.bittermelon.character.medical.MedicalStats;

import java.util.HashMap;
import java.util.Map;


public abstract class GroupCompartment extends Compartment {

    protected Map<String, SimpleCompartment> compartments = new HashMap<>();

    public GroupCompartment(String name, MedicalStats medicalStats) {
        super(name, medicalStats);
        initializeCompartments();
    }

    public abstract void initializeCompartments();

    public Compartment getCompartment(String name) {
        return compartments.get(name);
    }

    public void addCompartment(SimpleCompartment compartment) {
        compartments.put(compartment.getName(), compartment);
    }

    public Map<String, SimpleCompartment> getCompartments() {
        return compartments;
    }

    @Override
    public void setBloodFlow(double bloodFlow) {
        this.bloodFlow = bloodFlow;
        updateBloodFlow();
    }

    public void updateBloodFlow() {
        for (Compartment compartment : compartments.values()) {
            compartment.setBloodFlow(bloodFlow);
        }
    }

    public void increaseCompartmentHealth(String name, double health) {
        if (compartments.containsKey(name)) {
            compartments.get(name).addHealth(health);
        }
    }

    public void decreaseCompartmentHealth(String name, double health) {
        if (compartments.containsKey(name)) {
            compartments.get(name).removeHealth(health);
        }
    }
}
