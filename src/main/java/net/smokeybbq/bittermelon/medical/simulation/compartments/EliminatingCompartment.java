package net.smokeybbq.bittermelon.medical.simulation.compartments;

import net.smokeybbq.bittermelon.medical.substance.Substance;

public class EliminatingCompartment extends SimpleCompartment {

    private double rateConstant;

    public EliminatingCompartment(String name, double volume) {
        super(name, volume);
    }

    @Override
    public double getDerivative(double sourceConcentration, Substance drug) {
        return bloodFlow * sourceConcentration - (bloodFlow / 2) * getConcentration(drug) - rateConstant * getConcentration(drug);
    }

    @Override
    public void setRateConstant(double rateConstant) {
        this.rateConstant = rateConstant;
    }

    public double getRateConstant() {
        return rateConstant;
    }
}
