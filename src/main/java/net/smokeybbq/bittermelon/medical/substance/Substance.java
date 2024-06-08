package net.smokeybbq.bittermelon.medical.substance;

public abstract class Substance {
    protected String name;
    protected double eMax;
    protected double halfMaximalEffectiveConcentration;
    protected double overdoseLimit;
    protected double absorptionRateConstant;
    protected double eliminationRateConstant;
    protected double metabolismRateConstant;
    protected double absorptionModifier, eliminationModifier, metabolismModifier;

    public Substance(double absorptionModifier, double eliminationModifier, double metabolismModifier) {
        this.absorptionModifier = absorptionModifier;
        this.eliminationModifier = eliminationModifier;
        this.metabolismModifier = metabolismModifier;
    }

    public String getName() {
        return name;
    }

    public double getEMax() {
        return eMax;
    }

    public double getHalfMaximalEffectiveConcentration() {
        return halfMaximalEffectiveConcentration;
    }

    public double getOverdoseLimit() {
        return overdoseLimit;
    }

    public double getAbsorptionRateConstant() {
        return absorptionRateConstant;
    }

    public double getEliminationRateConstant() {
        return eliminationRateConstant;
    }

    public double getMetabolismRateConstant() { return  metabolismRateConstant;}

}
