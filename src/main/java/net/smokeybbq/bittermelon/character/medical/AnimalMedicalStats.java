package net.smokeybbq.bittermelon.character.medical;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.simulation.compartments.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static net.minecraft.SharedConstants.TICKS_PER_SECOND;

public class AnimalMedicalStats extends MedicalStats {
    private float bloodLevel;
    private float respirationRate;
    private float bloodPressureSystolic;
    private float bloodPressureDiastolic;
    private float bodyTemperature;
    private final CardiovascularSystem cardiovascularSystem;


    public AnimalMedicalStats(Character character, Map<String, Compartment> compartments) {
        super(character, compartments);
        initializeCompartments();
        cardiovascularSystem = new CardiovascularSystem(compartments);
    }

    public void initializeCompartments() {
    }

    public void additionalUpdate() {

    }

    public void brain() {
        Compartment brainstem = compartments.get("head").getCompartment("brain").getCompartment("brainstem");

        if (shouldRun(brainstem.getFunction())) {
            cardiovascularSystem.update();
        }
    }

    public static boolean shouldRun(float probability) {
        Random random = new Random();
        return random.nextFloat() < probability;
    }

}