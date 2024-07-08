package net.smokeybbq.bittermelon.character.medical;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.simulation.IVAdministration;
import net.smokeybbq.bittermelon.medical.simulation.compartments.CompartmentTag;
import net.smokeybbq.bittermelon.medical.substance.ImmuneResponse;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.simulation.PBPKModel;
import net.smokeybbq.bittermelon.medical.simulation.compartments.Compartment;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimulationHandler {

    private final List<PBPKModel> simulations = new CopyOnWriteArrayList<>();
    private final Character character;
    private final Map<String, Compartment> compartments;

    // Immunity and inflammatory constants
    private static final float INFLAMMATORY_DECAY = -0.1F;
    private static final float INFLAMMATORY_RESPONSE = 0.5F;
    private static final float IMMUNE_ACTIVATION = 0.01F;
    private static final float RESERVE_CAPACITY = 1000;
    private static final float IMMUNITY_MAXIMUM = 1000;
    private float immuneReserve;
    Set<Compartment> immuneCompartments = new HashSet<>();
    PBPKModel immuneSystemSimulation;

    public SimulationHandler(Character character, Map<String, Compartment> compartments) {
        this.character = character;
        this.compartments = compartments;
        initialize();
    }

    public void initialize() {
        immuneSystemSimulation = new IVAdministration(1000, character, new ImmuneResponse("Immune Response", 0F, 0F, 0F, 0.001F));
        addSimulation(immuneSystemSimulation);

        for (Compartment outerCompartment : compartments.values()) {
            outerCompartment.traverseCompartments(compartment -> {
                if (compartment.hasTag(CompartmentTag.IMMUNE)) {
                    immuneCompartments.add(compartment);
                }
            });
        }
    }

    public void update() {
        for (PBPKModel simulation : simulations) {
            simulation.runSimulation();
        }

        decay();
        substanceEffect();
        growImmuneReserve();
    }

    // Immunity and inflammatory decay
    private void decay() {
        for (Compartment outerCompartment : compartments.values()) {
            outerCompartment.traverseCompartments(compartment -> {
                compartment.modifyInflammation(INFLAMMATORY_DECAY);
            });
        }
    }

    public void substanceEffect() {
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

    private void toxicDamage(Substance substance, Compartment compartment) {
        float toxicDamage = substance.getToxicDamage(compartment) * compartment.getConcentration(substance);
        compartment.modifyHealth(toxicDamage);

        if (toxicDamage >= 0.5 && immuneSystemSimulation.getTotalConcentration() < IMMUNITY_MAXIMUM) {
            immuneResponse(compartment);
        }
    }

    private void immuneResponse(Compartment compartment) {
        Compartment circulatorySystem = compartments.get("circulatory_system");
        for (Map.Entry<Substance, Float> entry : circulatorySystem.getMainCompartment().getConcentrations().entrySet()) {
            if (entry.getKey() instanceof ImmuneResponse) {
                circulatorySystem.getMainCompartment().updateConcentration(entry.getKey(), activateImmuneReserve());
            }
        }

        compartment.modifyInflammation(INFLAMMATORY_RESPONSE);
    }

    public float activateImmuneReserve() {
        float available = immuneReserve - IMMUNE_ACTIVATION;
        return Math.max(available, 0);
    }

    public void growImmuneReserve() {
        if (immuneReserve < RESERVE_CAPACITY) {
            for (Compartment compartment : immuneCompartments) {
                immuneReserve += compartment.getHealth() / 10;
            }

        }
    }

    public void addSimulation(PBPKModel simulation) {
        simulations.add(simulation);
    }

    public void removeSimulation(PBPKModel simulation) {
        simulations.remove(simulation);
    }
}