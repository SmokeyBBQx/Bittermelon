package net.smokeybbq.bittermelon.medical.conditions;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.symptoms.Fever;
import net.smokeybbq.bittermelon.medical.symptoms.Headache;
import net.smokeybbq.bittermelon.medical.symptoms.Symptom;

public class Influenza extends Condition {

    private Symptom fever, headache, ache, fatigue, cough, congestion, nausea, vomiting;

    public Influenza(float duration, boolean chronic, float severity, String affectedArea, Character character) {
        super(duration, chronic, severity, affectedArea, character);
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
        fever = new Fever(severity, character, affectedArea);
        headache = new Headache(severity, character, affectedArea);

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
