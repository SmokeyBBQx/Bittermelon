package net.smokeybbq.bittermelon.character.medical;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.conditions.Condition;
import net.smokeybbq.bittermelon.medical.simulation.PBPKModel;
import net.smokeybbq.bittermelon.medical.simulation.compartments.CirculatoryCompartment;
import net.smokeybbq.bittermelon.medical.simulation.compartments.Compartment;
import net.smokeybbq.bittermelon.medical.simulation.compartments.EliminatingCompartment;
import net.smokeybbq.bittermelon.medical.simulation.compartments.SimpleCompartment;

import java.util.*;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class MedicalStats {
    private static final double INITIAL_HEALTH = 100.0;
    private Map<String, Double> organHealth = new HashMap<>();
    private Map<String, Double> organBloodFlow = new HashMap<>();
    private List<Condition> conditions = new ArrayList<>();
    private double bloodLevel, pulseRate, respirationRate, bloodPressureSystolic, bloodPressureDiastolic, bodyTemperature;
    private double bloodOxygen = 0;
    private double maxHeartRate = 220;
    private Character character;
    private int pulseTimer = 0;
    private double heartEffort = 0;
    private int pulse = 0;
    private int timer = 0;
    private int actualPulseRate = 0;
    private Double volumeGI, volumeLiver, volumeCirculatory, volumeKidney, volumeHeart, volumeLung, volumeBrain, volumeAdiposeTissue, volumeBone, volumeMuscle, volumeLymphatic, volumeEndocrine, volumeOther;
    private SimpleCompartment GI, liver, peripheral, kidney, lung, heart, brain, adiposeTissue, bone, muscle, lymphatic, endocrine, other;
    private CirculatoryCompartment circulatory;
    private Map<String, Compartment> compartmentMap;
    public SimulationHandler simulationHandler;

    public MedicalStats(Character character) {
        this.character = character;
        initializeOrgans();
        initializeVolumes(character.getWeight());
        createCompartments();
        updateBloodFlow();
        simulationHandler = new SimulationHandler(character, compartmentMap);
    }

    private void initializeVolumes(double weight) {
        volumeGI =  0.0207 * weight;
        volumeCirculatory = 0.25 * 0.075 * weight;
        volumeKidney = 0.0051 * weight;
        volumeLiver = 0.0341 * weight;
        volumeHeart = 0.0069 * weight;
        volumeLung = 0.0415 * weight;
        volumeBrain = 0.0252 * weight;
        volumeAdiposeTissue = 0.1363 * weight;
        volumeBone = 0.1484 * weight;
        volumeMuscle = 0.3156 * weight;
        volumeLymphatic = 0.0019 * weight;
        volumeEndocrine = 0.0016 * weight;
        volumeOther = 0.1363 * weight;
    }

    private void createCompartments() {
        GI = new EliminatingCompartment("Gastrointestinal", volumeGI);
        liver = new EliminatingCompartment("Liver", volumeLiver);
        kidney = new EliminatingCompartment("Kidneys", volumeKidney);
        circulatory = new CirculatoryCompartment("Circulatory System", volumeCirculatory);
        lung = new SimpleCompartment("Lungs", volumeLung);
        heart = new SimpleCompartment("Heart", volumeHeart);
        brain = new SimpleCompartment("Brain", volumeBrain);
        adiposeTissue = new SimpleCompartment("Adipose Tissue", volumeAdiposeTissue);
        bone = new SimpleCompartment("Bone", volumeBone);
        muscle = new SimpleCompartment("Muscle", volumeMuscle);
        lymphatic = new SimpleCompartment("Lymphatic System", volumeLymphatic);
        endocrine = new SimpleCompartment("Endocrine System", volumeEndocrine);
        other = new SimpleCompartment("Other", volumeOther);

        compartmentMap.put("Gastrointestinal", GI);
        compartmentMap.put("Liver", liver);
        compartmentMap.put("Peripheral System", peripheral);
        compartmentMap.put("Kidneys", kidney);
        compartmentMap.put("Circulatory System", circulatory);
        compartmentMap.put("Lungs", lung);
        compartmentMap.put("Heart", heart);
        compartmentMap.put("Brain", brain);
        compartmentMap.put("Adipose Tissue", adiposeTissue);
        compartmentMap.put("Bone", bone);
        compartmentMap.put("Muscle", muscle);
        compartmentMap.put("Lymphatic System", lymphatic);
        compartmentMap.put("Endocrine System", endocrine);
        compartmentMap.put("Other", other);
    }

    public void modifyHealth(String name, double health) {
        if (organHealth.containsKey(name)) {
            double currentHealth = organHealth.get(name);
            double newHealth = Math.max(0, currentHealth - health);
            organHealth.put(name, newHealth);
            updateBloodFlow();
        }
    }

    public void update() {
        cardiovascularSystem();
        simulationHandler.update();
    }

    public void cardiovascularSystem() {
        pulseRate = 20 * organHealth.get("Heart") / 100 * Math.max(heartEffort, 0.1);

        if (bloodOxygen < 99) {
            heartEffort -= 0.1;
        } else {
            heartEffort += 0.1;
        }

        pulseTimer++;
        timer++;
        bloodPressureSystolic -= 0.1;
        bloodPressureDiastolic -= 0.1;
        bloodOxygen -= 0.1;

        if (pulseTimer >= pulseRate) {
            pulseTimer = 0;
            bloodOxygen += organBloodFlow.get("Lungs") * (organBloodFlow.get("Arterial Blood") + organBloodFlow.get("Venous Blood"));
            bloodOxygen = Math.min(bloodOxygen, 100);
            bloodPressureSystolic += 3;
            bloodPressureDiastolic += 2;
            pulse++;
        }

        if (timer >= 1200) {
            actualPulseRate = pulse;
            timer = 0;
            pulse = 0;
        }

        System.out.println("Diastolic: " + bloodPressureDiastolic);
        System.out.println("Systolic: " + bloodPressureSystolic);
        System.out.println("Oxygen: " + bloodOxygen);
        System.out.println("Pulse Rate: " + pulseRate);
        System.out.println("Actual Pulse Rate: " + actualPulseRate);
     }

    private void initializeOrgans() {
        String[] organs = {"Gastrointestinal", "Liver", "Arterial Blood", "Venous Blood", "Lungs",
                "Heart", "Brain", "Adipose Tissue", "Bone", "Muscle", "Lymphatic System",
                "Endocrine System", "Other", "Kidneys"};
        for (String organ : organs) {
            organHealth.put(organ, INITIAL_HEALTH);
        }

        organHealth.put("Lungs", 80.0);
    }
    private void updateBloodFlow() {
        double heartHealth = organHealth.get("Heart") / 100.0;
        for (String organ : organHealth.keySet()) {
            if (organ.equals("Heart")) {
                organBloodFlow.put(organ, heartHealth);
            } else {
                organBloodFlow.put(organ, heartHealth * organHealth.get(organ) / 100.0);
            }
        }
    }

    public double getBloodFlow(String organName) {
        return organBloodFlow.getOrDefault(organName, 0.0);
    }

    public void increaseOrganHealth(String organName, double health) {
        if (organHealth.containsKey(organName)) {
            double updatedHealth = organHealth.get(organName) + health;
            organHealth.put(organName, updatedHealth);
        }
    }

    public void decreaseOrganHealth(String organName, double health) {
        if (organHealth.containsKey(organName)) {
            double updatedHealth = organHealth.get(organName) - health;
            updatedHealth = Math.max(updatedHealth, 0.0);
            organHealth.put(organName, updatedHealth);
        }
    }
    public double getOrganHealth(String organName) {
        return organHealth.getOrDefault(organName, 0.0);
    }

    public void addCondition(Condition condition) {
        conditions.add(condition);
    }

    public List<Condition> getConditions() {
        return conditions;
    }
}
