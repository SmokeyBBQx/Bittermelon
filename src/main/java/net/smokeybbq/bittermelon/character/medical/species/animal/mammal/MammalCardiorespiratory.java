package net.smokeybbq.bittermelon.character.medical.species.animal.mammal;

import net.smokeybbq.bittermelon.character.medical.species.animal.Cardiorespiratory;
import net.smokeybbq.bittermelon.medical.compartments.Compartment;
import java.util.Map;

public class MammalCardiorespiratory extends Cardiorespiratory {

    public MammalCardiorespiratory(Map<String, Compartment> compartments) {
        super(compartments);
    }

    protected void heartRhythm() {
        switch (rhythm) {
            case SINUS -> sinus();
            case VENTRICULAR_FIBRILLATION -> ventricularFibrillation();
            case ATRIAL_FIBRILLATION -> atrialFibrillation();
            case PEA -> PEA();
            case ASYSTOLE -> asystole();
        }
    }

    private void checkForArrhythmia() {
        float heartHealth = compartments.get("chest").getCompartment("heart").getFunction();

        float fibrillationChance = (100 - heartHealth) / 1000;

    }

    @Override
    protected float respiratoryFunction() {
        Compartment chest = compartments.get("chest");

        float tracheaFunction = chest.getCompartment("trachea").getFunction();
        float respiratoryFunction = 0;

        for (Compartment compartment : respiratoryCompartments) {
            respiratoryFunction += compartment.getFunction();
        }

        return tracheaFunction * respiratoryFunction;
    }
}
