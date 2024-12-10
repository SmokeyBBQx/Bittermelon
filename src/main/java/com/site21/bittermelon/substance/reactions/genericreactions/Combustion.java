package com.site21.bittermelon.substance.reactions.genericreactions;

import com.site21.bittermelon.substance.Substance;
import com.site21.bittermelon.substance.reactions.Reaction;

import java.util.Map;

import static com.site21.bittermelon.init.Substances.*;

public class Combustion {
    private final Substance substance;

    public Combustion(Substance substance) {
        this.substance = substance;
    }

    public Reaction buildReaction() {
        Map<Substance, Integer> formula = substance.getFormula();
        int C = formula.getOrDefault(CARBON.get(), 0);
        int H = formula.getOrDefault(HYDROGEN.get(), 0);
        int O = formula.getOrDefault(OXYGEN.get(), 0);

        int oxygenNeeded = (2 * C + H / 2) - O;
        oxygenNeeded = (int) Math.ceil(oxygenNeeded / 2.0);
        int waterProportion = H / 2;

        return new Reaction.ReactionBuilder()
                .addReactant(substance, 1, 1)
                .addReactant(GASEOUS_OXYGEN.get(), oxygenNeeded, 1)
                .addProduct(GASEOUS_CARBON_DIOXIDE.get(), C)
                .addProduct(GASEOUS_WATER.get(), waterProportion)
                .build();
    }
}
