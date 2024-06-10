package net.smokeybbq.bittermelon.medical.infections;

import net.smokeybbq.bittermelon.medical.symptoms.Symptom;
import java.util.ArrayList;
import java.util.List;

public abstract class Infection {
    // Instance variables
    protected List<Symptom> symptoms;
    protected int incubationPeriod;
    protected int transmissionModifier;

    public Infection() {
        this.symptoms = new ArrayList<>();
    }

    public abstract List<Symptom> getSymptoms();

    public int getIncubationPeriod() {
        return incubationPeriod;
    }

    public void setIncubationPeriod(int incubationPeriod) {
        this.incubationPeriod = incubationPeriod;
    }
}