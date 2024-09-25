package com.site21.bittermelon.substance.substances;

import com.site21.bittermelon.substance.Substance;
import com.site21.bittermelon.substance.SubstanceProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Molecule extends Substance {
    private final Map<Supplier<Substance>, Integer> formula = new HashMap<>();
    private final float density;
    private final float heatCapacity;

    public Molecule(SubstanceProperties properties, float density, float heatCapacity) {
        super(properties);
        this.density = density;
        this.heatCapacity = heatCapacity;
    }

    public Molecule addFormula(Supplier<Substance> atom, Integer subscript) {
        formula.put(atom, subscript);
        return this;
    }

    public Molecule addFormula(Supplier<Substance> atom) {
        formula.put(atom, 1);
        return this;
    }

    @Override
    public float getMolarMass() {
        float molarMass = 0;

        for (Map.Entry<Supplier<Substance>, Integer> entry : formula.entrySet()) {
            Substance atom = entry.getKey().get();
            Integer subscript = entry.getValue();
            molarMass += atom.getMolarMass() * subscript;
        }

        return molarMass;
    }

    @Override
    public float getDensity() {
        return density;
    }

    @Override
    public float getSpecificVolume() {
        return getMolarMass() / density;
    }

    @Override
    public float getHeatCapacity() {
        return heatCapacity;
    }
}
