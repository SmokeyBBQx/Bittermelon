package net.smokeybbq.bittermelon.character.medical;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.conditions.Condition;
import net.smokeybbq.bittermelon.medical.simulation.compartments.CirculatoryCompartment;
import net.smokeybbq.bittermelon.medical.simulation.compartments.Compartment;
import net.smokeybbq.bittermelon.medical.simulation.compartments.EliminatingCompartment;
import net.smokeybbq.bittermelon.medical.simulation.compartments.SimpleCompartment;

import java.util.*;

public class MedicalStats {
    private static final int INITIAL_HEALTH = 100;
    private List<Condition> conditions = new ArrayList<>();
    private float bloodLevel, pulseRate, respirationRate, bloodPressureSystolic, bloodPressureDiastolic;
    private float bloodOxygen = 100;
    private int maxHeartRate = 220;
    private Character character;
    private float pulseTimer = 0;
    private float heartEffort = 0;
    private int pulse = 0;
    private int timer = 0;
    private int BPM = 0;
    private float bodyTemperature;
    private float volumeGI, volumeLiver, volumeCirculatory, volumeKidney, volumeHeart, volumeLung, volumeBrain, volumeAdiposeTissue, volumeBone, volumeMuscle, volumeLymphatic, volumeEndocrine, volumeOther;
    private SimpleCompartment GI, liver, kidney, lung, heart, brain, adiposeTissue, bone, muscle, lymphatic, endocrine, other;
    private CirculatoryCompartment circulatory;
    private Map<String, Compartment> compartments = new HashMap<>();
    public SimulationHandler simulationHandler;

    public MedicalStats(Character character) {
        this.character = character;
        createCompartments();
        updateBloodFlow();
        simulationHandler = new SimulationHandler(character, compartments);
    }

//    private void updateVolumes() {
//        float weight = character.getWeight();
//
//        volumeGI = 0.0207 * weight;
//        volumeCirculatory = 0.075 * weight;
//        volumeKidney = 0.0051 * weight;
//        volumeLiver = 0.0341 * weight;
//        volumeHeart = 0.0069 * weight;
//        volumeLung = 0.0415 * weight;
//        volumeBrain = 0.0252 * weight;
//        volumeAdiposeTissue = 0.1363 * weight;
//        volumeBone = 0.1484 * weight;
//        volumeMuscle = 0.3156 * weight;
//        volumeLymphatic = 0.0019 * weight;
//        volumeEndocrine = 0.0016 * weight;
//        volumeOther = 0.1363 * weight;
//    }

    private void createCompartments() {
        GI = new SimpleCompartment("Gastrointestinal", volumeGI);
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

        compartments.put("Gastrointestinal", GI);
        compartments.put("Liver", liver);
        compartments.put("Kidneys", kidney);
        compartments.put("Circulatory System", circulatory);
        compartments.put("Lungs", lung);
        compartments.put("Heart", heart);
        compartments.put("Brain", brain);
        compartments.put("Adipose Tissue", adiposeTissue);
        compartments.put("Bone", bone);
        compartments.put("Muscle", muscle);
        compartments.put("Lymphatic System", lymphatic);
        compartments.put("Endocrine System", endocrine);
        compartments.put("Other", other);
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
        pulseRate = compartments.get("Heart").getHealth() / 100 * heartEffort;

        if (bloodOxygen < 99) {
            heartEffort -= 0.1F;
        } else {
            heartEffort += 0.1F;
        }
        heartEffort = Math.max(heartEffort, 0.1F);

        pulseTimer++;
        timer++;

        bloodOxygen -= 0.05F;
        bloodOxygen = Math.max(bloodOxygen, 0);

        // One heartbeat
        if (pulseTimer >= pulseRate) {
            float lungBloodFlow = compartments.get("Lungs").getBloodFlow();
            float circulatoryBloodFlow = compartments.get("Circulatory System").getBloodFlow();

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
        if (BPM > maxHeartRate) {
            compartments.get("Heart").removeHealth(0.5F);
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
        float heartHealth = compartments.get("Heart").getHealth() / 100.0F;
        for (Compartment compartment : compartments.values()) {
            if (compartment.equals("Heart")) {
                compartment.setBloodFlow(heartHealth);
            } else {
                compartment.setBloodFlow(heartHealth * compartment.getHealth() / 100.0F);
            }
        }
    }

    public float getBloodFlow(String name) {
        if (compartments.containsKey(name)) {
            return compartments.get(name).getBloodFlow();
        }
        return 0;
    }

    public void addCompartmentHealth(String name, float health) {
        if (compartments.containsKey(name)) {
            compartments.get(name).addHealth(health);
        }
    }

    public void removeCompartmentHealth(String name, float health) {
        if (compartments.containsKey(name)) {
            compartments.get(name).removeHealth(health);
        }
    }
    public float getCompartmentHealth(String name) {
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
