package net.smokeybbq.bittermelon.medical.conditions;

import net.minecraft.world.entity.player.Player;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.substance.medicine.Penicillin;
import net.smokeybbq.bittermelon.medical.substance.toxins.Toxin;
import net.smokeybbq.bittermelon.medical.symptoms.Fever;
import net.smokeybbq.bittermelon.medical.symptoms.Symptom;

public class Influenza extends Condition {

    private Fever fever;

    public Influenza(double duration, boolean chronic, double severity, String affectedArea, Character character) {
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
        fever = new Fever(severity, character);

        symptoms.add(fever);
    }

    @Override
    public void treat(Substance drug, double effectiveness) {
        String drugName = drug.getName();

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
