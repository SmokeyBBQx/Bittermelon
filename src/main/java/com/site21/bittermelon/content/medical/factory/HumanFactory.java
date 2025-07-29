package com.site21.bittermelon.content.medical.factory;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.blood.BloodType;
import com.site21.bittermelon.content.medical.compartments.*;
import com.site21.bittermelon.content.medical.medicalstats.AnimalMedicalStats;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
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

        CompartmentInstance wholeBody = createMajorBodyPart("Whole Body", new VisualData(0, 0), null, 0);
        compartments.add(wholeBody);

        LayerData bodyLayer = new LayerData(ResourceLocation.withDefaultNamespace("textures/block/white_terracotta.png"), "Body");
        wholeBody.addLayer(bodyLayer);

        buildHead(wholeBody, compartments);
        buildChest(wholeBody, compartments);
        buildAbdomen(wholeBody, compartments);
//        buildArms(wholeBody, compartments);
//        buildLegs(wholeBody, compartments);
//        buildBack(wholeBody, compartments);

        return new AnimalMedicalStats(compartments, wholeBody.getUUID(), character.getUUID());
    }

    private static void buildHead(CompartmentInstance wholeBody, @NotNull List<CompartmentInstance> compartments) {
        CompartmentInstance head = createMajorBodyPart("Head", new VisualData(0, -20), wholeBody, 0);
        compartments.add(head);

        LayerData faceLayer = new LayerData(ResourceLocation.withDefaultNamespace("textures/block/white_terracotta.png"), "Face");
        LayerData skinLayer = new LayerData(ResourceLocation.withDefaultNamespace("textures/block/white_terracotta.png"), "Skin");
        LayerData fatLayer = new LayerData(ResourceLocation.withDefaultNamespace("textures/block/horn_coral_block.png"), "Fat");
        LayerData skullLayer = new LayerData(ResourceLocation.withDefaultNamespace("textures/block/bone_block_side.png"), "Skull");
        LayerData cranialCavity = new LayerData(ResourceLocation.withDefaultNamespace("textures/block/bone_block_side.png"), "Cranial Cavity");
        LayerData meningesLayer = new LayerData(ResourceLocation.withDefaultNamespace("textures/block/dead_brain_coral_block.png"), "Meninges");
        LayerData brainLayer = new LayerData(ResourceLocation.withDefaultNamespace("textures/block/brain_coral_block.png"), "Brain");

        head.addLayer(faceLayer);
        head.addLayer(skinLayer);
        head.addLayer(fatLayer);
        head.addLayer(skullLayer);
        head.addLayer(cranialCavity);

        // Head layers
        CompartmentInstance headSkin = createSoftTissue("Skin", new VisualData(0, 0), head, 1, 10);
        CompartmentInstance headFat = createSoftTissue("Fat", new VisualData(0, 0), head, 2, 15);

        CompartmentInstance skull = createHardTissue("Skull", new VisualData(0, 0), head, 3, 150);
        // Brain and components
        CompartmentInstance brain = createMajorBodyPart("Brain", new VisualData(0, 0), head, 4);

        brain.addLayer(meningesLayer);
        brain.addLayer(brainLayer);

        CompartmentInstance meninges = createSoftTissue("Meninges", new VisualData(0, 0), brain, 0, 2);



        // Brain regions
        CompartmentInstance frontalLobe = createSoftTissue("Frontal Lobe", new VisualData(-5, -4), brain, 1, 5);
        frontalLobe.setAttribute(MedicalAttribute.BRAIN_MOTOR_ABILITY, 1f);
        CompartmentInstance parietalLobe = createSoftTissue("Parietal Lobe", new VisualData(5, -4), brain, 1, 5);
        parietalLobe.setAttribute(MedicalAttribute.NERVOUS, 1f);
        CompartmentInstance temporalLobe = createSoftTissue("Temporal Lobe", new VisualData(-5, 4), brain, 1, 4);
        CompartmentInstance occipitalLobe = createSoftTissue("Occipital Lobe", new VisualData(5, 4), brain, 1, 3);
        CompartmentInstance cerebellum = createSoftTissue("Cerebellum", new VisualData(0, 8), brain, 1, 4);
        CompartmentInstance brainstem = createSoftTissue("Brainstem", new VisualData(0, 12), brain, 1, 2);
        brainstem.setAttribute(MedicalAttribute.BRAIN_VITALS, 1f);

        // Face components
        CompartmentInstance leftEye = createSoftTissue("Left Eye", new VisualData(-3, -6), head, 0, 2);
        CompartmentInstance rightEye = createSoftTissue("Right Eye", new VisualData(3, -6), head, 0, 2);
        CompartmentInstance leftEar = createSoftTissue("Left Ear", new VisualData(-8, 0), head, 0, 1);
        CompartmentInstance rightEar = createSoftTissue("Right Ear", new VisualData(8, 0), head, 0, 1);
        CompartmentInstance nose = createSoftTissue("Nose", new VisualData(0, -2), head, 0, 2);
        CompartmentInstance mouth = createSoftTissue("Mouth", new VisualData(0, 4), head, 0, 2);
        mouth.setAttribute(MedicalAttribute.BITE, 1f);

        LayerData insideMouthLayer = new LayerData(ResourceLocation.withDefaultNamespace("textures/block/brain_coral_block.png"), "Inside Mouth");
        mouth.addLayer(insideMouthLayer);

        CompartmentInstance tongue = createSoftTissue("Tongue", new VisualData(0, 0), mouth, 0, 2);
        CompartmentInstance teeth = createHardTissue("Teeth", new VisualData(0, -2), mouth, 0, 2);

        compartments.addAll(Arrays.asList(headSkin, headFat, skull, brain, meninges,
                frontalLobe, parietalLobe, temporalLobe, occipitalLobe, cerebellum, brainstem,
                leftEye, rightEye, leftEar, rightEar, nose, mouth, tongue, teeth));
    }

    private static void buildChest(CompartmentInstance wholeBody, @NotNull List<CompartmentInstance> compartments) {
//        CompartmentInstance chest = createMajorBodyPart("Chest", new VisualData(30, -15), wholeBody, 0);
////        chest.getCompartmentSpace().setBackgroundTexture(ResourceLocation.withDefaultNamespace("textures/block/netherrack.png"));
//        chest.setItem(Items.BLUE_WOOL);
//        compartments.add(chest);
//
//        // Chest layers
//        CompartmentInstance chestSkin = createSoftTissue("Skin", new VisualData(0, 0), chest, 0, 30);
//        CompartmentInstance chestFat = createSoftTissue("Fat", new VisualData(0, 0), chest, 1, 40);
//        CompartmentInstance chestMuscles = createSoftTissue("Muscles", new VisualData(0, 0), chest, 2, 100);
//
//        // Skeletal components
//        CompartmentInstance ribs = createHardTissue("Ribs", new VisualData(0 * 5, 1 * 5, 3, 5, 27, 20), chest, 4, 60);
//        ribs.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/organs/anatomical_ribs.png"));
////        CompartmentInstance sternum = createHardTissue("Sternum", new VisualData(0, -4), chest, 3, 20);
//
//        // Membrane layer
//        CompartmentInstance pleuralMembrane = createSoftTissue("Pleural Membrane", new VisualData(0, 0), chest, 3, 5);
//
//        // Cardiopulmonary system
//        CompartmentInstance heart = createSoftTissue("Heart", new VisualData(10 * 5, 9 * 5, 0, 5, 10, 9), chest, 4, 30);
//        heart.setAttribute(MedicalAttribute.CIRCULATION, 1f);
//        heart.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/organs/anatomical_heart.png"));
//        CompartmentInstance pericardium = createSoftTissue("Pericardium", new VisualData(0, 0), heart, 0, 2);
//        CompartmentInstance leftLung = createSoftTissue("Left Lung", new VisualData(14 * 5, 2 * 5, 1, 5, 13, 18), chest, 4, 40);
//        leftLung.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/organs/anatomical_left_lung.png"));
//        leftLung.setAttribute(MedicalAttribute.RESPIRATION, 1f);
//        CompartmentInstance rightLung = createSoftTissue("Right Lung", new VisualData(0 * 5, 2 * 5, 1, 5, 13, 18), chest, 4, 40);
//        rightLung.setAttribute(MedicalAttribute.RESPIRATION, 1f);
//        rightLung.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/organs/anatomical_right_lung.png"));
//
//        // Other organs
//        CompartmentInstance thymus = createSoftTissue("Thymus", new VisualData(0, -8), chest, 4, 5);
//        CompartmentInstance esophagus = createSoftTissue("Esophagus", new VisualData(0, -10), chest, 4, 10);
//        CompartmentInstance trachea = createSoftTissue("Trachea", new VisualData(12 * 5, 0 * 5, 2, 5, 3, 9), chest, 4, 10);
//        trachea.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/organs/anatomical_trachea.png"));
//
//        compartments.addAll(Arrays.asList(chestSkin, chestFat, chestMuscles, ribs,
//                pleuralMembrane, heart, pericardium, leftLung, rightLung, thymus, esophagus, trachea));
    }

    private static void buildAbdomen(CompartmentInstance wholeBody, @NotNull List<CompartmentInstance> compartments) {
        CompartmentInstance abdomen = createMajorBodyPart("Abdomen", new VisualData(0, 30), wholeBody, 0);
        abdomen.setItem(Items.RED_WOOL);
        compartments.add(abdomen);

        LayerData skinLayer = new LayerData(ResourceLocation.withDefaultNamespace("textures/block/white_terracotta.png"), "Skin");
        LayerData fatLayer = new LayerData(ResourceLocation.withDefaultNamespace("textures/block/horn_coral_block.png"), "Fat");
        LayerData muscleLayer = new LayerData(ResourceLocation.withDefaultNamespace("textures/block/netherrack.png"), "Muscle");
        LayerData peritoneumLayer = new LayerData(ResourceLocation.withDefaultNamespace("textures/block/dead_brain_coral_block.png"), "Peritoneum");
        LayerData abdominalCavity = new LayerData(ResourceLocation.withDefaultNamespace("textures/block/netherrack.png"), "Abdominal Cavity");
        LayerData retroperitonealSpace = new LayerData(ResourceLocation.withDefaultNamespace("textures/block/netherrack.png"), "Retroperitoneal Space");

        abdomen.addLayer(skinLayer);
        abdomen.addLayer(fatLayer);
        abdomen.addLayer(muscleLayer);
        abdomen.addLayer(peritoneumLayer);
        abdomen.addLayer(abdominalCavity);
        abdomen.addLayer(retroperitonealSpace);

        // Abdominal layers
        CompartmentInstance abdomenSkin = createSoftTissue("Skin", new VisualData(0, 0), abdomen, 0, 40);
        CompartmentInstance abdomenFat = createSoftTissue("Fat", new VisualData(0, 0), abdomen, 1, 60);
        CompartmentInstance abdominalMuscles = createSoftTissue("Abdominal Muscles", new VisualData(0, 0), abdomen, 2, 100);
        CompartmentInstance peritoneum = createSoftTissue("Peritoneum", new VisualData(0, 0), abdomen, 3, 5);

        // Digestive organs
        CompartmentInstance stomach = createSoftTissue("Stomach", new VisualData(4 * 5, 0, 2, 5, 15, 14), abdomen, 4, 30);
        stomach.setItem(STOMACH.get());
        stomach.setAttribute(MedicalAttribute.DIGESTION, 1);
        stomach.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/organs/anatomical_stomach.png"));
        CompartmentInstance smallIntestine = createSoftTissue("Small Intestine", new VisualData(1 * 5, 8 * 5, 0, 5, 17, 19), abdomen, 4, 40);
      // TODO: TEMPORARY ATTRIBUTE
       smallIntestine.setAttribute(MedicalAttribute.CIRCULATION, 1);
       smallIntestine.setAttribute(MedicalAttribute.MOVEMENT, 1);
       smallIntestine.setAttribute(MedicalAttribute.MANIPULATION, 1);
        smallIntestine.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/organs/anatomical_small_intestine.png"));
        CompartmentInstance colon = createSoftTissue("Colon", new VisualData(-1 * 5, 9 * 5, 1, 5, 22, 19), abdomen, 4, 40);
        colon.setItem(COLON.get());
        colon.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/organs/anatomical_colon.png"));

        // Accessory organs
        CompartmentInstance liver = createSoftTissue("Liver", new VisualData(0, 0, 4, 5, 19, 14), abdomen, 4, 45);
        liver.setItem(LIVER.get());
        liver.setAttribute(MedicalAttribute.ELIMINATION, 1);
        liver.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/organs/anatomical_liver.png"));

        CompartmentInstance gallbladder = createSoftTissue("Gallbladder", new VisualData(5 * 5, 10 * 5, 3, 5, 3, 3), abdomen, 4, 5);
        gallbladder.setItem(GALLBLADDER.get());
        gallbladder.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/organs/anatomical_gallbladder.png"));

        CompartmentInstance pancreas = createSoftTissue("Pancreas", new VisualData(5, -2, 1.8f), abdomen, 4, 10);
        CompartmentInstance spleen = createSoftTissue("Spleen", new VisualData(6, -5, 1.8f), abdomen, 4, 15);

        // Urinary system
        CompartmentInstance leftKidney = createSoftTissue("Left Kidney", new VisualData(-7, 0, 2.8f), abdomen, 5, 15);
        leftKidney.setItem(KIDNEY.get());
        CompartmentInstance rightKidney = createSoftTissue("Right Kidney", new VisualData(7, 0, 2.8f), abdomen, 5, 15);
        rightKidney.setItem(KIDNEY.get());
        CompartmentInstance bladder = createSoftTissue("Bladder", new VisualData(0, 150, 1.5f), abdomen, 4, 20);
        bladder.setItem(BLADDER.get());

        // Skeletal component
        CompartmentInstance pelvis = createHardTissue("Pelvis", new VisualData(0, 12, 6.2f), abdomen, 3, 80);
        pelvis.setItem(PELVIS.get());

        compartments.addAll(Arrays.asList(abdomenSkin, abdomenFat, abdominalMuscles, peritoneum,
                stomach, smallIntestine, colon, liver, gallbladder, pancreas, spleen,
                leftKidney, rightKidney, bladder, pelvis));
    }

    private static void buildArms(CompartmentInstance wholeBody, List<CompartmentInstance> compartments) {
        // Left Arm
        CompartmentInstance leftArm = createMajorBodyPart("Left Arm", new VisualData(-15, -10), wholeBody, 0);
        buildLimb(leftArm, "Left", true, compartments);

        // Right Arm
        CompartmentInstance rightArm = createMajorBodyPart("Right Arm", new VisualData(15, -10), wholeBody, 0);
        buildLimb(rightArm, "Right", true, compartments);
        rightArm.setHealth(10);
        compartments.addAll(Arrays.asList(leftArm, rightArm));
    }

    private static void buildLegs(CompartmentInstance wholeBody, List<CompartmentInstance> compartments) {
        // Left Leg
        CompartmentInstance leftLeg = createMajorBodyPart("Left Leg", new VisualData(-6, 20), wholeBody, 0);
        buildLimb(leftLeg, "Left", false, compartments);

        // Right Leg
        CompartmentInstance rightLeg = createMajorBodyPart("Right Leg", new VisualData(6, 20), wholeBody, 0);
        buildLimb(rightLeg, "Right", false, compartments);

        compartments.addAll(Arrays.asList(leftLeg, rightLeg));
    }

    private static void buildLimb(CompartmentInstance limb, @NotNull String side, boolean isArm, List<CompartmentInstance> compartments) {
        int baseHealth = isArm ? 15 : 25;
        int xOffset = side.equals("Left") ? -2 : 2;

        CompartmentInstance skin = createSoftTissue("Skin", new VisualData(0, 0), limb, 0, baseHealth);
        CompartmentInstance fat = createSoftTissue("Fat", new VisualData(0, 0), limb, 1, baseHealth + 5);
        CompartmentInstance muscles = createSoftTissue("Muscles", new VisualData(0, 0), limb, 2, isArm ? 50 : 150);
        CompartmentInstance tendons = createSoftTissue("Tendons", new VisualData(xOffset, 0), limb, 3, isArm ? 10 : 20);

        if (isArm) {
            skin.setAttribute(MedicalAttribute.MANIPULATION, 1f);
            fat.setAttribute(MedicalAttribute.MANIPULATION, 1f);
            muscles.setAttribute(MedicalAttribute.MANIPULATION, 1f);
            tendons.setAttribute(MedicalAttribute.MANIPULATION, 1f);
            CompartmentInstance humerus = createHardTissue(side + " Humerus", new VisualData(0, -5), limb, 3, 30);
            humerus.setAttribute(MedicalAttribute.MANIPULATION, 1f);
            CompartmentInstance radiusUlna = createHardTissue(side + " Radius Ulna", new VisualData(0, 5), limb, 3, 20);
            radiusUlna.setAttribute(MedicalAttribute.MANIPULATION, 1f);
            CompartmentInstance hand = createSoftTissue(side + " Hand", new VisualData(0, 12), limb, 0, 20);
            hand.setAttribute(MedicalAttribute.MANIPULATION, 1f);
            CompartmentInstance wrist = createSoftTissue(side + " Wrist", new VisualData(0, 8), limb, 0, 10);
            wrist.setAttribute(MedicalAttribute.MANIPULATION, 1f);
            compartments.addAll(Arrays.asList(humerus, radiusUlna, hand, wrist));
        } else {
            skin.setAttribute(MedicalAttribute.MOVEMENT, 1f);
            fat.setAttribute(MedicalAttribute.MOVEMENT, 1f);
            muscles.setAttribute(MedicalAttribute.MOVEMENT, 1f);
            tendons.setAttribute(MedicalAttribute.MOVEMENT, 1f);
            CompartmentInstance femur = createHardTissue(side + " Femur", new VisualData(0, -5), limb, 3, 50);
            femur.setAttribute(MedicalAttribute.MOVEMENT, 1f);
            CompartmentInstance tibiaFibula = createHardTissue(side + " Tibia Fibula", new VisualData(0, 10), limb, 3, 40);
            tibiaFibula.setAttribute(MedicalAttribute.MOVEMENT, 1f);
            CompartmentInstance foot = createSoftTissue(side + " Foot", new VisualData(0, 20), limb, 0, 30);
            foot.setAttribute(MedicalAttribute.MOVEMENT, 1f);
            CompartmentInstance ankle = createSoftTissue(side + " Ankle", new VisualData(0, 15), limb, 0, 10);
            ankle.setAttribute(MedicalAttribute.MOVEMENT, 1f);
            compartments.addAll(Arrays.asList(femur, tibiaFibula, foot, ankle));
        }

        compartments.addAll(Arrays.asList(skin, fat, muscles, tendons));
    }

    private static void buildBack(CompartmentInstance wholeBody, @NotNull List<CompartmentInstance> compartments) {
        CompartmentInstance back = createMajorBodyPart("Back", new VisualData(0, -5, 1.5f), wholeBody, 0);
        compartments.add(back);

        // Back layers
        CompartmentInstance backSkin = createSoftTissue("Skin", new VisualData(0, 0), back, 0, 25);
        CompartmentInstance backFat = createSoftTissue("Fat", new VisualData(0, 0), back, 1, 35);
        CompartmentInstance backMuscles = createSoftTissue("Muscles", new VisualData(0, 0), back, 2, 150);

        // Skeletal components
        CompartmentInstance spine = createHardTissue("Spine", new VisualData(0, 0, 0.8f), back, 3, 100);
        CompartmentInstance scapula = createHardTissue("Scapula", new VisualData(0, -10), back, 3, 30);

        compartments.addAll(Arrays.asList(backSkin, backFat, backMuscles, spine, scapula));
    }

    private static @NotNull CompartmentInstance createMajorBodyPart(String name, VisualData visualData, CompartmentInstance parent, int layer) {
        CompartmentInstance tissue = new CompartmentInstance(SOFT_TISSUE.get(), -1, name, visualData);

        if (parent != null) {
            parent.addCompartment(layer, tissue);
        }
        return tissue;
    }

    private static @NotNull CompartmentInstance createSoftTissue(String name, VisualData visualData, CompartmentInstance parent, int layer, float maxHealth) {
        CompartmentInstance tissue = new CompartmentInstance(SOFT_TISSUE.get(), maxHealth, name, visualData);

        tissue.setAttribute(MedicalAttribute.FUNCTION, 1);
        parent.addCompartment(layer, tissue);
        return tissue;
    }

    private static @NotNull CompartmentInstance createHardTissue(String name, VisualData visualData, CompartmentInstance parent, int layer, float maxHealth) {
        CompartmentInstance tissue = new CompartmentInstance(HARD_TISSUE.get(), maxHealth, name, visualData);

        tissue.setAttribute(MedicalAttribute.FUNCTION, 1);
        parent.addCompartment(layer, tissue);
        return tissue;
    }
}
