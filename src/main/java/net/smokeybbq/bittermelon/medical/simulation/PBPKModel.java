package net.smokeybbq.bittermelon.medical.simulation;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.medical.MedicalStats;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.simulation.compartments.*;

import java.util.*;

public abstract class PBPKModel {
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
    protected String drugName;
    public PBPKModel(double dosage, Character character, Substance drug) {
        this.dosage = dosage;
        this.drug = drug;
        this.character = character;
        this.medicalStats = character.getMedicalStats();
        compartments = medicalStats.getCompartments();
        loadCompartments();
        initializeSimulation();
    }

    protected void loadCompartments() {
        GI = (EliminatingCompartment) compartments.get("Gastrointestinal");
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


    protected abstract void initializeSimulation();
    public abstract void runSimulation();
    protected void updateRateConstants() {
        GI.setRateConstant(drug.getAbsorptionRateConstant());
        kidney.setRateConstant(drug.getEliminationRateConstant());
        liver.setRateConstant(-drug.getMetabolismRateConstant());
    }

    protected void clearMapping() {
        for (Compartment compartment : compartments.values()) {
            compartment.clearConcentrationMapping(drug);
        }
    }

    protected void handleSimpleCompartments() {
        double[] simpleDerivatives = new double[simpleCompartments.length];

        for (var i = 0; i < simpleCompartments.length; i++) {
            simpleDerivatives[i] = simpleCompartments[i].getDerivative(
                    circulatory.getConcentration(drug),
                    simpleCompartments[i].getConcentration(drug),
                    simpleCompartments[i].getVolume(),
                    medicalStats.getBloodFlow(simpleCompartments[i].getName())
            );
        }

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
            concentrationSum += compartment.getConcentration(drug);
        }

        return concentrationSum;
    }
}
