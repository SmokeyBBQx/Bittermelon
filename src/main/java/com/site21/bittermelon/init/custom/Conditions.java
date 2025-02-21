package com.site21.bittermelon.init.custom;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.substance.Substance;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.CONDITIONS_REGISTRY_KEY;
import static com.site21.bittermelon.init.neoforge.BitterRegistries.SUBSTANCE_REGISTRY_KEY;

public class Conditions {
    public static final DeferredRegister<Function<?, ?>> CONDITIONS = DeferredRegister.create(CONDITIONS_REGISTRY_KEY, Bittermelon.MOD_ID);

}
