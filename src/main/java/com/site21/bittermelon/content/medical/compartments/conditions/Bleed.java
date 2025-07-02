package com.site21.bittermelon.content.medical.compartments.conditions;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.medical.compartments.*;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.init.custom.Compartments;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Random;

import static com.site21.bittermelon.content.medical.compartments.CompartmentTag.*;
import static com.site21.bittermelon.content.medical.compartments.CompartmentTag.CAPILLARY_BLEED;

public class Bleed extends Compartment {
    public static final float BASE_MAJOR_ARTERIAL_BLEED_RATE = 2.0f;
    public static final float BASE_ARTERIAL_BLEED_RATE = 1.0f;
    public static final float BASE_VENOUS_BLEED_RATE = 0.5f;
    public static final float BASE_CAPILLARY_BLEED_RATE = 0.2f;

    public Bleed(String id, EnumSet<CompartmentTag> defaultTags) {
        super(id, defaultTags);
    }

//    @Override
//    public void tick(MedicalStats medicalStats, @NotNull CompartmentInstance instance) {
//        super.tick(medicalStats, instance);
//        instance.modifyHealth(-0.001f);
//    }

    public static void generateBleed(@NotNull CompartmentInstance target, MedicalStats medicalStats, float damage) {
//        CompartmentInstance parent = target.getParent(medicalStats);
//
//        if (parent == null) return;
//
//        if (parent.hasTag(BODY_PART) && parent.hasTag(DOES_BLEED)) {
//            if (parent.hasTag(BLOOD_VESSEL)) {
//                generateVesselBleed(parent, damage, medicalStats);
//            } else {
//                randomBleeds(target, damage, medicalStats);
//            }
//        }
    }

    private static void generateVesselBleed(CompartmentInstance vessel, float damage, MedicalStats medicalStats) {
        float bleedRate = getBleedRateForVessel(vessel);
        CompartmentTag bleedType = getBleedTypeForVessel(vessel);
//        if (bleedRate > 0) {
//            CompartmentInstance bleed = new CompartmentInstance(Compartments.BLEED.get(), damage, "Bleed", false);
//            bleed.addTag(bleedType);
//            bleed.setAttribute(MedicalAttribute.BLEED, bleedRate);
//            setBleedIcon(bleed);
//            medicalStats.addCompartment(bleed);
//        }
    }

    private static float getBleedRateForVessel(@NotNull CompartmentInstance vessel) {
        if (vessel.hasTag(MAJOR_ARTERY)) return BASE_MAJOR_ARTERIAL_BLEED_RATE;
        if (vessel.hasTag(ARTERY)) return BASE_ARTERIAL_BLEED_RATE;
        if (vessel.hasTag(VEIN)) return BASE_VENOUS_BLEED_RATE;
        if (vessel.hasTag(CAPILLARY)) return BASE_CAPILLARY_BLEED_RATE;
        return 0f;
    }

    private static @Nullable CompartmentTag getBleedTypeForVessel(@NotNull CompartmentInstance vessel) {
        if (vessel.hasTag(MAJOR_ARTERY)) return MAJOR_ARTERIAL_BLEED;
        if (vessel.hasTag(ARTERY)) return ARTERIAL_BLEED;
        if (vessel.hasTag(VEIN)) return VENOUS_BLEED;
        if (vessel.hasTag(CAPILLARY)) return CAPILLARY_BLEED;
        return null;
    }

    private static void setBleedIcon(@NotNull CompartmentInstance bleed) {
        if (bleed.hasTag(ARTERIAL_BLEED)) {
            bleed.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/sprites/medical/arterial_bleed.png"));
        } else if (bleed.hasTag(VENOUS_BLEED)) {
            bleed.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/sprites/medical/venous_bleed.png"));
        } else {
            bleed.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/sprites/medical/capillary_bleed.png"));
        }
    }

    public static void randomBleeds(CompartmentInstance parent, float damage, MedicalStats medicalStats) {
        Random random = new Random();

        int MAX_BLEED = 5;
        int MIN_BLEED = 1;

        int numBleeds = Math.max(MIN_BLEED, Math.min(MAX_BLEED, 1 + (int)(damage / 4)));

//        for (int i = 0; i < numBleeds; i++) {
//            float severityRoll = random.nextFloat();
//            float damageImpact = damage / 100;
//            float bleedSeverity = 1 + damage * random.nextFloat();
//
//            CompartmentInstance bleed;
//            if (severityRoll > 0.8 - damageImpact) {
//                bleed = new CompartmentInstance(Compartments.BLEED.get(), bleedSeverity, "Arterial Bleed", true);
//                bleed.addTag(ARTERIAL_BLEED);
//                bleed.setAttribute(MedicalAttribute.BLEED, BASE_ARTERIAL_BLEED_RATE);
//            } else if (severityRoll > 0.6 - damageImpact) {
//                bleed = new CompartmentInstance(Compartments.BLEED.get(), bleedSeverity, "Venous Bleed", true);
//                bleed.addTag(VENOUS_BLEED);
//                bleed.setAttribute(MedicalAttribute.BLEED, BASE_VENOUS_BLEED_RATE);
//            } else {
//                bleed = new CompartmentInstance(Compartments.BLEED.get(), bleedSeverity, "Capillary Bleed", true);
//                bleed.addTag(CAPILLARY_BLEED);
//                bleed.setAttribute(MedicalAttribute.BLEED, BASE_CAPILLARY_BLEED_RATE);
//            }
//
//            bleed.initializeWithParent(parent);
//            setBleedIcon(bleed);
//
//            medicalStats.addCompartment(bleed);
//            if (!parent.isHidden()) {
//                // TODO: if statement is acting weird
//                bleed.setHidden(false);
//            }
//        }
    }
}
