package net.smokeybbq.bittermelon.medical.substance.medicine;

import net.smokeybbq.bittermelon.medical.substance.Substance;

public abstract class Antibiotics extends Substance {
    public Antibiotics(float absorptionModifier, float eliminationModifier, float metabolismModifier, float toxicModifier) {
        super(absorptionModifier, eliminationModifier, metabolismModifier, toxicModifier);
    }


}
