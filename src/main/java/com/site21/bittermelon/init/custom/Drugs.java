package com.site21.bittermelon.init.custom;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.medical.compartment.MedicalAttribute;
import com.site21.bittermelon.common.systems.medical.drug.Drug;
import com.site21.bittermelon.common.content.drugs.CyanideDrug;
import com.site21.bittermelon.common.content.drugs.SpaceMirageDrug;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.DRUG_REGISTRY_KEY;

public class Drugs {
    public static final DeferredRegister<Drug> DRUGS = DeferredRegister.create(DRUG_REGISTRY_KEY, Bittermelon.MOD_ID);

    public static final DeferredHolder<Drug, CyanideDrug> CYANIDE = DRUGS.register("cyanide", CyanideDrug::new);
    public static final DeferredHolder<Drug, Drug> SPACE_MIRAGE = DRUGS.register("space_mirage",
            () -> new SpaceMirageDrug(0.002f, 0.005f).setAttribute(MedicalAttribute.RESPIRATION, 0.9f));
}
