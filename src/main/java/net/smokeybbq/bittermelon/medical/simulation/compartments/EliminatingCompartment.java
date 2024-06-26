package net.smokeybbq.bittermelon.medical.simulation.compartments;

import net.smokeybbq.bittermelon.character.medical.MedicalStats;
import net.smokeybbq.bittermelon.medical.substance.Substance;

public class EliminatingCompartment extends SimpleCompartment {

    private float rateConstant;

    public EliminatingCompartment(String name, MedicalStats medicalStats) {
        super(name, medicalStats);
    }

    @Override
    public float getDerivative(float sourceConcentration, Substance drug) {
        return bloodFlow * sourceConcentration - rateConstant * getConcentration(drug);
    }

    @Override
    public void setRateConstant(float rateConstant) {
        this.rateConstant = rateConstant;
    }

    public float getRateConstant() {
        return rateConstant;
    }
}
