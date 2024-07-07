package net.smokeybbq.bittermelon.medical.simulation.compartments;

import net.smokeybbq.bittermelon.character.medical.MedicalStats;

public class SingleGroupCompartment extends GroupCompartment {

    Compartment mainCompartment;
    public SingleGroupCompartment(String name, float permeability) {
        super(name, permeability);
        mainCompartment = new Compartment(name, permeability);
        subCompartments.put(name, mainCompartment);
    }

    @Override
    public void initializeCompartments() {
        subCompartments.put(name, mainCompartment);
    }

    @Override
    public Compartment getMainCompartment() {
        return mainCompartment;
    }
}
