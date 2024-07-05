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
        circulatory = (CirculatoryCompartment) compartments.get("Circulatory System").getMainOrgan();

        circulatory.addConcentration(substance, dosage);
        totalConcentration = getTotalConcentration();
    }

    @Override
    public void simulation() {
        float circulatoryConcentration = circulatory.getConcentration(substance);

        float circulatoryDerivative = circulatory.getDerivative(circulatoryConcentration, simpleCompartments, substance);

        handleSimpleCompartments();
        circulatory.updateConcentration(substance, circulatoryDerivative, timeStep);
    }
}
