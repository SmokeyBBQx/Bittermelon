package net.smokeybbq.bittermelon.medical.simulation.compartments;

import net.smokeybbq.bittermelon.medical.substance.Substance;

public class SimpleCompartment extends Compartment {

    public SimpleCompartment(String name, double volume) {
        super(name, volume);
    }

    public double getDerivative(double sourceConcentration, Substance drug) {
        return bloodFlow * sourceConcentration - (bloodFlow / 2) * getConcentration(drug);
    }
}
