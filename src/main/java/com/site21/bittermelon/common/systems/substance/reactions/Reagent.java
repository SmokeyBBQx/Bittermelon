package com.site21.bittermelon.common.systems.substance.reactions;

import com.site21.bittermelon.common.systems.substance.Nature;
import com.site21.bittermelon.common.systems.substance.Substance;
import com.site21.bittermelon.common.systems.substance.SubstanceStack;
import net.minecraft.core.Holder;

import java.util.EnumMap;
import java.util.Set;

public record Reagent(
        int proportion,
        int order,
        EnumMap<Nature, Float> natureRequirements,
        Set<Holder<Substance>> substanceRequirements
) {
    boolean matches(SubstanceStack stack) {
        Substance substance = stack.getSubstance();

        for (Holder<Substance> holder : substanceRequirements) {
            if (holder.value().equals(substance)) return true;
        }

        EnumMap<Nature, Float> nature = substance.getNature();
        for (var entry : natureRequirements.entrySet()) {
            if (nature.getOrDefault(entry.getKey(), 0f) < entry.getValue()) return false;
        }

        return true;
    }

    public static class Builder {
        private int proportion = 1;
        private int order = 0;
        private final EnumMap<Nature, Float> natureRequirements = new EnumMap<>(Nature.class);
        private final Set<Holder<Substance>> substanceRequirements = new java.util.HashSet<>();

        public Builder proportion(int proportion) {
            this.proportion = proportion;
            return this;
        }

        public Builder order(int order) {
            this.order = order;
            return this;
        }

        public Builder addNatureRequirement(Nature nature, float amount) {
            natureRequirements.put(nature, amount);
            return this;
        }

        public Builder addSubstanceRequirement(Holder<Substance> substance) {
            substanceRequirements.add(substance);
            return this;
        }

        public Reagent build() {
            return new Reagent(proportion, order, natureRequirements, substanceRequirements);
        }
    }
}
