package net.smokeybbq.bittermelon.character.medical.species;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.compartments.Compartment;
import net.smokeybbq.bittermelon.medical.simulation.SimulationHandler;

import java.util.Map;

public class AnimalMedicalStats extends MedicalStats {
    protected SimulationHandler simulationHandler;
    public AnimalMedicalStats(Character character, Map<String, Compartment> compartments) {
        super(character, compartments);
    }

    public SimulationHandler getSimulationHandler() {
        if (simulationHandler == null) {
            simulationHandler = new SimulationHandler(character, compartments);
        }
        return simulationHandler;
    }

    @Override
    public void update() {
        SimulationHandler handler = getSimulationHandler();
        handler.update();
        additionalUpdate();
    }

    @Override
    public void additionalUpdate() {

    }

}
