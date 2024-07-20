package net.smokeybbq.bittermelon.character.medical.species;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.simulation.pbpk.PBPKModel;
import net.smokeybbq.bittermelon.medical.compartments.Compartment;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class MedicalStats {
    protected final List<PBPKModel> simulations = new CopyOnWriteArrayList<>();
    protected final Character character;
    protected final Map<String, Compartment> compartments;
    public MedicalStats(Character character, Map<String, Compartment> compartments) {
        this.character = character;
        this.compartments = compartments;
    }

    public void update() {
        additionalUpdate();
    }

    public abstract void additionalUpdate();

    public void addCompartment(Compartment compartment) {
        compartments.put(compartment.getName(), compartment);
    }

    public Map<String, Compartment> getCompartments() {
        return compartments;
    }

    public static boolean shouldRun(float probability) {
        Random random = new Random();
        return random.nextFloat() < probability;
    }
}
