package net.smokeybbq.bittermelon.medical.conditions;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.substance.Substance;

public abstract class Condition {
    protected String name;
    protected double duration;
    protected boolean chronic;
    protected double severity;
    protected String affectedArea;
    protected Character character;
    protected String[] suitableTreatments;
    public Condition(double duration, boolean chronic, double severity, String affectedArea, Character character) {
        this.duration = duration;
        this.chronic = chronic;
        this.severity = severity;
        this.affectedArea = affectedArea;
        this.character= character;
    }

    protected abstract void symptoms();

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setChronic(boolean chronic) {
        this.chronic = chronic;
    }

    public void setSeverity(double severity) {
        this.severity = severity;
    }

    public void setAffectedArea(String affectedArea) {
        this.affectedArea = affectedArea;
    }

    public String getAffectedArea() {
        return affectedArea;
    }

    public String[] getSuitableTreatments() {
        return suitableTreatments;
    }

    public abstract void treat(Substance drug, double effectiveness);
}
