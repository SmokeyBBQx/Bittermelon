package net.smokeybbq.bittermelon.medical.substance.medicine;

import net.smokeybbq.bittermelon.medical.substance.Substance;

public class Penicillin extends Substance {

    public Penicillin(double absorptionModifier, double eliminationModifier, double metabolismModifier) {
        super(absorptionModifier, eliminationModifier, metabolismModifier);
        this.absorptionRateConstant = 0.1 * absorptionModifier;
        this.eliminationRateConstant = 0.5 * eliminationModifier;
        this.metabolismRateConstant = 0.5 * metabolismModifier;
    }

    @Override
    public void toxicDamage(double effectiveness) {

    }
}
