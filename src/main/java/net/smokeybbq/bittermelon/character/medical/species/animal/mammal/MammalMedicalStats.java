package net.smokeybbq.bittermelon.character.medical.species.animal.mammal;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.medical.species.animal.AnimalMedicalStats;
import net.smokeybbq.bittermelon.medical.compartments.Compartment;
import net.smokeybbq.bittermelon.medical.simulation.ImmuneSimulationHandler;
import net.smokeybbq.bittermelon.medical.simulation.SimulationHandler;

import java.util.*;

public class MammalMedicalStats extends AnimalMedicalStats {
    private float bloodLevel;
    private float respirationRate;
    private float bloodPressureSystolic;
    private float bloodPressureDiastolic;
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
    }

    public void brain() {
        Compartment brainstem = compartments.get("head").getCompartment("brain").getCompartment("brainstem");

        if (shouldRun(brainstem.getFunction())) {
            mammalCardiorespiratory.update();
        }
    }

}