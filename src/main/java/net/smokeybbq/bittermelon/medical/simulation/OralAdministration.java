package net.smokeybbq.bittermelon.medical.simulation;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.medicine.Medicine;
import net.smokeybbq.bittermelon.medical.simulation.compartments.*;

public class OralAdministration extends PBPKModel {
    public OralAdministration(double dosage, Character character, Medicine drug) {
        super(dosage, character, drug);
        simpleCompartments = new SimpleCompartment[]{kidney, lung, heart, brain, adiposeTissue, bone, muscle, lymphatic, endocrine, other};
    }

    @Override
    protected void initializeSimulation() {
        GI.addConcentration(dosage);
        totalConcentration = getTotalConcentration();
    }

    @Override
    public void runSimulation() {
        if (totalConcentration < 1) {
            updateRateConstants();

            double GIConcentration = GI.getConcentration();
            double liverConcentration = liver.getConcentration();
            double circulatoryConcentration = circulatory.getConcentration();
            double GIBloodFlow = medicalStats.getBloodFlow("Gastrointestinal");
            double circulatoryBloodFlow = medicalStats.getBloodFlow("Circulatory System");

            double GIDerivative = GI.getDerivative(GIConcentration, liverConcentration, volumeGI, GIBloodFlow);

            double portalVein = (drug.getAbsorptionRateConstant() * GIConcentration * volumeGI) / volumeLiver;
            double liverDerivative = portalVein - liver.getDerivative(liverConcentration, circulatoryConcentration, volumeLiver, circulatoryBloodFlow);

            double circulatoryDrugSource = (circulatoryBloodFlow * (liverConcentration - circulatoryConcentration)) / volumeCirculatory;
            double circulatoryDerivative = circulatory.getDerivative(circulatoryDrugSource, simpleCompartments);

            handleSimpleCompartments();
            GI.updateConcentration(GIDerivative, timeStep);
            liver.updateConcentration(liverDerivative, timeStep);
            circulatory.updateConcentration(circulatoryDerivative, timeStep);

            drugEffect();
            totalConcentration = getTotalConcentration();

            t += timeStep;
        } else {
            removeFromSimulations();
        }
    }
}
