package net.smokeybbq.bittermelon.medical.simulation;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.simulation.compartments.*;

public class OralAdministration extends PBPKModel {
    EliminatingCompartment GI, liver;
    CirculatoryCompartment circulatory;

    public OralAdministration(double dosage, Character character, Substance drug) {
        super(dosage, character, drug);
    }

    @Override
    protected void initializeSimulation() {
        GI = (EliminatingCompartment) compartments.get("Gastrointestinal");
        liver = (EliminatingCompartment) compartments.get("Liver");
        circulatory = (CirculatoryCompartment) compartments.get("Circulatory System");

        GI.addConcentration(drug, dosage);
        totalConcentration = getTotalConcentration();
    }

    @Override
    public void simulation() {
        double GIConcentration = GI.getConcentration(drug);
        double liverConcentration = liver.getConcentration(drug);
        double circulatoryConcentration = circulatory.getConcentration(drug);

        double liverBloodFlow = liver.getBloodFlow();

        double GIDerivative = -drug.getAbsorptionRateConstant() * GIConcentration;
        double liverDerivative = -GIDerivative - (drug.getMetabolismRateConstant() + liverBloodFlow) * liverConcentration + liverBloodFlow / 2 * circulatoryConcentration;

        double circulatoryDrugSource = liverBloodFlow * liverConcentration;
        double circulatoryDerivative = circulatory.getDerivative(circulatoryDrugSource, simpleCompartments, drug);

        handleSimpleCompartments();

        GI.updateConcentration(drug, GIDerivative, timeStep);
        liver.updateConcentration(drug, liverDerivative, timeStep);
        circulatory.updateConcentration(drug, circulatoryDerivative, timeStep);
    }
}
