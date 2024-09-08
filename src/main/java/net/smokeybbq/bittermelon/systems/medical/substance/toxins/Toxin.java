package net.smokeybbq.bittermelon.systems.medical.substance.toxins;

import net.smokeybbq.bittermelon.systems.medical.compartments.Compartment;
import net.smokeybbq.bittermelon.systems.medical.substance.Chemical;
import net.smokeybbq.bittermelon.systems.medical.substance.Substance;

public class Toxin extends Chemical {
    public Toxin(String name, float absorptionModifier, float eliminationModifier, float metabolismModifier, float toxicModifier) {
        super(name, absorptionModifier, eliminationModifier, metabolismModifier, toxicModifier);
        absorptionRateConstant = 1 * absorptionModifier;
        eliminationRateConstant = 1 * eliminationModifier;
        metabolismRateConstant = 1 * metabolismModifier;
    }

    @Override
    public float interact(Substance substance) {
        return 0;
    }

    @Override
    public void effect(Compartment compartment, float concentration) {

    }
}