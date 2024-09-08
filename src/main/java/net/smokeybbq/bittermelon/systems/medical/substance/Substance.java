package net.smokeybbq.bittermelon.systems.medical.substance;

import net.smokeybbq.bittermelon.systems.medical.compartments.Compartment;

import java.util.HashMap;
import java.util.Map;

public abstract class Substance {
    protected String name;
    protected float absorptionRateConstant;
    protected float eliminationRateConstant;
    protected float metabolismRateConstant;
    protected float absorptionModifier, eliminationModifier, metabolismModifier;
    protected float toxicModifier;
    protected float defaultToxicDamage;
    protected Map<String, Float> toxicDamage = new HashMap<>();

    public Substance(String name, float toxicModifier) {
        this.name = name;
        this.toxicModifier = toxicModifier;
    }

    public abstract float interact(Substance substance);

    public abstract void effect(Compartment compartment, float concentration);

    public String getName() {
        return name;
    }

    public float getAbsorptionRateConstant() {
        return absorptionRateConstant;
    }

    public float getEliminationRateConstant() {
        return eliminationRateConstant;
    }

    public float getMetabolismRateConstant() { return  metabolismRateConstant;}

    public float getToxicDamage(Compartment compartment) {
        return toxicDamage.getOrDefault(compartment.getName(), defaultToxicDamage);
    }
}
