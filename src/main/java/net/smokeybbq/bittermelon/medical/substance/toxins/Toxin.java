package net.smokeybbq.bittermelon.medical.substance.toxins;

import net.smokeybbq.bittermelon.medical.substance.Substance;

public class Toxin extends Substance {
    public Toxin(double absorptionModifier, double eliminationModifier, double metabolismModifier) {
        super(absorptionModifier, eliminationModifier, metabolismModifier);
    }

    @Override
    public void toxicDamage(double effectiveness) {

    }
}
