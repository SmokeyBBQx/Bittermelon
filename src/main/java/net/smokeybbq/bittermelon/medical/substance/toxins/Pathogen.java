package net.smokeybbq.bittermelon.medical.substance.toxins;

import net.smokeybbq.bittermelon.medical.simulation.compartments.Compartment;
import net.smokeybbq.bittermelon.medical.substance.ImmuneResponse;
import net.smokeybbq.bittermelon.medical.substance.Substance;

import java.util.HashSet;
import java.util.Set;

public abstract class Pathogen extends Organism {
    protected float infectionRate;
    protected Set<String> preferredEnvironments = new HashSet<>();
    protected int spreadModifier;
    public Pathogen(String name, float toxicModifier, float infectionRate) {
        super(name, toxicModifier);
        this.infectionRate = infectionRate;
    }

    private void initialize() {
        preferredEnvironments.add("left_lung");
    }

    @Override
    public float interact(Substance substance) {
        if (substance instanceof ImmuneResponse) {
            return -toxicModifier;
        }
        return 0;
    }

    @Override
    public void effect(Compartment compartment, float concentration) {
        if (preferredEnvironments.contains(compartment.getName())) {
            compartment.updateConcentration(this, 2 * infectionRate * concentration);
        } else {
            compartment.updateConcentration(this, infectionRate * concentration);
        }
    }
}
