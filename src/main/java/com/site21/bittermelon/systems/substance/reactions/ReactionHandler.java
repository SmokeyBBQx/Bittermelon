package com.site21.bittermelon.systems.substance.reactions;

import com.site21.bittermelon.systems.substance.Substance;
import com.site21.bittermelon.systems.substance.SubstanceStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ReactionHandler {
    private static ReactionHandler instance;
    private final Map<Substance, Set<Reaction>> reactionsByReactant = new HashMap<>();

    private ReactionHandler() {
        initializeReactionCache();
    }

    public static ReactionHandler getInstance() {
        if (instance == null) {
            instance = new ReactionHandler();
        }
        return instance;
    }

    private void initializeReactionCache() {
        for (Reaction reaction : Reactions.getAllReactions()) {
            if (reaction != null) {
                for (Substance reactant : reaction.getReactants().keySet()) {
                    reactionsByReactant.computeIfAbsent(reactant, k -> new HashSet<>()).add(reaction);
                }
            }
        }
    }

    public void handleReactions(List<SubstanceStack> mixture, ReactionContainer container) {
        Set<Reaction> potentialReactions = getPotentialReactions(mixture);

        for (Reaction reaction : potentialReactions) {
            executeReaction(reaction, mixture, container);
        }

        // TODO: Store active reactions?
    }


    private @NotNull Set<Reaction> getPotentialReactions(@NotNull List<SubstanceStack> mixture) {
        Set<Reaction> potentialReactions = new HashSet<>();

        for (SubstanceStack stack : mixture) {
            Set<Reaction> reactions = reactionsByReactant.get(stack.getSubstance());
            if (reactions != null) {
                potentialReactions.addAll(reactions);
            }
        }

        return potentialReactions;
    }

    private void executeReaction(Reaction reaction, @NotNull List<SubstanceStack> mixture, ReactionContainer container) {
        List<SubstanceStack> relatedStacks = mixture.stream()
                .filter(stack -> reaction.getReactants().containsKey(stack.getSubstance()))
                .toList();

        handleReaction(reaction, relatedStacks, getConcentrations(relatedStacks), container);
    }

    private @NotNull Map<Substance, Float> getConcentrations(@NotNull List<SubstanceStack> mixture) {
        Map<Substance, Float> concentrations = new HashMap<>();
        for (SubstanceStack stack : mixture) {
            concentrations.put(stack.getSubstance(), stack.getAmount());
        }
        return concentrations;
    }

    public void handleReaction(@NotNull Reaction reaction, @NotNull List<SubstanceStack> mixture, Map<Substance, Float> concentrations, @NotNull ReactionContainer container) {
        double reactionRate = reaction.calculateReactionRate(concentrations, container.getTemperature());

        // Calculate the limiting factor based on available reactants
        double limitingFactor = Double.MAX_VALUE;
        for (SubstanceStack stack : mixture) {
            int proportion = reaction.getReactantProportion(stack.getSubstance());
            if (proportion > 0) {
                limitingFactor = Math.min(limitingFactor, stack.getAmount() / proportion);
            }
        }

        // Limit the reaction rate to the available reactants
        reactionRate = Math.min(reactionRate, limitingFactor);

        // Consume reactants
        for (SubstanceStack stack : mixture) {
            int proportion = reaction.getReactantProportion(stack.getSubstance());
            stack.modifyAmount((float) (-proportion * reactionRate));
        }

        // Produce products
        for (Map.Entry<Substance, Integer> entry : reaction.getProducts().entrySet()) {
            Substance product = entry.getKey();
            int proportion = entry.getValue();

            SubstanceStack productStack = new SubstanceStack(product, (float) (proportion * reactionRate));

            if (productStack.getAmount() > 0) {
                container.updateSubstance(productStack);
            }
        }

        updateContainerTemperature(reaction, container);
    }

    private void updateContainerTemperature(@NotNull Reaction reaction, @NotNull ReactionContainer container) {
//        float enthalpyChange = reaction.getEnthalpyChange();
//        float containerHeatCapacity = container.getHeatCapacity();
//        float temperatureChange = enthalpyChange / containerHeatCapacity;
//        container.modifyTemperature(temperatureChange);
    }
}
