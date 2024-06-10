package net.smokeybbq.bittermelon.medical.conditions;

import net.minecraft.world.entity.player.Player;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.symptoms.Symptom;

import java.util.ArrayList;
import java.util.List;

public abstract class Condition {
    protected String name;
    protected float duration;
    protected boolean chronic;
    protected float severity;
    protected String affectedArea;
    protected Character character;
    protected List<Symptom> symptoms = new ArrayList<>();
    protected String[] suitableTreatments;
    public Condition(float duration, boolean chronic, float severity, String affectedArea, Character character) {
        this.duration = duration;
        this.chronic = chronic;
        this.severity = severity;
        this.affectedArea = affectedArea;
        this.character= character;
        symptoms();
    }

    public abstract void update();

    protected abstract void symptoms();

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setChronic(boolean chronic) {
        this.chronic = chronic;
    }

    public void setSeverity(float severity) {
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

    public List<Symptom> getSymptoms() {
        return symptoms;
    }

    public float getSeverity() {
        return severity;
    }

    public abstract void treat(Substance drug, float effectiveness);
}
