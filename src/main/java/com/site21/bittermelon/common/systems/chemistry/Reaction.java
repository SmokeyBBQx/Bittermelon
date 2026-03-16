package com.site21.bittermelon.common.systems.chemistry;

import com.site21.bittermelon.common.systems.substance.SubstanceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record Reaction(
        List<Reagent> reagents,
        List<ReactionCondition> conditions,
        int minTemperature,
        int temperatureChange,
        List<ReactionEffect> effects
) {
    public boolean canOccur(Reactor reactor, Level level, BlockPos pos) {
        for (ReactionCondition condition : conditions) {
            if (!condition.test(reactor, level, pos)) {
                return false;
            }
        }

        int reagentMatches = 0;
        for (SubstanceStack stack : reactor.getSubstances()) {
            for (Reagent reagent : reagents) {
                if (reagent.matches(stack)) {
                    reagentMatches++;
                    break;
                }
            }
        }

        return reagentMatches == reagents.size();
    }

    public int getReactionRate(Reactor reactor, Level level, BlockPos pos) {
        return 2;
    }

    public boolean react(Reactor reactor, Level level, BlockPos pos) {
        float proportion = 1;
        int rate = getReactionRate(reactor, level, pos);
        Map<SubstanceStack, Reagent> reactants = new HashMap<>();

        for (Reagent reagent : reagents) {
            boolean found = false;
            for (SubstanceStack stack : reactor.getSubstances()) {
                if (reagent.matches(stack)) {
                    reactants.put(stack, reagent);
                    if (reagent.proportion() > 0) {
                        proportion = Math.min(proportion, (float) stack.getAmount() / (reagent.proportion() * rate));
                    }
                    found = true;
                    break;
                }
            }

            if (!found) return false;
        }

        if (proportion <= 0) return false;

        int scaledAmount = (int) (proportion * rate);

        for (var entry : reactants.entrySet()) {
            reactor.removeSubstance(entry.getKey(), scaledAmount * entry.getValue().proportion());
        }

        for (ReactionEffect effect : effects) {
            effect.apply(reactor, level, pos, scaledAmount);
        }

        return true;
    }
}
