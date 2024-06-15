package net.smokeybbq.bittermelon.medical.substance.medicine;

import net.smokeybbq.bittermelon.medical.substance.Substance;

public class Penicillin extends Substance {

    public Penicillin(float absorptionModifier, float eliminationModifier, float metabolismModifier) {
        super(absorptionModifier, eliminationModifier, metabolismModifier);
        this.absorptionRateConstant = 0.1F * absorptionModifier;
        this.eliminationRateConstant = 0.5F * eliminationModifier;
        this.metabolismRateConstant = 0.5F * metabolismModifier;
    }

    @Override
    public void toxicDamage(float effectiveness) {

    }
}
