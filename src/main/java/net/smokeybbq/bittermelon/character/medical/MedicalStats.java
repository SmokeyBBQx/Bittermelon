package net.smokeybbq.bittermelon.character.medical;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.simulation.PBPKModel;
import net.smokeybbq.bittermelon.medical.simulation.compartments.Compartment;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static net.smokeybbq.bittermelon.medical.simulation.compartments.anatomies.HumanFactory.createCompartments;

public abstract class MedicalStats {
    protected final List<PBPKModel> simulations = new CopyOnWriteArrayList<>();
    protected final Character character;
    protected final Map<String, Compartment> compartments;
    public SimulationHandler simulationHandler;
    public MedicalStats(Character character, Map<String, Compartment> compartments) {
        this.character = character;
        this.compartments = compartments;
    }

    public SimulationHandler getSimulationHandler() {
        if (simulationHandler == null) {
            simulationHandler = new SimulationHandler(character, compartments);
        }
        return simulationHandler;
    }

    public void update() {
        SimulationHandler handler = getSimulationHandler();
        handler.update();
        additionalUpdate();
    }

    public abstract void additionalUpdate();

    public void addCompartment(Compartment compartment) {
        compartments.put(compartment.getName(), compartment);
    }

    public Map<String, Compartment> getCompartments() {
        return compartments;
    }
}
