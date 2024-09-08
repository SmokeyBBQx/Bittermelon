package net.smokeybbq.bittermelon.systems.medical.simulation.pbpk;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.systems.medical.substance.Substance;

public class TopicalAdministration extends PBPKModel {
    public TopicalAdministration(float dosage, Character character, Substance substance) {
        super(dosage, character, substance);
    }

    @Override
    protected void initializeSimulation() {

    }

    @Override
    protected void simulation() {

    }
}
