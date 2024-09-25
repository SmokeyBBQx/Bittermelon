package com.site21.bittermelon.substance.reactions;

import com.site21.bittermelon.substance.Substance;
import com.site21.bittermelon.substance.SubstanceStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.site21.bittermelon.substance.reactions.Reactions.getAllReactions;

public class ReactionHandler {
    private static ReactionHandler instance;

    private ReactionHandler() {
    }

    public static ReactionHandler getInstance() {
        if (instance == null) {
            instance = new ReactionHandler();
        }
        return instance;
    }

    public void handleReactions(List<SubstanceStack> mixture, ReactionContainer container) {
        for (Reaction reaction : getAllReactions()) {
            checkForReaction(reaction, mixture, container);
        }

        // TODO: Store reactions?
    }

    private void checkForReaction(Reaction reaction, List<SubstanceStack> mixture, ReactionContainer container) {
        Map<Substance, Integer> reactants = reaction.getReactants();
        List<SubstanceStack> relatedStacks = new ArrayList<>();
        Map<Substance, Float> concentrations = new HashMap<>();

        for (SubstanceStack stack : mixture) {
            if (reactants.containsKey(stack.getSubstance())) {
                if (stack.getAmount() > reactants.get(stack.getSubstance())) {
                    relatedStacks.add(stack);
                    concentrations.put(stack.getSubstance(), stack.getAmount());
                }
            }
        }

        if (reactants.keySet().containsAll(concentrations.keySet())) {
            handleReaction(reaction, relatedStacks, concentrations, container);
        }
    }

    public void handleReaction(Reaction reaction, List<SubstanceStack> mixture, Map<Substance, Float> concentrations, ReactionContainer container) {
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

    private void updateContainerTemperature(Reaction reaction, ReactionContainer container) {
        float enthalpyChange = reaction.getEnthalpyChange();
        float containerHeatCapacity = container.getHeatCapacity();
        float temperatureChange = enthalpyChange / containerHeatCapacity;
        container.modifyTemperature(temperatureChange);
    }
}
