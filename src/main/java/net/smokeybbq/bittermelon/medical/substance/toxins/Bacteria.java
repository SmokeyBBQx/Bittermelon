package net.smokeybbq.bittermelon.medical.substance.toxins;

import net.smokeybbq.bittermelon.medical.substance.Substance;

public class Bacteria extends Organism {
    public Bacteria(String name, float absorptionModifier, float eliminationModifier, float metabolismModifier, float toxicModifier) {
        super(name, absorptionModifier, eliminationModifier, metabolismModifier, toxicModifier);
        defaultToxicDamage = 0.3F;
    }

    @Override
    public float interact(Substance substance) {
        return 0;
    }
}
