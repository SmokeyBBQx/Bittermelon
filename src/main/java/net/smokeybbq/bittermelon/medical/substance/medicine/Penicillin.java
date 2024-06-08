package net.smokeybbq.bittermelon.medical.substance.medicine;

import net.smokeybbq.bittermelon.medical.substance.Substance;

public class Penicillin extends Substance {

    public Penicillin(double absorptionModifier, double eliminationModifier, double metabolismModifier) {
        super(absorptionModifier, eliminationModifier, metabolismModifier);
        this.absorptionRateConstant = 0.1;
        this.eliminationRateConstant = 0.1;
        this.metabolismRateConstant = 0.1;
    }
}
