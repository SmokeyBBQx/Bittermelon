package net.smokeybbq.bittermelon.medical.infections.infectionStages;

import net.smokeybbq.bittermelon.medical.infections.Infection;
import net.smokeybbq.bittermelon.medical.infections.InfectionStage;
import net.smokeybbq.bittermelon.medical.symptoms.Symptom;

import java.util.List;

public class AcuteStage implements InfectionStage {
    @Override
    public void nextStage(Infection infection) {
        infection.setInfectionStage(new DeclineStage());
    }

    @Override
    public String getStageName() {
        return "Acute";
    }

    @Override
    public List<Symptom> getSymptoms() {
        return List.of();
    }
}