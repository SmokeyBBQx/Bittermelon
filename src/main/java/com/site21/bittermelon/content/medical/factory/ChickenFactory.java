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

public class ChickenFactory implements AnatomyFactoryOld {

    @Contract("_, _ -> new")
    public @NotNull MedicalStatsOld build(BloodType bloodType, Character character) {
        List<CompartmentOld> compartments = new ArrayList<>();

        BodyPart wholeBody = createMajorBodyPart("Whole Body", null);
        compartments.add(wholeBody);

        buildHead(wholeBody, compartments);
        buildBody(wholeBody, compartments);
        buildWings(wholeBody, compartments);
        buildLegs(wholeBody, compartments);
        buildTail(wholeBody, compartments);

        return new MedicalStatsOld(bloodType, compartments, character);
    }

    private static void buildHead(BodyPart wholeBody, @NotNull List<CompartmentOld> compartments) {
        BodyPart head = createMajorBodyPart("Head", wholeBody);
        compartments.add(head);

        // Head layers
        BodyPart headSkin = createRevealedBodyPart("Skin", head, 2.5f, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart skull = createBodyPart("Skull", headSkin, 15, EnumSet.of(CompartmentTag.HARD_TISSUE));

        // Brain
        BodyPart brain = createMajorBodyPart("Brain", skull);
        BodyPart opticTectum = createBodyPart("Optic Tectum", brain, 1f, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart cerebellum = createBodyPart("Cerebellum", brain, 1f, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        cerebellum.setAttribute(FunctionType.BRAIN_MOTOR_ABILITY, 1f);
        BodyPart medulla = createBodyPart("Medulla Oblongata", brain, 1f, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        medulla.setAttribute(FunctionType.BRAIN_VITALS, 1f);


        // Face components
        BodyPart beak = createRevealedBodyPart("Beak", head, 7.5f, EnumSet.of(CompartmentTag.HARD_TISSUE));
        beak.setAttribute(FunctionType.BITE, 1f);
        BodyPart comb = createRevealedBodyPart("Comb", head, 2.5f, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart wattles = createRevealedBodyPart("Wattles", head, 2.5f, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart leftEye = createRevealedBodyPart("Left Eye", head, 1, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart rightEye = createRevealedBodyPart("Right Eye", head, 1, EnumSet.of(CompartmentTag.SOFT_TISSUE));

        compartments.addAll(Arrays.asList(headSkin, skull, brain, opticTectum, cerebellum, medulla, beak, comb, wattles, leftEye, rightEye));
    }

    private static void buildBody(BodyPart wholeBody, @NotNull List<CompartmentOld> compartments) {
        BodyPart body = createMajorBodyPart("Body", wholeBody);
        compartments.add(body);

        // Body layers
        BodyPart skin = createRevealedBodyPart("Skin", body, 10, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart feathers = createRevealedBodyPart("Feathers", skin, 5, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart muscles = createBodyPart("Muscles", skin, 20, EnumSet.of(CompartmentTag.SOFT_TISSUE));

        // Skeletal components
        BodyPart ribs = createBodyPart("Ribs", muscles, 15, EnumSet.of(CompartmentTag.HARD_TISSUE));
        BodyPart keel = createBodyPart("Keel", muscles, 12.5f, EnumSet.of(CompartmentTag.HARD_TISSUE));
        BodyPart wishbone = createBodyPart("Wishbone", muscles, 7.5f, EnumSet.of(CompartmentTag.HARD_TISSUE));

        // Internal organs
        BodyPart heart = createBodyPart("Heart", muscles, 7.5f, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        heart.setAttribute(FunctionType.CIRCULATION, 1f);
        BodyPart leftLung = createBodyPart("Left Lung", muscles, 5, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        leftLung.setAttribute(FunctionType.RESPIRATORY, 1f);
        BodyPart rightLung = createBodyPart("Right Lung", muscles, 5, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        rightLung.setAttribute(FunctionType.RESPIRATORY, 1f);
        BodyPart airSacs = createBodyPart("Air Sacs", muscles, 10, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        airSacs.setAttribute(FunctionType.RESPIRATORY, 1f);

        // Digestive system
        BodyPart crop = createBodyPart("Crop", muscles, 7.5f, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        crop.defaultItem(STOMACH.get());
        BodyPart proventriculus = createBodyPart("Proventriculus", muscles, 5, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart gizzard = createBodyPart("Gizzard", muscles, 10, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        gizzard.defaultItem(STOMACH.get());
        BodyPart intestines = createBodyPart("Intestines", muscles, 12.5f, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart liver = createBodyPart("Liver", muscles, 7.5f, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        liver.defaultItem(LIVER.get());

        compartments.addAll(Arrays.asList(skin, feathers, muscles, ribs, keel, wishbone,
                heart, leftLung, rightLung, airSacs, crop, proventriculus, gizzard, intestines, liver));
    }

    private static void buildWings(BodyPart wholeBody, List<CompartmentOld> compartments) {
        // Left Wing
        BodyPart leftWing = createMajorBodyPart("Left Wing", wholeBody);
        buildWing(leftWing, "Left", compartments);

        // Right Wing
        BodyPart rightWing = createMajorBodyPart("Right Wing", wholeBody);
        buildWing(rightWing, "Right", compartments);

        compartments.addAll(Arrays.asList(leftWing, rightWing));
    }

    private static void buildWing(BodyPart wing, String side, @NotNull List<CompartmentOld> compartments) {
        BodyPart skin = createRevealedBodyPart("Skin", wing, 5, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart feathers = createRevealedBodyPart("Flight Feathers", wing, 4, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart muscles = createBodyPart("Muscles", skin, 12.5f, EnumSet.of(CompartmentTag.SOFT_TISSUE));

        BodyPart humerus = createBodyPart(side + " Humerus", muscles, 7.5f, EnumSet.of(CompartmentTag.HARD_TISSUE));
        BodyPart radius = createBodyPart(side + " Radius", muscles, 6, EnumSet.of(CompartmentTag.HARD_TISSUE));
        BodyPart ulna = createBodyPart(side + " Ulna", muscles, 6, EnumSet.of(CompartmentTag.HARD_TISSUE));

        skin.setAttribute(FunctionType.FLIGHT, 1f);
        feathers.setAttribute(FunctionType.FLIGHT, 1f);
        muscles.setAttribute(FunctionType.FLIGHT, 1f);
        humerus.setAttribute(FunctionType.FLIGHT, 1f);
        radius.setAttribute(FunctionType.FLIGHT, 1f);
        ulna.setAttribute(FunctionType.FLIGHT, 1f);

        compartments.addAll(Arrays.asList(skin, feathers, muscles, humerus, radius, ulna));
    }

    private static void buildLegs(BodyPart wholeBody, List<CompartmentOld> compartments) {
        // Left Leg
        BodyPart leftLeg = createMajorBodyPart("Left Leg", wholeBody);
        buildLeg(leftLeg, "Left", compartments);

        // Right Leg
        BodyPart rightLeg = createMajorBodyPart("Right Leg", wholeBody);
        buildLeg(rightLeg, "Right", compartments);

        compartments.addAll(Arrays.asList(leftLeg, rightLeg));
    }

    private static void buildLeg(BodyPart leg, String side, @NotNull List<CompartmentOld> compartments) {
        BodyPart skin = createRevealedBodyPart("Skin", leg, 4, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart scales = createRevealedBodyPart("Scales", skin, 5, EnumSet.of(CompartmentTag.HARD_TISSUE));
        BodyPart muscles = createBodyPart("Muscles", skin, 15, EnumSet.of(CompartmentTag.SOFT_TISSUE));

        BodyPart femur = createBodyPart(side + " Femur", muscles, 10, EnumSet.of(CompartmentTag.HARD_TISSUE));
        BodyPart tibiotarsus = createBodyPart(side + " Tibiotarsus", muscles, 12.5f, EnumSet.of(CompartmentTag.HARD_TISSUE));
        BodyPart tarsometatarsus = createBodyPart(side + " Tarsometatarsus", muscles, 7.5f, EnumSet.of(CompartmentTag.HARD_TISSUE));
        BodyPart foot = createRevealedBodyPart(side + " Foot", leg, 5, EnumSet.of(CompartmentTag.SOFT_TISSUE));

        skin.setAttribute(FunctionType.MOVEMENT, 1f);
        scales.setAttribute(FunctionType.MOVEMENT, 1f);
        muscles.setAttribute(FunctionType.MOVEMENT, 1f);
        femur.setAttribute(FunctionType.MOVEMENT, 1f);
        tibiotarsus.setAttribute(FunctionType.MOVEMENT, 1f);
        tarsometatarsus.setAttribute(FunctionType.MOVEMENT, 1f);
        foot.setAttribute(FunctionType.MOVEMENT, 1f);

        // Spurs (if present)
        BodyPart spur = createRevealedBodyPart(side + " Spur", tarsometatarsus, 4, EnumSet.of(CompartmentTag.HARD_TISSUE));

        compartments.addAll(Arrays.asList(skin, scales, muscles, femur, tibiotarsus, tarsometatarsus, foot, spur));
    }

    private static void buildTail(BodyPart wholeBody, @NotNull List<CompartmentOld> compartments) {
        BodyPart tail = createMajorBodyPart("Tail", wholeBody);
        compartments.add(tail);

        BodyPart tailSkin = createRevealedBodyPart("Skin", tail, 2.5f, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart tailFeathers = createRevealedBodyPart("Tail Feathers", tail, 4, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart tailMuscles = createBodyPart("Muscles", tailSkin, 5, EnumSet.of(CompartmentTag.SOFT_TISSUE));
        BodyPart pygostyle = createBodyPart("Pygostyle", tailMuscles, 6, EnumSet.of(CompartmentTag.HARD_TISSUE));

        compartments.addAll(Arrays.asList(tailSkin, tailFeathers, tailMuscles, pygostyle));
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
}
