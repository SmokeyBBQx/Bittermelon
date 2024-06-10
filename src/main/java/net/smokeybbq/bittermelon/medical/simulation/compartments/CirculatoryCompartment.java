package net.smokeybbq.bittermelon.medical.simulation.compartments;

import net.smokeybbq.bittermelon.medical.substance.Substance;

public class CirculatoryCompartment extends Compartment {
    public CirculatoryCompartment(String name, double volume) {
        super(name, volume);
    }

    public double getDerivative(double source, SimpleCompartment[] compartments, Substance drug) {
        double circulationOut = 0;
        double circulationIn = 0;

        for (SimpleCompartment compartment : compartments) {
            // The total concentration in all compartments receiving from the circulatory system
            circulationOut += compartment.getBloodFlow() * getConcentration(drug);

            // The total concentration returning to the circulatory system from those compartments

            // Livers and kidneys won't return concentration
                circulationIn += compartment.getBloodFlow() / 2 * compartment.getConcentration(drug);

        }
        double derivative = source + circulationIn - circulationOut;

        return derivative;
    }
}
