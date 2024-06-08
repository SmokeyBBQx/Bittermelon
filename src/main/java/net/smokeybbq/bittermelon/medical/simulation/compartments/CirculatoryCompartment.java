package net.smokeybbq.bittermelon.medical.simulation.compartments;

import net.smokeybbq.bittermelon.medical.substance.Substance;

public class CirculatoryCompartment extends Compartment {
    public CirculatoryCompartment(String name, double volume) {
        super(name, volume);
    }

    public double getDerivative(double source, SimpleCompartment[] compartments, Substance drug) {
        for (SimpleCompartment compartment : compartments) {
            source -= compartment.getDerivative(this.concentration, compartment.getConcentration(drug), this.volume, 0);
        }

        return source;
    }
}
