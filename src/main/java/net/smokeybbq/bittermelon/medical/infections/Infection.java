package net.smokeybbq.bittermelon.medical.infections;

import net.smokeybbq.bittermelon.medical.symptoms.Symptom;
import java.util.ArrayList;
import java.util.List;

public abstract class Infection {
    // Instance variables
    protected List<Symptom> symptoms;

    // Does the body cure the illness eventually?
    protected Boolean isPersistent;

    protected int baseSeverity;

    //The transmissionModifier determines how quickly the infection spreads to other living entities
    protected int transmissionModifier;

    //An infection has five stages: Incubation, Prodromal, Acute, Decline and Convalescent
    protected InfectionStage infectionStage;

    public Infection() {
        this.symptoms = new ArrayList<>();
    }

    public  List<Symptom> getSymptoms(){
        return infectionStage.getSymptoms();
    }

    public void setInfectionStage(InfectionStage infectionStage) {
        this.infectionStage = infectionStage;
    }

    public InfectionStage getInfectionStage() {
        return infectionStage;
    }
}