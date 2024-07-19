package net.smokeybbq.bittermelon.medical.substance.toxins;

import net.smokeybbq.bittermelon.medical.substance.ImmuneResponse;
import net.smokeybbq.bittermelon.medical.substance.Substance;

public class Bacteria extends Pathogen {
    public Bacteria(String name, float toxicModifier, float infectionRate) {
        super(name, toxicModifier, infectionRate);
        defaultToxicDamage = -1 * toxicModifier;
    }

}
