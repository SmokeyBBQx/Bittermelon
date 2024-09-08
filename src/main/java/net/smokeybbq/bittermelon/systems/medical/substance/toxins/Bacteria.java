package net.smokeybbq.bittermelon.systems.medical.substance.toxins;

public class Bacteria extends Pathogen {
    public Bacteria(String name, float toxicModifier, float infectionRate) {
        super(name, toxicModifier, infectionRate);
        defaultToxicDamage = -1 * toxicModifier;
    }

}
