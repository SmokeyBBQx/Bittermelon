package com.site21.bittermelon.init.custom;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.medical.drugs.Drug;
import com.site21.bittermelon.content.medical.drugs.implementations.CyanideDrug;
import net.minecraft.core.Holder;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.DRUG_REGISTRY_KEY;

public class Drugs {
    public static final DeferredRegister<Drug> DRUGS = DeferredRegister.create(DRUG_REGISTRY_KEY, Bittermelon.MOD_ID);

    public static final DeferredHolder<Drug, CyanideDrug> CYANIDE = DRUGS.register("cyanide", CyanideDrug::new);
}
