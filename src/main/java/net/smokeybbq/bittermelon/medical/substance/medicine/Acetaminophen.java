package net.smokeybbq.bittermelon.medical.substance.medicine;

import net.smokeybbq.bittermelon.medical.substance.Substance;

public class Acetaminophen extends Substance {
    public Acetaminophen(float absorptionModifier, float eliminationModifier, float metabolismModifier) {
        super(absorptionModifier, eliminationModifier, metabolismModifier);
        this.absorptionRateConstant = 0.1F * absorptionModifier;
        this.eliminationRateConstant = 0.1F * eliminationModifier;
        this.metabolismRateConstant = 0.1F * metabolismModifier;
        name = "Acetaminophen";
        halfMaximalEffectiveConcentration = 100;
        eMax = 1;
    }

    @Override
    public void toxicDamage(float effectiveness) {

    }
}
