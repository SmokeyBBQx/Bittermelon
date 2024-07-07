package net.smokeybbq.bittermelon.medical.simulation;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.simulation.compartments.Compartment;
import net.smokeybbq.bittermelon.medical.substance.Substance;

public class IVAdministration extends PBPKModel {
    Compartment circulatory;

    public IVAdministration(float dosage, Character character, Substance drug) {
        super(dosage, character, drug);
    }

    @Override
    protected void initializeSimulation() {
        circulatory = compartments.get("circulatory_system").getMainCompartment();

        circulatory.updateConcentration(substance, dosage);
        totalConcentration = getTotalConcentration();
    }

    @Override
    public void simulation() {
        handleSimpleCompartments();
    }
}
