package com.site21.bittermelon.init.custom;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.anatomies.AnimalAnatomy;
import com.site21.bittermelon.common.systems.medical.legacy.anatomy.Anatomy;
import com.site21.bittermelon.common.systems.medical.legacy.anatomy.factory.HumanFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.ANATOMY_REGISTRY_KEY;

public class Anatomies {
    public static final DeferredRegister<Anatomy> ANATOMIES = DeferredRegister.create(ANATOMY_REGISTRY_KEY, Bittermelon.MOD_ID);

    public static final DeferredHolder<Anatomy, Anatomy> HUMAN = ANATOMIES.register("human",
            () -> new AnimalAnatomy(new HumanFactory())
    );
}
