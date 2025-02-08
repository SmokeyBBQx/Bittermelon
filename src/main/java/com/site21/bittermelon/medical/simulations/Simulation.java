package com.site21.bittermelon.medical.simulations;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.medical.compartments.conditions.ForeignSubstance;
import com.site21.bittermelon.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.substance.SubstanceStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public class Simulation {
    protected final SubstanceStack stack;
    protected final MedicalStats medicalStats;
    protected final List<Pair<Function<MedicalStats, Float>, Float>> absorptionModifiers;
    protected final SubstanceStack stackCopy;
    public boolean finished = false;

    public Simulation(@NotNull SubstanceStack stack, MedicalStats medicalStats, List<Pair<Function<MedicalStats, Float>, Float>> absorptionModifiers) {
        this.stack = stack;
        this.medicalStats = medicalStats;
        this.absorptionModifiers = absorptionModifiers;
        this.stackCopy = stack.copy();
    }

    public void update() {
        if (stack.getAmount() > 0) {
            float absorptionRate = getAbsorptionRate();
            stackCopy.setAmount(absorptionRate * stack.getAmount());
            stack.setAmount(-absorptionRate * stack.getAmount());
            medicalStats.updateSubstance(stackCopy);
        } else {
            finished = true;
        }
    }

    private float getAbsorptionRate() {
        if (absorptionModifiers.isEmpty()) return 1f;

        float totalWeight = 0f;
        float weightedSum = 0f;

        for (var modifier : absorptionModifiers) {
            float modifierValue = modifier.getFirst().apply(medicalStats);
            float weight = modifier.getSecond();
            weightedSum += modifierValue * weight;
            totalWeight += weight;
        }

        return (weightedSum / totalWeight) * stack.getSubstance().getAbsorptionRate();
    }
}
