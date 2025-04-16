package com.site21.bittermelon.init.custom;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.medical.compartments.Compartment;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.FunctionType;
import com.site21.bittermelon.content.medical.compartments.MajorBodyPart;
import com.site21.bittermelon.content.medical.compartments.conditions.Bleed;
import com.site21.bittermelon.content.medical.compartments.firstaid.Retractor;
import com.site21.bittermelon.content.substance.Substance;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumSet;
import java.util.function.Supplier;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.COMPARTMENT_REGISTRY_KEY;
import static com.site21.bittermelon.init.neoforge.BitterRegistries.SUBSTANCE_REGISTRY_KEY;

public class Compartments {
    public static final DeferredRegister<Compartment> COMPARTMENTS = DeferredRegister.create(COMPARTMENT_REGISTRY_KEY, Bittermelon.MOD_ID);

    public static final Supplier<Compartment> BLEED = COMPARTMENTS.register("bleed", () -> new Bleed("bleed", EnumSet.of(
            CompartmentTag.BLEED,
            CompartmentTag.CONDITION
    )));

    public static final Supplier<Compartment> TRAUMATIC_AMPUTATION = COMPARTMENTS.register("traumatic_amputation", () -> new Compartment("traumatic_amputation", EnumSet.of(
            CompartmentTag.TRAUMATIC_AMPUTATION,
            CompartmentTag.CONDITION,
            CompartmentTag.INJURY
    )));

    public static final Supplier<Compartment> INJURY = COMPARTMENTS.register("injury", () -> new Compartment(
            "injury",
            EnumSet.of(CompartmentTag.CONDITION, CompartmentTag.INJURY),
            compartmentInstance -> {
                compartmentInstance.setAttribute(FunctionType.HEALTH, -compartmentInstance.getMaxHealth());
                compartmentInstance.setAttribute(FunctionType.PAIN, compartmentInstance.getMaxHealth());
            }
    ));

    public static final Supplier<Compartment> PAIN = COMPARTMENTS.register("pain", () -> new Compartment(
            "pain",
            EnumSet.of(CompartmentTag.CONDITION, CompartmentTag.PAIN),
            compartmentInstance -> {
                compartmentInstance.setAttribute(FunctionType.FUNCTION, 0.5f);
                compartmentInstance.setAttribute(FunctionType.PAIN, compartmentInstance.getMaxHealth());
            }
    ));

    public static final Supplier<Compartment> BANDAGE = COMPARTMENTS.register("bandage", () -> new Compartment("bandage", EnumSet.of(
            CompartmentTag.FIRST_AID,
            CompartmentTag.BANDAGE
    )));

    public static final Supplier<Compartment> TOOL = COMPARTMENTS.register("tool", () -> new Compartment("tool", EnumSet.of(
            CompartmentTag.FIRST_AID
    )));

    public static final Supplier<Compartment> RETRACTOR = COMPARTMENTS.register("retractor", () -> new Retractor("retractor", EnumSet.of(
            CompartmentTag.FIRST_AID
    )));

    public static final Supplier<Compartment> MAJOR_BODY_PART = COMPARTMENTS.register("major_body_part", () -> new MajorBodyPart("major_body_part", EnumSet.of(
            CompartmentTag.MAJOR_BODY_PART
    )));

    public static final Supplier<Compartment> SOFT_TISSUE = COMPARTMENTS.register("soft_tissue", () -> new Compartment("soft_tissue", EnumSet.of(
            CompartmentTag.BODY_PART,
            CompartmentTag.SOFT_TISSUE
    )));

    public static final Supplier<Compartment> HARD_TISSUE = COMPARTMENTS.register("hard_tissue", () -> new Compartment("hard_tissue", EnumSet.of(
            CompartmentTag.BODY_PART,
            CompartmentTag.HARD_TISSUE
    )));
}
