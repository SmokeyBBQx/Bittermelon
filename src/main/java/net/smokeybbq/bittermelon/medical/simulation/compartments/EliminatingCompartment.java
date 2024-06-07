package net.smokeybbq.bittermelon.medical.simulation.compartments;

public class EliminatingCompartment extends SimpleCompartment {

    private double rateConstant;
    public EliminatingCompartment(String name, double volume) {
        super(name, volume);
    }

    @Override
    public double getDerivative(double sourceFlow, double targetFlow, double volume, double bloodFlow) {
        return (bloodFlow * (sourceFlow - targetFlow)) / volume - rateConstant * concentration;
    }

    @Override
    public void setRateConstant(double rateConstant) {
        this.rateConstant = rateConstant;
    }
}
