package net.smokeybbq.bittermelon.medical.simulation;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.simulation.pbpk.PBPKModel;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.compartments.Compartment;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimulationHandler {

    protected final List<PBPKModel> simulations = new CopyOnWriteArrayList<>();
    protected final Character character;
    protected final Map<String, Compartment> compartments;

    public SimulationHandler(Character character, Map<String, Compartment> compartments) {
        this.character = character;
        this.compartments = compartments;
        initialize();
    }

    protected void initialize() {
    }

    public void update() {
        for (PBPKModel simulation : simulations) {
            simulation.runSimulation();
        }

        substanceEffect();
    }

    protected void substanceEffect() {
        for (Compartment outerCompartment : compartments.values()) {
            outerCompartment.traverseCompartments(compartment -> {
                Map<Substance, Float> concentrations = compartment.getConcentrations();
                List<Substance> substances = new ArrayList<>(concentrations.keySet());
                int n = substances.size();

                // Perform toxic damage and prepare for interactions
                Map<Substance, Float> removals = new HashMap<>();
                for (int i = 0; i < n; i++) {
                    Substance substance1 = substances.get(i);
                    float concentration1 = concentrations.get(substance1);

                    // Toxic damage
                    toxicDamage(substance1, compartment);

                    // Interactions
                    for (int j = 0; j < n; j++) {
                        if (i != j) {
                            Substance substance2 = substances.get(j);
                            float interaction = substance1.interact(substance2);
                            if (interaction != 0) {
                                float removalAmount = interaction * concentration1;
                                removals.merge(substance2, removalAmount, Float::sum);
                            }
                        }
                    }
                }

                // Apply removals
                for (Map.Entry<Substance, Float> entry : removals.entrySet()) {
                    compartment.updateConcentration(entry.getKey(), entry.getValue());
                }
            });
        }
    }

    protected void toxicDamage(Substance substance, Compartment compartment) {
        float toxicDamage = substance.getToxicDamage(compartment) * compartment.getConcentration(substance);
        compartment.modifyHealth(toxicDamage);
    }

    public void addSimulation(PBPKModel simulation) {
        simulations.add(simulation);
    }

    public void removeSimulation(PBPKModel simulation) {
        simulations.remove(simulation);
    }
}