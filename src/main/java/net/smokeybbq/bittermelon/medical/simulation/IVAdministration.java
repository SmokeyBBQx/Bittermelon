package net.smokeybbq.bittermelon.medical.simulation;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.simulation.compartments.CirculatoryCompartment;
import net.smokeybbq.bittermelon.medical.substance.Substance;

public class IVAdministration extends PBPKModel {
    CirculatoryCompartment circulatory;

    public IVAdministration(float dosage, Character character, Substance drug) {
        super(dosage, character, drug);
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
