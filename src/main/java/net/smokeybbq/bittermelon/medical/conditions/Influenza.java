package net.smokeybbq.bittermelon.medical.conditions;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.symptoms.Fever;
import net.smokeybbq.bittermelon.medical.symptoms.Headache;
import net.smokeybbq.bittermelon.medical.symptoms.Symptom;

import java.util.List;

public class Influenza extends Condition {

    private Symptom fever, headache, ache, fatigue, cough, congestion, nausea, vomiting;

    public Influenza(float duration, boolean chronic, Character character, List<String> affectedAreas, float amplifier) {
        super(duration, chronic, character, affectedAreas, amplifier);
    }


    @Override
    public void update() {
        for (Symptom symptom : symptoms) {
            symptom.update();
        }
    }

    @Override
    protected void symptoms() {
        suitableTreatments = new String[]{"Acetaminophen"};
        fever = new Fever(character, affectedAreas, amplifier);
        headache = new Headache(character, affectedAreas, amplifier);

        symptoms.add(fever);
    }

    @Override
    public void treat(Substance drug, float effectiveness) {
        String drugName = drug.getName();
        System.out.println("Drug Name " + drugName);

        switch (drugName) {
            case "Acetaminophen":
                fever.decreaseAmplifier(effectiveness);

                break;
            default:
                for (Symptom symptom : symptoms) {
                    symptom.decreaseAmplifier(effectiveness);
                }
        }
    }
}
