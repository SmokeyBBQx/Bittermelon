package com.site21.bittermelon.common.systems.substance.reactions;

import com.site21.bittermelon.common.systems.substance.Nature;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.*;

public class ReactionManager {
    private static ReactionManager instance;
    private final List<Reaction> reactions = new ArrayList<>();
    private final EnumMap<Nature, Set<Reaction>> reactionGraph = new EnumMap<>(Nature.class);

    public static ReactionManager getInstance() {
        if (instance == null) instance = new ReactionManager();
        return instance;
    }

    public void register(Reaction reaction) {
        reactions.add(reaction);

        for (Reagent reagent : reaction.reagents()) {
            for (Nature nature : reagent.natureRequirements().keySet()) {
                reactionGraph.computeIfAbsent(nature, k -> new HashSet<>())
                        .add(reaction);
            }
        }

//        if (!isIndexed(reaction))
//            reactions.add(reaction);
    }

    private boolean isIndexed(Reaction reaction) {
        for (Reagent reagent : reaction.reagents()) {
            if (!reagent.natureRequirements().isEmpty()) return true;
            if (!reagent.substanceRequirements().isEmpty()) return true;
        }
        return false;
    }

    public void build(List<Reaction> reactions) {
        for (Reaction reaction : reactions) {
            for (Reagent reagent : reaction.reagents()) {
                for (var entry : reagent.natureRequirements().entrySet()) {
                    reactionGraph.computeIfAbsent(entry.getKey(), k -> new HashSet<>()).add(reaction);
                }
            }
        }
    }

    public List<Reaction> findMatch(Reactor reactor, Level level, BlockPos pos) {
        EnumSet<Nature> natures = reactor.getNatures();
        Map<Reaction, Integer> hits = new HashMap<>();

        for (Nature nature : natures) {
            Set<Reaction> indexed = reactionGraph.get(nature);
            if (indexed == null) continue;
            for (Reaction reaction : indexed) {
                hits.merge(reaction, 1, Integer::sum);
            }
        }

        List<Map.Entry<Reaction, Integer>> candidates = new ArrayList<>();
        for (Map.Entry<Reaction, Integer> entry : hits.entrySet()) {
            Reaction reaction = entry.getKey();
            int count = entry.getValue();

            if (count < getRequiredNatureCount(reaction)) continue;
            if (!reaction.canOccur(reactor, level, pos)) continue;

            candidates.add(entry);
        }

        candidates.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        List<Reaction> result = new ArrayList<>(candidates.size());
        for (Map.Entry<Reaction, Integer> entry : candidates) {
            result.add(entry.getKey());
        }

        return result;
    }

    private int getRequiredNatureCount(Reaction reaction) {
        Set<Nature> distinct = EnumSet.noneOf(Nature.class);
        for (Reagent reagent : reaction.reagents()) {
            distinct.addAll(reagent.natureRequirements().keySet());
        }
        return distinct.size();
    }
}
