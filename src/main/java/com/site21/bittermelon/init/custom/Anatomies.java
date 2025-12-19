package com.site21.bittermelon.init.custom;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.anatomies.HumanAnatomy;
import com.site21.bittermelon.common.systems.medical.Anatomy;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.ANATOMY_REGISTRY_KEY;

public class Anatomies {
    public static final DeferredRegister<Anatomy> ANATOMIES = DeferredRegister.create(ANATOMY_REGISTRY_KEY, Bittermelon.MOD_ID);

    public static final DeferredHolder<Anatomy, Anatomy> HUMAN_ANATOMY = ANATOMIES.register("human",
            HumanAnatomy::new
    );
}
