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
            circulationOut += compartment.getBloodFlow();

            // The total concentration returning to the circulatory system from those compartments

            // Livers and kidneys won't return concentration
            if (compartment.getName() != "Liver" && compartment.getName() != "Kidneys") {
                circulationIn += compartment.getBloodFlow() / 2 * compartment.getConcentration(drug);
            }
        }
        source = source + circulationIn - circulationOut * concentrations.getOrDefault(drug, 0.0);

        return source;
    }
}
