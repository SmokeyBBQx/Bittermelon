package com.site21.bittermelon.content.medical.damage;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.init.custom.Compartments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class DamageGenerator {
    protected final EnumSet<CompartmentTag> allowedCompartments;
    protected static final Random random = new Random();
    protected boolean shouldDismember = false;

    public DamageGenerator(EnumSet<CompartmentTag> allowedCompartments) {
        this.allowedCompartments = allowedCompartments;
    }

    public DamageGenerator() {
        this(EnumSet.of(CompartmentTag.SOFT_TISSUE, CompartmentTag.HARD_TISSUE, CompartmentTag.MAJOR_BODY_PART));
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

    public @Nullable DamageResult generateDamage(MedicalStats medicalStats, int area, int minDepth, int maxDepth, float damage, Character character, @NotNull LivingEntity entity) {
        if (entity.level().isClientSide) return null;

        List<CompartmentInstance> initialCompartments = getInitialCompartments(medicalStats);
        if (initialCompartments.isEmpty()) return null;

        CompartmentInstance targetBodyPart = initialCompartments.get(random.nextInt(initialCompartments.size()));
        List<UUID> validChildren = filterCompartments(targetBodyPart.getChildren(), medicalStats);
        List<InjuryResult> injuryResults = new ArrayList<>();

        for (int i = 0; i < area; i++) {
            InjuryResult injuryResult = inflictInjury(validChildren, damage, medicalStats, entity);
            if (injuryResult == null) return null;

            injuryResults.add(injuryResult);

            CompartmentInstance injury = injuryResult.injury();
            medicalStats.addCompartment(injury);

            int depth = minDepth + (int) (Math.pow(random.nextFloat(), 2) * (maxDepth - minDepth));
            for (int j = 0; j < depth; j++) {
                if (injury.getParent(medicalStats) == null) break;

                injuryResult = inflictInjury(filterCompartments(injury.getParent(medicalStats).getChildren(), medicalStats),
                        injury.getMaxHealth() / 2, medicalStats, entity);
                if (injuryResult == null) break;

                if (injuryResult.injury().getParent(medicalStats) == null) break;

                if (injuryResult.injury().getHealth(medicalStats) > injuryResult.injury().getParent(medicalStats).getHealth(medicalStats)) {
                    depth++;
                }

                injuryResults.add(injuryResult);

                injury = injuryResult.injury();
                medicalStats.addCompartment(injury);
            }
        }

        if (injuryResults.isEmpty()) {
            Bittermelon.LOGGER.error("No injuries for damage inflicted upon {} with area: {} minDepth: {} maxDepth: {} damage: {} for character: {} ({})", targetBodyPart, area, minDepth, maxDepth, damage, character.getName(), character.getUUID());
            return null;
        }

        return new DamageResult(targetBodyPart, injuryResults);
    }

    public @Nullable DamageResult generateDamage(@NotNull MedicalStats medicalStats, int maxArea, int minDepth, int maxDepth, float damage, float performance, Character character, LivingEntity entity) {
        int area = 1 + random.nextInt((int) (maxArea * performance > 1 ? maxArea * performance : 1));
        maxDepth = 1 + random.nextInt((int) (maxDepth * performance > 1 ? maxDepth * performance : 1));

        return generateDamage(medicalStats, area, minDepth, maxDepth, damage, character, entity);
    }

    private @Nullable InjuryResult inflictInjury(@NotNull List<UUID> compartments, float damage, MedicalStats medicalStats, LivingEntity entity) {
        if (compartments.isEmpty()) return null;

        UUID targetID = compartments.get(random.nextInt(compartments.size()));
        CompartmentInstance target = medicalStats.getCompartment(targetID);
        if (target == null) {
            Bittermelon.LOGGER.error("Skipping target compartment for injury with UUID: {}", targetID);
            List<UUID> newCompartments = new ArrayList<>(compartments);
            newCompartments.remove(targetID);
            return inflictInjury(newCompartments, damage, medicalStats, entity);
        }

        if (target.hasTag(CompartmentTag.MAJOR_BODY_PART)) {
            return inflictInjury(new ArrayList<>(target.getChildren()), damage, medicalStats, entity);
        }

        float injuryDamage = 1 + random.nextFloat() * damage;

        if (shouldDismember && injuryDamage > target.getHealth(medicalStats) && random.nextFloat() > 0.5f) {
            return handleDismemberment(target, medicalStats, entity);
        }

        return createInjury(injuryDamage, target, medicalStats);
    }

    protected abstract InjuryResult createInjury(float damage, CompartmentInstance target, MedicalStats medicalStats);

    @Contract("_, _, _ -> new")
    private @NotNull InjuryResult handleDismemberment(@NotNull CompartmentInstance target, @NotNull MedicalStats medicalStats, LivingEntity entity) {
        CompartmentInstance amputation = new CompartmentInstance(Compartments.TRAUMATIC_AMPUTATION.get(), target.getMaxHealth(), "Traumatic Amputation" + " (" + target.getName() + ")", false);
        amputation.setParent(target.getParentID());
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

    private List<UUID> filterCompartments(@NotNull HashSet<UUID> compartments, MedicalStats medicalStats) {
        return compartments.stream()
                .filter(id -> isValidCompartment(id, medicalStats))
                .toList();
    }

    protected List<CompartmentInstance> getInitialCompartments(@NotNull MedicalStats medicalStats) {
        return medicalStats.getCompartments().values().stream()
                .filter(compartment -> isValidInitialCompartment(compartment, medicalStats))
                .toList();
    }

    private boolean isValidInitialCompartment(@NotNull CompartmentInstance compartment, MedicalStats medicalStats) {
        return compartment.hasTag(CompartmentTag.MAJOR_BODY_PART)
                && !compartment.isHidden()
                && compartment.getParent(medicalStats) != null
                && compartment.getParent(medicalStats).hasTag(CompartmentTag.MAJOR_BODY_PART);
    }

    protected boolean isValidCompartment(@NotNull UUID compartmentID, @NotNull MedicalStats medicalStats) {
        if (medicalStats.getCompartment(compartmentID) == null) return false;
        return medicalStats.getCompartment(compartmentID).getTags().stream().anyMatch(allowedCompartments::contains);
    }
}

