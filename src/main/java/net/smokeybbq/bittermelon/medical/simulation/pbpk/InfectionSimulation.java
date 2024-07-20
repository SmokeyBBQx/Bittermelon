package net.smokeybbq.bittermelon.medical.simulation.pbpk;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.substance.Substance;

public class InfectionSimulation extends PBPKModel {

    public InfectionSimulation(float dosage, Character character, Substance substance) {
        super(dosage, character, substance);
    }

    @Override
    protected void initializeSimulation() {

    }

    @Override
    protected void simulation() {


    }
}
