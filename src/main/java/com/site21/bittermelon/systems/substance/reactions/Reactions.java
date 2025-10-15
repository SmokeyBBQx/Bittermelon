package com.site21.bittermelon.systems.substance.reactions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.site21.bittermelon.init.custom.Substances.*;

public class Reactions {
    private static final List<Reaction> ALL_REACTIONS = new ArrayList<>();

    public static final Reaction HYDROGEN_PEROXIDE_DECOMPOSITION = new  Reaction.ReactionBuilder()
            .addReactant(HYDROGEN_PEROXIDE.get(), 2, 1)
            .addProduct(WATER.get(), 2)
            .addProduct(OXYGEN.get(), 1)
            .activationEnergy(75.3f)
            .preExponentialFactor(2.9e10f)
            .enthalpyChange(-98.2f)
            .build();

    public static void initReactions() {

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
