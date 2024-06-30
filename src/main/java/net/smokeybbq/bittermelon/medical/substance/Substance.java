package net.smokeybbq.bittermelon.medical.substance;

import java.util.HashMap;
import java.util.Map;

public abstract class Substance {
    protected String name;
    protected float eMax;
    protected float halfMaximalEffectiveConcentration;
    protected float absorptionRateConstant;
    protected float eliminationRateConstant;
    protected float metabolismRateConstant;
    protected float absorptionModifier, eliminationModifier, metabolismModifier;

    protected float toxicModifier;
    protected float defaultToxicDamage;
    protected Map<String, Float> toxicDamage = new HashMap<>();
    public boolean toxic;

    public Substance(float absorptionModifier, float eliminationModifier, float metabolismModifier, float toxicModifier) {
        this.absorptionModifier = absorptionModifier;
        this.eliminationModifier = eliminationModifier;
        this.metabolismModifier = metabolismModifier;
        this.toxicModifier = toxicModifier;
    }

    public abstract float interact(Substance substance);

    public String getName() {
        return name;
    }

    public float getEMax() {
        return eMax;
    }

    public float getHalfMaximalEffectiveConcentration() {
        return halfMaximalEffectiveConcentration;
    }

    public float getAbsorptionRateConstant() {
        return absorptionRateConstant;
    }

    public float getEliminationRateConstant() {
        return eliminationRateConstant;
    }

    public float getMetabolismRateConstant() { return  metabolismRateConstant;}

    public float getDefaultToxicDamage() {
        return defaultToxicDamage;
    }

    public Map<String, Float> getToxicDamage() {
        return toxicDamage;
    }
}
