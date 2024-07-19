package net.smokeybbq.bittermelon.medical.substance.medicine;

import net.smokeybbq.bittermelon.medical.simulation.compartments.Compartment;
import net.smokeybbq.bittermelon.medical.substance.Chemical;
import net.smokeybbq.bittermelon.medical.substance.Substance;

public class Corticosteroids extends Chemical {
    public Corticosteroids(String name, float absorptionModifier, float eliminationModifier, float metabolismModifier, float toxicModifier) {
        super(name, absorptionModifier, eliminationModifier, metabolismModifier, toxicModifier);
    }

    @Override
    public float interact(Substance substance) {
        return 0;
    }

    @Override
    public void effect(Compartment compartment, float concentration) {
        compartment.modifyInflammation(-toxicModifier * concentration);
    }
}
