package net.smokeybbq.bittermelon.medical.substance.toxins;

import net.smokeybbq.bittermelon.medical.simulation.compartments.Compartment;
import net.smokeybbq.bittermelon.medical.substance.Substance;

public class Organism extends Substance {
    public Organism(String name, float toxicModifier) {
        super(name, toxicModifier);
        eliminationRateConstant = 0;
        metabolismRateConstant = 0;
        absorptionRateConstant = 0;
    }

    @Override
    public float interact(Substance substance) {
        return 0;
    }

    @Override
    public void effect(Compartment compartment, float concentration) {

    }
}
