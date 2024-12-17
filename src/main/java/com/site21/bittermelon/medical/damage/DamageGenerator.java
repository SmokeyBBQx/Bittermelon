package com.site21.bittermelon.medical.damage;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.compartments.Injury;
import com.site21.bittermelon.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
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
        this(EnumSet.of(CompartmentType.SOFT_TISSUE, CompartmentType.HARD_TISSUE));
    }

    public @Nullable DamageResult generateDamage(@NotNull MedicalStats medicalStats, int area, int minDepth, int maxDepth, float damage, Character character, LivingEntity entity) {
        List<Compartment> initialCompartments = getInitialCompartments(medicalStats);
        if (initialCompartments.isEmpty()) return null;

        Compartment targetBodyPart = initialCompartments.get(random.nextInt(initialCompartments.size()));
        List<InjuryResult> injuryResults = new ArrayList<>();

        for (int i = 0; i < area; i++) {
            InjuryResult injuryResult = inflictInjury(targetBodyPart.getChildren(), damage, medicalStats, character, entity);
            if (injuryResult == null) return null;

            injuryResults.add(injuryResult);

            Injury injury = injuryResult.injury();
            medicalStats.addCompartment(injury);

            int depth = minDepth + (int) (Math.pow(random.nextFloat(), 2) * (maxDepth - minDepth));
            for (int j = 0; j < depth; j++) {
                injuryResult = inflictInjury(filterCompartments(injury.getOwner().getChildren()),
                        injury.getMaxHealth() / 2, medicalStats, character, entity);
                if (injuryResult == null) break;

                injuryResults.add(injuryResult);

                injury = injuryResult.injury();
                medicalStats.addCompartment(injury);
            }
        }

        return new DamageResult(targetBodyPart, injuryResults);
    }

    private @Nullable InjuryResult inflictInjury(@NotNull List<Compartment> compartments, float damage, MedicalStats medicalStats, Character character, LivingEntity entity) {
        if (compartments.isEmpty()) return null;

        Compartment target = compartments.get(random.nextInt(compartments.size()));
        float injuryDamage = 1 + random.nextFloat() * damage;

        if (shouldDismember && injuryDamage > target.getHealth() && random.nextFloat() > 0.5f) {
            handleDismemberment(target, character, medicalStats, entity);
        }

        return createInjury(injuryDamage, target, character, entity);
    }

    protected abstract InjuryResult createInjury(float damage, Compartment target, Character character, LivingEntity entity);

    private void handleDismemberment(@NotNull Compartment target, @NotNull Character character, @NotNull MedicalStats medicalStats, LivingEntity entity) {
        Injury amputation = new Injury(EnumSet.of(CompartmentType.TRAUMATIC_AMPUTATION), "Traumatic Amputation" + " (" + target.getName() + ")", target.getOwner(), 10, character, entity);
        amputation.reveal();
        medicalStats.addCompartment(amputation);
        medicalStats.removeCompartment(target);
        String message = character.getName() + "'s " + target.getName().toLowerCase() + " was dismembered.";
        LocalMessageHelper.sendLocalMessage(entity, 5, Component.literal(message));
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
        return compartment.hasType(CompartmentType.MAJOR_BODY_PART) && !compartment.isHidden() && compartment.getOwner() != null;
    }

    protected boolean isValidCompartment(@NotNull Compartment compartment) {
        return compartment.getTypes().stream().anyMatch(allowedCompartments::contains);
    }
}

