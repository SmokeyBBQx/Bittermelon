package net.smokeybbq.bittermelon.systems.medical.infections.infectionStages;

import net.smokeybbq.bittermelon.systems.medical.infections.Infection;
import net.smokeybbq.bittermelon.systems.medical.infections.InfectionStage;
import net.smokeybbq.bittermelon.systems.medical.symptoms.Symptom;

import java.util.List;


public class DeclineStage implements InfectionStage {
    @Override
    public void nextStage(Infection infection) {
        infection.setInfectionStage(new ConvalescentStage());
    }

    @Override
    public String getStageName() {
        return "Decline";
    }

    @Override
    public List<Symptom> getSymptoms() {
        return List.of();
    }
}


