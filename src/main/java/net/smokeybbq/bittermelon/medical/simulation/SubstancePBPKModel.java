package net.smokeybbq.bittermelon.medical.simulation;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.medical.MedicalStats;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.simulation.compartments.*;

import java.util.*;

public abstract class SubstancePBPKModel {
    protected double dosage;
    protected Character character;
    protected double timeStep = 0.01;
    protected double t = 0;
    protected Substance drug;
    protected SimpleCompartment GI, liver, kidney, lung, heart, brain, adiposeTissue, bone, muscle, lymphatic, endocrine, other;
    protected CirculatoryCompartment circulatory;
    protected Map<String, Compartment> compartments;
    protected double totalConcentration;
    protected MedicalStats medicalStats;
    protected SimpleCompartment[] simpleCompartments;

    public SubstancePBPKModel(double dosage, Character character, Substance drug) {
        this.dosage = dosage;
        this.drug = drug;
        this.character = character;
        this.medicalStats = character.getMedicalStats();
        compartments = medicalStats.getCompartments();
        loadCompartments();
        initializeSimulation();
    }

    // Load compartments from the character's medical stats
    protected void loadCompartments() {
        GI = (SimpleCompartment) compartments.get("Gastrointestinal");
        liver = (EliminatingCompartment) compartments.get("Liver");
        kidney = (EliminatingCompartment) compartments.get("Kidneys");
        lung = (SimpleCompartment) compartments.get("Lungs");
        heart = (SimpleCompartment) compartments.get("Heart");
        brain = (SimpleCompartment) compartments.get("Brain");
        adiposeTissue = (SimpleCompartment) compartments.get("Adipose Tissue");
        bone = (SimpleCompartment) compartments.get("Bone");
        muscle = (SimpleCompartment) compartments.get("Muscle");
        lymphatic = (SimpleCompartment) compartments.get("Lymphatic System");
        endocrine = (SimpleCompartment) compartments.get("Endocrine System");
        other = (SimpleCompartment) compartments.get("Other");
        circulatory = (CirculatoryCompartment) compartments.get("Circulatory System");
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
        GI.setRateConstant(drug.getAbsorptionRateConstant());
        kidney.setRateConstant(drug.getEliminationRateConstant());
        liver.setRateConstant(drug.getMetabolismRateConstant());
    }

    protected void clearMapping() {
        for (Compartment compartment : compartments.values()) {
            compartment.clearConcentrationMapping(drug);
        }
    }


    protected void handleSimpleCompartments() {
        double[] simpleDerivatives = new double[simpleCompartments.length];

        // Calculate the derivatives for each simple compartment
        for (var i = 0; i < simpleCompartments.length; i++) {
            simpleDerivatives[i] = simpleCompartments[i].getDerivative(circulatory.getConcentration(drug), drug);
        }

        // Update the concentrations based on the derivatives
        for (var i = 0; i < simpleCompartments.length; i++) {
            simpleCompartments[i].updateConcentration(drug, simpleDerivatives[i], timeStep);
        }
    }

    public void removeFromSimulations() {
        medicalStats.simulationHandler.removeSimulation(this);
    }

    public double getTotalConcentration() {
        double concentrationSum = 0;

        for (Compartment compartment : compartments.values()) {
            double n =  compartment.getConcentration(drug);
            concentrationSum += n;

            //DEBUG LINES
             System.out.println(compartment.getName() + " " + n);
        }

        System.out.println("------------------------");

        return concentrationSum;
    }
}
