package net.smokeybbq.bittermelon.medical.simulation;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.medicine.Medicine;
import net.smokeybbq.bittermelon.medical.simulation.compartments.SimpleCompartment;

public class IVAdministration extends PBPKModel {

    public IVAdministration(double dosage, Character character, Medicine drug) {
        super(dosage, character, drug);
        simpleCompartments = new SimpleCompartment[]{GI, liver, kidney, lung, heart, brain, adiposeTissue, bone, muscle, lymphatic, endocrine, other};
    }

    @Override
    protected void initializeSimulation() {
        circulatory.addConcentration(dosage);
        totalConcentration = getTotalConcentration();
    }

    @Override
    public void runSimulation() {
        if (totalConcentration < 1) {
            updateRateConstants();

            double circulatoryConcentration = circulatory.getConcentration();

            double circulatoryDerivative = circulatory.getDerivative(circulatoryConcentration, simpleCompartments);

            circulatory.updateConcentration(circulatoryDerivative, timeStep);
            handleSimpleCompartments();

            drugEffect();
            totalConcentration = getTotalConcentration();

            t += timeStep;

        } else {
            removeFromSimulations();
        }
    }
}
