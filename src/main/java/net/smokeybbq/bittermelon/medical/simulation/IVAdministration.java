package net.smokeybbq.bittermelon.medical.simulation;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.simulation.compartments.EliminatingCompartment;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.simulation.compartments.SimpleCompartment;

public class IVAdministration extends PBPKModel {

    public IVAdministration(float dosage, Character character, Substance drug) {
        super(dosage, character, drug);
        simpleCompartments = new SimpleCompartment[]{GI, liver, kidney, lung, heart, brain, adiposeTissue, bone, muscle, lymphatic, endocrine, other};
    }

    @Override
    protected void initializeSimulation() {
        circulatory.addConcentration(drug, dosage);
        totalConcentration = getTotalConcentration();
    }

    @Override
    public void simulation() {
        float circulatoryConcentration = circulatory.getConcentration(drug);

        float circulatoryDerivative = circulatory.getDerivative(circulatoryConcentration, simpleCompartments, drug);

        handleSimpleCompartments();
        circulatory.updateConcentration(drug, circulatoryDerivative, timeStep);
    }
}
