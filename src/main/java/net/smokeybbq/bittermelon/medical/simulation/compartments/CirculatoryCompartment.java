package net.smokeybbq.bittermelon.medical.simulation.compartments;

import java.util.List;

public class CirculatoryCompartment extends Compartment {
    public CirculatoryCompartment(String name, double volume) {
        super(name, volume);
    }

    public double getDerivative(double source, SimpleCompartment[] compartments) {
        for (SimpleCompartment compartment : compartments) {
            source -= compartment.getDerivative(this.concentration, compartment.getConcentration(), this.volume, 0);
        }

        return source;
    }
}
