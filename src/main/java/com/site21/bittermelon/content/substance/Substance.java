package com.site21.bittermelon.content.substance;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.SUBSTANCE_REGISTRY;

public class Substance {
    private final Properties properties;
    private float absorptionRate;
    private final int color;
    private final String name;
    private final float molarMass;
    private final float density;
    private final float heatCapacity;

    public Substance(Properties properties, String name, int color, float molarMass, float density, float heatCapacity) {
        this.properties = properties;
        this.name = name;
        this.color = color;
        this.molarMass = molarMass;
        this.density = density;
        this.heatCapacity = heatCapacity;
    }

    public Properties getProperties() {
        return properties;
    }

    public float getAbsorptionRate() {
        return absorptionRate;
    }

    public String getName() {
        return name;
    }

    public Integer getColor() {
        return color;
    }

    public float getMolarMass() {
        return molarMass;
    }

    public float getDensity() {
        return density;
    }

    public float getSpecificVolume() {
        return molarMass / density;
    }

    public float getHeatCapacity() {
        return heatCapacity;
    }

    public Map<Substance, Integer> getFormula() {
        return null;
    }

    public DataComponentMap components() {
        return DataComponentMap.EMPTY;
    }

    public Holder<Substance> builtInRegistryHolder() {
        // TODO: Add an actual way to get the holder
        String name = getName().toLowerCase().replace(" ", "_");

        return SUBSTANCE_REGISTRY.getHolder(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, name)).get();
//        return this.holder;
    }

    public static class Properties {
        private float transparency = 1;
        private float slipperiness = 0.1f;
        private String flavor;
        private String smell;

        public float getTransparency() {
            return transparency;
        }

        public float getSlipperiness() {
            return slipperiness;
        }

        public String getFlavor() {
            return flavor;
        }

        public String getSmell() {
            return smell;
        }

        public Properties slipperiness(float slipperiness) {
            this.slipperiness = slipperiness;
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

        public Properties transparency(float transparency) {
            this.transparency = transparency;
            return this;
        }
    }
}
