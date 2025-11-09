package com.site21.bittermelon.common.systems.substance;

import com.site21.bittermelon.common.systems.medical.drugs.Drug;
import com.site21.bittermelon.common.systems.medical.drugs.DrugHelper;
import com.site21.bittermelon.common.systems.medical.drugs.DrugInstance;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.SUBSTANCE_REGISTRY;

public class Substance {
    private final String name;
    private final Substance.Properties properties;

    public Substance(String name, Properties properties) {
        this.name = name;
        this.properties = properties;
    }

    public void onTouch(SubstanceStack stack, LivingEntity entity) {

    }

    public void onConsume(SubstanceStack stack, LivingEntity entity) {
        if (properties.drug != null) {
            DrugHelper.ingestDrug(entity, new DrugInstance(properties.drug, stack.getVolume()));
        }
    }

    public String getName() {
        return name;
    }

    public DataComponentMap components() {
        return DataComponentMap.EMPTY;
    }

    /**
     * Calculates the molar volume of the substance in cm³/mol.
     * @return Molar volume in cm³/mol.
     */
    public float getMolarVolume() {
        return properties.molarMass / properties.density;
    }

    /**
     * Gets the slipperiness of the substance.
     * @return Slipperiness value (0.0 - 1.0).
     */
    public float getSlipperiness() {
        return properties.slipperiness;
    }

    public String getFlavor() {
        return properties.flavor;
    }

    public String getSmell() {
        return properties.smell;
    }

    public Integer getColor() {
        return properties.color;
    }

    public @NotNull Holder<Substance> builtInRegistryHolder() {
        return SUBSTANCE_REGISTRY.getHolder(SUBSTANCE_REGISTRY.getId(this)).orElseThrow();
    }

    public static class Properties {
        float molarMass = 18.02f; // g/mol
        float density = 1; // g/cm^3
        float transparency = 1;
        float slipperiness = 0.1f;
        int color = 0xFFAAD5DB;
        String flavor = "";
        String smell = "";
        Holder<Drug> drug;

        public Properties molarMass(float molarMass) {
            this.molarMass = molarMass;
            return this;
        }

        public Properties density(float density) {
            this.density = density;
            return this;
        }

        public Properties transparency(float transparency) {
            this.transparency = transparency;
            return this;
        }

        public Properties slipperiness(float slipperiness) {
            this.slipperiness = slipperiness;
            return this;
        }

        public Properties color(int color) {
            this.color = color;
            return this;
        }

        public Properties flavor(String flavor) {
            this.flavor = flavor;
            return this;
        }

        public Properties smell(String smell) {
            this.smell = smell;
            return this;
        }

        public Properties drug(Holder<Drug> drug) {
            this.drug = drug;
            return this;
        }
    }
}
