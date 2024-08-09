package net.smokeybbq.bittermelon.medical.compartments;

public class SingleGroupCompartment extends GroupCompartment {

    Compartment mainCompartment;
    public SingleGroupCompartment(String name, float permeability, float volume, float healFactor) {
        super(name, permeability, volume, healFactor);
        mainCompartment = new Compartment(name, permeability, volume, healFactor);
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
