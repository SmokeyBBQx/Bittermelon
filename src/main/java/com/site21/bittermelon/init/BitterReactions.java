package com.site21.bittermelon.init;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.substance.reactions.Reaction;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.site21.bittermelon.init.BitterRegistries.REACTION_REGISTRY_KEY;

public class BitterReactions {
    public static final DeferredRegister<Reaction> REACTIONS = DeferredRegister.create(REACTION_REGISTRY_KEY, Bittermelon.MOD_ID);

//    public static final Supplier<Reaction> HABER_PROCESS = REACTIONS.register("haber_process",
//            () -> new Reaction.ReactionBuilder()
//                    .addReactant(NITROGEN.get(), 1, 1)
//                    .addReactant(HYDROGEN.get(), 3, 1)
//                    .addProduct(AMMONIA.get(), 2)
//                    .activationEnergy(230.0f)
//                    .preExponentialFactor(2.4e9f)
//                    .enthalpyChange(-92.4f)
//                    .build());

}
