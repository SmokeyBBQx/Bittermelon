package com.site21.bittermelon.common.systems.chemistry;

import com.site21.bittermelon.common.systems.substance.Nature;
import com.site21.bittermelon.common.systems.substance.Substance;
import com.site21.bittermelon.common.systems.substance.SubstanceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;

import java.util.*;

public class ReactionManager {
    private static ReactionManager instance;
    private final List<Reaction> reactions = new ArrayList<>();
    private final EnumMap<Nature, Set<Reaction>> natureToReactions = new EnumMap<>(Nature.class);
    private final Map<Substance, Set<Reaction>> substanceToReactions = new HashMap<>();

    public static ReactionManager getInstance() {
        if (instance == null) instance = new ReactionManager();
        return instance;
    }

    public void register(Reaction reaction) {
        reactions.add(reaction);

        for (Reagent reagent : reaction.reagents()) {
            for (var entry : reagent.natureRequirements().entrySet()) {
                natureToReactions
                        .computeIfAbsent(entry.getKey(), k -> new HashSet<>())
                        .add(reaction);
            }

            for (Holder<Substance> holder : reagent.substanceRequirements()) {
                substanceToReactions
                        .computeIfAbsent(holder.value(), k -> new HashSet<>())
                        .add(reaction);
            }
        }
    }

    public void clear() {
        reactions.clear();
        natureToReactions.clear();
        substanceToReactions.clear();
    }

    public Set<Reaction> findMatch(Reactor reactor, Level level, BlockPos pos) {
        Set<Reaction> candidates = new HashSet<>();

        for (Nature nature : reactor.getNatures()) {
            Set<Reaction> indexed = natureToReactions.get(nature);
            if (indexed != null) candidates.addAll(indexed);
        }

        for (SubstanceStack stack : reactor.getSubstances()) {
            Set<Reaction> indexed = substanceToReactions.get(stack.getSubstance());
            if (indexed != null) candidates.addAll(indexed);
        }

        Set<Reaction> result = new HashSet<>();
        for (Reaction reaction : candidates) {
            if (reaction.canOccur(reactor, level, pos)) result.add(reaction);
        }

        return result;
    }
}
