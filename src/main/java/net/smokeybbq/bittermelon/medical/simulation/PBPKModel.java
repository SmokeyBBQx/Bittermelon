package net.smokeybbq.bittermelon.medical.simulation;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.medical.MedicalStats;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.simulation.compartments.*;

import java.util.*;

public abstract class PBPKModel {
    protected float dosage;
    protected Character character;
    protected float timeStep = 0.01F;
    protected float t = 0;
    protected Substance drug;
    protected Map<String, Compartment> compartments;
    protected float totalConcentration;
    protected MedicalStats medicalStats;
    protected List<SimpleCompartment> simpleCompartments = new ArrayList<>();

    public PBPKModel(float dosage, Character character, Substance drug) {
        this.dosage = dosage;
        this.drug = drug;
        this.character = character;
        this.medicalStats = character.getMedicalStats();
        compartments = medicalStats.getCompartments();
        initializeSimpleCompartments();
        initializeSimulation();
    }

    private void initializeSimpleCompartments() {
        // Initialize compartments that will get their concentration from circulatory system
        for (Compartment compartment : compartments.values()) {
            if (compartment instanceof GroupCompartment) {
                simpleCompartments.addAll(((GroupCompartment) compartment).getCompartments().values());
            } else if (compartment instanceof SimpleCompartment) {
                simpleCompartments.add((SimpleCompartment) compartment);
            }
        }
    }

    // Abstract method to initialize the simulation (set dosage, etc.), to be implemented by subclasses
    protected abstract void initializeSimulation();

    // Run the simulation for one time step
    public void runSimulation() {
        if (totalConcentration > 1) {
            updateRateConstants(); // Update the rate constants for absorption, metabolism, and elimination
            simulation(); // Perform the simulation implemented by subclass
            totalConcentration = getTotalConcentration();
            t += timeStep;

            //DEBUGGING LINES
            System.out.println("Running simulation at time: " + t);
            System.out.println();
            System.out.println("Total concentration: " + totalConcentration);

        } else {
            clearMapping(); // Clear the concentration mappings in the compartments
            removeFromSimulations(); // Remove this simulation from the handler/character

            //DEBUGGING LINES
            System.out.println("Clearing mapping and removing simulation at time: " + t);
        }
    }

    // Abstract method for the simulation logic, to be implemented by subclasses
    protected abstract void simulation();

    protected void updateRateConstants() {
        compartments.get("Gastrointestinal").setRateConstant(drug.getAbsorptionRateConstant());
        compartments.get("Kidneys").setRateConstant(drug.getEliminationRateConstant());
        compartments.get("Liver").setRateConstant(drug.getMetabolismRateConstant());
    }

    protected void clearMapping() {
        for (Compartment compartment : compartments.values()) {
            compartment.clearConcentrationMapping(drug);
        }
    }

    protected void handleSimpleCompartments() {
        float[] simpleDerivatives = new float[simpleCompartments.size()];

        // Calculate the derivatives for each simple compartment
        for (var i = 0; i < simpleCompartments.size(); i++) {
            simpleDerivatives[i] = simpleCompartments.get(i).getDerivative(compartments.get("Circulatory System").getConcentration(drug), drug);
        }

        // Update the concentrations based on the derivatives
        for (var i = 0; i < simpleCompartments.size(); i++) {
            simpleCompartments.get(i).updateConcentration(drug, simpleDerivatives[i], timeStep);
        }
    }

    protected float getSubstanceInteractions() {
        for (Compartment compartment : compartments.values()) {
            Set<Substance> substances = compartment.getConcentrations().keySet();
            for (Substance substance : substances) {
                return drug.interact(substance);
            }
        }

        return 0;
    }

    public void removeFromSimulations() {
        medicalStats.simulationHandler.removeSimulation(this);
    }

    public float getTotalConcentration() {
        float concentrationSum = 0;

        for (Compartment compartment : compartments.values()) {
            float n =  compartment.getConcentration(drug);
            concentrationSum += n;

            //DEBUG LINES
             System.out.println(compartment.getName() + " " + n);
        }

        System.out.println("------------------------");

        return concentrationSum;
    }
}
