package com.site21.bittermelon.medical.damage;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.compartments.Injury;
import com.site21.bittermelon.medical.compartments.conditions.Pain;
import com.site21.bittermelon.medical.medicalstats.MedicalStats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class DamageGenerator {
    protected final EnumSet<CompartmentType> allowedCompartments;
    protected static final Random random = new Random();
    protected boolean shouldDismember = false;

    public DamageGenerator(EnumSet<CompartmentType> allowedCompartments) {
        this.allowedCompartments = allowedCompartments;
    }

    public DamageGenerator() {
        this(EnumSet.of(CompartmentType.SOFT_TISSUE, CompartmentType.HARD_TISSUE, CompartmentType.MAJOR_BODY_PART));
    }

    /**
     * Generates damage to a character's body based on specified parameters.
     * Selects a major body part as the initial target, then creates injuries in multiple child compartments based on area.
     * For each affected area, damage can penetrate through multiple compartments based on depth.
     *
     * @param medicalStats MedicalStats of targeted character
     * @param area Amount of children compartments affected within the targeted body part (e.g., 3 would create 3 separate injury sites)
     * @param minDepth Minimum number of nested compartments the damage penetrates through
     * @param maxDepth Maximum number of nested compartments the damage can penetrate
     * @param damage Base damage value
     * @param character The character receiving the damage
     * @param entity The living entity associated with the character
     * @return A DamageResult containing the targeted body part and list of created injuries, or null if no valid target was found
     */

    public @Nullable DamageResult generateDamage(@NotNull MedicalStats medicalStats, int area, int minDepth, int maxDepth, float damage, Character character, LivingEntity entity) {
        List<Compartment> initialCompartments = getInitialCompartments(medicalStats);
        if (initialCompartments.isEmpty()) return null;

        Compartment targetBodyPart = initialCompartments.get(random.nextInt(initialCompartments.size()));
        List<Compartment> validChildren = filterCompartments(targetBodyPart.getChildren());
        List<InjuryResult> injuryResults = new ArrayList<>();

        for (int i = 0; i < area; i++) {
            InjuryResult injuryResult = inflictInjury(validChildren, damage, medicalStats, character, entity);
            if (injuryResult == null) return null;

            injuryResults.add(injuryResult);

            Injury injury = injuryResult.injury();
            medicalStats.addCompartment(injury);

            int depth = minDepth + (int) (Math.pow(random.nextFloat(), 2) * (maxDepth - minDepth));
            for (int j = 0; j < depth; j++) {
                injuryResult = inflictInjury(filterCompartments(injury.getOwner().getChildren()),
                        injury.getMaxHealth() / 2, medicalStats, character, entity);
                if (injuryResult == null) break;

                if (injuryResult.injury().getHealth() > injuryResult.injury().getOwner().getHealth()) {
                    depth++;
                }

                injuryResults.add(injuryResult);

                injury = injuryResult.injury();
                medicalStats.addCompartment(injury);
            }
        }

        return new DamageResult(targetBodyPart, injuryResults);
    }

    public @Nullable DamageResult generateDamage(@NotNull MedicalStats medicalStats, int maxArea, int minDepth, int maxDepth, float damage, float performance, Character character, LivingEntity entity) {
        int area = 1 + random.nextInt((int) (maxArea * performance > 1 ? maxArea * performance : 1));
        maxDepth = 1 + random.nextInt((int) (maxDepth * performance > 1 ? maxDepth * performance : 1));

        return generateDamage(medicalStats, area, minDepth, maxDepth, damage, character, entity);
    }

    private @Nullable InjuryResult inflictInjury(@NotNull List<Compartment> compartments, float damage, MedicalStats medicalStats, Character character, LivingEntity entity) {
        if (compartments.isEmpty()) return null;

        Compartment target = compartments.get(random.nextInt(compartments.size()));
        if (target.hasType(CompartmentType.MAJOR_BODY_PART)) {
            return inflictInjury(target.getChildren(), damage, medicalStats, character, entity);
        }

        float injuryDamage = 1 + random.nextFloat() * damage;

        if (shouldDismember && injuryDamage > target.getHealth() && random.nextFloat() > 0.5f) {
            return handleDismemberment(target, character, medicalStats, entity);
        }

        return createInjury(injuryDamage, target, character, entity);
    }

    protected abstract InjuryResult createInjury(float damage, Compartment target, Character character, LivingEntity entity);

    @Contract("_, _, _, _ -> new")
    private @NotNull InjuryResult handleDismemberment(@NotNull Compartment target, @NotNull Character character, @NotNull MedicalStats medicalStats, LivingEntity entity) {
        Injury amputation = new Injury(EnumSet.of(CompartmentType.TRAUMATIC_AMPUTATION), "Traumatic Amputation" + " (" + target.getName() + ")", target.getOwner(), target.getMaxHealth(), character, entity);
        amputation.reveal();
        medicalStats.addCompartment(amputation);

        medicalStats.removeCompartment(target);
        String message = target.getName().toLowerCase() + " was dismembered.";

        if (target.getItem() != null) {
            Entity itemEntity = target.getItem().getEntityRepresentation();
            if (itemEntity != null) {
                entity.level().addFreshEntity(itemEntity);
            }
        }

        return new InjuryResult(amputation, message);
    }

    private List<Compartment> filterCompartments(@NotNull List<Compartment> compartments) {
        return compartments.stream()
                .filter(this::isValidCompartment)
                .toList();
    }

    protected List<Compartment> getInitialCompartments(@NotNull MedicalStats medicalStats) {
        return medicalStats.getCompartments().stream()
                .filter(this::isValidInitialCompartment)
                .toList();
    }

    private boolean isValidInitialCompartment(@NotNull Compartment compartment) {
        return compartment.hasType(CompartmentType.MAJOR_BODY_PART)
                && !compartment.isHidden()
                && compartment.getOwner() != null
                && compartment.getOwner().hasType(CompartmentType.MAJOR_BODY_PART);
    }

    protected boolean isValidCompartment(@NotNull Compartment compartment) {
        return compartment.getTypes().stream().anyMatch(allowedCompartments::contains);
    }
}

