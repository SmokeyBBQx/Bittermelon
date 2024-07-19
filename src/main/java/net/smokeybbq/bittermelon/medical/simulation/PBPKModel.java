package net.smokeybbq.bittermelon.medical.simulation;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.medical.AnimalMedicalStats;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.simulation.compartments.*;

import java.util.*;

public abstract class PBPKModel {
    protected float dosage;
    protected Character character;
    protected float timeStep = 0.01F;
    protected float t = 0;
    protected Substance substance;
    protected Map<String, Compartment> compartments;
    protected float totalConcentration;
    protected AnimalMedicalStats medicalStats;
    protected List<Compartment> simpleCompartments = new ArrayList<>();

    public PBPKModel(float dosage, Character character, Substance substance) {
        this.dosage = dosage;
        this.substance = substance;
        this.character = character;
        this.medicalStats = character.getMedicalStats();
        compartments = medicalStats.getCompartments();
        initializeSimpleCompartments();
    }

    private void initializeSimpleCompartments() {
        for (Compartment outerCompartment : compartments.values()) {
            outerCompartment.traverseCompartments(compartment -> {
                if (compartment.getClass().equals(Compartment.class))
                    simpleCompartments.add(compartment);
            });
        }

        compartments.get("circulatory_system").getMainCompartment().excludeFromCirculation = true;
    }


    // Abstract method to initialize the simulation (set dosage, etc.), to be implemented by subclasses
    protected abstract void initializeSimulation();

    // Run the simulation for one time step
    public void runSimulation() {
        if (totalConcentration > 1) {
            simulation(); // Perform the simulation implemented by subclass
            totalConcentration = getTotalConcentration();
            t += timeStep;

            //DEBUGGING LINES
            System.out.println("Running simulation at time: " + t);
            System.out.println();
            System.out.println(substance.getName() + " Total concentration: " + totalConcentration);

        } else {
            clearMapping(); // Clear the concentration mappings in the compartments
            removeFromSimulations(); // Remove this simulation from the handler/character

            //DEBUGGING LINES
            System.out.println("Clearing mapping and removing simulation at time: " + t);
        }
    }

    // Abstract method for the simulation logic, to be implemented by subclasses
    protected abstract void simulation();

    protected void clearMapping() {
        for (Compartment compartment : compartments.values()) {
            compartment.clearConcentration(substance);
        }
    }

    protected void handleSimpleCompartments() {
        Compartment circulatorySystem = compartments.get("circulatory_system").getMainCompartment();
        float totalSubstance = circulatorySystem.getConcentration(substance);
        float totalBloodFlow = 0;

        for (Compartment compartment : simpleCompartments) {
            if (!compartment.excludeFromCirculation) {
                totalBloodFlow += compartment.getBloodFlow();
            }
        }

        // Calculate distribution amounts for each compartment
        Map<Compartment, Float> distributionAmounts = new HashMap<>();

        for (Compartment compartment : simpleCompartments) {
            if (!compartment.excludeFromCirculation) {
                float distributionRate = compartment.getBloodFlow() / totalBloodFlow;
                float amount = totalSubstance * distributionRate;
                distributionAmounts.put(compartment, amount);
            }
        }

        // Move substance from circulatory system to other compartments
        for (Map.Entry<Compartment, Float> entry : distributionAmounts.entrySet()) {
            Compartment targetCompartment = entry.getKey();
            float amount = entry.getValue();

            targetCompartment.updateConcentration(substance, amount * timeStep);
            circulatorySystem.updateConcentration(substance, -amount * timeStep);

            if (targetCompartment.hasTag(CompartmentTag.METABOLIZING)) {
                targetCompartment.eliminateConcentration(substance, substance.getMetabolismRateConstant());
            }

            if (targetCompartment.hasTag(CompartmentTag.ELIMINATING)) {
                targetCompartment.eliminateConcentration(substance, substance.getEliminationRateConstant());
            }
        }

        // Calculate and move substance back to circulatory system
        for (Compartment compartment : simpleCompartments) {
            if (!compartment.excludeFromCirculation) {
                float returnAmount = compartment.getBloodFlow();
                compartment.moveConcentration(circulatorySystem, substance, returnAmount, timeStep);
            }
        }
    }


    public void removeFromSimulations() {
        medicalStats.simulationHandler.removeSimulation(this);
    }

    public float getTotalConcentration() {
        float concentrationSum = 0;

        for (Compartment compartment : simpleCompartments) {
            float n = compartment.getConcentration(substance);
            concentrationSum += n;

            //DEBUG LINES
            System.out.println(substance.getName() + " " + compartment.getName() + " " + n);
            System.out.println(substance.getName() + " " + compartment.getName() + " BF: " + compartment.getBloodFlow());
        }

        System.out.println("------------------------");

        return concentrationSum;
    }
}
