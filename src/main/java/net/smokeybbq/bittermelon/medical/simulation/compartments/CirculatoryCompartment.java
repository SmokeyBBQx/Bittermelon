package net.smokeybbq.bittermelon.medical.simulation.compartments;

import net.smokeybbq.bittermelon.character.medical.MedicalStats;
import net.smokeybbq.bittermelon.medical.substance.Substance;

import java.util.List;

public class CirculatoryCompartment extends Compartment {
    public CirculatoryCompartment(String name, MedicalStats medicalStats) {
        super(name, medicalStats);
    }

    public float getDerivative(float source, List<SimpleCompartment> compartments, Substance drug) {
        float circulationOut = 0;
        float circulationIn = 0;

        for (SimpleCompartment compartment : compartments) {
            // The total concentration in all compartments receiving from the circulatory system
            circulationOut += compartment.getBloodFlow() * getConcentration(drug);

            // The total concentration returning to the circulatory system from those compartments

            // Livers and kidneys won't return concentration
            if (compartment.getName() != "Liver" && compartment.getName() != "Kidneys") {
                circulationIn += compartment.getBloodFlow() / 2 * compartment.getConcentration(drug);
            }
        }
        float derivative = source + circulationIn - circulationOut;

        return derivative;
    }
}
