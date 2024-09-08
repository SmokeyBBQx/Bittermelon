package net.smokeybbq.bittermelon.systems.medical.substance.medicine;

import net.smokeybbq.bittermelon.systems.medical.compartments.Compartment;
import net.smokeybbq.bittermelon.systems.medical.substance.Chemical;
import net.smokeybbq.bittermelon.systems.medical.substance.Substance;

public class Immunosuppresants extends Chemical {
    public Immunosuppresants(String name, float absorptionModifier, float eliminationModifier, float metabolismModifier, float toxicModifier) {
        super(name, absorptionModifier, eliminationModifier, metabolismModifier, toxicModifier);
    }

    @Override
    public float interact(Substance substance) {
        return 0;
    }

    @Override
    public void effect(Compartment compartment, float concentration) {
        compartment.modifyImmunePrivilege(toxicModifier * concentration);
    }
}
