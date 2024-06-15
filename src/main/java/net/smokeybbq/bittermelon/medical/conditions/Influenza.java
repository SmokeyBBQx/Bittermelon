package net.smokeybbq.bittermelon.medical.conditions;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.symptoms.Cough;
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
        fever = new Fever(character, "Hypothalamus", amplifier);
        headache = new Headache(character, "Head", amplifier);
        cough = new Cough(character, "Throat", amplifier);

        symptoms.add(fever);
    }

    @Override
    public void treat(Substance drug, float effectiveness, String area) {
        String drugName = drug.getName();
        System.out.println("Drug Name " + drugName);

        switch (drugName) {
            case "Acetaminophen" -> {
                for (Symptom symptom : symptoms) {
                    if (symptom.getAffectedArea().equals(area) && symptom instanceof Fever) {
                        symptom.decreaseAmplifier(effectiveness);
                    }
                }
            }
            case "Dextromethorphan" -> {
                for (Symptom symptom : symptoms) {
                    if (symptom.getAffectedArea().equals(area) && symptom instanceof Cough) {
                        symptom.decreaseAmplifier(effectiveness);
                    }
                }
            }

            default -> {
                for (Symptom symptom : symptoms) {
                    symptom.decreaseAmplifier(effectiveness);
                }
            }
        }
    }
}
