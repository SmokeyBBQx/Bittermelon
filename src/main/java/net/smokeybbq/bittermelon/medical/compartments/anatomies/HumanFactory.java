package net.smokeybbq.bittermelon.medical.compartments.anatomies;

import net.smokeybbq.bittermelon.medical.compartments.Compartment;
import net.smokeybbq.bittermelon.medical.compartments.CompartmentTag;
import net.smokeybbq.bittermelon.medical.compartments.GroupCompartment;
import net.smokeybbq.bittermelon.medical.compartments.SingleGroupCompartment;

import java.util.HashMap;
import java.util.Map;

public class HumanFactory {
    private static final float WEIGHT = 70.0f; // in kg

    public static Map<String, Compartment> createCompartments() {
        Map<String, Compartment> compartments = new HashMap<>();

        float brainDensity = 1.036F;
        float lungDensity = 0.26F;
        float liverDensity = 1.06F;
        float kidneyDensity = 1.05F;
        float pancreasDensity = 1.04F;
        float heartDensity = 1.06F;
        float bloodDensity = 1.06F;
        float GIDensity = 1.04F;
        float boneDensity = 1.85F;
        float skinDensity = 1.1F;
        float tendonDensity = 1.1F;
        float muscleDensity = 1.06F;
        float averageDensity = 1.07F;

        GroupCompartment head = new GroupCompartment("head", 1, 8 * WEIGHT);
        head.addSubCompartment(new SingleGroupCompartment("skull", 0.0001F, 3 * WEIGHT / boneDensity));

        GroupCompartment brain = new GroupCompartment("brain", 0.1F, 2.0f * WEIGHT / brainDensity);
        brain.addSubCompartment(new SingleGroupCompartment("frontal_lobe", 0.1F, 0.4f * WEIGHT / brainDensity));
        brain.addSubCompartment(new SingleGroupCompartment("parietal_lobe", 0.1F, 0.4f * WEIGHT / brainDensity));
        brain.addSubCompartment(new SingleGroupCompartment("temporal_lobe", 0.1F, 0.3f * WEIGHT / brainDensity));
        brain.addSubCompartment(new SingleGroupCompartment("occipital_lobe", 0.1F, 0.2f * WEIGHT / brainDensity));
        brain.addSubCompartment(new SingleGroupCompartment("cerebellum", 0.1F, 0.3f * WEIGHT / brainDensity));
        brain.addSubCompartment(new SingleGroupCompartment("brainstem", 0.1F, 0.2f * WEIGHT / brainDensity));
        brain.addSubCompartment(new SingleGroupCompartment("meninges", 0.1F, 0.2f * WEIGHT / brainDensity));

        head.addSubCompartment(new SingleGroupCompartment("left_eye", 0.1F, 0.1f * WEIGHT / averageDensity));
        head.addSubCompartment(new SingleGroupCompartment("right_eye", 0.1F, 0.1f * WEIGHT / averageDensity));
        head.addSubCompartment(new SingleGroupCompartment("left_ear", 0.1F, 0.05f * WEIGHT / averageDensity));
        head.addSubCompartment(new SingleGroupCompartment("right_ear", 0.1F, 0.05f * WEIGHT / averageDensity));
        head.addSubCompartment(new SingleGroupCompartment("skin", 0.2F, 0.5f * WEIGHT / skinDensity));
        head.addSubCompartment(new SingleGroupCompartment("nose", 0.3F, 0.1f * WEIGHT / averageDensity));
        head.addSubCompartment(new SingleGroupCompartment("mouth", 0.5F, 0.1f * WEIGHT / averageDensity));
        head.addSubCompartment(new SingleGroupCompartment("tongue", 0.5F, 0.1f * WEIGHT / averageDensity));
        head.addSubCompartment(new SingleGroupCompartment("teeth", 0.0001F, 0.1f * WEIGHT / boneDensity));

        GroupCompartment chest = new GroupCompartment("chest", 1, 25.0f * WEIGHT);
        chest.addSubCompartment(new SingleGroupCompartment("heart", 0.4F, 0.5f * WEIGHT / heartDensity));

        SingleGroupCompartment leftLung = new SingleGroupCompartment("left_lung", 0.7F, 0.6f * WEIGHT / lungDensity);
        leftLung.addTag(CompartmentTag.RESPIRATORY);
        chest.addSubCompartment(leftLung);

        SingleGroupCompartment rightLung = new SingleGroupCompartment("right_lung", 0.7F, 0.6f * WEIGHT / lungDensity);
        rightLung.addTag(CompartmentTag.RESPIRATORY);
        chest.addSubCompartment(rightLung);

        SingleGroupCompartment thymus = new SingleGroupCompartment("thymus", 0.3F, 0.05f * WEIGHT / averageDensity);
        thymus.addTag(CompartmentTag.IMMUNE);
        chest.addSubCompartment(thymus);

        chest.addSubCompartment(new SingleGroupCompartment("muscles", 0.4F, 10.0f * WEIGHT / muscleDensity));
        chest.addSubCompartment(new SingleGroupCompartment("ribs", 0.1F, 3.0f * WEIGHT / boneDensity));
        chest.addSubCompartment(new SingleGroupCompartment("sternum", 0.1F, 0.3f * WEIGHT / boneDensity));
        chest.addSubCompartment(new SingleGroupCompartment("esophagus", 0.5F, 0.2f * WEIGHT / averageDensity));
        chest.addSubCompartment(new SingleGroupCompartment("trachea", 0.5F, 0.1f * WEIGHT / averageDensity));
        chest.addSubCompartment(new SingleGroupCompartment("skin", 0.2F, 1.0f * WEIGHT / skinDensity));

        GroupCompartment abdomen = new GroupCompartment("abdomen", 1, 30.0f * WEIGHT);

        SingleGroupCompartment stomach = new SingleGroupCompartment("stomach", 0.7F, 0.3f * WEIGHT / GIDensity);
        stomach.addTag(CompartmentTag.GASTROINTESTINAL);
        abdomen.addSubCompartment(stomach);

        SingleGroupCompartment smallIntestine = new SingleGroupCompartment("small_intestine", 0.9F, 1.0f * WEIGHT / GIDensity);
        smallIntestine.addTag(CompartmentTag.GASTROINTESTINAL);
        abdomen.addSubCompartment(smallIntestine);

        SingleGroupCompartment largeIntestine = new SingleGroupCompartment("large_intestine", 0.8F, 1.0f * WEIGHT / GIDensity);
        largeIntestine.addTag(CompartmentTag.GASTROINTESTINAL);
        abdomen.addSubCompartment(largeIntestine);

        SingleGroupCompartment liver = new SingleGroupCompartment("liver", 0.9F, 2.0f * WEIGHT / liverDensity);
        liver.addTag(CompartmentTag.METABOLIZING);
        abdomen.addSubCompartment(liver);

        abdomen.addSubCompartment(new SingleGroupCompartment("gallbladder", 0.5F, 0.05f * WEIGHT / pancreasDensity));
        abdomen.addSubCompartment(new SingleGroupCompartment("pancreas", 0.5F, 0.1f * WEIGHT / pancreasDensity));

        SingleGroupCompartment spleen = new SingleGroupCompartment("spleen", 0.4F, 0.2f * WEIGHT / pancreasDensity);
        spleen.addTag(CompartmentTag.IMMUNE);
        abdomen.addSubCompartment(spleen);

        SingleGroupCompartment leftKidney = new SingleGroupCompartment("left_kidney", 0.9F, 0.15f * WEIGHT / kidneyDensity);
        leftKidney.addTag(CompartmentTag.ELIMINATING);
        abdomen.addSubCompartment(leftKidney);

        SingleGroupCompartment rightKidney = new SingleGroupCompartment("right_kidney", 0.9F, 0.15f * WEIGHT / kidneyDensity);
        rightKidney.addTag(CompartmentTag.ELIMINATING);
        abdomen.addSubCompartment(rightKidney);

        abdomen.addSubCompartment(new SingleGroupCompartment("bladder", 0.4F, 0.2f * WEIGHT / averageDensity));
        abdomen.addSubCompartment(new SingleGroupCompartment("muscles", 0.4F, 10.0f * WEIGHT / muscleDensity));
        abdomen.addSubCompartment(new SingleGroupCompartment("pelvis", 0.2F, 2.0f * WEIGHT / boneDensity));
        abdomen.addSubCompartment(new SingleGroupCompartment("skin", 0.2F, 1.0f * WEIGHT / skinDensity));

        GroupCompartment leftUpperLimb = new GroupCompartment("left_arm", 1, 5.0f * WEIGHT);
        leftUpperLimb.addSubCompartment(new SingleGroupCompartment("left_humerus", 0.1F, 0.5f * WEIGHT / boneDensity));
        leftUpperLimb.addSubCompartment(new SingleGroupCompartment("left_radius_ulna", 0.1F, 0.3f * WEIGHT / boneDensity));
        leftUpperLimb.addSubCompartment(new SingleGroupCompartment("left_hand", 0.3F, 0.5f * WEIGHT / boneDensity));
        leftUpperLimb.addSubCompartment(new SingleGroupCompartment("left_wrist", 0.3F, 0.1f * WEIGHT / boneDensity));
        leftUpperLimb.addSubCompartment(new SingleGroupCompartment("skin", 0.2F, 0.5f * WEIGHT / skinDensity));
        leftUpperLimb.addSubCompartment(new SingleGroupCompartment("muscles", 0.4F, 2.5f * WEIGHT / muscleDensity));
        leftUpperLimb.addSubCompartment(new SingleGroupCompartment("tendons", 0.2F, 0.2f * WEIGHT / tendonDensity));

        GroupCompartment rightUpperLimb = new GroupCompartment("right_arm", 1, 5.0f * WEIGHT);
        rightUpperLimb.addSubCompartment(new SingleGroupCompartment("right_humerus", 0.1F, 0.5f * WEIGHT / boneDensity));
        rightUpperLimb.addSubCompartment(new SingleGroupCompartment("right_radius_ulna", 0.1F, 0.3f * WEIGHT / boneDensity));
        rightUpperLimb.addSubCompartment(new SingleGroupCompartment("right_hand", 0.3F, 0.5f * WEIGHT / boneDensity));
        rightUpperLimb.addSubCompartment(new SingleGroupCompartment("right_wrist", 0.3F, 0.1f * WEIGHT / boneDensity));
        rightUpperLimb.addSubCompartment(new SingleGroupCompartment("skin", 0.2F, 0.5f * WEIGHT / skinDensity));
        rightUpperLimb.addSubCompartment(new SingleGroupCompartment("muscles", 0.4F, 2.5f * WEIGHT / muscleDensity));
        rightUpperLimb.addSubCompartment(new SingleGroupCompartment("tendons", 0.2F, 0.2f * WEIGHT / tendonDensity));

        GroupCompartment leftLowerLimb = new GroupCompartment("left_leg", 1, 15.0f * WEIGHT);
        leftLowerLimb.addSubCompartment(new SingleGroupCompartment("left_femur", 0.1F, 1.5f * WEIGHT / boneDensity));
        leftLowerLimb.addSubCompartment(new SingleGroupCompartment("left_tibia_fibula", 0.1F, 1.0f * WEIGHT / boneDensity));
        leftLowerLimb.addSubCompartment(new SingleGroupCompartment("left_foot", 0.3F, 1.0f * WEIGHT / boneDensity));
        leftLowerLimb.addSubCompartment(new SingleGroupCompartment("left_ankle", 0.3F, 0.2f * WEIGHT / boneDensity));
        leftLowerLimb.addSubCompartment(new SingleGroupCompartment("skin", 0.2F, 1.0f * WEIGHT / skinDensity));
        leftLowerLimb.addSubCompartment(new SingleGroupCompartment("muscles", 0.4F, 8.0f * WEIGHT / muscleDensity));
        leftLowerLimb.addSubCompartment(new SingleGroupCompartment("tendons", 0.2F, 0.5f * WEIGHT / tendonDensity));

        GroupCompartment rightLowerLimb = new GroupCompartment("right_leg", 1, 15.0f * WEIGHT);
        rightLowerLimb.addSubCompartment(new SingleGroupCompartment("right_femur", 0.1F, 1.5f * WEIGHT / boneDensity));
        rightLowerLimb.addSubCompartment(new SingleGroupCompartment("right_tibia_fibula", 0.1F, 1.0f * WEIGHT / boneDensity));
        rightLowerLimb.addSubCompartment(new SingleGroupCompartment("right_foot", 0.3F, 1.0f * WEIGHT / boneDensity));
        rightLowerLimb.addSubCompartment(new SingleGroupCompartment("right_ankle", 0.3F, 0.2f * WEIGHT / boneDensity));
        rightLowerLimb.addSubCompartment(new SingleGroupCompartment("skin", 0.2F, 1.0f * WEIGHT / skinDensity));
        rightLowerLimb.addSubCompartment(new SingleGroupCompartment("muscles", 0.4F, 8.0f * WEIGHT / muscleDensity));
        rightLowerLimb.addSubCompartment(new SingleGroupCompartment("tendons", 0.2F, 0.5f * WEIGHT / tendonDensity));

        GroupCompartment back = new GroupCompartment("back", 1, 15.0f * WEIGHT);
        back.addSubCompartment(new SingleGroupCompartment("spine", 0.1F, 3.0f * WEIGHT / boneDensity));
        back.addSubCompartment(new SingleGroupCompartment("scapula", 0.1F, 0.5f * WEIGHT / boneDensity));
        back.addSubCompartment(new SingleGroupCompartment("muscles", 0.4F, 10.0f * WEIGHT / muscleDensity));
        back.addSubCompartment(new SingleGroupCompartment("skin", 0.2F, 1.0f * WEIGHT / skinDensity));

        SingleGroupCompartment circulatory = new SingleGroupCompartment("circulatory_system", 1, 7.0f * WEIGHT / bloodDensity);

        compartments.put("circulatory_system", circulatory);
        compartments.put("head", head);
        compartments.put("chest", chest);
        compartments.put("abdomen", abdomen);
        compartments.put("left_arm", leftUpperLimb);
        compartments.put("right_arm", rightUpperLimb);
        compartments.put("left_leg", leftLowerLimb);
        compartments.put("right_leg", rightLowerLimb);
        compartments.put("back", back);
        return compartments;
    }
}