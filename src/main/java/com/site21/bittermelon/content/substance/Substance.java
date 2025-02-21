package com.site21.bittermelon.content.substance;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.SUBSTANCE_REGISTRY;

public class Substance {
    private String smell = "";
    private String taste = "";
    private float absorptionRate;
    private final int color;
    private final String name;
    private final float molarMass;
    private final float density;
    private final float heatCapacity;

    public Substance(String name, int color, float molarMass, float density, float heatCapacity) {
        this.name = name;
        this.color = color;
        this.molarMass = molarMass;
        this.density = density;
        this.heatCapacity = heatCapacity;
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

    }
}
