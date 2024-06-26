package net.smokeybbq.bittermelon.medical.simulation;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.simulation.compartments.*;

public class OralAdministration extends PBPKModel {
    EliminatingCompartment GI, liver;
    CirculatoryCompartment circulatory;

    public OralAdministration(float dosage, Character character, Substance drug) {
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
        float GIConcentration = GI.getConcentration(drug);
        float liverConcentration = liver.getConcentration(drug);
        float circulatoryConcentration = circulatory.getConcentration(drug);

        float liverBloodFlow = liver.getBloodFlow();

        float GIDerivative = -drug.getAbsorptionRateConstant() * GIConcentration;
        float liverDerivative = -GIDerivative - (drug.getMetabolismRateConstant() + liverBloodFlow) * liverConcentration + liverBloodFlow / 2 * circulatoryConcentration;

        float circulatoryDrugSource = liverBloodFlow * liverConcentration;
        float circulatoryDerivative = circulatory.getDerivative(circulatoryDrugSource, simpleCompartments, drug);

        handleSimpleCompartments();

        GI.updateConcentration(drug, GIDerivative, timeStep);
        liver.updateConcentration(drug, liverDerivative, timeStep);
        circulatory.updateConcentration(drug, circulatoryDerivative, timeStep);
    }
}
