package net.smokeybbq.bittermelon.medical.simulation.compartments;

public class SingleGroupCompartment extends GroupCompartment {

    Compartment mainCompartment;
    public SingleGroupCompartment(String name, float permeability, float volume) {
        super(name, permeability, volume);
        mainCompartment = new Compartment(name, permeability, volume);
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
