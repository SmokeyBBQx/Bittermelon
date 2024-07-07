package net.smokeybbq.bittermelon.medical.simulation;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.simulation.compartments.*;

public class OralAdministration extends PBPKModel {
    Compartment GI, liver, circulatory;

    public OralAdministration(float dosage, Character character, Substance drug) {
        super(dosage, character, drug);
    }

    @Override
    protected void initializeSimulation() {
        GI = compartments.get("abdomen").getCompartment("gastrointestinal").getMainCompartment();
        liver = compartments.get("abdomen").getCompartment("liver").getMainCompartment();
        circulatory = compartments.get("circulatory_system").getMainCompartment();

        GI.updateConcentration(substance, dosage);
        GI.excludeFromCirculation = true;

        totalConcentration = getTotalConcentration();
    }

    @Override
    public void simulation() {
        GI.moveConcentration(liver, substance, substance.getAbsorptionRateConstant(), timeStep);
        liver.eliminateConcentration(substance, substance.getMetabolismRateConstant());
        liver.moveConcentration(circulatory, substance, liver.getBloodFlow(), timeStep);

        handleSimpleCompartments();
    }
}
