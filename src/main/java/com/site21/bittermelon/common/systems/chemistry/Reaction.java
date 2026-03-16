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

        for (Reagent reagent : reagents) {
            boolean found = false;
            for (SubstanceStack stack : reactor.getSubstances()) {
                if (reagent.matches(stack)) {
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }

        return true;
    }

    public int getReactionRate(Reactor reactor, Level level, BlockPos pos) {
        return 1000;
    }

    public boolean react(Reactor reactor, Level level, BlockPos pos) {
        Map<SubstanceStack, Reagent> reactants = new HashMap<>();
        int scaledAmount = getReactionRate(reactor, level, pos);

        for (Reagent reagent : reagents) {
            boolean found = false;
            for (SubstanceStack stack : reactor.getSubstances()) {
                if (reagent.matches(stack)) {
                    reactants.put(stack, reagent);
                    if (reagent.proportion() > 0) {
                        scaledAmount = Math.min(scaledAmount, stack.getAmount() / reagent.proportion());
                    }
                    found = true;
                    break;
                }
            }

            if (!found) return false;
        }

        if (scaledAmount <= 0) return false;

        for (var entry : reactants.entrySet()) {
            reactor.removeSubstanceNoUpdate(entry.getKey(), scaledAmount * entry.getValue().proportion());
        }

        for (ReactionEffect effect : effects) {
            effect.apply(reactor, level, pos, scaledAmount);
        }

        reactor.setChanged();
        return true;
    }
}
