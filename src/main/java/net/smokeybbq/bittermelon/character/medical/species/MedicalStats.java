package net.smokeybbq.bittermelon.character.medical.species;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.systems.medical.compartments.Compartment;
import net.smokeybbq.bittermelon.systems.medical.symptoms.Symptom;

import java.util.*;

public abstract class MedicalStats {
    protected final Character character;
    protected final Map<String, Compartment> compartments;
    protected final Map<String, Symptom> symptoms = new HashMap<>();
    public MedicalStats(Character character, Map<String, Compartment> compartments) {
        this.character = character;
        this.compartments = compartments;
    }

    public void update() {
        for (Compartment outerCompartment : compartments.values()) {
            outerCompartment.traverseCompartments(Compartment::update);
        }

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
