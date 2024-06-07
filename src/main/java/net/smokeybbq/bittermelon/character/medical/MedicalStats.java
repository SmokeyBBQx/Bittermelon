package net.smokeybbq.bittermelon.character.medical;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.conditions.Condition;
import net.smokeybbq.bittermelon.medical.medicine.Medicine;
import net.smokeybbq.bittermelon.medical.simulation.PBPKModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class MedicalStats {
    private static final double INITIAL_HEALTH = 100.0;
    private Map<String, Double> organHealth = new HashMap<>();
    private Map<String, Double> organBloodFlow = new HashMap<>();
    private List<Condition> conditions = new ArrayList<>();
    private List<PBPKModel> simulations = new ArrayList<>();
    private double bloodLevel, pulseRate, respirationRate, bloodPressureSystolic, bloodPressureDiastolic, bodyTemperature;
    private double bloodOxygen = 0;
    private double maxHeartRate = 220;
    private Character character;
    private int pulseTimer = 0;
    private double heartEffort = 0;
    private int pulse = 0;
    private int timer = 0;
    private int actualPulseRate = 0;

    public MedicalStats(Character character) {
        this.character = character;
        initializeOrgans();
        updateBloodFlow();
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

        for (PBPKModel simulation : simulations) {
            simulation.runSimulation();
        }
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

    public void addSimulation(PBPKModel simulation) {
        simulations.add(simulation);
    }

    public void removeSimulation(PBPKModel simulation) {
        simulations.remove(simulation);
    }
}
