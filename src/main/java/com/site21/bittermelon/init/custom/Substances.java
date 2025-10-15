package com.site21.bittermelon.init.custom;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.systems.substance.Substance;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.site21.bittermelon.init.custom.Drugs.CYANIDE;
import static com.site21.bittermelon.init.neoforge.BitterRegistries.SUBSTANCE_REGISTRY_KEY;

public class Substances {
    public static final DeferredRegister<Substance> SUBSTANCES = DeferredRegister.create(SUBSTANCE_REGISTRY_KEY, Bittermelon.MOD_ID);

    public static final Supplier<Substance> OXYGEN = SUBSTANCES.register("oxygen", () -> new Substance(
            "Oxygen",
            new Substance.Properties()
    ));

    public static final Supplier<Substance> WATER = SUBSTANCES.register("water", () -> new Substance(
            "Water",
            new Substance.Properties()
    ));


    public static final Supplier<Substance> HYDROGEN_PEROXIDE = SUBSTANCES.register("hydrogen_peroxide", () -> new Substance(
            "Hydrogen Peroxide",
            new Substance.Properties()
    ));

    public static final Supplier<Substance> BLOOD = SUBSTANCES.register("blood", () -> new Substance(
            "Blood",
            new Substance.Properties()
                    .color(0xFFB52222)
    ));

    public static final Supplier<Substance> URINE = SUBSTANCES.register("urine", () -> new Substance(
            "Urine",
            new Substance.Properties()
                    .color(0xFFFFDC65)
    ));

    public static final Supplier<Substance> VOMIT = SUBSTANCES.register("vomit", () -> new Substance(
            "Vomit",
            new Substance.Properties()
                    .color(0xFFCFBD8B)
    ));

    public static final Supplier<Substance> SODIUM_CYANIDE = SUBSTANCES.register("sodium_cyanide", () -> new Substance(
            "Sodium Cyanide",
            new Substance.Properties()
                    .color(0xFFFFFFFF)
                    .drug(CYANIDE)
                    .smell("almondy")
                    .flavor("almondy")
    ));

    public static final Supplier<Substance> HYDROGEN_CYANIDE = SUBSTANCES.register("hydrogen_cyanide", () -> new Substance(
            "Hydrogen Cyanide",
            new Substance.Properties()
                    .color(0xFFFFFFFF)
                    .drug(CYANIDE)
                    .smell("almondy")
                    .flavor("bitter")
    ));

    public static final Supplier<Substance> MOTOR_OIL = SUBSTANCES.register("motor_oil", () -> new Substance(
            "Motor Oil",
            new Substance.Properties()
                    .density(0.93f)
                    .molarMass(880)
                    .color(0xFFE3B324)
                    .slipperiness(0.5f)
                    .flavor("oily")
    ));

    public static final Supplier<Substance> SPACE_MIRAGE = SUBSTANCES.register("space_mirage", () -> new Substance(
            "Space Mirage",
            new Substance.Properties()
                    .density(0.93f)
                    .molarMass(880)
                    .color(0xFFB1A5C2)
                    .slipperiness(0.5f)
                    .drug(Drugs.SPACE_MIRAGE)
                    .flavor("oily")
    ));
}
