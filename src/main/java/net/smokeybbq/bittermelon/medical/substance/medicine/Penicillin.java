package net.smokeybbq.bittermelon.medical.substance.medicine;

import net.smokeybbq.bittermelon.medical.substance.Substance;

public class Penicillin extends Substance {

    public Penicillin(double absorptionModifier, double eliminationModifier, double metabolismModifier) {
        super(absorptionModifier, eliminationModifier, metabolismModifier);
        this.absorptionRateConstant = 0.2 * absorptionModifier;
        this.eliminationRateConstant = 0.3 * eliminationModifier;
        this.metabolismRateConstant = 0.2 * metabolismModifier;
    }

    @Override
    public void toxicDamage(double effectiveness) {

    }
}
