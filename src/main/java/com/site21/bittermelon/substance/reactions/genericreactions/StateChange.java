package com.site21.bittermelon.substance.reactions.genericreactions;

import com.site21.bittermelon.substance.Substance;
import com.site21.bittermelon.substance.reactions.Reaction;

public class StateChange {
    private final Substance substance;
    private final Substance result;
    private final int temperature;

    public StateChange(Substance substance, Substance result, int temperature) {
        this.substance = substance;
        this.result = result;
        this.temperature = temperature;
    }

    public Reaction buildReaction() {
        return new Reaction.ReactionBuilder()
                .addReactant(substance, 1, 1)
                .addProduct(result, 1)
                .build();
    }
}
