package com.site21.bittermelon.content.medical.factory;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.blood.BloodType;
import com.site21.bittermelon.content.medical.compartments.deprecated.CompartmentOld;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.FunctionType;
import com.site21.bittermelon.content.medical.compartments.deprecated.bodyparts.BodyPart;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStatsOld;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.site21.bittermelon.init.neoforge.BitterItems.*;

public class HumanFactoryOld implements AnatomyFactoryOld {

    @Contract("_, _ -> new")
    public @NotNull MedicalStatsOld build(BloodType bloodType, Character character) {
        List<CompartmentOld> compartments = new ArrayList<>();

        BodyPart wholeBody = createMajorBodyPart("Whole Body", null);
        compartments.add(wholeBody);

        buildHead(wholeBody, compartments);
        buildChest(wholeBody, compartments);
        buildAbdomen(wholeBody, compartments);
        buildArms(wholeBody, compartments);
        buildLegs(wholeBody, compartments);
        buildBack(wholeBody, compartments);

        return new MedicalStatsOld(bloodType, compartments, character);
    }

    private static void buildHead(BodyPart wholeBody, @NotNull List<CompartmentOld> compartments) {
        BodyPart head = createMajorBodyPart("Head", wholeBody);
        compartments.add(head);

        // Head layers
        BodyPart headSkin = createRevealedBodyPart("Skin", head, 10, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart headFat = createBodyPart("Fat", headSkin, 15, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart skull = createBodyPart("Skull", headFat, 150, EnumSet.of(CompartmentTag.HARD_TISSUE));

        // Brain and components
        BodyPart brain = createMajorBodyPart("Brain", skull);
        BodyPart meninges = createRevealedBodyPart("Meninges", brain, 2, EnumSet.of(CompartmentTag.SOFT_TISSUE));

        // Brain regions
        BodyPart frontalLobe = createBodyPart("Frontal Lobe", meninges, 5, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        frontalLobe.setAttribute(FunctionType.BRAIN_MOTOR_ABILITY, 1f);
        BodyPart parietalLobe = createBodyPart("Parietal Lobe", meninges, 5, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart temporalLobe = createBodyPart("Temporal Lobe", meninges, 4, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart occipitalLobe = createBodyPart("Occipital Lobe", meninges, 3, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart cerebellum = createBodyPart("Cerebellum", meninges, 4, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart brainstem = createBodyPart("Brainstem", meninges, 2, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        brainstem.setAttribute(FunctionType.BRAIN_VITALS, 1f);

        // Face components
        BodyPart leftEye = createRevealedBodyPart("Left Eye", head, 2, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart rightEye = createRevealedBodyPart("Right Eye", head, 2, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart leftEar = createRevealedBodyPart("Left Ear", head, 1, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart rightEar = createRevealedBodyPart("Right Ear", head, 1, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart nose = createRevealedBodyPart("Nose", head, 2, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart mouth = createRevealedBodyPart("Mouth", head, 2, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        mouth.setAttribute(FunctionType.BITE, 1f);
        BodyPart tongue = createRevealedBodyPart("Tongue", mouth, 2, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart teeth = createRevealedBodyPart("Teeth", mouth, 2, EnumSet.of(CompartmentTag.HARD_TISSUE));
        teeth.setAttribute(FunctionType.BITE, 1f);

        compartments.addAll(Arrays.asList(headSkin, headFat, skull, brain, meninges,
                frontalLobe, parietalLobe, temporalLobe, occipitalLobe, cerebellum, brainstem, leftEye, rightEye, leftEar, rightEar, nose, mouth, tongue, teeth));
    }

    private static void buildChest(BodyPart wholeBody, @NotNull List<CompartmentOld> compartments) {
        BodyPart chest = createMajorBodyPart("Chest", wholeBody);
        compartments.add(chest);

        // Chest layers
        BodyPart chestSkin = createRevealedBodyPart("Skin", chest, 30, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart chestFat = createBodyPart("Fat", chestSkin, 40, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart chestMuscles = createBodyPart("Muscles", chestFat, 100, EnumSet.of(CompartmentTag.SOFT_TISSUE));

        // Skeletal components
        BodyPart ribs = createBodyPart("Ribs", chestMuscles, 60, EnumSet.of(CompartmentTag.HARD_TISSUE));
        BodyPart sternum = createBodyPart("Sternum", chestMuscles, 20, EnumSet.of(CompartmentTag.HARD_TISSUE));

        // Membrane layer
        BodyPart pleuralMembrane = createBodyPart("Pleural Membrane", ribs, 5, EnumSet.of(CompartmentTag.SOFT_TISSUE));

        // Cardiopulmonary system
        BodyPart heart = createBodyPart("Heart", pleuralMembrane, 30, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        heart.setAttribute(FunctionType.CIRCULATION, 1f);
        BodyPart pericardium = createBodyPart("Pericardium", heart, 2, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart leftLung = createBodyPart("Left Lung", pleuralMembrane, 40, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        leftLung.setAttribute(FunctionType.RESPIRATORY, 1f);
        BodyPart rightLung = createBodyPart("Right Lung", pleuralMembrane, 40, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        rightLung.setAttribute(FunctionType.RESPIRATORY, 1f);

        // Other organs
        BodyPart thymus = createBodyPart("Thymus", pleuralMembrane, 5, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart esophagus = createBodyPart("Esophagus", pleuralMembrane, 10, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart trachea = createBodyPart("Trachea", pleuralMembrane, 10, EnumSet.of(CompartmentTag.SOFT_TISSUE));

        compartments.addAll(Arrays.asList(chestSkin, chestFat, chestMuscles, ribs, sternum,
                pleuralMembrane, heart, pericardium, leftLung, rightLung, thymus, esophagus, trachea));
    }

    private static void buildAbdomen(BodyPart wholeBody, @NotNull List<CompartmentOld> compartments) {
        BodyPart abdomen = createMajorBodyPart("Abdomen", wholeBody);
        compartments.add(abdomen);

        // Abdominal layers
        BodyPart abdomenSkin = createRevealedBodyPart("Skin", abdomen, 40, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart abdomenFat = createBodyPart("Fat", abdomenSkin, 60, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart abdominalMuscles = createBodyPart("Abdominal Muscles", abdomenFat, 100, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart peritoneum = createBodyPart("Peritoneum", abdominalMuscles, 5, EnumSet.of(CompartmentTag.SOFT_TISSUE));

        // Digestive organs
        BodyPart stomach = createBodyPart("Stomach", peritoneum, 30, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        stomach.defaultItem(STOMACH.get());
        BodyPart smallIntestine = createBodyPart("Small Intestine", peritoneum, 40, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart largeIntestine = createBodyPart("Large Intestine", peritoneum, 40, EnumSet.of(CompartmentTag.SOFT_TISSUE));

        // Accessory organs
        BodyPart liver = createBodyPart("Liver", peritoneum, 45, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        liver.defaultItem(LIVER.get());
        BodyPart gallbladder = createBodyPart("Gallbladder", liver, 5, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        gallbladder.defaultItem(GALLBLADDER.get());
        gallbladder.reveal();
        BodyPart pancreas = createBodyPart("Pancreas", peritoneum, 10, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart spleen = createBodyPart("Spleen", peritoneum, 15, EnumSet.of(CompartmentTag.SOFT_TISSUE));

        // Urinary system
        BodyPart leftKidney = createBodyPart("Left Kidney", peritoneum, 15, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        leftKidney.defaultItem(KIDNEY.get());
        BodyPart rightKidney = createBodyPart("Right Kidney", peritoneum, 15, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        rightKidney.defaultItem(KIDNEY.get());
        BodyPart bladder = createBodyPart("Bladder", peritoneum, 20, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        bladder.defaultItem(BLADDER.get());

        // Skeletal component
        BodyPart pelvis = createBodyPart("Pelvis", peritoneum, 80, EnumSet.of(CompartmentTag.HARD_TISSUE));
        pelvis.defaultItem(PELVIS.get());

        compartments.addAll(Arrays.asList(abdomenSkin, abdomenFat, abdominalMuscles, peritoneum,
                stomach, smallIntestine, largeIntestine, liver, gallbladder, pancreas, spleen,
                leftKidney, rightKidney, bladder, pelvis));
    }

    private static void buildArms(BodyPart wholeBody, List<CompartmentOld> compartments) {
        // Left Arm
        BodyPart leftArm = createMajorBodyPart("Left Arm", wholeBody);
        buildLimb(leftArm, "Left", true, compartments);

        // Right Arm
        BodyPart rightArm = createMajorBodyPart("Right Arm", wholeBody);
        buildLimb(rightArm, "Right", true, compartments);

        compartments.addAll(Arrays.asList(leftArm, rightArm));
    }

    private static void buildLegs(BodyPart wholeBody, List<CompartmentOld> compartments) {
        // Left Leg
        BodyPart leftLeg = createMajorBodyPart("Left Leg", wholeBody);
        buildLimb(leftLeg, "Left", false, compartments);

        // Right Leg
        BodyPart rightLeg = createMajorBodyPart("Right Leg", wholeBody);
        buildLimb(rightLeg, "Right", false, compartments);

        compartments.addAll(Arrays.asList(leftLeg, rightLeg));
    }

    private static void buildLimb(BodyPart limb, String side, boolean isArm, List<CompartmentOld> compartments) {
        String limbType = isArm ? "Arm" : "Leg";
        int baseHealth = isArm ? 15 : 25;

        BodyPart skin = createRevealedBodyPart("Skin", limb, baseHealth, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart fat = createBodyPart(String.format("Fat", side, limbType), skin, baseHealth + 5, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart muscles = createBodyPart(String.format("Muscles", side, limbType), fat, isArm ? 50 : 150, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart tendons = createBodyPart(String.format("Tendons", side, limbType), muscles, isArm ? 10 : 20, EnumSet.of(CompartmentTag.SOFT_TISSUE));

        if (isArm) {
            skin.setAttribute(FunctionType.MANIPULATION, 1f);
            fat.setAttribute(FunctionType.MANIPULATION, 1f);
            muscles.setAttribute(FunctionType.MANIPULATION, 1f);
            tendons.setAttribute(FunctionType.MANIPULATION, 1f);
            BodyPart humerus = createBodyPart(side + " Humerus", muscles, 30, EnumSet.of(CompartmentTag.HARD_TISSUE));
            humerus.setAttribute(FunctionType.MANIPULATION, 1f);
            BodyPart radiusUlna = createBodyPart(side + " Radius Ulna", muscles, 20, EnumSet.of(CompartmentTag.HARD_TISSUE));
            radiusUlna.setAttribute(FunctionType.MANIPULATION, 1f);
            BodyPart hand = createRevealedBodyPart(side + " Hand", limb, 20, EnumSet.of(CompartmentTag.SOFT_TISSUE));
            hand.setAttribute(FunctionType.MANIPULATION, 1f);
            BodyPart wrist = createRevealedBodyPart(side + " Wrist", limb, 10, EnumSet.of(CompartmentTag.SOFT_TISSUE));
            wrist.setAttribute(FunctionType.MANIPULATION, 1f);
            compartments.addAll(Arrays.asList(humerus, radiusUlna, hand, wrist));
        } else {
            skin.setAttribute(FunctionType.MOVEMENT, 1f);
            fat.setAttribute(FunctionType.MOVEMENT, 1f);
            muscles.setAttribute(FunctionType.MOVEMENT, 1f);
            tendons.setAttribute(FunctionType.MOVEMENT, 1f);
            BodyPart femur = createBodyPart(side + " Femur", muscles, 50, EnumSet.of(CompartmentTag.HARD_TISSUE));
            femur.setAttribute(FunctionType.MOVEMENT, 1f);
            BodyPart tibiaFibula = createBodyPart(side + " Tibia Fibula", muscles, 40, EnumSet.of(CompartmentTag.HARD_TISSUE));
            tibiaFibula.setAttribute(FunctionType.MOVEMENT, 1f);
            BodyPart foot = createRevealedBodyPart(side + " Foot", limb, 30, EnumSet.of(CompartmentTag.SOFT_TISSUE));
            foot.setAttribute(FunctionType.MOVEMENT, 1f);
            BodyPart ankle = createRevealedBodyPart(side + " Ankle", limb, 10, EnumSet.of(CompartmentTag.SOFT_TISSUE));
            ankle.setAttribute(FunctionType.MOVEMENT, 1f);
            compartments.addAll(Arrays.asList(femur, tibiaFibula, foot, ankle));
        }

        compartments.addAll(Arrays.asList(skin, fat, muscles, tendons));
    }

    private static void buildBack(BodyPart wholeBody, List<CompartmentOld> compartments) {
        BodyPart back = createMajorBodyPart("Back", wholeBody);
        compartments.add(back);

        // Back layers
        BodyPart backSkin = createRevealedBodyPart("Skin", back, 25, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart backFat = createBodyPart("Fat", backSkin, 35, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart backMuscles = createBodyPart("Muscles", backFat, 150, EnumSet.of(CompartmentTag.SOFT_TISSUE));

        // Skeletal components
        BodyPart spine = createBodyPart("Spine", backMuscles, 100, EnumSet.of(CompartmentTag.HARD_TISSUE));
        BodyPart scapula = createBodyPart("Scapula", backMuscles, 30, EnumSet.of(CompartmentTag.HARD_TISSUE));

        compartments.addAll(Arrays.asList(backSkin, backFat, backMuscles, spine, scapula));
    }

    @Contract("_, _ -> new")
    private static @NotNull BodyPart createMajorBodyPart(String name, CompartmentOld owner) {
        return new BodyPart(EnumSet.of(CompartmentTag.MAJOR_BODY_PART), name, owner, -1, false);
    }

    private static @NotNull BodyPart createBodyPart(String name, CompartmentOld owner, float maxHealth, EnumSet<CompartmentTag> types) {
        BodyPart part = new BodyPart(types, name, owner, maxHealth);
        if (types.contains(CompartmentTag.SOFT_TISSUE)) {
            part.setDoesBleed(true);
        }
        return part;
    }

    private static @NotNull BodyPart createRevealedBodyPart(String name, CompartmentOld owner, float maxHealth, EnumSet<CompartmentTag> types) {
        BodyPart part = new BodyPart(types, name, owner, maxHealth, false);
        if (types.contains(CompartmentTag.SOFT_TISSUE)) {
            part.setDoesBleed(true);
        }
        return part;
    }

    private static @NotNull BodyPart createVessel(String name, CompartmentOld owner, float maxHealth, CompartmentTag type) {
        return createBodyPart(name, owner, maxHealth, EnumSet.of(CompartmentTag.SOFT_TISSUE, type));
    }
}
