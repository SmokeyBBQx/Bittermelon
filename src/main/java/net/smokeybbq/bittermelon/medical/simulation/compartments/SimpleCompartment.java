package net.smokeybbq.bittermelon.medical.simulation.compartments;

public class SimpleCompartment extends Compartment {

    public SimpleCompartment(String name, double volume) {
        super(name, volume);
    }

    public double getDerivative(double sourceFlow, double targetFlow, double volume, double bloodFlow) {
        return (bloodFlow * (sourceFlow - targetFlow)) / volume;
    }
}
