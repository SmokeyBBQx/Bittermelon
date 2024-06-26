package net.smokeybbq.bittermelon.medical.infections;

import com.sun.jna.WString;
import net.smokeybbq.bittermelon.medical.symptoms.Symptom;
import java.util.ArrayList;
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
    protected String pointOfOrigin;

    public Infection() {

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

    public String getPointOfOrigin() {
        return pointOfOrigin;
    }

    public void setPointOfOrigin(String pointOfOrigin) {
        this.pointOfOrigin = pointOfOrigin;
    }
}