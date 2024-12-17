package com.site21.bittermelon.medical.compartments.conditions;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.blocks.FluidBlock;
import com.site21.bittermelon.blocks.blockentities.FluidBlockEntity;
import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.compartments.Condition;
import com.site21.bittermelon.medical.compartments.FunctionType;
import com.site21.bittermelon.medical.compartments.bodyparts.BodyPart;
import com.site21.bittermelon.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.substance.SubstanceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Random;

import static com.site21.bittermelon.init.BitterBlocks.FLUID;
import static com.site21.bittermelon.init.Substances.LIQUID_BLOOD;
import static com.site21.bittermelon.medical.compartments.CompartmentType.*;

public class Bleed extends Condition {
    protected final float bleedRate;
    public static final float BASE_MAJOR_ARTERIAL_BLEED_RATE = 2.0f;
    public static final float BASE_ARTERIAL_BLEED_RATE = 1.0f;
    public static final float BASE_VENOUS_BLEED_RATE = 0.5f;
    public static final float BASE_CAPILLARY_BLEED_RATE = 0.2f;

    public Bleed(CompartmentType bleedType, String name, Compartment owner, float maxHealth, Character character,
                 LivingEntity entity, float bleedRate) {
        super(EnumSet.of(BLEED, bleedType), name, owner, maxHealth, character, entity);
        this.bleedRate = bleedRate;
        setAttribute(FunctionType.BLEED, bleedRate);
    }

    @Override
    public void update(MedicalStats medicalStats) {
        super.update(medicalStats);
    }

    public static void generateBleed(@NotNull Compartment owner, Character character, LivingEntity entity, float damage) {
        if (owner.getOwner() instanceof BodyPart bodyPart && bodyPart.doesBleed()) {
            if (bodyPart.hasType(BLOOD_VESSEL)) {
                generateVesselBleed(bodyPart, character, entity, damage);
            } else {
                randomBleeds(owner, character, entity, damage);
            }
        }
    }

    private static void generateVesselBleed(Compartment vessel, @NotNull Character character, LivingEntity entity, float damage) {
        MedicalStats medicalStats = character.getMedicalStats();
        float bleedRate = getBleedRateForVessel(vessel);
        CompartmentType bleedType = getBleedTypeForVessel(vessel);
        if (bleedRate > 0) {
            Bleed bleed = new Bleed(bleedType, "Bleed", vessel, damage, character, entity, bleedRate);
            setBleedIcon(bleed);
            medicalStats.addCompartment(bleed);
        }
    }

    private static float getBleedRateForVessel(@NotNull Compartment vessel) {
        if (vessel.hasType(MAJOR_ARTERY)) return BASE_MAJOR_ARTERIAL_BLEED_RATE;
        if (vessel.hasType(ARTERY)) return BASE_ARTERIAL_BLEED_RATE;
        if (vessel.hasType(VEIN)) return BASE_VENOUS_BLEED_RATE;
        if (vessel.hasType(CAPILLARY)) return BASE_CAPILLARY_BLEED_RATE;
        return 0f;
    }

    private static @Nullable CompartmentType getBleedTypeForVessel(@NotNull Compartment vessel) {
        if (vessel.hasType(MAJOR_ARTERY)) return MAJOR_ARTERIAL_BLEED;
        if (vessel.hasType(ARTERY)) return ARTERIAL_BLEED;
        if (vessel.hasType(VEIN)) return VENOUS_BLEED;
        if (vessel.hasType(CAPILLARY)) return CAPILLARY_BLEED;
        return null;
    }

    private static void setBleedIcon(@NotNull Compartment bleed) {
        if (bleed.hasType(ARTERIAL_BLEED)) {
            bleed.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/sprites/medical/arterial_bleed.png"));
        } else if (bleed.hasType(VENOUS_BLEED)) {
            bleed.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/sprites/medical/venous_bleed.png"));
        } else {
            bleed.setIcon(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/sprites/medical/capillary_bleed.png"));
        }
    }

    public static void randomBleeds(Compartment owner, @NotNull Character character, LivingEntity entity, float damage) {
        MedicalStats medicalStats = character.getMedicalStats();
        Random random = new Random();

        int MAX_BLEED = 5;
        int MIN_BLEED = 1;

        int numBleeds = Math.max(MIN_BLEED, Math.min(MAX_BLEED, 1 + (int)(damage / 4)));

        for (int i = 0; i < numBleeds; i++) {
            float severityRoll = random.nextFloat();
            float damageImpact = damage / 100;
            float bleedSeverity = 1 + damage * random.nextFloat();

            Bleed bleed;
            if (severityRoll > 0.8 - damageImpact) {
                bleed = new Bleed(ARTERIAL_BLEED,
                        "Arterial Bleed", owner, bleedSeverity, character, entity, BASE_ARTERIAL_BLEED_RATE);
            } else if (severityRoll > 0.6 - damageImpact) {
                bleed = new Bleed(VENOUS_BLEED,
                        "Venous Bleed", owner, bleedSeverity, character, entity, BASE_VENOUS_BLEED_RATE);
            } else {
                bleed = new Bleed(CAPILLARY_BLEED,
                        "Capillary Bleed", owner, bleedSeverity, character, entity, BASE_CAPILLARY_BLEED_RATE);
            }

            setBleedIcon(bleed);

            medicalStats.addCompartment(bleed);
            if (owner.isHidden()) {
                bleed.reveal();
            }
        }
    }

    public float getBleedRate() {
        return bleedRate;
    }
}
