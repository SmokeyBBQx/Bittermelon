package net.smokeybbq.bittermelon.character.medical;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.conditions.Condition;
import net.smokeybbq.bittermelon.medical.simulation.compartments.*;

import java.util.*;

public class MedicalStats {
    private final List<Condition> conditions = new ArrayList<>();
    private double bloodLevel;
    private double respirationRate;
    private double bloodPressureSystolic;
    private double bloodPressureDiastolic;
    private double bodyTemperature;
    private double bloodOxygen = 100;
    private float pulseTimer = 0;
    private double heartEffort = 0;
    private int pulse = 0;
    private int timer = 0;
    private int BPM = 0;
    private final Map<String, Compartment> compartments = new HashMap<>();
    public SimulationHandler simulationHandler;

    public MedicalStats(Character character) {
        createCompartments();
        updateBloodFlow();
        simulationHandler = new SimulationHandler(character, compartments);
    }
    private void createCompartments() {
        // List of compartments
        Compartment[] compartmentsArray = {
                // Internal Organs
                new EliminatingCompartment("Gastrointestinal", this),
                new EliminatingCompartment("Liver", this),
                new EliminatingCompartment("Kidneys", this),
                new CirculatoryCompartment("Circulatory System", this),
                new SimpleCompartment("Lungs", this),
                new SimpleCompartment("Heart", this),
                new SimpleCompartment("Brain", this),
                new SimpleCompartment("Lymphatic System", this),
                new SimpleCompartment("Endocrine System", this),
                new SimpleCompartment("Other", this),

                // Sensory Organs
                new SimpleCompartment("Left Eye", this),
                new SimpleCompartment("Right Eye", this),
                new SimpleCompartment("Nose", this),
                new SimpleCompartment("Left Ear", this),
                new SimpleCompartment("Right Ear", this),

                // Body Parts
                new BodyPartGroupCompartment("Head", this),
                new BodyPartGroupCompartment("Neck", this),
                new BodyPartGroupCompartment("Left Shoulder", this),
                new BodyPartGroupCompartment("Right Shoulder", this),
                new BodyPartGroupCompartment("Upper Back", this),
                new BodyPartGroupCompartment("Abdomen", this),
                new BodyPartGroupCompartment("Left Upper Arm", this),
                new BodyPartGroupCompartment("Left Lower Arm", this),
                new BodyPartGroupCompartment("Lower Back", this),
                new BodyPartGroupCompartment("Left Hand", this),
                new BodyPartGroupCompartment("Right Hand", this),
                new BodyPartGroupCompartment("Left Thigh", this),
                new BodyPartGroupCompartment("Right Thigh", this),
                new BodyPartGroupCompartment("Left Calf", this),
                new BodyPartGroupCompartment("Right Calf", this),
                new BodyPartGroupCompartment("Left Foot", this),
                new BodyPartGroupCompartment("Right Foot", this),
                new BodyPartGroupCompartment("Groin", this),
                new BodyPartGroupCompartment("Left Loin", this),
                new BodyPartGroupCompartment("Right Loin", this),
                new BodyPartGroupCompartment("Left Hip", this),
                new BodyPartGroupCompartment("Right Hip", this)
        };

        // Add compartments to the map
        for (Compartment compartment : compartmentsArray) {
            addCompartment(compartment);
        }
    }

    public void update() {
        updateBloodFlow();
        cardiovascularSystem();
        simulationHandler.update();
        for (Condition condition : conditions) {
            condition.update();
        }
    }

    public void cardiovascularSystem() {
        double pulseRate = compartments.get("Heart").getHealth() / 100 * heartEffort;

        if (bloodOxygen < 99) {
            heartEffort -= 0.1;
        } else {
            heartEffort += 0.1;
        }
        heartEffort = Math.max(heartEffort, 0.1);

        pulseTimer++;
        timer++;

        bloodOxygen -= 0.05;
        bloodOxygen = Math.max(bloodOxygen, 0);

        // One heartbeat
        if (pulseTimer >= pulseRate) {
            double lungBloodFlow = compartments.get("Lungs").getBloodFlow();
            double circulatoryBloodFlow = compartments.get("Circulatory System").getBloodFlow();

            pulseTimer = 0;
            bloodOxygen += lungBloodFlow * circulatoryBloodFlow;
            bloodOxygen = Math.min(bloodOxygen, 100);
            pulse++;
        }

        // Calculate BPM after 1200 ticks or 1 minute
        if (timer >= 1200) {
            BPM = pulse;
            timer = 0;
            pulse = 0;
        }

        // Cardiac Arrest
        double maxHeartRate = 220;
        if (BPM > maxHeartRate) {
            compartments.get("Heart").removeHealth(0.5);
            timer = 1200;
            pulseTimer -= 0.1F;
            pulseTimer = Math.max(pulseTimer, 0);
        }

        // Debugging
//        System.out.println("Heart Effort: " + heartEffort);
//        System.out.println("Oxygen: " + bloodOxygen);
//        System.out.println("Pulse Rate: " + pulseRate);
//        System.out.println("BPM: " + BPM);
     }

    private void updateBloodFlow() {
        double heartHealth = compartments.get("Heart").getHealth() / 100.0;
        for (Compartment compartment : compartments.values()) {
            if (compartment.getName().equals("Heart")) {
                compartment.setBloodFlow(heartHealth);
            } else {
                compartment.setBloodFlow(heartHealth * compartment.getHealth() / 100.0);
            }
        }
    }

    public double getBloodFlow(String name) {
        if (compartments.containsKey(name)) {
            return compartments.get(name).getBloodFlow();
        }
        return 0;
    }

    public void addCompartment(Compartment compartment) {
        compartments.put(compartment.getName(), compartment);
    }

    public void increaseCompartmentHealth(String name, double health) {
        if (compartments.containsKey(name)) {
            compartments.get(name).addHealth(health);
        }
    }

    public void decreaseCompartmentHealth(String name, double health) {
        if (compartments.containsKey(name)) {
            compartments.get(name).removeHealth(health);
        }
    }
    public double getCompartmentHealth(String name) {
        if (compartments.containsKey(name)) {
            return compartments.get(name).getHealth();
        }
        return 0;
    }

    public void addCondition(Condition condition) {
        conditions.add(condition);
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public Map<String, Compartment> getCompartments() {
        return compartments;
    }
}
