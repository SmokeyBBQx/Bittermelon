package net.smokeybbq.bittermelon.medical.simulation.compartments;

import net.smokeybbq.bittermelon.character.medical.MedicalStats;
import net.smokeybbq.bittermelon.medical.substance.Substance;

public class EliminatingCompartment extends SimpleCompartment {

    private double rateConstant;

    public EliminatingCompartment(String name, MedicalStats medicalStats) {
        super(name, medicalStats);
    }

    @Override
    public double getDerivative(double sourceConcentration, Substance drug) {
        return bloodFlow * sourceConcentration - rateConstant * getConcentration(drug);
    }

    @Override
    public void setRateConstant(double rateConstant) {
        this.rateConstant = rateConstant;
    }

    public double getRateConstant() {
        return rateConstant;
    }
}
