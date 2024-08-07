package net.smokeybbq.bittermelon.character.medical.species.animal.mammal;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.medical.species.animal.AnimalMedicalStats;
import net.smokeybbq.bittermelon.medical.compartments.Compartment;
import net.smokeybbq.bittermelon.medical.compartments.SingleGroupCompartment;
import net.smokeybbq.bittermelon.medical.simulation.ImmuneSimulationHandler;
import net.smokeybbq.bittermelon.medical.simulation.SimulationHandler;
import net.smokeybbq.bittermelon.medical.symptoms.Pain;
import net.smokeybbq.bittermelon.medical.symptoms.Symptom;

import java.util.*;

public class MammalMedicalStats extends AnimalMedicalStats {
    private float bodyTemperature;
    private final MammalCardiorespiratory mammalCardiorespiratory;


    public MammalMedicalStats(Character character, Map<String, Compartment> compartments) {
        super(character, compartments);
        initializeCompartments();
        mammalCardiorespiratory = new MammalCardiorespiratory(compartments);
    }

    @Override
    public SimulationHandler getSimulationHandler() {
        if (simulationHandler == null) {
            simulationHandler = new ImmuneSimulationHandler(character, compartments);
        }
        return simulationHandler;
    }

    public void initializeCompartments() {
    }

    public void additionalUpdate() {
        brain();
        handlePain();
    }

    private void handlePain() {
        for (Compartment outerCompartment : compartments.values()) {
            outerCompartment.traverseCompartments(compartment -> {
                String name = compartment.getName();
                float pain = compartment.getPain();

                if (pain > 1) {
                    symptoms.put(name, new Pain(character, name, pain));
                } else {
                    symptoms.remove(name);
                }
            });
        }

        // Pain shock
    }

    public void brain() {
        Compartment brain = compartments.get("head").getCompartment("brain");
        Compartment brainstem = brain.getCompartment("brainstem");
        Compartment frontalLobe = brain.getCompartment("frontal_lobe");
        Compartment parietalLobe = brain.getCompartment("parietal_lobe");
        Compartment temporalLobe = brain.getCompartment("temporal_lobe");
        Compartment occipitalLobe = brain.getCompartment("occipital_lobe");
        Compartment cerebellum = brain.getCompartment("cerebellum");

        if (shouldRun(brainstem.getFunction())) {
            mammalCardiorespiratory.update();
        }

        if (shouldRun(frontalLobe.getFunction())) {

        }

        if (shouldRun(temporalLobe.getFunction())) {

        }
    }

}