package net.smokeybbq.bittermelon.medical.simulation;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.simulation.compartments.Compartment;
import net.smokeybbq.bittermelon.medical.substance.Substance;

public class InfectionSimulation extends PBPKModel {
    public InfectionSimulation(double dosage, Character character, Substance drug) {
        super(dosage, character, drug);
    }

    @Override
    protected void initializeSimulation() {
        // Make new infection compartment that has growing concentration / however an infection grows
        // Immune system compartment?
        // Handle damage of infection like a toxin
    }

    @Override
    protected void simulation() {

    }
}
