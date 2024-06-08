package net.smokeybbq.bittermelon.medical.simulation;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.simulation.compartments.SimpleCompartment;

public class IVAdministration extends PBPKModel {

    public IVAdministration(double dosage, Character character, Substance drug) {
        super(dosage, character, drug);
        simpleCompartments = new SimpleCompartment[]{GI, liver, kidney, lung, heart, brain, adiposeTissue, bone, muscle, lymphatic, endocrine, other};
    }

    @Override
    protected void initializeSimulation() {
        circulatory.addConcentration(drug, dosage);
        totalConcentration = getTotalConcentration();
    }

    @Override
    public void runSimulation() {
        if (totalConcentration < 1) {
            updateRateConstants();

            double circulatoryConcentration = circulatory.getConcentration(drug);

            double circulatoryDerivative = circulatory.getDerivative(circulatoryConcentration, simpleCompartments, drug);

            circulatory.updateConcentration(drug, circulatoryDerivative, timeStep);
            handleSimpleCompartments();

            totalConcentration = getTotalConcentration();

            t += timeStep;

        } else {
            clearMapping();
            removeFromSimulations();
        }
    }
}
