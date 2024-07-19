package net.smokeybbq.bittermelon.character.medical;

import net.smokeybbq.bittermelon.medical.simulation.compartments.Compartment;
import net.smokeybbq.bittermelon.medical.simulation.compartments.CompartmentTag;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static net.minecraft.SharedConstants.TICKS_PER_SECOND;

public class CardiovascularSystem {
    private float bloodOxygen = 100;
    private float pulseTimer = 0;
    private float pulseRate = 0;
    private float heartEffort = 20;
    final float MAX_BLOOD_OXYGEN = 100f;
    final float MIN_HEART_EFFORT = 0.1f;
    HeartRhythm rhythm = HeartRhythm.VENTRICULAR_FIBRILLATION;
    private int BPM = 0;
    private final Map<String, Compartment> compartments;
    Set<Compartment> respiratoryCompartments = new HashSet<>();

    public CardiovascularSystem(Map<String, Compartment> compartments) {
        this.compartments = compartments;
        initialize();
    }


    private void initialize() {
        for (Compartment outerCompartment : compartments.values()) {
            outerCompartment.traverseCompartments(compartment -> {
                if (compartment.hasTag(CompartmentTag.RESPIRATORY)) {
                    respiratoryCompartments.add(compartment.getMainCompartment());
                }
            });
        }
    }

    public void update() {
        final int MAX_HEART_RATE = 220;

        bloodOxygen = Math.max(bloodOxygen - 0.05F, 0);

        // Cardiac Arrest
        if (BPM > MAX_HEART_RATE) {
            compartments.get("chest").getCompartment("heart").getMainCompartment().modifyHealth(-0.1f);
        }

//        logDebugInfo();

        if (bloodOxygen < 1) {
            for (Compartment outerCompartment : compartments.values()) {
                outerCompartment.traverseCompartments(compartment -> {
                    compartment.modifyHealth(-0.1F);
                });
            }
        }
    }

    private void heartRhythm() {
        switch (rhythm) {
            case SINUS -> {
                sinus();
            }
            case VENTRICULAR_FIBRILLATION -> {
                ventricularFibrillation();
            }
            case ATRIAL_FIBRILLATION -> {
                atrialFibrillation();
            }
            case PEA -> {
                PEA();
            }
            case ASYSTOLE -> {
                asystole();
            }
        }
    }

    public void switchHeartState(HeartRhythm heartRhythm) {
        rhythm = heartRhythm;
    }

    private void checkForArrhythmia() {
        float heartHealth = compartments.get("chest").getCompartment("heart").getFunction();

        float fibrillationChance = (100 - heartHealth) / 1000;

    }

    private void sinus() {
        pulseRate = heartEffort;
        pulseRate = Math.max(pulseRate, 0);

        heartEffort += (bloodOxygen < 99) ? -0.01f : 0.01f;
        heartEffort = Math.max(heartEffort, MIN_HEART_EFFORT);
        heartEffort = Math.min(heartEffort, 30);

        pulseTimer++;

        // One heartbeat
        if (pulseTimer >= pulseRate && pulseRate >= 0.1) {
            float respiratoryFunction = respiratoryFunction();
            float circulatoryFunction = compartments.get("circulatory_system").getFunction();

            pulseTimer = 0;
            bloodOxygen = Math.min(bloodOxygen + respiratoryFunction * circulatoryFunction, MAX_BLOOD_OXYGEN);
        }

        BPM = Math.round((TICKS_PER_SECOND * 60) / pulseRate);
    }

    private void ventricularFibrillation() {
        BPM = ThreadLocalRandom.current().nextInt(300, 600);
    }

    private void atrialFibrillation() {

    }

    private void PEA() {
        BPM = ThreadLocalRandom.current().nextInt(60, 75);
    }

    private void asystole() {
        BPM = 0;
    }

    private void logDebugInfo() {
        System.out.println("Heart Effort: " + heartEffort);
        System.out.println("Oxygen: " + bloodOxygen);
        System.out.println("Pulse Rate: " + pulseRate);
        System.out.println("Estimated BPM: " + BPM);
    }

    private float respiratoryFunction() {
        Compartment chest = compartments.get("chest");

        float tracheaFunction = chest.getCompartment("trachea").getFunction();
        float respiratoryFunction = 0;

        for (Compartment compartment : respiratoryCompartments) {
            respiratoryFunction += compartment.getFunction();
        }

        return tracheaFunction * respiratoryFunction;
    }

}
