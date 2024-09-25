package com.site21.bittermelon.init;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.substance.Substance;
import com.site21.bittermelon.substance.SubstanceProperties;
import com.site21.bittermelon.substance.substances.Atom;
import com.site21.bittermelon.substance.substances.Molecule;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.site21.bittermelon.init.ModRegistries.SUBSTANCE_REGISTRY_KEY;
import static com.site21.bittermelon.substance.reactions.Reactions.HABER_PROCESS;

public class SubstanceInit {
    public static final DeferredRegister<Substance> SUBSTANCES = DeferredRegister.create(SUBSTANCE_REGISTRY_KEY, Bittermelon.MOD_ID);

    // --- ATOMS ---

    public static final Supplier<Substance> REACTANT_A = SUBSTANCES.register("reactant_a", () -> new Atom(
            new SubstanceProperties.Builder("reactant_a")
                    .liquidColor(0x32a852)
                    .build(),
            1.008f, 70.85f, 12.5f
    ));

    public static final Supplier<Substance> REACTANT_B = SUBSTANCES.register("reactant_b", () -> new Atom(
            new SubstanceProperties.Builder("reactant_b")
                    .liquidColor(0x328ba8)
                    .build(),
            14.007f, 808, 12.5f
    ));

    public static final Supplier<Substance> PRODUCT = SUBSTANCES.register("product", () -> new Molecule(
            new SubstanceProperties.Builder("product")
                    .liquidColor(0xa8a432)
                    .build(),
            681.9f, 80f)
            .addFormula(REACTANT_A)
            .addFormula(REACTANT_B, 3)
    );

    public static final Supplier<Substance> HYDROGEN = SUBSTANCES.register("hydrogen", () -> new Atom(
            new SubstanceProperties.Builder("Hydrogen")
                    .solidColor(0xFFFFFF)
                    .liquidColor(0xE6E6E6)
                    .gasColor(0xF2F2F2)
                    .plasmaColor(0xFFA500)
                    .flavor("Odorless")
                    .freezingTemperature(-259.14f)
                    .boilingTemperature(-252.87f)
                    .plasmaTemperature(10000f)
                    .isGasVisible(false)
                    .build(),
            1.008f, 70.85f, 12.5f));

    //    public static final Supplier<Substance> HELIUM = SUBSTANCES.register("helium", () -> new Atom(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> CARBON = SUBSTANCES.register("carbon", () -> new Atom(new Substance.SubstanceProperties()));
    public static final Supplier<Substance> NITROGEN = SUBSTANCES.register("nitrogen", () -> new Atom(
            new SubstanceProperties.Builder("Nitrogen")
                    .solidColor(0xFFFFFF)
                    .liquidColor(0xE6E6E6)
                    .gasColor(0xF2F2F2)
                    .plasmaColor(0xFFA500)
                    .flavor("Odorless")
                    .freezingTemperature(-210f)
                    .boilingTemperature(-195.8f)
                    .plasmaTemperature(10000f)
                    .isGasVisible(false)
                    .build(),
            14.007f, 808, 12.5f));

    public static final Supplier<Substance> OXYGEN = SUBSTANCES.register("oxygen", () -> new Atom(
            new SubstanceProperties.Builder("Oxygen")
                    .solidColor(0xFFFFFF)
                    .liquidColor(0xE6E6E6)
                    .gasColor(0xF2F2F2)
                    .plasmaColor(0xFFA500)
                    .flavor("Odorless")
                    .freezingTemperature(-218.79f)
                    .boilingTemperature(-182.96f)
                    .plasmaTemperature(10000f)
                    .isGasVisible(false)
                    .build(),
            15.999f, 1141, 12.5f));

    //    public static final Supplier<Substance> FLUORINE = SUBSTANCES.register("fluorine", () -> new Atom(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> SODIUM = SUBSTANCES.register("sodium", () -> new Atom(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> MAGNESIUM = SUBSTANCES.register("magnesium", () -> new Atom(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> ALUMINUM = SUBSTANCES.register("aluminum", () -> new Atom(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> SILICON = SUBSTANCES.register("silicon", () -> new Atom(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> PHOSPHORUS = SUBSTANCES.register("phosphorus", () -> new Atom(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> SULFUR = SUBSTANCES.register("sulfur", () -> new Atom(new Substance.SubstanceProperties()));
    public static final Supplier<Substance> CHLORINE = SUBSTANCES.register("chlorine", () -> new Atom(
            new SubstanceProperties.Builder("Chlorine")
                    .solidColor(0xFFFF00)
                    .liquidColor(0xFFFF00)
                    .gasColor(0xFFFF00)
                    .plasmaColor(0xFFA500)
                    .flavor("Pungent")
                    .freezingTemperature(-101.5f)
                    .boilingTemperature(-34.04f)
                    .plasmaTemperature(10000f)
                    .isGasVisible(true)
                    .build(),
            35.45f, 1409, 75));
//    public static final Supplier<Substance> POTASSIUM = SUBSTANCES.register("potassium", () -> new Atom(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> CALCIUM = SUBSTANCES.register("calcium", () -> new Atom(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> IRON = SUBSTANCES.register("iron", () -> new Atom(new Substance.SubstanceProperties()));

    // --- MOLECULES ---

    public static final Supplier<Substance> WATER = SUBSTANCES.register("water", () -> new Molecule(
            new SubstanceProperties.Builder("Water")
                    .solidColor(0xFFFFFF)
                    .liquidColor(0x0000FF)
                    .gasColor(0xE6F3FF)
                    .plasmaColor(0x00FFFF)
                    .flavor("Tasteless")
                    .freezingTemperature(0f)
                    .boilingTemperature(100f)
                    .plasmaTemperature(10000f)
                    .isGasVisible(true)
                    .build(),
            1000, 75.3f)
            .addFormula(HYDROGEN, 2)
            .addFormula(OXYGEN));

    public static final Supplier<Substance> CARBON_DIOXIDE = SUBSTANCES.register("carbon_dioxide", () -> new Molecule(
            new SubstanceProperties.Builder("Carbon Dioxide")
                    .solidColor(0xFFFFFF)
                    .liquidColor(0xE6E6E6)
                    .gasColor(0xF2F2F2)
                    .plasmaColor(0xFFA500)
                    .flavor("Odorless")
                    .freezingTemperature(-78.5f)
                    .boilingTemperature(-56.6f)
                    .plasmaTemperature(10000f)
                    .isGasVisible(false)
                    .build(),
            914, 37.1f));

    public static final Supplier<Substance> METHANE = SUBSTANCES.register("methane", () -> new Molecule(
            new SubstanceProperties.Builder("Methane")
                    .solidColor(0xFFFFFF)
                    .liquidColor(0xE6E6E6)
                    .gasColor(0xF2F2F2)
                    .plasmaColor(0xFFA500)
                    .flavor("Odorless")
                    .freezingTemperature(-182.5f)
                    .boilingTemperature(-161.5f)
                    .plasmaTemperature(10000f)
                    .isGasVisible(false)
                    .build(),
            422, 35.7f));

    public static final Supplier<Substance> AMMONIA = SUBSTANCES.register("ammonia", () -> new Molecule(
            new SubstanceProperties.Builder("Ammonia")
                    .solidColor(0xFFFFFF)
                    .liquidColor(0xE6E6E6)
                    .gasColor(0xF2F2F2)
                    .plasmaColor(0xFFA500)
                    .flavor("Pungent")
                    .freezingTemperature(-77.73f)
                    .boilingTemperature(-33.34f)
                    .plasmaTemperature(10000f)
                    .isGasVisible(true)
                    .build(),
            681.9f, 80f)
            .addFormula(NITROGEN)
            .addFormula(HYDROGEN, 3));
//    public static final Supplier<Substance> GLUCOSE = SUBSTANCES.register("glucose", () -> new Molecule(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> ETHANOL = SUBSTANCES.register("ethanol", () -> new Molecule(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> ACETONE = SUBSTANCES.register("acetone", () -> new Molecule(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> GLYCEROL = SUBSTANCES.register("glycerol", () -> new Molecule(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> ACETIC_ACID = SUBSTANCES.register("acetic_acid", () -> new Molecule(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> UREA = SUBSTANCES.register("urea", () -> new Molecule(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> HYDROGEN_PEROXIDE = SUBSTANCES.register("hydrogen_peroxide", () -> new Molecule(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> METHANOL = SUBSTANCES.register("methanol", () -> new Molecule(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> BENZENE = SUBSTANCES.register("benzene", () -> new Molecule(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> CAFFEINE = SUBSTANCES.register("caffeine", () -> new Molecule(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> SUCROSE = SUBSTANCES.register("sucrose", () -> new Molecule(new Substance.SubstanceProperties()));

    // --- COMPLEX MIXTURES ---
//    public static final Supplier<Substance> STARCH = SUBSTANCES.register("starch", () -> new ComplexMixture(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> CELLULOSE = SUBSTANCES.register("cellulose", () -> new ComplexMixture(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> VEGETABLE_OIL = SUBSTANCES.register("vegetable_oil", () -> new ComplexMixture(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> HEMOGLOBIN_BLOOD = SUBSTANCES.register("hemoglobin_blood", () -> new ComplexMixture(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> CHLOROPHYLL = SUBSTANCES.register("chlorophyll", () -> new ComplexMixture(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> PROTEIN = SUBSTANCES.register("protein", () -> new ComplexMixture(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> DNA = SUBSTANCES.register("dna", () -> new ComplexMixture(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> RNA = SUBSTANCES.register("rna", () -> new ComplexMixture(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> ENZYME = SUBSTANCES.register("enzyme", () -> new ComplexMixture(new Substance.SubstanceProperties()));
//    public static final Supplier<Substance> COLLAGEN = SUBSTANCES.register("collagen", () -> new ComplexMixture(new Substance.SubstanceProperties()));

}
