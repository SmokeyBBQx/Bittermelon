package com.site21.bittermelon.init.custom;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.substance.Substance;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.SUBSTANCE_REGISTRY_KEY;

public class Substances {
    public static final DeferredRegister<Substance> SUBSTANCES = DeferredRegister.create(SUBSTANCE_REGISTRY_KEY, Bittermelon.MOD_ID);

    // --- ATOMS ---
    public static final Supplier<Substance> GASEOUS_OXYGEN = SUBSTANCES.register("gaseous_oxygen", () -> new Substance(
            new Substance.Properties(),
            "Gaseous Oxygen",
            1,
            32,
            1.429f,
            29.378f
    ));

    public static final Supplier<Substance> CARBON = SUBSTANCES.register("carbon", () -> new Substance(
            new Substance.Properties(),
            "Carbon",
            1,
            32,
            1.429f,
            29.378f
    ));

    public static final Supplier<Substance> OXYGEN = SUBSTANCES.register("oxygen", () -> new Substance(
            new Substance.Properties(),
            "Carbon",
            1,
            32,
            1.429f,
            29.378f
    ));

    public static final Supplier<Substance> HYDROGEN = SUBSTANCES.register("hydrogen", () -> new Substance(
            new Substance.Properties(),
            "Carbon",
            1,
            32,
            1.429f,
            29.378f
    ));

    public static final Supplier<Substance> NITROGEN = SUBSTANCES.register("nitrogen", () -> new Substance(
            new Substance.Properties(),
            "Carbon",
            1,
            32,
            1.429f,
            29.378f
    ));

    public static final Supplier<Substance> GASEOUS_NITROGEN = SUBSTANCES.register("gaseous_nitrogen", () -> new Substance(
            new Substance.Properties(),
            "Carbon",
            1,
            32,
            1.429f,
            29.378f
    ));

    public static final Supplier<Substance> GASEOUS_HYDROGEN = SUBSTANCES.register("gaseous_hydrogen", () -> new Substance(
            new Substance.Properties(),
            "Oxygen",
            1,
            32,
            1.429f,
            29.378f
    ));

    public static final Supplier<Substance> GASEOUS_CARBON_DIOXIDE = SUBSTANCES.register("gaseous_carbon_dioxide", () -> new Substance(
            new Substance.Properties(),
            "Oxygen",
            1,
            32,
            1.429f,
            29.378f
    ));

    public static final Supplier<Substance> GASEOUS_WATER = SUBSTANCES.register("gaseous_water", () -> new Substance(
            new Substance.Properties(),
            "Oxygen",
            1,
            32,
            1.429f,
            29.378f
    ));

    public static final Supplier<Substance> LIQUID_WATER = SUBSTANCES.register("liquid_water", () -> new Substance(
            new Substance.Properties()
                    .slipperiness(0.2f),
            "Liquid Water",
            0xFFAAD5DB,
            32,
            1.429f,
            29.378f
            )
    );

    public static final Supplier<Substance> SOLID_SODIUM_CHLORIDE = SUBSTANCES.register("solid_sodium_chloride", () -> new Substance(
            new Substance.Properties(),
            "Salt",
            1,
            58.443f,
            2.17f,
            50.5f
    ));

    public static final Supplier<Substance> LIQUID_HYDROGEN_PEROXIDE = SUBSTANCES.register("liquid_hydrogen_peroxide", () -> new Substance(
            new Substance.Properties(),
            "Liquid Hydrogen Peroxide",
            1,
            34.014f,
            1.11f,
            2.619f
    ));

    public static final Supplier<Substance> SOLID_UREA = SUBSTANCES.register("solid_urea", () -> new Substance(
            new Substance.Properties(),
            "Urea",
            1,
            60.06f,
            1.32f,
            2.619f
    ));

    public static final Supplier<Substance> GASEOUS_AMMONIA = SUBSTANCES.register("gaseous_ammonia", () -> new Substance(
            new Substance.Properties(),
            "Ammonia",
            1,
            17.031f,
            0.769f,
            2.619f
    ));

    public static final Supplier<Substance> LIQUID_BLOOD = SUBSTANCES.register("liquid_blood", () -> new Substance(
            new Substance.Properties(),
            "Liquid Blood",
            0xFFB52222,
            32,
            1.10f,
            2.619f
    ));
}
