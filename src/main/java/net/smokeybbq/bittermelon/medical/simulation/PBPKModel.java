package net.smokeybbq.bittermelon.medical.simulation;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.medical.MedicalStats;
import net.smokeybbq.bittermelon.medical.conditions.Condition;
import net.smokeybbq.bittermelon.medical.medicine.Medicine;
import net.smokeybbq.bittermelon.medical.simulation.compartments.*;

import java.util.*;

public abstract class PBPKModel {
    protected double dosage;
    protected Character character;
    protected double timeStep = 0.01;
    protected double t = 0;
    protected Medicine drug;
    protected Double volumeGI, volumeLiver, volumeCirculatory, volumeKidney, volumeHeart, volumeLung, volumeBrain, volumeAdiposeTissue, volumeBone, volumeMuscle, volumeLymphatic, volumeEndocrine, volumeOther;
    protected SimpleCompartment GI, liver, peripheral, kidney, lung, heart, brain, adiposeTissue, bone, muscle, lymphatic, endocrine, other;
    protected CirculatoryCompartment circulatory;
    protected Map<String, Compartment> compartmentMap;
    protected double totalConcentration;
    protected MedicalStats medicalStats;
    protected SimpleCompartment[] simpleCompartments;
    public PBPKModel(double dosage, Character character, Medicine drug) {
        this.dosage = dosage;
        this.drug = drug;
        this.character = character;
        this.medicalStats = character.getMedicalStats();
        initializeVolumes(character.getWeight());
        createCompartments();
        initializeSimulation();
    }

    protected void initializeVolumes(double weight) {
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

    protected void createCompartments() {
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

    protected abstract void initializeSimulation();
    public abstract void runSimulation();

    protected void updateRateConstants() {
        GI.setRateConstant(drug.getAbsorptionRateConstant());
        kidney.setRateConstant(drug.getEliminationRateConstant());
        liver.setRateConstant(-drug.getMetabolismRateConstant());
    }

    protected void handleSimpleCompartments() {
        double[] simpleDerivatives = new double[simpleCompartments.length];

        for (var i = 0; i < simpleCompartments.length; i++) {
            simpleDerivatives[i] = simpleCompartments[i].getDerivative(
                    circulatory.getConcentration(),
                    simpleCompartments[i].getConcentration(),
                    simpleCompartments[i].getVolume(),
                    medicalStats.getBloodFlow(simpleCompartments[i].getName())
            );
        }

        for (var i = 0; i < simpleCompartments.length; i++) {
            simpleCompartments[i].updateConcentration(simpleDerivatives[i], timeStep);
        }
    }
    public double calculateE(Compartment compartment) {
        return drug.getEMax() * compartment.getConcentration() / (drug.getEMax() * drug.getHalfMaximalEffectiveConcentration() + compartment.getConcentration());
    }

    public double drugEffect() {
        List<Condition> conditions = character.getMedicalStats().getConditions();

        for (Condition condition : conditions) {
            if (Arrays.stream(condition.getSuitableTreatments()).anyMatch(drug.getName()::equals)) {
                String affectedArea = condition.getAffectedArea();
                double effectiveness = calculateE(compartmentMap.get(affectedArea));
                condition.treat(drug, effectiveness);
            }
        }
        return 0;
    }

    public void removeFromSimulations() {
        character.getMedicalStats().removeSimulation(this);
    }

    public double getTotalConcentration() {
        double concentrationSum = 0;

        for (Compartment compartment : compartmentMap.values()) {
            concentrationSum += compartment.getConcentration();
        }

        return concentrationSum;
    }
}
