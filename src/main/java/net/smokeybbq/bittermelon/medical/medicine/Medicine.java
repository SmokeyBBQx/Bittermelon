package net.smokeybbq.bittermelon.medical.medicine;

public abstract class Medicine {
    protected String name;
    protected double eMax;
    protected double halfMaximalEffectiveConcentration;
    protected double overdoseLimit;
    protected double absorptionRateConstant;
    protected double eliminationRateConstant;
    protected double metabolismRateConstant;
    protected double absorptionModifier, eliminationModifier, metabolismModifier;

    public Medicine(double absorptionModifier, double eliminationModifier, double metabolismModifier) {
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
