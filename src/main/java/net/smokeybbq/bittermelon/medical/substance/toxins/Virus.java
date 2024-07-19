package net.smokeybbq.bittermelon.medical.substance.toxins;

import net.smokeybbq.bittermelon.medical.simulation.compartments.Compartment;
import net.smokeybbq.bittermelon.medical.substance.Substance;

public class Virus extends Pathogen {
    public Virus(String name, float toxicModifier, float infectionRate) {
        super(name, toxicModifier, infectionRate);
    }

    @Override
    public float interact(Substance substance) {
        return 0;
    }

    @Override
    public void effect(Compartment compartment, float concentration) {

    }
}
