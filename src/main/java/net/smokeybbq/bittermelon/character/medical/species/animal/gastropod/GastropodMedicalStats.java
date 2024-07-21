package net.smokeybbq.bittermelon.character.medical.species.animal.gastropod;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.medical.species.animal.AnimalMedicalStats;
import net.smokeybbq.bittermelon.medical.compartments.Compartment;
import net.smokeybbq.bittermelon.medical.simulation.ImmuneSimulationHandler;
import net.smokeybbq.bittermelon.medical.simulation.SimulationHandler;

import java.util.Map;

public class GastropodMedicalStats extends AnimalMedicalStats {
    private final GastropodCardiorespiratory gastropodCardiorespiratory;
    public GastropodMedicalStats(Character character, Map<String, Compartment> compartments) {
        super(character, compartments);
        gastropodCardiorespiratory = new GastropodCardiorespiratory(compartments);
    }

    @Override
    public SimulationHandler getSimulationHandler() {
        if (simulationHandler == null) {
            simulationHandler = new ImmuneSimulationHandler(character, compartments);
        }
        return simulationHandler;
    }

    public void additionalUpdate() {
        ganglia();
    }

    private void ganglia() {
        Compartment pleuralGanglia = compartments.get("visceral_hump").getCompartment("ganglia").getCompartment("pleural_ganglia");

        if (shouldRun(pleuralGanglia.getFunction())) {
            gastropodCardiorespiratory.update();
        }
    }
}
