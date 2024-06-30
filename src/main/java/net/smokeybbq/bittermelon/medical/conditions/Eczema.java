package net.smokeybbq.bittermelon.medical.conditions;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.symptoms.Cough;
import net.smokeybbq.bittermelon.medical.symptoms.Fever;
import net.smokeybbq.bittermelon.medical.symptoms.ItchyRash;
import net.smokeybbq.bittermelon.medical.symptoms.Symptom;

import java.util.List;

public class Eczema extends Condition {
    public Eczema(float duration, boolean chronic, Character character, List<String> affectedAreas, float amplifier) {
        super(duration, chronic, character, affectedAreas, amplifier);
    }

    @Override
    protected void symptoms() {
        suitableTreatments = new String[]{"Corticosteroid"};

        for (String affectedArea : affectedAreas) {
            symptoms.add(new ItchyRash(character, affectedArea, amplifier));
        }
    }

    @Override
    public void treat(Substance drug, float effectiveness, String area) {
        for (Symptom symptom : symptoms) {
            if (symptom.getAffectedArea().equals(area) && symptom instanceof ItchyRash) {
                symptom.decreaseAmplifier(effectiveness);
            }
        }
    }

    @Override
    public void update() {
        for (Symptom symptom : symptoms) {
            symptom.update();
        }
    }
}


