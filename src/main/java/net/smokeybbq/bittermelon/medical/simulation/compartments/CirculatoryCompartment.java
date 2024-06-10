package net.smokeybbq.bittermelon.medical.simulation.compartments;

import net.smokeybbq.bittermelon.medical.substance.Substance;

public class CirculatoryCompartment extends Compartment {
    public CirculatoryCompartment(String name, float volume) {
        super(name, volume);
    }

    public float getDerivative(float source, SimpleCompartment[] compartments, Substance drug) {
        float circulationOut = 0;
        float circulationIn = 0;

        for (SimpleCompartment compartment : compartments) {
            // The total concentration in all compartments receiving from the circulatory system
            circulationOut += compartment.getBloodFlow() * getConcentration(drug);

            // The total concentration returning to the circulatory system from those compartments

            // Livers and kidneys won't return concentration
                circulationIn += compartment.getBloodFlow() / 2 * compartment.getConcentration(drug);

        }
        float derivative = source + circulationIn - circulationOut;

        return derivative;
    }
}
