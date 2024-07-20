package net.smokeybbq.bittermelon.medical.substance;

import net.smokeybbq.bittermelon.medical.compartments.Compartment;

public class Chemical extends Substance {
    public Chemical(String name, float toxicModifier, float absorptionModifier, float eliminationModifier, float metabolismModifier) {
        super(name, toxicModifier);
        this.absorptionModifier = absorptionModifier;
        this.eliminationModifier = eliminationModifier;
        this.metabolismModifier = metabolismModifier;
    }

    @Override
    public float interact(Substance substance) {
        return 0;
    }

    @Override
    public void effect(Compartment compartment, float concentration) {

    }
}
