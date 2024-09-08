package net.smokeybbq.bittermelon.systems.medical.infections.infectionStages;

import net.smokeybbq.bittermelon.systems.medical.infections.Infection;
import net.smokeybbq.bittermelon.systems.medical.infections.InfectionStage;
import net.smokeybbq.bittermelon.systems.medical.symptoms.Symptom;

import java.util.List;


public class ConvalescentStage implements InfectionStage {
    @Override
    public void nextStage(Infection infection) {
        System.out.println("The infection is fully recovered.");
    }

    @Override
    public String getStageName() {
        return "Convalescent";
    }

    @Override
    public List<Symptom> getSymptoms() {
        return List.of();
    }
}