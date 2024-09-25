package com.site21.bittermelon.substance.reactions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.site21.bittermelon.init.SubstanceInit.*;

public class Reactions {
    private static final List<Reaction> ALL_REACTIONS = new ArrayList<>();
    public static Reaction METHANE_COMBUSTION;
    public static Reaction HABER_PROCESS;
    public static Reaction TEST_REACTION;

    public static void initReactions() {
        METHANE_COMBUSTION = new Reaction.ReactionBuilder()
                .addReactant(METHANE.get(), 1, 1)
                .addReactant(OXYGEN.get(), 2, 1)
                .addProduct(CARBON_DIOXIDE.get(), 1)
                .addProduct(WATER.get(), 2)
                .activationEnergy(201.2f)
                .preExponentialFactor(5.1e11f)
                .enthalpyChange(-890.3f)
                .build();

        HABER_PROCESS = new Reaction.ReactionBuilder()
                .addReactant(NITROGEN.get(), 1, 1)
                .addReactant(HYDROGEN.get(), 3, 1)
                .addProduct(AMMONIA.get(), 2)
                .activationEnergy(230.0f)
                .preExponentialFactor(2.4e9f)
                .enthalpyChange(-92.4f)
                .build();

        TEST_REACTION = new Reaction.ReactionBuilder()
                .addReactant(REACTANT_A.get(), 1, 1)
                .addReactant(REACTANT_B.get(), 3, 1)
                .addProduct(PRODUCT.get(), 2)
                .activationEnergy(230.0f)
                .preExponentialFactor(2.4e9f)
                .enthalpyChange(-92.4f)
                .build();

        for (Field field : Reactions.class.getDeclaredFields()) {
            if (Reaction.class.isAssignableFrom(field.getType())) {
                try {
                    Reaction reaction = (Reaction) field.get(null);
                    ALL_REACTIONS.add(reaction);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<Reaction> getAllReactions() {
        return new ArrayList<>(ALL_REACTIONS);
    }
}
