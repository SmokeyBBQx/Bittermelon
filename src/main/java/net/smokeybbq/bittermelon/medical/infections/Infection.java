package net.smokeybbq.bittermelon.medical.infections;

import net.smokeybbq.bittermelon.medical.compartments.Compartment;
import net.smokeybbq.bittermelon.medical.symptoms.Symptom;

import java.util.List;

public abstract class Infection {

    // Does the body cure the illness eventually?
    protected Boolean isPersistent;

    //Base severity for the illness
    protected int baseSeverity;

    //How fast the infection spreads in the body itself
    protected int spreadModifier;

    //The transmissionModifier determines how quickly the infection spreads to other living entities
    protected int transmissionModifier;

    //An infection has five stages: Incubation, Prodromal, Acute, Decline and Convalescent
    protected InfectionStage infectionStage;

    //The part of the body where the infection incubates, E.G. "leftFoot" or "Liver"
    protected Compartment siteOfEntry;
    protected float infectionRate;

    public Infection(Compartment siteOfEntry) {
        this.siteOfEntry = siteOfEntry;

    }

    public void update() {

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

    public Compartment getSiteOfEntry() {
        return siteOfEntry;
    }
}