package net.smokeybbq.bittermelon.character.medical;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.conditions.Condition;
import net.smokeybbq.bittermelon.medical.simulation.IVAdministration;
import net.smokeybbq.bittermelon.medical.simulation.compartments.GroupCompartment;
import net.smokeybbq.bittermelon.medical.substance.ImmuneResponse;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.simulation.PBPKModel;
import net.smokeybbq.bittermelon.medical.simulation.compartments.Compartment;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimulationHandler {

    private final List<PBPKModel> simulations = new CopyOnWriteArrayList<>();
    private final Character character;
    private final Map<String, Compartment> compartmentMap;

    // Immunity and inflammatory constants
    private static final float INFLAMMATORY_DECAY = 0.001F;
    private static final float INFLAMMATORY_RESPONSE = 0.005F;
    private static final float IMMUNITY_DECAY = 0.005F;
    private static final float IMMUNE_ACTIVATION = 1;

    public SimulationHandler(Character character, Map<String, Compartment> compartmentMap) {
        this.character = character;
        this.compartmentMap = compartmentMap;
    }

    public void initialize() {
        addSimulation(new IVAdministration(100, character, new ImmuneResponse("Immune Response", 0.1F, 0.1F, 0.1F, 0.001F)));
    }

    public void update() {
        for (PBPKModel simulation : simulations) {
            simulation.runSimulation();
        }

        decay();
        substanceEffect();
    }

    // Immunity and inflammatory decay
    private void decay() {
        for (Compartment compartment : compartmentMap.values()) {
            compartment.decreaseInflammation(INFLAMMATORY_DECAY);
            for (Map.Entry<Substance, Float> entry : compartment.getConcentrations().entrySet()) {
                if (entry.getKey() instanceof ImmuneResponse) {
                    compartment.removeConcentration(entry.getKey(), IMMUNITY_DECAY);
                }
            }
        }
    }

    public void substanceEffect() {
        List<Condition> conditions = character.getMedicalStats().getConditions();

//            for (String affectedArea : condition.getAffectedAreas()) {
//                Compartment compartment = compartmentMap.get(affectedArea);
        for (Compartment compartment : compartmentMap.values()) {
            if (compartment instanceof GroupCompartment) {
                GroupCompartment groupCompartment = (GroupCompartment) compartment;

                for (Compartment subCompartment : groupCompartment.getCompartments().values()) {
                    Set<Substance> substances = new HashSet<>(subCompartment.getConcentrations().keySet());

                    // Perform toxic damage to each substance first
                    for (Substance substance : substances) {
                        toxicDamage(substance, subCompartment);
                    }

                    // Iterate over each pair of substances for interaction
                    for (Substance substance1 : substances) {
                        for (Substance substance2 : substances) {
                            if (!substance1.equals(substance2)) {
                                subCompartment.removeConcentration(substance2, substance1.interact(substance2) * subCompartment.getConcentration(substance1));
                            }
                        }
                    }
                }
            }
        }
    }


        private void toxicDamage(Substance substance, Compartment compartment) {
        float toxicDamage = substance.getToxicDamage(compartment);

        if (toxicDamage * compartment.getConcentration(substance) >= 0.5) {
            immuneResponse(compartment);
        }
    }

    private void immuneResponse(Compartment compartment) {
        Compartment circulatorySystem = compartmentMap.get("Circulatory System");
        for (Map.Entry<Substance, Float> entry : circulatorySystem.getMainOrgan().getConcentrations().entrySet()) {
            if (entry.getKey() instanceof ImmuneResponse) {
                circulatorySystem.getMainOrgan().addConcentration(entry.getKey(), IMMUNE_ACTIVATION * character.getMedicalStats().getImmuneHealth());
            }
        }

        compartment.increaseInflammation(INFLAMMATORY_RESPONSE);
    }

    public void decreaseInflammation(Compartment compartment, float amount) {
        compartment.decreaseInflammation(amount);
    }

    public void increaseInflammation(Compartment compartment, float amount) {
        compartment.increaseInflammation(amount);
    }

    public void addSimulation(PBPKModel simulation) {
        simulations.add(simulation);
    }

    public void removeSimulation(PBPKModel simulation) {
        simulations.remove(simulation);
    }
}