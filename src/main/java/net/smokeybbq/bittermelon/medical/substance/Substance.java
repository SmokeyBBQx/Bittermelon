package net.smokeybbq.bittermelon.medical.substance;

public abstract class Substance {
    protected String name;
    protected float eMax;
    protected float halfMaximalEffectiveConcentration;
    protected float overdoseLimit;
    protected float absorptionRateConstant;
    protected float eliminationRateConstant;
    protected float metabolismRateConstant;
    protected float absorptionModifier, eliminationModifier, metabolismModifier;
    public boolean toxic;

    public Substance(float absorptionModifier, float eliminationModifier, float metabolismModifier) {
        this.absorptionModifier = absorptionModifier;
        this.eliminationModifier = eliminationModifier;
        this.metabolismModifier = metabolismModifier;
    }

    public String getName() {
        return name;
    }

    public float getEMax() {
        return eMax;
    }

    public float getHalfMaximalEffectiveConcentration() {
        return halfMaximalEffectiveConcentration;
    }

    public float getOverdoseLimit() {
        return overdoseLimit;
    }

    public float getAbsorptionRateConstant() {
        return absorptionRateConstant;
    }

    public float getEliminationRateConstant() {
        return eliminationRateConstant;
    }

    public float getMetabolismRateConstant() { return  metabolismRateConstant;}

    public abstract void toxicDamage(float effectiveness);

}
