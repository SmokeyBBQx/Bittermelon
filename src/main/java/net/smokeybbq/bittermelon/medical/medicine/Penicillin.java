package net.smokeybbq.bittermelon.medical.medicine;

public class Penicillin extends Medicine {

    public Penicillin(double absorptionModifier, double eliminationModifier, double metabolismModifier) {
        super(absorptionModifier, eliminationModifier, metabolismModifier);
        this.absorptionRateConstant = 0.1;
        this.eliminationRateConstant = 0.1;
        this.metabolismRateConstant = 0.1;
    }
}
