package net.smokeybbq.bittermelon.medical.substance.medicine;

import net.smokeybbq.bittermelon.medical.substance.Substance;

public class Acetaminophen extends Substance {
    public Acetaminophen(double absorptionModifier, double eliminationModifier, double metabolismModifier) {
        super(absorptionModifier, eliminationModifier, metabolismModifier);
        this.absorptionRateConstant = 0.1 * absorptionModifier;
        this.eliminationRateConstant = 0.1 * eliminationModifier;
        this.metabolismRateConstant = 0.1 * metabolismModifier;
    }

    @Override
    public void toxicDamage(double effectiveness) {

    }
}
