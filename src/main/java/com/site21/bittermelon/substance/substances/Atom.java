package com.site21.bittermelon.substance.substances;

import com.site21.bittermelon.substance.Substance;
import com.site21.bittermelon.substance.SubstanceProperties;

public class Atom extends Substance {
    private final float molarMass;
    private final float density;
    private final float heatCapacity;

    public Atom(SubstanceProperties properties, float molarMass, float density, float heatCapacity) {
        super(properties);
        this.molarMass = molarMass;
        this.density = density;
        this.heatCapacity = heatCapacity;
    }

    @Override
    public float getMolarMass() {
        return molarMass;
    }

    @Override
    public float getDensity() {
        return density;
    }

    @Override
    public float getSpecificVolume() {
        return molarMass / density;
    }

    @Override
    public float getHeatCapacity() {
        return heatCapacity;
    }
}
