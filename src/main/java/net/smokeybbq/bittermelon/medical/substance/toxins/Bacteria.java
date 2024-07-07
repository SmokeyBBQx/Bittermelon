package net.smokeybbq.bittermelon.medical.substance.toxins;

import net.smokeybbq.bittermelon.medical.substance.ImmuneResponse;
import net.smokeybbq.bittermelon.medical.substance.Substance;

public class Bacteria extends Organism {
    public Bacteria(String name, float absorptionModifier, float eliminationModifier, float metabolismModifier, float toxicModifier) {
        super(name, absorptionModifier, eliminationModifier, metabolismModifier, toxicModifier);
        defaultToxicDamage = -1 * toxicModifier;
    }

    @Override
    public float interact(Substance substance) {
        if (substance instanceof ImmuneResponse) {
            return -toxicModifier;
        }
        return 0;
    }
}
