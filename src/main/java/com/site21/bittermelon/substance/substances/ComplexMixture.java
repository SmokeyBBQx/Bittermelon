package com.site21.bittermelon.substance.substances;

import com.site21.bittermelon.substance.Substance;
import com.site21.bittermelon.substance.SubstanceProperties;
import com.site21.bittermelon.substance.reactions.Reaction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ComplexMixture extends Substance {
    Map<Supplier<Substance>, Float> composition = new HashMap<>();

    public ComplexMixture(SubstanceProperties properties) {
        super(properties);
    }


    @Override
    public float getMolarMass() {
        float molarMass = 0;

        for (Map.Entry<Supplier<Substance>, Float> entry : composition.entrySet()) {
            Substance component = entry.getKey().get();
            float amount = entry.getValue();
            molarMass += component.getMolarMass() * amount;
        }

        return molarMass;
    }

    @Override
    public float getDensity() {
        float density = 0;

        for (Map.Entry<Supplier<Substance>, Float> entry : composition.entrySet()) {
            Substance component = entry.getKey().get();
            float amount = entry.getValue();
            density += component.getDensity() * amount;
        }

        return density;
    }

    @Override
    public float getSpecificVolume() {
        return 1 / getDensity();
    }

    @Override
    public float getHeatCapacity() {
        return 0;
    }
}
