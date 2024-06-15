package net.smokeybbq.bittermelon.medical.substance.toxins;

import net.smokeybbq.bittermelon.medical.substance.Substance;

public class Toxin extends Substance {
    public Toxin(float absorptionModifier, float eliminationModifier, float metabolismModifier) {
        super(absorptionModifier, eliminationModifier, metabolismModifier);
    }

    @Override
    public void toxicDamage(float effectiveness) {

    }
}
