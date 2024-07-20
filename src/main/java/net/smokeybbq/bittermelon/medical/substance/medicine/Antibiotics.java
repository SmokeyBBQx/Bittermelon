package net.smokeybbq.bittermelon.medical.substance.medicine;

import net.smokeybbq.bittermelon.medical.compartments.Compartment;
import net.smokeybbq.bittermelon.medical.substance.Chemical;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.substance.toxins.Organism;

public class Antibiotics extends Chemical {
    public Antibiotics(String name, float absorptionModifier, float eliminationModifier, float metabolismModifier, float toxicModifier) {
        super(name, absorptionModifier, eliminationModifier, metabolismModifier, toxicModifier);
    }

    @Override
    public float interact(Substance substance) {
        if (substance instanceof Organism) {
            return toxicModifier;
        }
        return 0;
    }

    @Override
    public void effect(Compartment compartment, float concentration) {

    }
}
