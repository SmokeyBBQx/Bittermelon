package net.smokeybbq.bittermelon.character.medical;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.conditions.Condition;
import net.smokeybbq.bittermelon.medical.simulation.compartments.*;

import java.util.*;

import static net.smokeybbq.bittermelon.medical.simulation.compartments.anatomies.HumanFactory.createCompartments;

public class MedicalStats {
    private final List<Condition> conditions = new ArrayList<>();
    private float bloodLevel;
    private float respirationRate;
    private float bloodPressureSystolic;
    private float bloodPressureDiastolic;
    private float bodyTemperature;
    private float bloodOxygen = 100;
    private float pulseTimer = 0;
    private float heartEffort = 0;
    private int pulse = 0;
    private int timer = 0;
    private int BPM = 0;
    private final Map<String, Compartment> compartments;
    public SimulationHandler simulationHandler;
    private final Character character;
    Set<Compartment> immuneCompartments = new HashSet<>();
    Set<Compartment> respiratoryCompartments = new HashSet<>();

    public MedicalStats(Character character) {
        this.character = character;
        compartments = createCompartments();
        initializeCompartments();
    }

    public void initializeCompartments() {
        for (Compartment outerCompartment : compartments.values()) {
            outerCompartment.traverseCompartments(compartment -> {
                if (compartment.hasTag(CompartmentTag.IMMUNE)) {
                    immuneCompartments.add(compartment);
                }

                if (compartment.hasTag(CompartmentTag.RESPIRATORY)) {
                    respiratoryCompartments.add(compartment);
                }
            });
        }
    }

    public SimulationHandler getSimulationHandler() {
        if (simulationHandler == null) {
            simulationHandler = new SimulationHandler(character, compartments);
        }
        return simulationHandler;
    }

    public void update() {
        cardiovascularSystem();
        SimulationHandler handler = getSimulationHandler();
        handler.update();
        for (Condition condition : conditions) {
            condition.update();
        }
    }

    public void cardiovascularSystem() {
        float pulseRate = compartments.get("chest").getCompartment("heart").getHealth() / 100 * heartEffort;

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
            float respiratoryBloodFlow = 0;
            for (Compartment compartment : respiratoryCompartments) {
                if (compartment.hasTag(CompartmentTag.RESPIRATORY)) {
                    respiratoryBloodFlow *= compartment.getBloodFlow();
                }
            }
            float circulatoryBloodFlow = compartments.get("circulatory_system").getBloodFlow();

            pulseTimer = 0;
            bloodOxygen += respiratoryBloodFlow * circulatoryBloodFlow;
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
        float maxHeartRate = 220;
        if (BPM > maxHeartRate) {
            compartments.get("chest").getCompartment("heart").modifyHealth(-0.5F);
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
        Compartment heart = compartments.get("Heart");
        float heartHealth = heart.getHealth() / 100.0F;
        for (Map.Entry<String, Compartment> entry : compartments.entrySet()) {
            Compartment compartment = entry.getValue();
            if (compartment.getName().equals("Heart")) {
                compartment.setBloodFlow(heartHealth);
            } else {
                compartment.setBloodFlow(heartHealth * compartment.getHealth() / 100.0F);
            }
        }
    }

    public void addCompartment(Compartment compartment) {
        compartments.put(compartment.getName(), compartment);
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

    public float getImmuneHealth() {
        return 100;
    }
}
