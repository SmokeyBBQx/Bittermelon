package com.site21.bittermelon.content.medical.factory;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.blood.BloodType;
import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.compartments.FunctionType;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.site21.bittermelon.init.custom.Compartments.*;
import static com.site21.bittermelon.init.neoforge.BitterItems.*;

public class HumanFactory implements AnatomyFactory {

    @Override
    public MedicalStats build(BloodType bloodType, @NotNull Character character) {
        List<CompartmentInstance> compartments = new ArrayList<>();

        CompartmentInstance wholeBody = createMajorBodyPart("Whole Body", null);
        compartments.add(wholeBody);

        buildHead(wholeBody, compartments);
        buildChest(wholeBody, compartments);
        buildAbdomen(wholeBody, compartments);
        buildArms(wholeBody, compartments);
        buildLegs(wholeBody, compartments);
        buildBack(wholeBody, compartments);

        return new MedicalStats(compartments, bloodType, character.getUUID());
    }

    private static void buildHead(CompartmentInstance wholeBody, @NotNull List<CompartmentInstance> compartments) {
        CompartmentInstance head = createMajorBodyPart("Head", wholeBody);
        compartments.add(head);

        // Head layers
        CompartmentInstance headSkin = createRevealedSoftTissue("Skin", head, 10);
        CompartmentInstance headFat = createSoftTissue("Fat", headSkin, 15);
        CompartmentInstance skull = createHardTissue("Skull", headFat, 150);

        // Brain and components
        CompartmentInstance brain = createMajorBodyPart("Brain", skull);
        CompartmentInstance meninges = createRevealedSoftTissue("Meninges", brain, 2);

        // Brain regions
        CompartmentInstance frontalLobe = createSoftTissue("Frontal Lobe", meninges, 5);
        frontalLobe.setAttribute(FunctionType.BRAIN_MOTOR_ABILITY, 1f);
        CompartmentInstance parietalLobe = createSoftTissue("Parietal Lobe", meninges, 5);
        CompartmentInstance temporalLobe = createSoftTissue("Temporal Lobe", meninges, 4);
        CompartmentInstance occipitalLobe = createSoftTissue("Occipital Lobe", meninges, 3);
        CompartmentInstance cerebellum = createSoftTissue("Cerebellum", meninges, 4);
        CompartmentInstance brainstem = createSoftTissue("Brainstem", meninges, 2);
        brainstem.setAttribute(FunctionType.BRAIN_VITALS, 1f);

        // Face components
        CompartmentInstance leftEye = createRevealedSoftTissue("Left Eye", head, 2);
        CompartmentInstance rightEye = createRevealedSoftTissue("Right Eye", head, 2);
        CompartmentInstance leftEar = createRevealedSoftTissue("Left Ear", head, 1);
        CompartmentInstance rightEar = createRevealedSoftTissue("Right Ear", head, 1);
        CompartmentInstance nose = createRevealedSoftTissue("Nose", head, 2);
        CompartmentInstance mouth = createRevealedSoftTissue("Mouth", head, 2);
        mouth.setAttribute(FunctionType.BITE, 1f);
        CompartmentInstance tongue = createRevealedSoftTissue("Tongue", mouth, 2);
        CompartmentInstance teeth = createRevealedHardTissue("Teeth", mouth, 2);
        teeth.setAttribute(FunctionType.BITE, 1f);

        compartments.addAll(Arrays.asList(headSkin, headFat, skull, brain, meninges,
                frontalLobe, parietalLobe, temporalLobe, occipitalLobe, cerebellum, brainstem,
                leftEye, rightEye, leftEar, rightEar, nose, mouth, tongue, teeth));
    }

    private static void buildChest(CompartmentInstance wholeBody, @NotNull List<CompartmentInstance> compartments) {
        CompartmentInstance chest = createMajorBodyPart("Chest", wholeBody);
        compartments.add(chest);

        // Chest layers
        CompartmentInstance chestSkin = createRevealedSoftTissue("Skin", chest, 30);
        CompartmentInstance chestFat = createSoftTissue("Fat", chestSkin, 40);
        CompartmentInstance chestMuscles = createSoftTissue("Muscles", chestFat, 100);

        // Skeletal components
        CompartmentInstance ribs = createHardTissue("Ribs", chestMuscles, 60);
        CompartmentInstance sternum = createHardTissue("Sternum", chestMuscles, 20);

        // Membrane layer
        CompartmentInstance pleuralMembrane = createSoftTissue("Pleural Membrane", ribs, 5);

        // Cardiopulmonary system
        CompartmentInstance heart = createSoftTissue("Heart", pleuralMembrane, 30);
        heart.setAttribute(FunctionType.CIRCULATION, 1f);
        CompartmentInstance pericardium = createSoftTissue("Pericardium", heart, 2);
        CompartmentInstance leftLung = createSoftTissue("Left Lung", pleuralMembrane, 40);
        leftLung.setAttribute(FunctionType.RESPIRATORY, 1f);
        CompartmentInstance rightLung = createSoftTissue("Right Lung", pleuralMembrane, 40);
        rightLung.setAttribute(FunctionType.RESPIRATORY, 1f);

        // Other organs
        CompartmentInstance thymus = createSoftTissue("Thymus", pleuralMembrane, 5);
        CompartmentInstance esophagus = createSoftTissue("Esophagus", pleuralMembrane, 10);
        CompartmentInstance trachea = createSoftTissue("Trachea", pleuralMembrane, 10);

        compartments.addAll(Arrays.asList(chestSkin, chestFat, chestMuscles, ribs, sternum,
                pleuralMembrane, heart, pericardium, leftLung, rightLung, thymus, esophagus, trachea));
    }

    private static void buildAbdomen(CompartmentInstance wholeBody, @NotNull List<CompartmentInstance> compartments) {
        CompartmentInstance abdomen = createMajorBodyPart("Abdomen", wholeBody);
        compartments.add(abdomen);

        // Abdominal layers
        CompartmentInstance abdomenSkin = createRevealedSoftTissue("Skin", abdomen, 40);
        CompartmentInstance abdomenFat = createSoftTissue("Fat", abdomenSkin, 60);
        CompartmentInstance abdominalMuscles = createSoftTissue("Abdominal Muscles", abdomenFat, 100);
        CompartmentInstance peritoneum = createSoftTissue("Peritoneum", abdominalMuscles, 5);

        // Digestive organs
        CompartmentInstance stomach = createSoftTissue("Stomach", peritoneum, 30);
        stomach.setItem(STOMACH.get());
        CompartmentInstance smallIntestine = createSoftTissue("Small Intestine", peritoneum, 40);
        CompartmentInstance largeIntestine = createSoftTissue("Large Intestine", peritoneum, 40);

        // Accessory organs
        CompartmentInstance liver = createSoftTissue("Liver", peritoneum, 45);
        liver.setItem(LIVER.get());
        CompartmentInstance gallbladder = createSoftTissue("Gallbladder", liver, 5);
        gallbladder.setItem(GALLBLADDER.get());
        gallbladder.setHidden(false); // Revealed
        CompartmentInstance pancreas = createSoftTissue("Pancreas", peritoneum, 10);
        CompartmentInstance spleen = createSoftTissue("Spleen", peritoneum, 15);

        // Urinary system
        CompartmentInstance leftKidney = createSoftTissue("Left Kidney", peritoneum, 15);
        leftKidney.setItem(KIDNEY.get());
        CompartmentInstance rightKidney = createSoftTissue("Right Kidney", peritoneum, 15);
        rightKidney.setItem(KIDNEY.get());
        CompartmentInstance bladder = createSoftTissue("Bladder", peritoneum, 20);
        bladder.setItem(BLADDER.get());

        // Skeletal component
        CompartmentInstance pelvis = createHardTissue("Pelvis", peritoneum, 80);
        pelvis.setItem(PELVIS.get());

        compartments.addAll(Arrays.asList(abdomenSkin, abdomenFat, abdominalMuscles, peritoneum,
                stomach, smallIntestine, largeIntestine, liver, gallbladder, pancreas, spleen,
                leftKidney, rightKidney, bladder, pelvis));
    }

    private static void buildArms(CompartmentInstance wholeBody, List<CompartmentInstance> compartments) {
        // Left Arm
        CompartmentInstance leftArm = createMajorBodyPart("Left Arm", wholeBody);
        buildLimb(leftArm, "Left", true, compartments);

        // Right Arm
        CompartmentInstance rightArm = createMajorBodyPart("Right Arm", wholeBody);
        buildLimb(rightArm, "Right", true, compartments);

        compartments.addAll(Arrays.asList(leftArm, rightArm));
    }

    private static void buildLegs(CompartmentInstance wholeBody, List<CompartmentInstance> compartments) {
        // Left Leg
        CompartmentInstance leftLeg = createMajorBodyPart("Left Leg", wholeBody);
        buildLimb(leftLeg, "Left", false, compartments);

        // Right Leg
        CompartmentInstance rightLeg = createMajorBodyPart("Right Leg", wholeBody);
        buildLimb(rightLeg, "Right", false, compartments);

        compartments.addAll(Arrays.asList(leftLeg, rightLeg));
    }

    private static void buildLimb(CompartmentInstance limb, String side, boolean isArm, List<CompartmentInstance> compartments) {
        int baseHealth = isArm ? 15 : 25;

        CompartmentInstance skin = createRevealedSoftTissue("Skin", limb, baseHealth);
        CompartmentInstance fat = createSoftTissue("Fat", skin, baseHealth + 5);
        CompartmentInstance muscles = createSoftTissue("Muscles", fat, isArm ? 50 : 150);
        CompartmentInstance tendons = createSoftTissue("Tendons", muscles, isArm ? 10 : 20);

        if (isArm) {
            skin.setAttribute(FunctionType.MANIPULATION, 1f);
            fat.setAttribute(FunctionType.MANIPULATION, 1f);
            muscles.setAttribute(FunctionType.MANIPULATION, 1f);
            tendons.setAttribute(FunctionType.MANIPULATION, 1f);
            CompartmentInstance humerus = createHardTissue(side + " Humerus", muscles, 30);
            humerus.setAttribute(FunctionType.MANIPULATION, 1f);
            CompartmentInstance radiusUlna = createHardTissue(side + " Radius Ulna", muscles, 20);
            radiusUlna.setAttribute(FunctionType.MANIPULATION, 1f);
            CompartmentInstance hand = createRevealedSoftTissue(side + " Hand", limb, 20);
            hand.setAttribute(FunctionType.MANIPULATION, 1f);
            CompartmentInstance wrist = createRevealedSoftTissue(side + " Wrist", limb, 10);
            wrist.setAttribute(FunctionType.MANIPULATION, 1f);
            compartments.addAll(Arrays.asList(humerus, radiusUlna, hand, wrist));
        } else {
            skin.setAttribute(FunctionType.MOVEMENT, 1f);
            fat.setAttribute(FunctionType.MOVEMENT, 1f);
            muscles.setAttribute(FunctionType.MOVEMENT, 1f);
            tendons.setAttribute(FunctionType.MOVEMENT, 1f);
            CompartmentInstance femur = createHardTissue(side + " Femur", muscles, 50);
            femur.setAttribute(FunctionType.MOVEMENT, 1f);
            CompartmentInstance tibiaFibula = createHardTissue(side + " Tibia Fibula", muscles, 40);
            tibiaFibula.setAttribute(FunctionType.MOVEMENT, 1f);
            CompartmentInstance foot = createRevealedSoftTissue(side + " Foot", limb, 30);
            foot.setAttribute(FunctionType.MOVEMENT, 1f);
            CompartmentInstance ankle = createRevealedSoftTissue(side + " Ankle", limb, 10);
            ankle.setAttribute(FunctionType.MOVEMENT, 1f);
            compartments.addAll(Arrays.asList(femur, tibiaFibula, foot, ankle));
        }

        compartments.addAll(Arrays.asList(skin, fat, muscles, tendons));
    }

    private static void buildBack(CompartmentInstance wholeBody, @NotNull List<CompartmentInstance> compartments) {
        CompartmentInstance back = createMajorBodyPart("Back", wholeBody);
        compartments.add(back);

        // Back layers
        CompartmentInstance backSkin = createRevealedSoftTissue("Skin", back, 25);
        CompartmentInstance backFat = createSoftTissue("Fat", backSkin, 35);
        CompartmentInstance backMuscles = createSoftTissue("Muscles", backFat, 150);

        // Skeletal components
        CompartmentInstance spine = createHardTissue("Spine", backMuscles, 100);
        CompartmentInstance scapula = createHardTissue("Scapula", backMuscles, 30);

        compartments.addAll(Arrays.asList(backSkin, backFat, backMuscles, spine, scapula));
    }

    @Contract("_, _ -> new")
    private static @NotNull CompartmentInstance createMajorBodyPart(String name, CompartmentInstance parent) {
        CompartmentInstance bodyPart = new CompartmentInstance(MAJOR_BODY_PART.get(), -1, name, false);
        if (parent != null) {
            bodyPart.initializeWithParent(parent);
        }
        return bodyPart;
    }

    private static @NotNull CompartmentInstance createSoftTissue(String name, CompartmentInstance parent, float maxHealth) {
        CompartmentInstance tissue = new CompartmentInstance(SOFT_TISSUE.get(), maxHealth, name, true);
        tissue.initializeWithParent(parent);
        return tissue;
    }

    private static @NotNull CompartmentInstance createRevealedSoftTissue(String name, CompartmentInstance parent, float maxHealth) {
        CompartmentInstance tissue = new CompartmentInstance(SOFT_TISSUE.get(), maxHealth, name, false);
        tissue.initializeWithParent(parent);
        return tissue;
    }

    private static @NotNull CompartmentInstance createHardTissue(String name, CompartmentInstance parent, float maxHealth) {
        CompartmentInstance tissue = new CompartmentInstance(HARD_TISSUE.get(), maxHealth, name, true);
        tissue.initializeWithParent(parent);
        return tissue;
    }

    private static @NotNull CompartmentInstance createRevealedHardTissue(String name, CompartmentInstance parent, float maxHealth) {
        CompartmentInstance tissue = new CompartmentInstance(HARD_TISSUE.get(), maxHealth, name, false);
        tissue.initializeWithParent(parent);
        return tissue;
    }
}
