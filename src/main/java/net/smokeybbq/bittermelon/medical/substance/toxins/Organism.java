package net.smokeybbq.bittermelon.medical.substance.toxins;

import net.smokeybbq.bittermelon.medical.substance.Substance;

public class Organism extends Substance {
    public Organism(String name, float absorptionModifier, float eliminationModifier, float metabolismModifier, float toxicModifier) {
        super(name, absorptionModifier, eliminationModifier, metabolismModifier, toxicModifier);
        eliminationRateConstant = 0;
        metabolismRateConstant = 0;
        absorptionRateConstant = 0;
    }

    @Override
    public float interact(Substance substance) {
        return 0;
    }
}
