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
        GI = (EliminatingCompartment) compartments.get("Gastrointestinal").getMainOrgan();
        liver = (EliminatingCompartment) compartments.get("Liver").getMainOrgan();
        circulatory = (CirculatoryCompartment) compartments.get("Circulatory System").getMainOrgan();

        GI.addConcentration(substance, dosage);

        totalConcentration = getTotalConcentration();
    }

    @Override
    public void simulation() {
        float GIConcentration = GI.getConcentration(substance);
        float liverConcentration = liver.getConcentration(substance);
        float circulatoryConcentration = circulatory.getConcentration(substance);

        float liverBloodFlow = liver.getBloodFlow();

        float GIDerivative = -substance.getAbsorptionRateConstant() * GIConcentration;
        float liverDerivative = -GIDerivative - (substance.getMetabolismRateConstant() + liverBloodFlow) * liverConcentration + liverBloodFlow / 2 * circulatoryConcentration;

        float circulatoryDrugSource = liverBloodFlow * liverConcentration;
        float circulatoryDerivative = circulatory.getDerivative(circulatoryDrugSource, simpleCompartments, substance);

        handleSimpleCompartments();

        GI.updateConcentration(substance, GIDerivative, timeStep);
        liver.updateConcentration(substance, liverDerivative, timeStep);
        circulatory.updateConcentration(substance, circulatoryDerivative, timeStep);



    }
}
