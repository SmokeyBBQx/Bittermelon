package net.smokeybbq.bittermelon.systems.medical.substance;

import net.smokeybbq.bittermelon.systems.medical.compartments.Compartment;
import net.smokeybbq.bittermelon.systems.medical.substance.toxins.Organism;

import java.util.HashMap;
import java.util.Map;

public class ImmuneResponse extends Substance {
    Map<String, Float> immunity = new HashMap<>();
    public ImmuneResponse(String name, float toxicModifier) {
        super(name, toxicModifier);
        absorptionRateConstant = 0;
        eliminationRateConstant = 0;
        metabolismRateConstant = 0;
    }

    @Override
    public float interact(Substance substance) {
        if (substance instanceof Organism) {
            immunity.put(substance.getName(), 1F);
            return -toxicModifier * immunity.get(substance.getName());
        }
        return 0;
    }

    @Override
    public void effect(Compartment compartment, float concentration) {

    }

    @Override
    public float getToxicDamage(Compartment compartment) {
        if (compartment.getImmunePrivilege() <= 99) {
            return -0.5F / compartment.getImmunePrivilege();
        }
        return defaultToxicDamage;
    }
}
