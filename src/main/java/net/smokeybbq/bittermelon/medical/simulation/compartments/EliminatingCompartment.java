package net.smokeybbq.bittermelon.medical.simulation.compartments;

import net.smokeybbq.bittermelon.medical.substance.Substance;

public class EliminatingCompartment extends SimpleCompartment {

    private float rateConstant;

    public EliminatingCompartment(String name, float volume) {
        super(name, volume);
    }

    @Override
    public float getDerivative(float sourceConcentration, Substance drug) {
        return bloodFlow * sourceConcentration - (bloodFlow / 2) * getConcentration(drug) - rateConstant * getConcentration(drug);
    }

    @Override
    public void setRateConstant(float rateConstant) {
        this.rateConstant = rateConstant;
    }

    public float getRateConstant() {
        return rateConstant;
    }
}
