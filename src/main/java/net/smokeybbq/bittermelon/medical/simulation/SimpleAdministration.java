package net.smokeybbq.bittermelon.medical.simulation;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.simulation.compartments.Compartment;
import net.smokeybbq.bittermelon.medical.substance.Substance;

public class SimpleAdministration extends PBPKModel {
    Compartment entryCompartment;
    public SimpleAdministration(float dosage, Character character, Substance substance, Compartment entryCompartment) {
        super(dosage, character, substance);
        this.entryCompartment = entryCompartment;
        initializeSimulation();
    }

    @Override
    protected void initializeSimulation() {
        entryCompartment.updateConcentration(substance, dosage);
        totalConcentration = getTotalConcentration();
    }

    @Override
    protected void simulation() {
        handleSimpleCompartments();
    }
}
