package com.site21.bittermelon.init.custom;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.systems.chat.VerbSet;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.VERB_SET_REGISTRY_KEY;

public class VerbSets {
    public static final DeferredRegister<VerbSet> VERB_SETS = DeferredRegister.create(VERB_SET_REGISTRY_KEY, Bittermelon.MOD_ID);

    public static final Supplier<VerbSet> HUMAN = VERB_SETS.register("human", () -> new VerbSet(
            "says",
            "exclaims",
            "shouts",
            "asks",
            "whispers"
    ));
}
