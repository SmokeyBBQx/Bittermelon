package net.smokeybbq.bittermelon.character.medical.species.animal.gastropod;

import net.smokeybbq.bittermelon.character.medical.species.animal.Cardiorespiratory;
import net.smokeybbq.bittermelon.medical.compartments.Compartment;

import java.util.Map;

public class GastropodCardiorespiratory extends Cardiorespiratory {
    public GastropodCardiorespiratory(Map<String, Compartment> compartments) {
        super(compartments);
    }

    @Override
    protected void heartRhythm() {
        switch (rhythm) {
            case SINUS -> sinus();
            case VENTRICULAR_FIBRILLATION -> ventricularFibrillation();
            case PEA -> PEA();
            case ASYSTOLE -> asystole();
        }
    }
}
