package net.smokeybbq.bittermelon.medical.simulation;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.simulation.compartments.*;

public class OralAdministration extends PBPKModel {
    public OralAdministration(double dosage, Character character, Substance drug) {
        super(dosage, character, drug);
        simpleCompartments = new SimpleCompartment[]{kidney, lung, heart, brain, adiposeTissue, bone, muscle, lymphatic, endocrine, other};
    }

    @Override
    protected void initializeSimulation() {
        GI.addConcentration(drug, dosage);
        totalConcentration = getTotalConcentration();
    }

    @Override
    public void simulation() {
        double GIConcentration = GI.getConcentration(drug);
        double liverConcentration = liver.getConcentration(drug);
        double circulatoryConcentration = circulatory.getConcentration(drug);

        double GIBloodFlow = GI.getBloodFlow();
        double circulatoryBloodFlow = circulatory.getBloodFlow();

        double volumeGI = GI.getVolume();
        double volumeLiver = liver.getVolume();
        double volumeCirculatory = circulatory.getVolume();


        double GIDerivative = (drug.getAbsorptionRateConstant() * GIConcentration * volumeGI) / volumeLiver;
        double liverDerivative = GIDerivative - liver.getDerivative(liverConcentration, circulatoryConcentration, volumeLiver, circulatoryBloodFlow);

        double circulatoryDrugSource = (circulatoryBloodFlow * (liverConcentration - circulatoryConcentration)) / volumeCirculatory;
        double circulatoryDerivative = circulatory.getDerivative(circulatoryDrugSource, simpleCompartments, drug);

        handleSimpleCompartments();
        GI.updateConcentration(drug, GIDerivative, timeStep);
        liver.updateConcentration(drug, liverDerivative, timeStep);
        circulatory.updateConcentration(drug, circulatoryDerivative, timeStep);
    }
}
