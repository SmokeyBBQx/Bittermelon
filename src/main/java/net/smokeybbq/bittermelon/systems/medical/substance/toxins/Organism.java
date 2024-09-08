package net.smokeybbq.bittermelon.systems.medical.substance.toxins;

import net.smokeybbq.bittermelon.systems.medical.compartments.Compartment;
import net.smokeybbq.bittermelon.systems.medical.substance.Substance;

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
