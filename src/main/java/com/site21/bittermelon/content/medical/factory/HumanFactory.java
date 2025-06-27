package com.site21.bittermelon.content.medical.factory;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.blood.BloodType;
import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.FunctionType;
import com.site21.bittermelon.content.medical.compartments.VisualData;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.init.custom.Compartments;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
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

        CompartmentInstance wholeBody = createMajorBodyPart("Whole Body", new VisualData(0, 0), null, 0);
        compartments.add(wholeBody);

        buildHead(wholeBody, compartments);
        buildChest(wholeBody, compartments);
        buildAbdomen(wholeBody, compartments);
        buildArms(wholeBody, compartments);
        buildLegs(wholeBody, compartments);
        buildBack(wholeBody, compartments);

        return new MedicalStats(compartments, wholeBody.getUUID(), bloodType, character.getUUID());
    }

    private static void buildHead(CompartmentInstance wholeBody, @NotNull List<CompartmentInstance> compartments) {
        CompartmentInstance head = createMajorBodyPart("Head", new VisualData(0, -20), wholeBody, 0);
        compartments.add(head);

        // Head layers
        CompartmentInstance headSkin = createSoftTissue("Skin", new VisualData(0, 0), head, 1, 10);
        headSkin.getCompartmentSpace().setBackgroundTexture(ResourceLocation.withDefaultNamespace("textures/block/white_terracotta.png"));
        CompartmentInstance headFat = createSoftTissue("Fat", new VisualData(0, 0), head, 2, 15);
        headFat.getCompartmentSpace().setBackgroundTexture(ResourceLocation.withDefaultNamespace("textures/block/horn_coral_block.png"));

        CompartmentInstance laceration = new CompartmentInstance(INJURY.get(), new VisualData(5, 6, 0.5f), headSkin, 0, 10, "Laceration", false);
        laceration.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/sprites/medical/lacerations.png"));

        CompartmentInstance cut1 = new CompartmentInstance(INJURY.get(), new VisualData(2, -5, 1.2f), headSkin, 0, 10, "Cut", false);
        cut1.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/sprites/medical/cut_visual.png"));

        headFat.getCompartmentSpace().addToLayer(1, cut1.getUUID());

        CompartmentInstance cut2 = new CompartmentInstance(INJURY.get(), new VisualData(0, 0, 2.3f), headFat, 0, 15, "Cut", false);
        cut2.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/sprites/medical/cut_visual.png"));


        CompartmentInstance skull = createHardTissue("Skull", new VisualData(0, 0), head, 3, 150);
        skull.getCompartmentSpace().setBackgroundTexture(ResourceLocation.withDefaultNamespace("textures/block/bone_block_side.png"));

        // Brain and components
        CompartmentInstance meninges = createSoftTissue("Meninges", new VisualData(0, 0), head, 4, 2);
        meninges.getCompartmentSpace().setBackgroundTexture(ResourceLocation.withDefaultNamespace("textures/block/dead_brain_coral_block.png"));
        CompartmentInstance brain = createMajorBodyPart("Brain", new VisualData(0, 0), head, 5);
        brain.getCompartmentSpace().setBackgroundTexture(ResourceLocation.withDefaultNamespace("textures/block/brain_coral_block.png"));

        // Brain regions
        CompartmentInstance frontalLobe = createSoftTissue("Frontal Lobe", new VisualData(-5, -4), brain, 0, 5);
        frontalLobe.setAttribute(FunctionType.BRAIN_MOTOR_ABILITY, 1f);
        frontalLobe.getCompartmentSpace().setBackgroundTexture(ResourceLocation.withDefaultNamespace("textures/block/brain_coral_block.png"));
        CompartmentInstance parietalLobe = createSoftTissue("Parietal Lobe", new VisualData(5, -4), brain, 0, 5);
        parietalLobe.setAttribute(FunctionType.NERVOUS, 1f);
        parietalLobe.getCompartmentSpace().setBackgroundTexture(ResourceLocation.withDefaultNamespace("textures/block/brain_coral_block.png"));
        CompartmentInstance temporalLobe = createSoftTissue("Temporal Lobe", new VisualData(-5, 4), brain, 0, 4);
        temporalLobe.getCompartmentSpace().setBackgroundTexture(ResourceLocation.withDefaultNamespace("textures/block/brain_coral_block.png"));
        CompartmentInstance occipitalLobe = createSoftTissue("Occipital Lobe", new VisualData(5, 4), brain, 0, 3);
        occipitalLobe.getCompartmentSpace().setBackgroundTexture(ResourceLocation.withDefaultNamespace("textures/block/brain_coral_block.png"));
        CompartmentInstance cerebellum = createSoftTissue("Cerebellum", new VisualData(0, 8), brain, 0, 4);
        cerebellum.getCompartmentSpace().setBackgroundTexture(ResourceLocation.withDefaultNamespace("textures/block/brain_coral_block.png"));
        CompartmentInstance brainstem = createSoftTissue("Brainstem", new VisualData(0, 12), brain, 0, 2);
        brainstem.getCompartmentSpace().setBackgroundTexture(ResourceLocation.withDefaultNamespace("textures/block/brain_coral_block.png"));
        brainstem.setAttribute(FunctionType.BRAIN_VITALS, 1f);

        // Face components
        CompartmentInstance leftEye = createSoftTissue("Left Eye", new VisualData(-3, -6), head, 0, 2);
        CompartmentInstance rightEye = createSoftTissue("Right Eye", new VisualData(3, -6), head, 0, 2);
        CompartmentInstance leftEar = createSoftTissue("Left Ear", new VisualData(-8, 0), head, 0, 1);
        CompartmentInstance rightEar = createSoftTissue("Right Ear", new VisualData(8, 0), head, 0, 1);
        CompartmentInstance nose = createSoftTissue("Nose", new VisualData(0, -2), head, 0, 2);
        CompartmentInstance mouth = createSoftTissue("Mouth", new VisualData(0, 4), head, 0, 2);
        mouth.setAttribute(FunctionType.BITE, 1f);
        CompartmentInstance tongue = createSoftTissue("Tongue", new VisualData(0, 0), mouth, 0, 2);
        CompartmentInstance teeth = createHardTissue("Teeth", new VisualData(0, -2), mouth, 0, 2);

        compartments.addAll(Arrays.asList(headSkin, headFat, laceration, cut1, cut2, skull, brain, meninges,
                frontalLobe, parietalLobe, temporalLobe, occipitalLobe, cerebellum, brainstem,
                leftEye, rightEye, leftEar, rightEar, nose, mouth, tongue, teeth));
    }

    private static void buildChest(CompartmentInstance wholeBody, @NotNull List<CompartmentInstance> compartments) {
        CompartmentInstance chest = createMajorBodyPart("Chest", new VisualData(30, -15), wholeBody, 0);
        chest.getCompartmentSpace().setBackgroundTexture(ResourceLocation.withDefaultNamespace("textures/block/netherrack.png"));
        chest.setItem(Items.BLUE_WOOL);
        compartments.add(chest);

        // Chest layers
        CompartmentInstance chestSkin = createSoftTissue("Skin", new VisualData(0, 0), chest, 0, 30);
        CompartmentInstance chestFat = createSoftTissue("Fat", new VisualData(0, 0), chest, 1, 40);
        CompartmentInstance chestMuscles = createSoftTissue("Muscles", new VisualData(0, 0), chest, 2, 100);

        // Skeletal components
        CompartmentInstance ribs = createHardTissue("Ribs", new VisualData(0 * 5, 1 * 5, 3, 5, 27, 20), chest, 4, 60);
        ribs.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/organs/anatomical_ribs.png"));
//        CompartmentInstance sternum = createHardTissue("Sternum", new VisualData(0, -4), chest, 3, 20);

        // Membrane layer
        CompartmentInstance pleuralMembrane = createSoftTissue("Pleural Membrane", new VisualData(0, 0), chest, 3, 5);

        // Cardiopulmonary system
        CompartmentInstance heart = createSoftTissue("Heart", new VisualData(10 * 5, 9 * 5, 0, 5, 10, 9), chest, 4, 30);
        heart.setAttribute(FunctionType.CIRCULATION, 1f);
        heart.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/organs/anatomical_heart.png"));
        CompartmentInstance pericardium = createSoftTissue("Pericardium", new VisualData(0, 0), heart, 0, 2);
        CompartmentInstance leftLung = createSoftTissue("Left Lung", new VisualData(14 * 5, 2 * 5, 1, 5, 13, 18), chest, 4, 40);
        leftLung.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/organs/anatomical_left_lung.png"));
        leftLung.setAttribute(FunctionType.RESPIRATORY, 1f);
        CompartmentInstance rightLung = createSoftTissue("Right Lung", new VisualData(0 * 5, 2 * 5, 1, 5, 13, 18), chest, 4, 40);
        rightLung.setAttribute(FunctionType.RESPIRATORY, 1f);
        rightLung.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/organs/anatomical_right_lung.png"));

        // Other organs
        CompartmentInstance thymus = createSoftTissue("Thymus", new VisualData(0, -8), chest, 4, 5);
        CompartmentInstance esophagus = createSoftTissue("Esophagus", new VisualData(0, -10), chest, 4, 10);
        CompartmentInstance trachea = createSoftTissue("Trachea", new VisualData(12 * 5, 0 * 5, 2, 5, 3, 9), chest, 4, 10);
        trachea.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/organs/anatomical_trachea.png"));

        compartments.addAll(Arrays.asList(chestSkin, chestFat, chestMuscles, ribs,
                pleuralMembrane, heart, pericardium, leftLung, rightLung, thymus, esophagus, trachea));
    }

    private static void buildAbdomen(CompartmentInstance wholeBody, @NotNull List<CompartmentInstance> compartments) {
        CompartmentInstance abdomen = createMajorBodyPart("Abdomen", new VisualData(0, 30), wholeBody, 0);
        abdomen.setItem(Items.RED_WOOL);
        compartments.add(abdomen);

        // Abdominal layers
        CompartmentInstance abdomenSkin = createSoftTissue("Skin", new VisualData(0, 0), abdomen, 0, 40);
        abdomenSkin.getCompartmentSpace().setBackgroundTexture(ResourceLocation.withDefaultNamespace("textures/block/white_terracotta.png"));

        CompartmentInstance abdomenFat = createSoftTissue("Fat", new VisualData(0, 0), abdomen, 1, 60);
        abdomenFat.getCompartmentSpace().setBackgroundTexture(ResourceLocation.withDefaultNamespace("textures/block/horn_coral_block.png"));

        CompartmentInstance abdominalMuscles = createSoftTissue("Abdominal Muscles", new VisualData(0, 0), abdomen, 2, 100);
        CompartmentInstance peritoneum = createSoftTissue("Peritoneum", new VisualData(0, 0), abdomen, 3, 5);
        peritoneum.getCompartmentSpace().setBackgroundTexture(ResourceLocation.withDefaultNamespace("textures/block/dead_brain_coral_block.png"));
        CompartmentInstance cut = new CompartmentInstance(INJURY.get(), new VisualData(20, 40, 5, 2.3f, 16, 16), abdomen, 0, 15, "Cut", false);
        cut.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/sprites/medical/cut_visual.png"));
        cut.addTag(CompartmentTag.CUT);

        abdomen.getCompartmentSpace().addToLayer(1, cut.getUUID());
        abdomen.getCompartmentSpace().addToLayer(2, cut.getUUID());
        abdomen.getCompartmentSpace().addToLayer(3, cut.getUUID());
        abdomen.getCompartmentSpace().addToLayer(4, cut.getUUID());
//
//        CompartmentInstance cut2 = new CompartmentInstance(INJURY.get(), new VisualData(40, 40, 0, 2.3f, 20, 20), abdomen, 3, 15, "Cut", false);
//        cut2.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/sprites/medical/cut_visual.png"));
//        cut2.addTag(CompartmentTag.CUT);

        // Digestive organs
        CompartmentInstance stomach = createSoftTissue("Stomach", new VisualData(4 * 5, 0, 2, 5, 15, 14), abdomen, 4, 30);
        stomach.setItem(STOMACH.get());
        stomach.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/organs/anatomical_stomach.png"));
        stomach.getCompartmentSpace().setBackgroundTexture(ResourceLocation.withDefaultNamespace("textures/block/stripped_cherry_log_side.png"));
        CompartmentInstance smallIntestine = createSoftTissue("Small Intestine", new VisualData(1 * 5, 8 * 5, 0, 5, 17, 19), abdomen, 4, 40);
        smallIntestine.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/organs/anatomical_small_intestine.png"));
        CompartmentInstance colon = createSoftTissue("Colon", new VisualData(-1 * 5, 9 * 5, 1, 5, 22, 19), abdomen, 4, 40);
        colon.setItem(COLON.get());
        colon.getCompartmentSpace().setBackgroundTexture(ResourceLocation.withDefaultNamespace("textures/block/brain_coral_block.png"));
        colon.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/organs/anatomical_colon.png"));

        // Accessory organs
        CompartmentInstance liver = createSoftTissue("Liver", new VisualData(0, 0, 4, 5, 19, 14), abdomen, 4, 45);
        liver.setItem(LIVER.get());
        liver.getCompartmentSpace().setBackgroundTexture(ResourceLocation.withDefaultNamespace("textures/block/red_terracotta.png"));
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

        abdomen.getCompartmentSpace().addToLayer(5, bladder.getUUID());

        // Skeletal component
        CompartmentInstance pelvis = createHardTissue("Pelvis", new VisualData(0, 12, 6.2f), abdomen, 3, 80);
        pelvis.setItem(PELVIS.get());

        compartments.addAll(Arrays.asList(abdomenSkin, abdomenFat, abdominalMuscles, peritoneum, cut,
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
            skin.setAttribute(FunctionType.MANIPULATION, 1f);
            fat.setAttribute(FunctionType.MANIPULATION, 1f);
            muscles.setAttribute(FunctionType.MANIPULATION, 1f);
            tendons.setAttribute(FunctionType.MANIPULATION, 1f);
            CompartmentInstance humerus = createHardTissue(side + " Humerus", new VisualData(0, -5), limb, 3, 30);
            humerus.setAttribute(FunctionType.MANIPULATION, 1f);
            CompartmentInstance radiusUlna = createHardTissue(side + " Radius Ulna", new VisualData(0, 5), limb, 3, 20);
            radiusUlna.setAttribute(FunctionType.MANIPULATION, 1f);
            CompartmentInstance hand = createSoftTissue(side + " Hand", new VisualData(0, 12), limb, 0, 20);
            hand.setAttribute(FunctionType.MANIPULATION, 1f);
            CompartmentInstance wrist = createSoftTissue(side + " Wrist", new VisualData(0, 8), limb, 0, 10);
            wrist.setAttribute(FunctionType.MANIPULATION, 1f);
            compartments.addAll(Arrays.asList(humerus, radiusUlna, hand, wrist));
        } else {
            skin.setAttribute(FunctionType.MOVEMENT, 1f);
            fat.setAttribute(FunctionType.MOVEMENT, 1f);
            muscles.setAttribute(FunctionType.MOVEMENT, 1f);
            tendons.setAttribute(FunctionType.MOVEMENT, 1f);
            CompartmentInstance femur = createHardTissue(side + " Femur", new VisualData(0, -5), limb, 3, 50);
            femur.setAttribute(FunctionType.MOVEMENT, 1f);
            CompartmentInstance tibiaFibula = createHardTissue(side + " Tibia Fibula", new VisualData(0, 10), limb, 3, 40);
            tibiaFibula.setAttribute(FunctionType.MOVEMENT, 1f);
            CompartmentInstance foot = createSoftTissue(side + " Foot", new VisualData(0, 20), limb, 0, 30);
            foot.setAttribute(FunctionType.MOVEMENT, 1f);
            CompartmentInstance ankle = createSoftTissue(side + " Ankle", new VisualData(0, 15), limb, 0, 10);
            ankle.setAttribute(FunctionType.MOVEMENT, 1f);
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
        CompartmentInstance bodyPart = new CompartmentInstance(MAJOR_BODY_PART.get(), visualData, parent, layer, 20, name, false);
        bodyPart.setAttribute(FunctionType.FUNCTION, 1);
        return bodyPart;
    }

    private static @NotNull CompartmentInstance createSoftTissue(String name, VisualData visualData, CompartmentInstance parent, int layer, float maxHealth) {
        CompartmentInstance tissue = new CompartmentInstance(SOFT_TISSUE.get(), visualData, parent, layer, maxHealth, name, false);
        tissue.setAttribute(FunctionType.FUNCTION, 1);
        tissue.addTag(CompartmentTag.DOES_BLEED);
        return tissue;
    }

    private static @NotNull CompartmentInstance createHardTissue(String name, VisualData visualData, CompartmentInstance parent, int layer, float maxHealth) {
        CompartmentInstance tissue = new CompartmentInstance(HARD_TISSUE.get(), visualData, parent, layer, maxHealth, name, false);
        tissue.setAttribute(FunctionType.FUNCTION, 1);
        tissue.addTag(CompartmentTag.DOES_BLEED);
        return tissue;
    }
}
