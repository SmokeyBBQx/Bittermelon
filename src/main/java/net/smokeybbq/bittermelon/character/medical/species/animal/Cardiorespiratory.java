package net.smokeybbq.bittermelon.character.medical.species.animal;

import net.smokeybbq.bittermelon.character.medical.HeartRhythm;
import net.smokeybbq.bittermelon.medical.compartments.Compartment;
import net.smokeybbq.bittermelon.medical.compartments.CompartmentTag;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static net.minecraft.SharedConstants.TICKS_PER_SECOND;

public abstract class Cardiorespiratory {
    protected float bloodOxygen = 100;
    protected float pulseTimer = 0;
    protected float pulseRate = 0;
    protected float heartEffort = 20;
    protected final float MAX_BLOOD_OXYGEN = 100f;
    protected final float MIN_HEART_EFFORT = 0.1f;
    protected final float MAX_HEART_RATE = 220;
    protected HeartRhythm rhythm = HeartRhythm.VENTRICULAR_FIBRILLATION;
    protected int BPM = 0;
    protected final Map<String, Compartment> compartments;
    protected Set<Compartment> respiratoryCompartments = new HashSet<>();

    public Cardiorespiratory(Map<String, Compartment> compartments) {
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
        bloodOxygen = Math.max(bloodOxygen - 0.05F, 0);

        // Cardiac Arrest
        if (BPM > MAX_HEART_RATE) {
            compartments.get("chest").getCompartment("heart").getMainCompartment().modifyHealth(-0.1f);
        }

//        logDebugInfo();

        if (bloodOxygen < 1) {
            for (Compartment outerCompartment : compartments.values()) {
                outerCompartment.traverseCompartments(compartment -> compartment.modifyHealth(-0.1F));
            }
        }

        heartRhythm();
    }

    protected abstract void heartRhythm();

    public void switchHeartState(HeartRhythm heartRhythm) {
        rhythm = heartRhythm;
    }

    protected void sinus() {
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

    protected void ventricularFibrillation() {
        BPM = ThreadLocalRandom.current().nextInt(300, 600);
    }

    protected void atrialFibrillation() {

    }

    protected void PEA() {
        BPM = ThreadLocalRandom.current().nextInt(60, 75);
    }

    protected void asystole() {
        BPM = 0;
    }

    protected void logDebugInfo() {
        System.out.println("Heart Effort: " + heartEffort);
        System.out.println("Oxygen: " + bloodOxygen);
        System.out.println("Pulse Rate: " + pulseRate);
        System.out.println("Estimated BPM: " + BPM);
    }

    protected float respiratoryFunction() {
        float respiratoryFunction = 0;

        for (Compartment compartment : respiratoryCompartments) {
            respiratoryFunction += compartment.getFunction();
        }

        return respiratoryFunction;
    }

    public float getBloodOxygen() {return bloodOxygen;}
    public int getBPM() {return BPM;}

}
