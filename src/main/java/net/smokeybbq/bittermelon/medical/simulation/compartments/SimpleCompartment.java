package net.smokeybbq.bittermelon.medical.simulation.compartments;

import net.smokeybbq.bittermelon.character.medical.MedicalStats;
import net.smokeybbq.bittermelon.medical.substance.Substance;

public class SimpleCompartment extends Compartment {

    public SimpleCompartment(String name, MedicalStats medicalStats) {
        super(name, medicalStats);
    }

    public double getDerivative(double sourceConcentration, Substance drug) {
        return bloodFlow * sourceConcentration - (bloodFlow / 2) * getConcentration(drug);
    }
}
