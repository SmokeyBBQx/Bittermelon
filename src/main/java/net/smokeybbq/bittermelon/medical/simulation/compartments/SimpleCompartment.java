package net.smokeybbq.bittermelon.medical.simulation.compartments;

import net.smokeybbq.bittermelon.medical.substance.Substance;

public class SimpleCompartment extends Compartment {

    public SimpleCompartment(String name, float volume) {
        super(name, volume);
    }

    public float getDerivative(float sourceConcentration, Substance drug) {
        return bloodFlow * sourceConcentration - (bloodFlow / 2) * getConcentration(drug);
    }
}
